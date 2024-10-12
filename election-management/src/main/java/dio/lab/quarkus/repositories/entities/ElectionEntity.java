package dio.lab.quarkus.repositories.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.Set;

@Data
@Entity( name = "elections" )
public class ElectionEntity {

  @Id
  @UuidGenerator
  private String                       id;
  @OneToMany( mappedBy = "election" )
  private Set<ElectionCandidateEntity> votes;

}
