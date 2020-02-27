package lt.gzeskas.demo.albumsbrowser.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lt.gzeskas.demo.albumsbrowser.domain.Album;
import lt.gzeskas.demo.albumsbrowser.domain.Artist;
import lt.gzeskas.demo.albumsbrowser.services.ItunesRequestRateLimiter;
import lt.gzeskas.demo.albumsbrowser.services.cache.CachingSearchService;
import lt.gzeskas.demo.albumsbrowser.services.remote.clients.itunes.ItunesClient;
import lt.gzeskas.demo.albumsbrowser.services.remote.clients.itunes.RateLimitedRestItunesClient;
import lt.gzeskas.demo.albumsbrowser.services.remote.clients.itunes.RestItunesClient;
import lt.gzeskas.demo.albumsbrowser.services.stats.NotExecutedRequestStatistics;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Configuration
public class ItunesClientConfiguration {

    @Bean
    WebClient itunesWebClient(@Value("${itunes-service.url}") String url) {
        return WebClient
                .builder()
                .baseUrl(url)
                .build();
    }

    @Bean
    ItunesRequestRateLimiter  itunesRequestRateLimiter(@Value("${itunes-service.requestLimitPerHour}") long requestLimitPerHour) {
        return new ItunesRequestRateLimiter(requestLimitPerHour);
    }

    @Bean("itunesRestClient")
    RestItunesClient itunesRestClient(WebClient webClient,
                                      ObjectMapper objectMapper) {
        return new RestItunesClient(webClient, objectMapper);
    }
    @Bean
    @Primary
    ItunesClient rateLimitingItunesClient(@Qualifier("itunesRestClient") ItunesClient itunesClient,
                                          ItunesRequestRateLimiter itunesRequestRateLimiter) {
        return new RateLimitedRestItunesClient(itunesClient, itunesRequestRateLimiter);
    }

    @Bean
    NotExecutedRequestStatistics statisticService() {
        return new NotExecutedRequestStatistics();
    }

    @Bean
    CachingSearchService cachingAlbumSearchService(ItunesClient itunesClient,
                                                   @Value("${top5-albums.expire-after-write}") Duration expireAfterWrite,
                                                   @Value("${artist-search.expire-after-write}") Duration artistSearchExpireAfterWrite,
                                                   NotExecutedRequestStatistics notExecutedRequestStatistics) {
        Cache<Long, Flux<Album>> albumsCache = Caffeine.newBuilder()
                .expireAfterWrite(expireAfterWrite)
                .build();
        Cache<String, Flux<Artist>> artistSearchCache = Caffeine.newBuilder()
                .expireAfterWrite(artistSearchExpireAfterWrite)
                .build();
        return new CachingSearchService(itunesClient, albumsCache, artistSearchCache, notExecutedRequestStatistics);
    }

}
