package lt.gzeskas.demo.albumsbrowser.repository.local;

import lt.gzeskas.demo.albumsbrowser.domain.Artist;
import lt.gzeskas.demo.albumsbrowser.repository.UserSettingsRepository;
import org.jdbi.v3.core.Jdbi;
import reactor.core.publisher.Mono;

public class JdbiUserSettingRepository implements UserSettingsRepository {
    private static final String FAVOURITE_ARTIST_SELECT_QUERY = "select s.favourite_artist_id, s.favourite_artist_name from settings as s where s.id = :userId";
    private final Jdbi jdbi;

    public JdbiUserSettingRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Mono<Artist> saveFavouriteArtist(long userId, Artist artist) {
        jdbi.useHandle(handle -> handle.useTransaction(h -> {
            boolean settingsExists = h.createQuery("select favourite_artist_id from settings where id = :userId")
                    .bind("userId", userId)
                    .mapTo(Long.class)
                    .findOne()
                    .isPresent();
            if (settingsExists) {
                h.createUpdate("UPDATE settings SET favourite_artist_id = :favourite_artist_id, favourite_artist_name = :favourite_artist_name WHERE id = :userId")
                        .bind("favourite_artist_id", artist.getId())
                        .bind("favourite_artist_name", artist.getName())
                        .bind("userId", userId)
                        .execute();
            } else {
                h.createUpdate("INSERT INTO settings(id, favourite_artist_id, favourite_artist_name) VALUES(:userId, :favourite_artist_id, :favourite_artist_name)")
                        .bind("favourite_artist_id", artist.getId())
                        .bind("favourite_artist_name", artist.getName())
                        .bind("userId", userId)
                        .execute();
            }
            })
        );
        return getFavouriteArtist(userId);
    }

    @Override
    public Mono<Artist> getFavouriteArtist(long userId) {
        return jdbi.withHandle(handle ->
                Mono.justOrEmpty(
                        handle.createQuery(FAVOURITE_ARTIST_SELECT_QUERY)
                                .bind("userId", userId)
                                .map((rs, ctx) -> new Artist(rs.getLong(1), rs.getString(2)))
                                .findOne()
                )
        );
    }
}
