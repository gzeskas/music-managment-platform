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
    public Mono<Artist> findOne(long userId) {
        return null;
    }

    @Override
    public Mono<Artist> save(long userId, Artist artist) {
        return null;
    }
}
