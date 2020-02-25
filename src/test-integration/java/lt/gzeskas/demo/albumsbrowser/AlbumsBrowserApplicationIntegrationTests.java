package lt.gzeskas.demo.albumsbrowser;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.gzeskas.demo.albumsbrowser.payload.Artist;
import lt.gzeskas.demo.albumsbrowser.responses.Album;
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
                .uri(URI.create("http://localhost:8080/search/artist?term=" + searchTerm + "&limit=5"))
                .setHeader("Accept", "application/json")
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HTTP_OK, response.statusCode());

        var artistSearchResponse = objectMapper.readValue(response.body(), ArtistSearchResponse[].class);
        Assertions.assertEquals(5, artistSearchResponse.length);
        Assertions.assertEquals("ABBA", artistSearchResponse[0].getName());
    }

    @Test
    void shouldBeAbleToRetrieveTop5AlbumsByArtist() throws Exception {
        final long artistAmgId = 3492L;
        var request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/artist/" + artistAmgId + "/albums/top5"))
                .setHeader("Accept", "application/json")
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HTTP_OK, response.statusCode());

        var albums = objectMapper.readValue(response.body(), Album[].class);
        Assertions.assertEquals(5, albums.length);
        Assertions.assertEquals("Gold: Greatest Hits", albums[0].getName());
        Assertions.assertEquals("More ABBA Gold", albums[1].getName());
        Assertions.assertEquals("20th Century Masters - The Millennium Collection: The Best of ABBA", albums[2].getName());
        Assertions.assertEquals("The Essential Collection", albums[3].getName());
        Assertions.assertEquals("The Visitors", albums[4].getName());
    }

    @Test
    void shouldBeAbleToSaveFavouriteArtist() throws Exception {
        final long userId = 12323L;
        var favouriteArtist = new Artist(23123L, "some name");
        var request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(favouriteArtist)))
                .uri(URI.create("http://localhost:8080/user/" + userId + "/settings/favourite/artist"))
                .setHeader("Accept", "application/json")
                .setHeader("Content-type", "application/json")
                .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HTTP_OK, response.statusCode());
        var savedArtist = objectMapper.readValue(response.body(), Artist.class);
        Assertions.assertEquals(favouriteArtist, savedArtist);
    }
}
