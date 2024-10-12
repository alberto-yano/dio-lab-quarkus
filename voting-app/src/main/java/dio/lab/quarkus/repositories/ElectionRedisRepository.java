package dio.lab.quarkus.repositories;

import dio.lab.quarkus.domain.CandidateDto;
import dio.lab.quarkus.domain.ElectionDto;
import io.quarkus.cache.CacheResult;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.keys.KeyCommands;
import io.quarkus.redis.datasource.sortedset.SortedSetCommands;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class ElectionRedisRepository {

  private final SortedSetCommands<String, String> sortedSetCommands;
  private final KeyCommands<String>               keyCommands;
  private final RedisDataSource                   redisDataSource;

  public List<ElectionDto> findAll() {
    log.info( "Retrieving elections from redis" );
    return this.keyCommands.keys( "election:*" ).stream().map(
        id -> findById( id.replace( "election:", "" ) ) ).toList();
  }

  @CacheResult( cacheName = "memoization" )
  public ElectionDto findById( String id ) {
    log.info( "Retrieving election " + id + " from redis" );
    List<CandidateDto> candidates = this.sortedSetCommands.zrange( "election:" + id, 0, -1 ).stream().map(
        CandidateDto::new ).toList();
    return new ElectionDto( id, candidates );
  }

  public void vote( String electionId, CandidateDto candidate ) {
    log.info( "Voting for " + candidate.id() );
    this.sortedSetCommands.zincrby( "election:" + electionId, 1, candidate.id() );
    this.redisDataSource.pubsub( String.class ).publish( "votes", "election:" + electionId + "#" + candidate.id() );
  }

}
