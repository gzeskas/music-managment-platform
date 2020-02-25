package lt.gzeskas.demo.albumsbrowser.services.itunes.response.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AlbumEntity implements Entity {
    private final long collectionId;
    private final String collectionName;

    @JsonCreator
    public AlbumEntity(@JsonProperty("collectionId") long collectionId,
                       @JsonProperty("collectionName") String collectionName) {
        this.collectionId = collectionId;
        this.collectionName = collectionName;
    }

    @Override
    public WrapperType getWrapperType() {
        return WrapperType.Collection;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public long getCollectionId() {
        return collectionId;
    }
}
