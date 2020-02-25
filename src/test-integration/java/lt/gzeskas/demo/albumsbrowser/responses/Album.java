package lt.gzeskas.demo.albumsbrowser.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Album {
    private final long id;
    private final String name;

    @JsonCreator
    public Album(@JsonProperty("id") long id,
                 @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }
}
