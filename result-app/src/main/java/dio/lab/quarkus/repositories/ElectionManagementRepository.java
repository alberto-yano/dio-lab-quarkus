package dio.lab.quarkus.repositories;

import dio.lab.quarkus.dtos.ElectionDto;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient( configKey = "election-management" )
public interface ElectionManagementRepository {

  @GET
  @Path( "/api/elections" )
  Uni<List<ElectionDto>> getElections();

}
