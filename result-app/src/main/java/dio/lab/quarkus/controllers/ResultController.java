package dio.lab.quarkus.controllers;

import dio.lab.quarkus.dtos.ElectionDto;
import dio.lab.quarkus.repositories.ElectionManagementRepository;
import io.smallrye.mutiny.Multi;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestStreamElementType;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Path( "/" )
public class ResultController {

  private final ElectionManagementRepository electionManagementRepository;

  public ResultController( @RestClient ElectionManagementRepository electionManagementRepository ) {
    this.electionManagementRepository = electionManagementRepository;
  }

  @GET
  @RestStreamElementType( MediaType.APPLICATION_JSON )
  public Multi<List<ElectionDto>> results() {
    return Multi.createFrom().ticks().every( Duration.of( 5, ChronoUnit.SECONDS ) ).onItem().transformToMultiAndMerge(
        aLong -> this.electionManagementRepository.getElections().toMulti() );
  }

}
