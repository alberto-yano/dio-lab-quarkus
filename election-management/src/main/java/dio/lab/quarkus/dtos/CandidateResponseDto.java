package dio.lab.quarkus.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Optional;

@JsonInclude( JsonInclude.Include.NON_EMPTY )
public record CandidateResponseDto( String id,
                                    Optional<String> photo,
                                    String fullName,
                                    String email,
                                    Optional<String> phone,
                                    Optional<String> jobTitle ) {

}
