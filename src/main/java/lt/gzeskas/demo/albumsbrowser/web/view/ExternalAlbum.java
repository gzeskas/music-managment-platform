package lt.gzeskas.demo.albumsbrowser.web.view;

import lt.gzeskas.demo.albumsbrowser.domain.Album;

public class ExternalAlbum {
    private final long id;
    private final String name;

    public ExternalAlbum(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public static ExternalAlbum map(Album album) {
        return new ExternalAlbum(album.getId(), album.getName());
    }

}
