package lt.gzeskas.demo.albumsbrowser.domain;

public class Artist {
    private final long id;
    private final String name;

    public Artist(long id, String name) {
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
