package lt.gzeskas.demo.albumsbrowser.services;

import lt.gzeskas.demo.albumsbrowser.domain.Artist;
import reactor.core.publisher.Mono;

public interface FavouriteArtistService {
    Mono<Artist> findOne(long userId);
    Mono<Artist> save(long userId, Artist artist);
}
