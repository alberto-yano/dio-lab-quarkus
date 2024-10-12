package dio.lab.quarkus.repositories;

import dio.lab.quarkus.domain.Candidate;
import dio.lab.quarkus.domain.Election;
import dio.lab.quarkus.repositories.entities.CandidateEntity;
import dio.lab.quarkus.repositories.entities.ElectionCandidateEntity;
import dio.lab.quarkus.repositories.entities.ElectionEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@ApplicationScoped
@RequiredArgsConstructor
public class ElectionSqlRepository {

  private final EntityManager entityManager;

  public List<Election> findAll() {
    return this.entityManager.createQuery( "from elections", ElectionEntity.class ).getResultList().stream().map(
        ElectionSqlRepository::from ).toList();
  }

  @Transactional
  public Election save( Election election ) {
    ElectionEntity electionEntity = Objects.nonNull( election.id() ) ? this.entityManager.find( ElectionEntity.class,
                                                                                                election.id() ) : new ElectionEntity();
    ElectionEntity electionEntitySaved = this.entityManager.merge( electionEntity );
    final Set<ElectionCandidateEntity> electionCandidateEntityDb = Objects.isNull(
        electionEntitySaved.getVotes() ) ? new HashSet<>() : electionEntitySaved.getVotes();
    election.votes().forEach( ( candidate, votes ) -> {
      CandidateEntity candidateEntity = this.entityManager.find( CandidateEntity.class, candidate.id() );
      ElectionCandidateEntity electionCandidateEntity = electionCandidateEntityDb.stream().filter(
          f -> f.getCandidate().getId().equalsIgnoreCase( candidate.id() ) ).findFirst().orElse(
          new ElectionCandidateEntity(
              new ElectionCandidateEntity.ElectionCandidateIdEntity( electionEntitySaved.getId(), candidate.id() ),
              votes, candidateEntity, electionEntitySaved ) );
      electionCandidateEntityDb.add( this.entityManager.merge( electionCandidateEntity ) );
    } );
    electionEntitySaved.setVotes( electionCandidateEntityDb );
    //    electionEntity = this.entityManager.merge( electionEntitySaved );
    return from( electionEntitySaved );
  }

  protected static Election from( ElectionEntity electionEntity ) {
    return new Election( electionEntity.getId(), from( electionEntity.getVotes() ) );
  }

  protected static ElectionEntity from( Election election ) {
    ElectionEntity electionEntity = new ElectionEntity();
    electionEntity.setId( election.id() );
    electionEntity.setVotes( ElectionCandidateSqlRepository.from( election.votes() ) );
    return electionEntity;
  }

  protected static Map<Candidate, Integer> from( Set<ElectionCandidateEntity> entitySet ) {
    Map<Candidate, Integer> result = new HashMap<>();
    for( ElectionCandidateEntity electionCandidateEntity : entitySet ) {
      result.put( CandidateSqlRepository.from( electionCandidateEntity.getCandidate() ),
                  electionCandidateEntity.getVotes() );
    }
    return result;
  }

}
