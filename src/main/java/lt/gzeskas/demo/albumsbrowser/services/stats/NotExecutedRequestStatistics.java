package lt.gzeskas.demo.albumsbrowser.services.stats;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class NotExecutedRequestStatistics {
    private final Map<Key, Long> statsByArtist = new ConcurrentHashMap<>();

    public void add(Key key) {
        statsByArtist.putIfAbsent(key, 0L);
        statsByArtist.computeIfPresent(key, (k, value) ->  ++value);
    }

    public List<Key> getMostPopularNotExecutedRequests(long limit) {
        return statsByArtist.entrySet().stream().sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public void remove(List<Key> artistsAmgId) {
        artistsAmgId.forEach(statsByArtist::remove);
    }

}
