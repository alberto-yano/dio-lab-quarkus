package dio.lab.quarkus.domain;

import java.util.List;

public record ElectionDto( String id,
                           List<CandidateDto> candidates ) {

}
