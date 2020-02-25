package lt.gzeskas.demo.albumsbrowser.services.itunes.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lt.gzeskas.demo.albumsbrowser.services.itunes.response.entity.ArtistEntity;

import java.util.List;

public class SearchResponse {
    private final List<ArtistEntity> results;

    @JsonCreator
    public SearchResponse(@JsonProperty("results") List<ArtistEntity> results) {
        this.results = results;
    }

    public List<ArtistEntity> getResults() {
        return results;
    }
}
