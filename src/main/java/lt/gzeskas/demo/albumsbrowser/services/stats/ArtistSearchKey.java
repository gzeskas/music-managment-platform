package lt.gzeskas.demo.albumsbrowser.services.stats;

public class ArtistSearchKey implements Key {
    private final String term;

    public ArtistSearchKey(String term) {
        this.term = term;
    }

    @Override
    public String getKey() {
        return term;
    }

    @Override
    public Type getType() {
        return Type.ARTIST;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArtistSearchKey)) return false;

        ArtistSearchKey that = (ArtistSearchKey) o;

        return term != null ? term.equals(that.term) : that.term == null;
    }

    @Override
    public int hashCode() {
        return term != null ? term.hashCode() : 0;
    }
}
