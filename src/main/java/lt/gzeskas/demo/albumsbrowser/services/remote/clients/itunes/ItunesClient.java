package lt.gzeskas.demo.albumsbrowser.services.remote.clients.itunes;

import lt.gzeskas.demo.albumsbrowser.domain.Album;
import lt.gzeskas.demo.albumsbrowser.domain.Artist;
import reactor.core.publisher.Flux;

public interface ItunesClient {
    Flux<Artist> searchByTerm(String term, long limit);
    Flux<Album> findTop5ByArtist(long artistAmgId);
}
