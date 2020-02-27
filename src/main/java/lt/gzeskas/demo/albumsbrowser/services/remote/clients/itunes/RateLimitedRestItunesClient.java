package lt.gzeskas.demo.albumsbrowser.services.remote.clients.itunes;

import lt.gzeskas.demo.albumsbrowser.domain.Album;
import lt.gzeskas.demo.albumsbrowser.domain.Artist;
import lt.gzeskas.demo.albumsbrowser.services.ItunesRequestRateLimiter;
import lt.gzeskas.demo.albumsbrowser.services.remote.clients.itunes.exception.ItunesRequestsExceededException;
import reactor.core.publisher.Flux;

public class RateLimitedRestItunesClient implements ItunesClient {
    private final ItunesClient delegate;
    private final ItunesRequestRateLimiter itunesRequestRateLimiter;

    public RateLimitedRestItunesClient(ItunesClient delegate,
                                       ItunesRequestRateLimiter itunesRequestRateLimiter) {
        this.delegate = delegate;
        this.itunesRequestRateLimiter = itunesRequestRateLimiter;
    }

    @Override
    public Flux<Artist> searchByTerm(String term, long limit) {
        if (!itunesRequestRateLimiter.isRequestAvailable()) {
            return Flux.error(new ItunesRequestsExceededException());
        }
        return delegate.searchByTerm(term, limit);
    }

    @Override
    public Flux<Album> findTop5ByArtist(long artistAmgId) {
        if (!itunesRequestRateLimiter.isRequestAvailable()) {
            return Flux.error(new ItunesRequestsExceededException(), true);
        }
        return delegate.findTop5ByArtist(artistAmgId);
    }
}
