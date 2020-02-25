package lt.gzeskas.demo.albumsbrowser.web.view;

import lt.gzeskas.demo.albumsbrowser.domain.Artist;

public class ExternalArtist {
    private final String name;

    public ExternalArtist(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ExternalArtist map(Artist artist) {
        return new ExternalArtist(artist.getName());
    }

}
