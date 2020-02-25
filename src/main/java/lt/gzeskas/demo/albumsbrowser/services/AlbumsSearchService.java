package lt.gzeskas.demo.albumsbrowser.services;

import lt.gzeskas.demo.albumsbrowser.domain.Album;
import reactor.core.publisher.Flux;

public interface AlbumsSearchService {
    Flux<Album> findTop5ByArtist(long artistAmgId);
}
