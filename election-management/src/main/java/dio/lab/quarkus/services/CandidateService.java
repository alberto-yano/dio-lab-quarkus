package dio.lab.quarkus.services;

import dio.lab.quarkus.domain.Candidate;
import dio.lab.quarkus.repositories.CandidateSqlRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class CandidateService {

  private final CandidateSqlRepository candidateSqlRepository;

  public List<Candidate> findAll() {
    return this.candidateSqlRepository.findAll();
  }

  @Transactional
  public Candidate save( Candidate candidate ) {
    return this.candidateSqlRepository.save( candidate );
  }

}
