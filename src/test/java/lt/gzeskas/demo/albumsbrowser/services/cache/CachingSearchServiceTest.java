package lt.gzeskas.demo.albumsbrowser.services.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lt.gzeskas.demo.albumsbrowser.domain.Album;
import lt.gzeskas.demo.albumsbrowser.domain.Artist;
import lt.gzeskas.demo.albumsbrowser.services.remote.clients.itunes.ItunesClient;
import lt.gzeskas.demo.albumsbrowser.services.stats.NotExecutedRequestStatistics;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;

class CachingSearchServiceTest {
    private final ItunesClient itunesClient = mock(ItunesClient.class);
    private final Cache<Long, Flux<Album>> cache = Caffeine.newBuilder().build();
    private final Cache<String, Flux<Artist>> artistCache = Caffeine.newBuilder().build();
    private final CachingSearchService cachingSearchService = new CachingSearchService(itunesClient, cache, artistCache, new NotExecutedRequestStatistics());

    @Test
    void shouldBeAbleToCacheTopAlbumsResponseFromService() {
        long artistAmgId = 123123L;
        when(itunesClient.findTop5ByArtist(artistAmgId))
                .thenReturn(Flux.fromIterable(
                        Arrays.asList(
                                new Album(1L, "test"),
                                new Album(2L, "test2")
                        )
                        )
                );
        cachingSearchService.findTop5ByArtist(artistAmgId).collectList().block();
        cachingSearchService.findTop5ByArtist(artistAmgId).collectList().block();
        cachingSearchService.findTop5ByArtist(artistAmgId).collectList().block();
        verify(itunesClient, times(1)).findTop5ByArtist(artistAmgId);
    }

    @Test
    void shouldBeAbleToCacheArtistResponseFromService() {
        String term = "ABBA";
        when(itunesClient.searchByTerm(term, 5))
                .thenReturn(Flux.fromIterable(
                        Collections.singletonList(
                                new Artist(1L, "ABBA")
                        ))
                );
        cachingSearchService.searchByTerm(term).collectList().block();
        cachingSearchService.searchByTerm(term).collectList().block();
        cachingSearchService.searchByTerm(term).collectList().block();
        verify(itunesClient, times(1)).searchByTerm(term, 5);
    }

}