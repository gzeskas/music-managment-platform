package lt.gzeskas.demo.albumsbrowser.services.itunes;

import lt.gzeskas.demo.albumsbrowser.domain.Artist;
import lt.gzeskas.demo.albumsbrowser.services.ArtistSearchService;

import java.util.stream.Stream;

public class ItunesWebService implements ArtistSearchService {
    @Override
    public Stream<Artist> searchByTerm(String term) {
        return Stream.of(new Artist(term));
    }
}
