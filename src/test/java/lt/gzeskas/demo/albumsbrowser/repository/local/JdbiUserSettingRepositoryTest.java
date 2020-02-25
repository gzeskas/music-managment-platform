package lt.gzeskas.demo.albumsbrowser.repository.local;

import lt.gzeskas.demo.albumsbrowser.config.PersistenceConfiguration;
import lt.gzeskas.demo.albumsbrowser.domain.Artist;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.Random;

class JdbiUserSettingRepositoryTest {
    private final Random random = new Random();
    private final Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
    private final JdbiUserSettingRepository repository = new JdbiUserSettingRepository(jdbi);

    @BeforeEach
    void setUp() {
        jdbi.useHandle(handle -> handle.execute(PersistenceConfiguration.getInitialSqlCreateScript()));
    }

    @AfterEach
    void tearDown() {
        jdbi.useHandle(handle -> handle.execute("DROP TABLE settings;"));
    }

    @Test
    void shouldBeAbleToSaveNewUserFavouriteArtist() {
        final long userId = random.nextLong();
        final long artistId = random.nextLong();
        Mono<Artist> saved = repository.saveFavouriteArtist(userId, new Artist(artistId, "test name"));
        var artist = saved.block();
        Assertions.assertNotNull(artist);
        Assertions.assertEquals(artistId, artist.getId());
    }

    @Test
    void shouldBeAbleToUpdateExistingFavouriteUserArtist() {
        final long userId = random.nextLong();
        final long artistId = random.nextLong();
        repository.saveFavouriteArtist(userId, new Artist(artistId, "test name")).block();
        final long newArtistId = random.nextLong();
        final String newName = "some name";
        var artist = repository.saveFavouriteArtist(userId, new Artist(newArtistId, newName)).block();
        Assertions.assertNotNull(artist);
        Assertions.assertEquals(newArtistId, artist.getId());
        Assertions.assertEquals(newName, artist.getName());
    }

    @Test
    void shouldBeAbleToRetrieveFavouriteArtistIfItIsSaved() {
        final long userId = random.nextLong();
        final long artistId = random.nextLong();
        final String name = "test name";
        repository.saveFavouriteArtist(userId, new Artist(artistId, name)).block();
        var artist = repository.getFavouriteArtist(userId).block();
        Assertions.assertNotNull(artist);
        Assertions.assertEquals(artistId, artist.getId());
        Assertions.assertEquals(name, artist.getName());
    }

    @Test
    void shouldReturnEmptyResultIfThereIsNoFavouriteArtistSaved() {
        Mono<Artist> favouriteArtist = repository.getFavouriteArtist(random.nextLong());
        Assertions.assertTrue(favouriteArtist.blockOptional().isEmpty());
    }
}