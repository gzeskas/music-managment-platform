package lt.gzeskas.demo.albumsbrowser.services.stats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

class NotExecutedRequestStatisticsTest {

    @Test
    public void shouldBeAbleToRetrieveTop2Artists() {
        NotExecutedRequestStatistics statisticService = new NotExecutedRequestStatistics();
        final long firstArtistId = 1L;
        final long secondArtistId = 2L;
        final long thirdArtistId = 3L;

        statisticService.add(new AlbumsSearchKey(firstArtistId));
        statisticService.add(new AlbumsSearchKey(firstArtistId));

        statisticService.add(new AlbumsSearchKey(secondArtistId));
        statisticService.add(new AlbumsSearchKey(secondArtistId));
        statisticService.add(new AlbumsSearchKey(secondArtistId));

        statisticService.add(new AlbumsSearchKey(thirdArtistId));

        List<Key> mostPopularArtists = statisticService.getMostPopularNotExecutedRequests(2L);

        Assertions.assertEquals(2, mostPopularArtists.size());
        Assertions.assertEquals(secondArtistId, ((AlbumsSearchKey) mostPopularArtists.get(0)).getKey());
        Assertions.assertEquals(firstArtistId, ((AlbumsSearchKey) mostPopularArtists.get(1)).getKey());
    }

    @Test
    void shouldBeAbleToRemoveFromStats() {
        NotExecutedRequestStatistics statisticService = new NotExecutedRequestStatistics();
        long artistId = 1L;
        long secondArtistId = 2L;
        statisticService.add(new AlbumsSearchKey(artistId));
        statisticService.add(new AlbumsSearchKey(secondArtistId));
        statisticService.remove(Collections.singletonList(new AlbumsSearchKey(artistId)));
        List<Key> mostPopularArtists = statisticService.getMostPopularNotExecutedRequests(5L);
        Assertions.assertEquals(1, mostPopularArtists.size());
        Assertions.assertEquals(secondArtistId, ((AlbumsSearchKey) mostPopularArtists.get(0)).getKey());
    }
}