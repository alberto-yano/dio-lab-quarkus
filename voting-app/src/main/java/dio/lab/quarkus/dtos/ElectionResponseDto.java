package dio.lab.quarkus.dtos;

import java.util.List;

public record ElectionResponseDto( String id,
                                   List<String> candidates ) {

}
