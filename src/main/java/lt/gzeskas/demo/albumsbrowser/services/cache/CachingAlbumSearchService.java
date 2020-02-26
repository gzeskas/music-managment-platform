package lt.gzeskas.demo.albumsbrowser.services.cache;

import lt.gzeskas.demo.albumsbrowser.domain.Album;
import lt.gzeskas.demo.albumsbrowser.services.AlbumsSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

public class CachingAlbumSearchService implements AlbumsSearchService {
    private static final Logger logger = LoggerFactory.getLogger(CachingAlbumSearchService.class);
    private final AlbumsSearchService delegate;
    private final long requestCountPerHour;
    private final Map<Long, Flux<Album>> cache = new ConcurrentHashMap<>();
    private final AtomicLong requestCounter = new AtomicLong(0);
    private final Queue<Long> pendingRequests = new ConcurrentLinkedQueue<>();

    public CachingAlbumSearchService(AlbumsSearchService delegate, long requestCountPerHour) {
        this.delegate = delegate;
        this.requestCountPerHour = requestCountPerHour;
    }

    @Override
    public Flux<Album> findTop5ByArtist(long artistAmgId) {
        cache.computeIfAbsent(artistAmgId, (artistId) -> queryDelegate(artistId).cache());
        return cache.getOrDefault(artistAmgId, Flux.empty());
    }

    private Flux<Album> queryDelegate(long artistAmgId) {
        if (requestCounter.incrementAndGet() > requestCountPerHour) {
            pendingRequests.add(artistAmgId);
            logger.info("Received to much request to query, putting request to queue, artistAmgId: {}", artistAmgId);
            return Flux.empty();
        }
        return delegate.findTop5ByArtist(artistAmgId);
    }

    @Scheduled(fixedDelay = 1000L * 3600L) //every hour
    public void tryExecutePendingRequest() {
        requestCounter.getAndSet(0L);
        logger.info("There is pending request count: {}", pendingRequests.size());
        pendingRequests
                .forEach(artistAmgId -> cache.put(artistAmgId, queryDelegate(artistAmgId).cache()));
    }

}
