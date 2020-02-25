package lt.gzeskas.demo.albumsbrowser.services;

import lt.gzeskas.demo.albumsbrowser.domain.Artist;
import reactor.core.publisher.Mono;

public interface FavouriteArtistService {
    Mono<Artist> findFavouriteArtist(long userId);
    Mono<Artist> saveFavouriteArtist(long userId, Artist artist);
}
