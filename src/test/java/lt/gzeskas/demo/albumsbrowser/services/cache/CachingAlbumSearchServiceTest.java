package lt.gzeskas.demo.albumsbrowser.services.cache;

import lt.gzeskas.demo.albumsbrowser.domain.Album;
import lt.gzeskas.demo.albumsbrowser.services.AlbumsSearchService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class CachingAlbumSearchServiceTest {
    private final AlbumsSearchService albumsSearchServiceMock = mock(AlbumsSearchService.class);
    private final CachingAlbumSearchService cachingAlbumSearchService = new CachingAlbumSearchService(albumsSearchServiceMock, 1);

    @AfterEach
    void tearDown() {
        cachingAlbumSearchService.tryExecutePendingRequest();
    }

    @Test
    void shouldBeAbleToCacheResponseFromService() {
        long artistAmgId = 123123L;
        when(albumsSearchServiceMock.findTop5ByArtist(artistAmgId))
                .thenReturn(Flux.fromIterable(
                            Arrays.asList(
                                    new Album(1L, "test"),
                                    new Album(2L, "test2")
                            )
                        )
                );
        cachingAlbumSearchService.findTop5ByArtist(artistAmgId).collectList().block();
        cachingAlbumSearchService.findTop5ByArtist(artistAmgId).collectList().block();
        cachingAlbumSearchService.findTop5ByArtist(artistAmgId).collectList().block();
        verify(albumsSearchServiceMock, times(1)).findTop5ByArtist(artistAmgId);
    }

    @Test
    void shouldBeAbleToProcessRequestsFromQueue() {
        long artistAmgId = 123123L;
        when(albumsSearchServiceMock.findTop5ByArtist(artistAmgId))
                .thenReturn(Flux.fromIterable(
                        Arrays.asList(
                                new Album(1L, "test"),
                                new Album(2L, "test2")
                        )
                        )
                );

        when(albumsSearchServiceMock.findTop5ByArtist(artistAmgId + 1))
                .thenReturn(Flux.fromIterable(Collections.singletonList(new Album(3L, "test3"))));
        cachingAlbumSearchService.findTop5ByArtist(artistAmgId).collectList().block();
        cachingAlbumSearchService.findTop5ByArtist(artistAmgId + 1).collectList().block();
        verify(albumsSearchServiceMock, times(1)).findTop5ByArtist(artistAmgId);
        verify(albumsSearchServiceMock, times(0)).findTop5ByArtist(artistAmgId +  1);
        cachingAlbumSearchService.tryExecutePendingRequest();
        verify(albumsSearchServiceMock, times(1)).findTop5ByArtist(artistAmgId +  1);
    }
}