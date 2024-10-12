package dio.lab.quarkus.repositories;

import dio.lab.quarkus.domain.Candidate;
import dio.lab.quarkus.repositories.entities.ElectionCandidateEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
@RequiredArgsConstructor
public class ElectionCandidateSqlRepository {

  private final EntityManager entityManager;

  @Transactional
  public void setVote( String electionId, String candidateId, Integer votes ) {
    ElectionCandidateEntity.ElectionCandidateIdEntity id = new ElectionCandidateEntity.ElectionCandidateIdEntity();
    id.setElectionId( electionId );
    id.setCandidateId( candidateId );
    ElectionCandidateEntity electionCandidateEntity = this.entityManager.find( ElectionCandidateEntity.class, id );
    electionCandidateEntity.setVotes( votes );
    this.entityManager.merge( electionCandidateEntity );
  }

  protected static Set<ElectionCandidateEntity> from( Map<Candidate, Integer> votes ) {
    Set<ElectionCandidateEntity> electionCandidateEntities = new HashSet<>();
    for( Candidate candidate : votes.keySet() ) {
      ElectionCandidateEntity electionCandidateEntity = new ElectionCandidateEntity();
      electionCandidateEntity.setCandidate( CandidateSqlRepository.from( candidate ) );
      electionCandidateEntity.setVotes( votes.get( candidate ) );
      electionCandidateEntities.add( electionCandidateEntity );
    }
    return electionCandidateEntities;
  }

}
