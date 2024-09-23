package AdServer.Objects;

import java.util.Objects;

public class AdIdentifiers {

    private final int size;
    private final String language; //TODO: make this an enum
    private final String country; //TODO: make this an enum

    public AdIdentifiers(int size, String language, String country) {
        this.size = size;
        this.language = language;
        this.country = country;
    }

    public int getSize() {
        return size;
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "AdIdentifiers{" +
                "size=" + size +
                ", language=" + language +
                ", country='" + country + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdIdentifiers adIdentifiers = (AdIdentifiers) o;
        return size == adIdentifiers.size &&
                Objects.equals(language, adIdentifiers.language) &&
                Objects.equals(country, adIdentifiers.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, language, country);
    }
}
