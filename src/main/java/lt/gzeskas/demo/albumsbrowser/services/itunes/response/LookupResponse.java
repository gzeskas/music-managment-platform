package lt.gzeskas.demo.albumsbrowser.services.itunes.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lt.gzeskas.demo.albumsbrowser.services.itunes.response.entity.Entity;

import java.util.List;

public class LookupResponse {
    private final List<Entity> results;

    @JsonCreator
    public LookupResponse(@JsonProperty("results") List<Entity> results) {
        this.results = results;
    }

    public List<Entity> getResults() {
        return results;
    }
}
