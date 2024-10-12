package dio.lab.quarkus.config;

import io.quarkus.arc.DefaultBean;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.sortedset.SortedSetCommands;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;

@Dependent
public class ElectionManagementConfig {

  @Produces
  @DefaultBean
  public SortedSetCommands<String, String> getSortedSetCommands( RedisDataSource redisDataSource ) {
    return redisDataSource.sortedSet( String.class, String.class );
  }

}
