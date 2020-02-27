package lt.gzeskas.demo.albumsbrowser.config;

import lt.gzeskas.demo.albumsbrowser.services.AlbumsSearchService;
import lt.gzeskas.demo.albumsbrowser.services.ArtistSearchService;
import lt.gzeskas.demo.albumsbrowser.services.ItunesRequestRateLimiter;
import lt.gzeskas.demo.albumsbrowser.services.stats.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
public class SchedulersConfiguration {
    private final NotExecutedRequestStatistics statisticService;
    private final AlbumsSearchService albumsSearchService;
    private final ArtistSearchService artistSearchService;
    private final ItunesRequestRateLimiter itunesRequestRateLimiter;

    public SchedulersConfiguration(NotExecutedRequestStatistics statisticService,
                                   AlbumsSearchService albumsSearchService,
                                   ArtistSearchService artistSearchService,
                                   ItunesRequestRateLimiter itunesRequestRateLimiter) {
        this.statisticService = statisticService;
        this.albumsSearchService = albumsSearchService;
        this.artistSearchService = artistSearchService;
        this.itunesRequestRateLimiter = itunesRequestRateLimiter;
    }

    @Scheduled(cron = "0 0,59 * ? * * *") //at the end of every hour.
    public void updateMostPopularNotResolvedRequests() {
        List<Key> mostPopularKeys = statisticService.getMostPopularNotExecutedRequests(itunesRequestRateLimiter.reset());
        mostPopularKeys.forEach(
                identity -> {
                    if (identity.getType().equals(Type.ARTIST)) {
                        artistSearchService.searchByTerm(((ArtistSearchKey) identity).getKey());
                    } else if (identity.getType().equals(Type.ALBUM)) {
                        albumsSearchService.findTop5ByArtist(((AlbumsSearchKey) identity).getKey());
                    }
                }
        );
        statisticService.remove(mostPopularKeys);
    }

}
