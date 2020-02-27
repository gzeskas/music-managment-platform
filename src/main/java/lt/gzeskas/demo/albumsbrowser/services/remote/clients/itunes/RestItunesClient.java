package lt.gzeskas.demo.albumsbrowser.services.remote.clients.itunes;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.gzeskas.demo.albumsbrowser.domain.Album;
import lt.gzeskas.demo.albumsbrowser.domain.Artist;
import lt.gzeskas.demo.albumsbrowser.services.remote.clients.itunes.response.LookupResponse;
import lt.gzeskas.demo.albumsbrowser.services.remote.clients.itunes.response.SearchResponse;
import lt.gzeskas.demo.albumsbrowser.services.remote.clients.itunes.response.entity.AlbumEntity;
import lt.gzeskas.demo.albumsbrowser.services.remote.clients.itunes.response.entity.WrapperType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class RestItunesClient implements ItunesClient {
    private static final Logger logger = LoggerFactory.getLogger(RestItunesClient.class);
    private static final String SEARCH_ARTIST_BY_TERM_TEMPLATE = "/search?entity=allArtist&term={term}&limit=5";
    private static final String FIND_TOP_5_ALBUMS_BY_ARTIST_TEMPLATE = "/lookup?amgArtistId={artistAmgId}&entity=album&limit=5";
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public RestItunesClient(WebClient webClient,
                            ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public Flux<Artist> searchByTerm(String term, long limit) {
            return webClient.method(HttpMethod.GET)
                    .uri(SEARCH_ARTIST_BY_TERM_TEMPLATE, term, limit)
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .flatMap(bytes -> readValue(bytes, SearchResponse.class))
                    .flatMapIterable(SearchResponse::getResults)
                    .onErrorContinue((throwable, o) -> logger.error("Couldn't query itunes service.", throwable))
                    .map(info -> new Artist(info.getAmgArtistId(), info.getArtistName()));
    }

    @Override
    public Flux<Album> findTop5ByArtist(long artistAmgId) {
        return webClient.method(HttpMethod.GET)
                .uri(FIND_TOP_5_ALBUMS_BY_ARTIST_TEMPLATE, artistAmgId)
                .retrieve()
                .bodyToMono(byte[].class)
                .flatMap(bytes -> readValue(bytes, LookupResponse.class))
                .flatMapIterable(LookupResponse::getResults)
                .filter(entity -> entity.getWrapperType().equals(WrapperType.Collection))
                .map(entity -> (AlbumEntity) entity)
                .onErrorContinue((throwable, o) -> logger.error("Couldn't query itunes service.", throwable))
                .map(entity -> new Album(entity.getCollectionId(), entity.getCollectionName()));
    }

    private <T> Mono<T> readValue(byte[] body, Class<T> clazz) {
        return Mono.fromCompletionStage(() -> CompletableFuture.supplyAsync(() -> {
            try {
                return objectMapper.readValue(body, clazz);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }
}
