package lt.gzeskas.demo.albumsbrowser.services;

import lt.gzeskas.demo.albumsbrowser.domain.Artist;
import reactor.core.publisher.Flux;

import java.util.stream.Stream;

public interface ArtistSearchService {
    Flux<Artist> searchByTerm(String term, int limit);
}
