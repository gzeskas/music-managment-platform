package lt.gzeskas.demo.albumsbrowser.services;

import lt.gzeskas.demo.albumsbrowser.domain.Artist;

import java.util.stream.Stream;

public interface ArtistSearchService {
    Stream<Artist> searchByTerm(String term);
}
