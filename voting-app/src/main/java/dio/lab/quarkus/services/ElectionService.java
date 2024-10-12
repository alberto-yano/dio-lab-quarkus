package dio.lab.quarkus.services;

import dio.lab.quarkus.domain.ElectionDto;
import dio.lab.quarkus.repositories.ElectionRedisRepository;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.runtime.Startup;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class ElectionService {

  private final ElectionRedisRepository electionRedisRepository;
  private final ReactiveRedisDataSource reactiveRedisDataSource;

  @Startup
  public void init() {
    Multi<String> sub = reactiveRedisDataSource.pubsub( String.class ).subscribe( "elections" );
    sub.emitOn( Infrastructure.getDefaultWorkerPool() ).subscribe().with( id -> {
      log.info( "Election " + id + " received from subscription" );
      ElectionDto election = this.electionRedisRepository.findById( id );
      log.info( "Election " + election.id() + " starting" );
    } );
  }

  public List<ElectionDto> findAll() {
    return this.electionRedisRepository.findAll();
  }

  public void vote( String electionId, String candidateId ) {
    this.electionRedisRepository.findById( electionId ).candidates().stream().filter(
        candidate -> candidate.id().equalsIgnoreCase( candidateId ) ).findFirst().ifPresent(
        candidate -> this.electionRedisRepository.vote( electionId, candidate ) );
  }

}
