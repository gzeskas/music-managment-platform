package lt.gzeskas.demo.albumsbrowser.services.itunes;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.gzeskas.demo.albumsbrowser.domain.Album;
import lt.gzeskas.demo.albumsbrowser.domain.Artist;
import lt.gzeskas.demo.albumsbrowser.services.AlbumsSearchService;
import lt.gzeskas.demo.albumsbrowser.services.ArtistSearchService;
import lt.gzeskas.demo.albumsbrowser.services.itunes.response.SearchResponse;
import lt.gzeskas.demo.albumsbrowser.services.itunes.response.entity.WrapperType;
import lt.gzeskas.demo.albumsbrowser.services.itunes.response.entity.AlbumEntity;
import lt.gzeskas.demo.albumsbrowser.services.itunes.response.LookupResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

public class ItunesWebService implements ArtistSearchService, AlbumsSearchService {
    private static final Logger logger = LoggerFactory.getLogger(ItunesWebService.class);
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public ItunesWebService(WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public Flux<Artist> searchByTerm(String term, int limit) {
            return webClient.method(HttpMethod.GET)
                    .uri("/search?entity=allArtist&term={term}&limit={limit}", term, limit)
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .flatMap(bytes -> readValue(bytes, SearchResponse.class))
                    .flatMapIterable(SearchResponse::getResults)
                    .onErrorContinue((throwable, o) -> logger.error("Couldn't query itunes service.", throwable))
                    .map(info -> new Artist(info.getAmgArtistId(), info.getArtistName()));
    }

    @Override
    public Flux<Album> findTop5ByArtist(long artistAmgId) {
        //TODO: URI template to const
        return webClient.method(HttpMethod.GET)
                .uri("/lookup?amgArtistId={artistAmgId}&entity=album&limit=5", artistAmgId)
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
        try {
            return Mono.just(objectMapper.readValue(body, clazz));
        } catch (IOException e) {
            return Mono.error(e);
        }
    }
}
