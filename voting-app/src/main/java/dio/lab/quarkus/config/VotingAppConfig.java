package dio.lab.quarkus.config;

import io.quarkus.arc.DefaultBean;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.keys.KeyCommands;
import io.quarkus.redis.datasource.sortedset.SortedSetCommands;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;

@Dependent
public class VotingAppConfig {

  @Produces
  @DefaultBean
  public SortedSetCommands<String, String> getSortedSetCommands( RedisDataSource redisDataSource ) {
    return redisDataSource.sortedSet( String.class, String.class );
  }

  @Produces
  @DefaultBean
  public KeyCommands<String> getKeyCommands( RedisDataSource redisDataSource ) {
    return redisDataSource.key( String.class );
  }

}
