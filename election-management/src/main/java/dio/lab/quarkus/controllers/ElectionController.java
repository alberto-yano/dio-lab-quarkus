package dio.lab.quarkus.controllers;

import dio.lab.quarkus.domain.Election;
import dio.lab.quarkus.dtos.ElectionResponseDto;
import dio.lab.quarkus.services.ElectionService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@RequiredArgsConstructor
@Path( "/api/elections" )
@Produces( MediaType.APPLICATION_JSON )
@Consumes( MediaType.APPLICATION_JSON )
public class ElectionController {

  private final ElectionService electionService;

  @GET
  public List<ElectionResponseDto> listElections() {
    return toElectionResponseDto( this.electionService.findAll() );
  }

  @POST
  @ResponseStatus( RestResponse.StatusCode.CREATED )
  public void createElection() {
    this.electionService.createElection();
  }

  private List<ElectionResponseDto> toElectionResponseDto( List<Election> elections ) {
    return elections.stream().map( this::toElectionResponseDto ).toList();
  }

  private ElectionResponseDto toElectionResponseDto( Election election ) {
    return new ElectionResponseDto( election.id(), election.votes().entrySet().stream().map(
        entry -> new ElectionResponseDto.CandidateWithVoteResponseDto( entry.getKey().id(), entry.getKey().photo(),
                                                                       entry.getKey().givenName() + " " + entry.getKey().familyName(),
                                                                       entry.getKey().email(), entry.getKey().phone(),
                                                                       entry.getKey().jobTitle(),
                                                                       entry.getValue() ) ).toList() );
  }

}
