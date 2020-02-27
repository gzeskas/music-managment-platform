package lt.gzeskas.demo.albumsbrowser.services.remote.clients.itunes.response.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "wrapperType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AlbumEntity.class, name = "collection"),
        @JsonSubTypes.Type(value = ArtistEntity.class, name = "artist")
})
public interface Entity {
    WrapperType getWrapperType();
}
