package lt.gzeskas.demo.albumsbrowser.services.remote.clients.itunes;

import lt.gzeskas.demo.albumsbrowser.services.ItunesRequestRateLimiter;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.*;

class RateLimitedRestItunesClientTest {

    @Test
    void shouldEReturnExceptionWhenRateIsReached() {
        ItunesClient client = mock(ItunesClient.class);
        when(client.findTop5ByArtist(anyLong())).thenReturn(Flux.empty());
        RateLimitedRestItunesClient rateLimitedRestItunesClient = new RateLimitedRestItunesClient(
                client,
                new ItunesRequestRateLimiter(1)
        );
        rateLimitedRestItunesClient.findTop5ByArtist(3123L).blockFirst();
        rateLimitedRestItunesClient.findTop5ByArtist(31113L).onErrorResume(throwable -> Flux.empty()).blockFirst();
        verify(client, times(1)).findTop5ByArtist(anyLong());
    }
}