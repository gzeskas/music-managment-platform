package lt.gzeskas.demo.albumsbrowser.services.cache;

import com.github.benmanes.caffeine.cache.Cache;
import lt.gzeskas.demo.albumsbrowser.domain.Album;
import lt.gzeskas.demo.albumsbrowser.domain.Artist;
import lt.gzeskas.demo.albumsbrowser.services.AlbumsSearchService;
import lt.gzeskas.demo.albumsbrowser.services.ArtistSearchService;
import lt.gzeskas.demo.albumsbrowser.services.remote.clients.itunes.ItunesClient;
import lt.gzeskas.demo.albumsbrowser.services.remote.clients.itunes.exception.ItunesRequestsExceededException;
import lt.gzeskas.demo.albumsbrowser.services.stats.AlbumsSearchKey;
import lt.gzeskas.demo.albumsbrowser.services.stats.ArtistSearchKey;
import lt.gzeskas.demo.albumsbrowser.services.stats.NotExecutedRequestStatistics;
import reactor.core.publisher.Flux;

public class CachingSearchService implements AlbumsSearchService, ArtistSearchService {
    private final long DEFAULT_LIMIT_SEARCH = 5;
    private final ItunesClient itunesClient;
    private final Cache<Long, Flux<Album>> albumCache;
    private final Cache<String, Flux<Artist>> artistCache;
    private final NotExecutedRequestStatistics statisticService;

    public CachingSearchService(ItunesClient itunesClient,
                                Cache<Long, Flux<Album>> albumCache,
                                Cache<String, Flux<Artist>> artistCache,
                                NotExecutedRequestStatistics statisticService) {
        this.itunesClient = itunesClient;
        this.albumCache = albumCache;
        this.artistCache = artistCache;
        this.statisticService = statisticService;
    }

    @Override
    public Flux<Album> findTop5ByArtist(long artistAmgId) {
        albumCache.asMap().computeIfAbsent(artistAmgId, (artistId) -> getFromClient(artistId).cache());
        return albumCache.asMap().getOrDefault(artistAmgId, Flux.error(new ItunesRequestsExceededException()));
    }

    private Flux<Album> getFromClient(long artistAmgId) {
        return itunesClient.findTop5ByArtist(artistAmgId)
                .doOnError(ItunesRequestsExceededException.class, e -> statisticService.add(new AlbumsSearchKey(artistAmgId)));
    }

    @Override
    public Flux<Artist> searchByTerm(String term) {
        artistCache.asMap().computeIfAbsent(term,
                (artistId) -> itunesClient.searchByTerm(term, DEFAULT_LIMIT_SEARCH)
                        .doOnError(ItunesRequestsExceededException.class, e -> statisticService.add(new ArtistSearchKey(term)))
                        .cache());
        return artistCache.asMap().getOrDefault(term, Flux.error(new ItunesRequestsExceededException()));
    }
}
