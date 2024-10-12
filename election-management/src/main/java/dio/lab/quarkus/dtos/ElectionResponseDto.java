package dio.lab.quarkus.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Optional;

public record ElectionResponseDto( String id,
                                   List<CandidateWithVoteResponseDto> candidates ) {

  @JsonInclude( JsonInclude.Include.NON_EMPTY )
  public record CandidateWithVoteResponseDto( String id,
                                              Optional<String> photo,
                                              String fullName,
                                              String email,
                                              Optional<String> phone,
                                              Optional<String> jobTitle,
                                              Integer votes ) {

  }

}
