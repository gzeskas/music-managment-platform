package lt.gzeskas.demo.albumsbrowser;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.gzeskas.demo.albumsbrowser.responses.ArtistSearchResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class AlbumsBrowserApplicationIntegrationTests {
    private static final int HTTP_OK = 200;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(2))
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldBeAbleToSearchForArtist() throws Exception {
        final var searchTerm = "Abba";
        var request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/search/artist?term=" + searchTerm))
                .setHeader("Accept", "application/json")
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HTTP_OK, response.statusCode());

        var artistSearchResponse = objectMapper.readValue(response.body(), ArtistSearchResponse[].class);
        Assertions.assertEquals(1, artistSearchResponse.length);
        Assertions.assertEquals(searchTerm, artistSearchResponse[0].getName());
    }

    @Test
    void shouldBeAbleToSaveFavouriteArtist() throws Exception {

    }

    @Test
    void shouldBeAbleToRetrieveTop5AlbumsByArtist() throws Exception {

    }
}
