package lt.gzeskas.demo.albumsbrowser.services.remote.clients.itunes.response.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ArtistEntity implements Entity {
    private final String artistName;
    private final long amgArtistId;

    @JsonCreator
    public ArtistEntity(@JsonProperty("artistName") String artistName,
                        @JsonProperty("amgArtistId") long amgArtistId) {
        this.artistName = artistName;
        this.amgArtistId = amgArtistId;
    }

    @Override
    public WrapperType getWrapperType() {
        return WrapperType.Artist;
    }

    public String getArtistName() {
        return artistName;
    }

    public long getAmgArtistId() {
        return amgArtistId;
    }
}
