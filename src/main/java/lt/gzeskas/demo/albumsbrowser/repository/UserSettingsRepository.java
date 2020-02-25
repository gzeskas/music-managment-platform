package lt.gzeskas.demo.albumsbrowser.repository;

import lt.gzeskas.demo.albumsbrowser.domain.Artist;
import reactor.core.publisher.Mono;

public interface UserSettingsRepository {
    Mono<Artist> saveFavouriteArtist(long userId, Artist artist);
    Mono<Artist> getFavouriteArtist(long userId);
}
