package dio.lab.quarkus.repositories.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.Set;

@Data
@Entity( name = "candidates" )
public class CandidateEntity {

  @Id
  @UuidGenerator
  private String                       id;
  private String                       photo;
  @Column( name = "given_name" )
  private String                       givenName;
  @Column( name = "family_name" )
  private String                       familyName;
  private String                       email;
  private String                       phone;
  @Column( name = "job_title" )
  private String                       jobTitle;
  @OneToMany( mappedBy = "candidate" )
  private Set<ElectionCandidateEntity> votes;

}
