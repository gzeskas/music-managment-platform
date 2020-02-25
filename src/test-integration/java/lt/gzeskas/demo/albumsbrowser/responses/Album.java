package lt.gzeskas.demo.albumsbrowser.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Album {
    private final String name;

    @JsonCreator
    public Album(@JsonProperty("name") String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
