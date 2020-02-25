package lt.gzeskas.demo.albumsbrowser.domain;

public class Album {
    private final long id;
    private final String name;

    public Album(long id, String name) {
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

