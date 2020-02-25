package lt.gzeskas.demo.albumsbrowser.web.view;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lt.gzeskas.demo.albumsbrowser.domain.Artist;

public class ExternalArtist {
    private final long id;
    private final String name;

    @JsonCreator
    public ExternalArtist(@JsonProperty("id") long id,
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

    public static ExternalArtist map(Artist artist) {
        return new ExternalArtist(artist.getId(), artist.getName());
    }

}
