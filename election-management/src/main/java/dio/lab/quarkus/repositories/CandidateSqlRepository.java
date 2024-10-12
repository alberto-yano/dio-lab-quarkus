package dio.lab.quarkus.repositories;

import dio.lab.quarkus.domain.Candidate;
import dio.lab.quarkus.repositories.entities.CandidateEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class CandidateSqlRepository {

  private final EntityManager entityManager;

  public List<Candidate> findAll() {
    return this.entityManager.createQuery( "from candidates", CandidateEntity.class ).getResultList().stream().map(
        CandidateSqlRepository::from ).toList();
  }

  @Transactional
  public Candidate save( Candidate candidate ) {
    return from( this.entityManager.merge(
        Objects.nonNull( candidate.id() ) ? merge( this.entityManager.find( CandidateEntity.class, candidate.id() ),
                                                   candidate ) : from( candidate ) ) );
  }

  protected static Candidate from( CandidateEntity candidateEntity ) {
    return new Candidate( candidateEntity.getId(), Optional.ofNullable( candidateEntity.getPhoto() ),
                          candidateEntity.getGivenName(), candidateEntity.getFamilyName(), candidateEntity.getEmail(),
                          Optional.ofNullable( candidateEntity.getPhone() ),
                          Optional.ofNullable( candidateEntity.getJobTitle() ) );
  }

  protected static CandidateEntity from( Candidate candidate ) {
    CandidateEntity candidateEntity = new CandidateEntity();
    candidateEntity.setPhoto( candidate.photo().orElse( null ) );
    candidateEntity.setGivenName( candidate.givenName() );
    candidateEntity.setFamilyName( candidate.familyName() );
    candidateEntity.setEmail( candidate.email() );
    candidateEntity.setPhone( candidate.phone().orElse( null ) );
    candidateEntity.setJobTitle( candidate.jobTitle().orElse( null ) );
    return candidateEntity;
  }

  protected static CandidateEntity merge( CandidateEntity candidateEntity, Candidate candidate ) {
    String temp = candidate.phone().orElse( null );
    if( candidateEntity.getPhoto() == null || ( Objects.nonNull( temp ) && !candidateEntity.getPhoto().equalsIgnoreCase(
        temp ) ) ) {
      candidateEntity.setPhoto( temp );
    }
    temp = candidate.givenName();
    if( candidateEntity.getGivenName() == null || ( Objects.nonNull(
        temp ) && !candidateEntity.getGivenName().equalsIgnoreCase( temp ) ) ) {
      candidateEntity.setGivenName( temp );
    }
    temp = candidate.familyName();
    if( candidateEntity.getFamilyName() == null || ( Objects.nonNull(
        temp ) && !candidateEntity.getFamilyName().equalsIgnoreCase( temp ) ) ) {
      candidateEntity.setFamilyName( temp );
    }
    temp = candidate.email();
    if( candidateEntity.getEmail() == null || ( Objects.nonNull( temp ) && !candidateEntity.getEmail().equalsIgnoreCase(
        temp ) ) ) {
      candidateEntity.setEmail( temp );
    }
    temp = candidate.phone().orElse( null );
    if( candidateEntity.getPhone() == null || ( Objects.nonNull( temp ) && !candidateEntity.getPhone().equalsIgnoreCase(
        temp ) ) ) {
      candidateEntity.setPhone( temp );
    }
    temp = candidate.jobTitle().orElse( null );
    if( candidateEntity.getJobTitle() == null || ( Objects.nonNull(
        temp ) && !candidateEntity.getJobTitle().equalsIgnoreCase( temp ) ) ) {
      candidateEntity.setJobTitle( temp );
    }
    return candidateEntity;
  }

}
