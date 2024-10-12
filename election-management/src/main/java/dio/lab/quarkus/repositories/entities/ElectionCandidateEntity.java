package dio.lab.quarkus.repositories.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity( name = "election_candidate" )
public class ElectionCandidateEntity {

  @EmbeddedId
  private ElectionCandidateIdEntity id;
  private Integer                   votes;

  @ManyToOne
  @MapsId( "candidateId" )
  @JoinColumn( name = "candidate_id" )
  private CandidateEntity candidate;

  @ManyToOne
  @MapsId( "electionId" )
  @JoinColumn( name = "election_id" )
  private ElectionEntity election;

  @Data
  @Embeddable
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ElectionCandidateIdEntity implements Serializable {

    @Column( name = "election_id" )
    private String electionId;

    @Column( name = "candidate_id" )
    private String candidateId;

  }

}
