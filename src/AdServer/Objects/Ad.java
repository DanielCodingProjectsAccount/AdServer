package AdServer.Objects;

public class Ad {
    private final int id;
    private final String data;
    private final AdIdentifiers adIdentifiers;

    public Ad(int id, String data, AdIdentifiers adIdentifiers) {
        this.id = id;
        this.data = data;
        this.adIdentifiers = adIdentifiers;
    }

    public int getId() {
        return id;
    }

    public AdIdentifiers getAdIdentifiers() {
        return adIdentifiers;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return data;
    }
}
