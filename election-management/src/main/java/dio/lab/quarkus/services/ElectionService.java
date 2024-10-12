package dio.lab.quarkus.services;

import dio.lab.quarkus.domain.Candidate;
import dio.lab.quarkus.domain.Election;
import dio.lab.quarkus.repositories.ElectionCandidateSqlRepository;
import dio.lab.quarkus.repositories.ElectionSqlRepository;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.sortedset.SortedSetCommands;
import io.quarkus.runtime.Startup;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
public class ElectionService {

  private static final String                            PREFIX_ELECTION = "election:";
  private final        CandidateService                  candidateService;
  private final        ElectionSqlRepository             electionSqlRepository;
  private final        ElectionCandidateSqlRepository    electionCandidateSqlRepository;
  private final        RedisDataSource                   redisDataSource;
  private final        ReactiveRedisDataSource           reactiveRedisDataSource;
  private final        SortedSetCommands<String, String> commands;

  @Startup
  public void init() {
    Multi<String> subVotes = this.reactiveRedisDataSource.pubsub( String.class ).subscribe( "votes" );
    subVotes.emitOn( Infrastructure.getDefaultExecutor() ).subscribe().with( this::sync );
  }

  public List<Election> findAll() {
    return this.electionSqlRepository.findAll();
  }

  @Transactional
  public void createElection() {
    List<Candidate>         candidates = this.candidateService.findAll();
    Map<Candidate, Integer> votes      = new HashMap<>();
    for( Candidate candidate : candidates ) {
      votes.put( candidate, 0 );
    }
    Election election = new Election( null, votes );
    election = this.electionSqlRepository.save( election );
    Map<String, Double> rank = election.votes().entrySet().stream().collect(
        Collectors.toMap( entry -> entry.getKey().id(), entry -> entry.getValue().doubleValue() ) );
    this.commands.zadd( PREFIX_ELECTION + election.id(), rank );
    this.redisDataSource.pubsub( String.class ).publish( "elections", election.id() );
  }

  private void sync( String id ) {
    String[] token       = id.split( "#" );
    String   electionId  = token[0].split( ":" )[1];
    String   candidateId = token[1];
    int votes = (int) this.commands.zrangeWithScores( token[0], 0, -1 ).stream().filter(
        c -> c.value.equals( token[1] ) ).findFirst().orElseThrow().score();
    this.electionCandidateSqlRepository.setVote( electionId, candidateId, votes );
  }

}
