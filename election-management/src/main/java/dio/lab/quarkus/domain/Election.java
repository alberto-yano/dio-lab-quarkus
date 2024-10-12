package dio.lab.quarkus.domain;

import java.util.Map;

public record Election( String id,
                        Map<Candidate, Integer> votes ) {

}
