package dio.lab.quarkus.controllers;

import dio.lab.quarkus.domain.CandidateDto;
import dio.lab.quarkus.domain.ElectionDto;
import dio.lab.quarkus.dtos.ElectionResponseDto;
import dio.lab.quarkus.services.ElectionService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@RequiredArgsConstructor
@Path( "/api/voting" )
@Produces( MediaType.APPLICATION_JSON )
@Consumes( MediaType.APPLICATION_JSON )
public class VotingController {

  private final ElectionService electionService;

  @GET
  public List<ElectionResponseDto> elections() {
    return from( this.electionService.findAll() );
  }

  @POST
  @Path( "/elections/{electionId}/candidates/{candidateId}" )
  @ResponseStatus( RestResponse.StatusCode.ACCEPTED )
  public void vote( @PathParam( "electionId" ) String electionId, @PathParam( "candidateId" ) String candidateId ) {
    this.electionService.vote( electionId, candidateId );
  }

  private List<ElectionResponseDto> from( List<ElectionDto> elections ) {
    return elections.stream().map( this::from ).toList();
  }

  private ElectionResponseDto from( ElectionDto election ) {
    return new ElectionResponseDto( election.id(), election.candidates().stream().map( CandidateDto::id ).toList() );
  }

}
