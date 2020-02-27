package lt.gzeskas.demo.albumsbrowser.services.stats;

public class AlbumsSearchKey implements Key {
    private final long artistAmgId;

    public AlbumsSearchKey(long artistAmgId) {
        this.artistAmgId = artistAmgId;
    }

    @Override
    public Long getKey() {
        return artistAmgId;
    }

    @Override
    public Type getType() {
        return Type.ALBUM;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AlbumsSearchKey)) return false;

        AlbumsSearchKey that = (AlbumsSearchKey) o;

        return artistAmgId == that.artistAmgId;
    }

    @Override
    public int hashCode() {
        return (int) (artistAmgId ^ (artistAmgId >>> 32));
    }
}
