package dio.lab.quarkus.dtos;

import jakarta.validation.constraints.Email;

import java.util.Optional;

public record CandidateRequestDto( Optional<String> photo,
                                   String givenName,
                                   String familyName,
                                   @Email String email,
                                   Optional<String> phone,
                                   Optional<String> jobTitle ) {

}
