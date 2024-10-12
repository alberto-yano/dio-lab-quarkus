package dio.lab.quarkus.controllers;

import dio.lab.quarkus.domain.Candidate;
import dio.lab.quarkus.dtos.CandidateRequestDto;
import dio.lab.quarkus.dtos.CandidateResponseDto;
import dio.lab.quarkus.services.CandidateService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@RequiredArgsConstructor
@Path( "/api/candidates" )
@Produces( MediaType.APPLICATION_JSON )
@Consumes( MediaType.APPLICATION_JSON )
public class CandidateController {

  private final CandidateService candidateService;

  @GET
  public List<CandidateResponseDto> findAll() {
    return toCandidateResponseDto( this.candidateService.findAll() );
  }

  @POST
  @ResponseStatus( RestResponse.StatusCode.CREATED )
  public void createCandidate( CandidateRequestDto candidateRequest ) {
    this.candidateService.save( toCandidate( candidateRequest ) );
  }

  @PUT
  @Path( "/{id}" )
  public CandidateResponseDto updateCandidate( @PathParam( "id" ) String id, CandidateRequestDto candidateRequest ) {
    return toCandidateResponseDto( this.candidateService.save( toCandidate( id, candidateRequest ) ) );
  }

  private List<CandidateResponseDto> toCandidateResponseDto( List<Candidate> candidates ) {
    return candidates.stream().map( this::toCandidateResponseDto ).toList();
  }

  private CandidateResponseDto toCandidateResponseDto( Candidate candidate ) {
    return new CandidateResponseDto( candidate.id(), candidate.photo(),
                                     candidate.givenName() + " " + candidate.familyName(), candidate.email(),
                                     candidate.phone(), candidate.jobTitle() );
  }

  private Candidate toCandidate( CandidateRequestDto candidateRequestDto ) {
    return toCandidate( null, candidateRequestDto );
  }

  private Candidate toCandidate( String id, CandidateRequestDto candidateRequestDto ) {
    return new Candidate( id, candidateRequestDto.photo(), candidateRequestDto.givenName(),
                          candidateRequestDto.familyName(), candidateRequestDto.email(), candidateRequestDto.phone(),
                          candidateRequestDto.jobTitle() );
  }

}
