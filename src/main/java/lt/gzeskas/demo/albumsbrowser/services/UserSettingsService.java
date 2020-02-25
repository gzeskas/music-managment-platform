package lt.gzeskas.demo.albumsbrowser.services;

import lt.gzeskas.demo.albumsbrowser.domain.Artist;
import lt.gzeskas.demo.albumsbrowser.repository.UserSettingsRepository;
import reactor.core.publisher.Mono;

public class UserSettingsService implements FavouriteArtistService {
    private final UserSettingsRepository userSettingsRepository;

    public UserSettingsService(UserSettingsRepository userSettingsRepository) {
        this.userSettingsRepository = userSettingsRepository;
    }

    @Override
    public Mono<Artist> findFavouriteArtist(long userId) {
        return userSettingsRepository.getFavouriteArtist(userId);
    }

    @Override
    public Mono<Artist> saveFavouriteArtist(long userId, Artist artist) {
        return userSettingsRepository.saveFavouriteArtist(userId, artist);
    }
}
