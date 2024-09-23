package AdServer.DBs;

import AdServer.Objects.Ad;
import AdServer.Objects.AdIdentifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AdDB {

    private static volatile AdDB instance;
    private final ConcurrentMap<AdIdentifiers, ConcurrentHashMap<Integer, Ad>> adIdentifierToAdList =
            new ConcurrentHashMap<>();

    private AdDB() {
    }

    public static AdDB getInstance() {
        if (instance == null) {
            synchronized (AdDB.class) {
                if (instance == null) {
                    instance = new AdDB();
                }
            }
        }
        return instance;
    }

    public List<Ad> getAdsByIdentifiers(AdIdentifiers adIdentifiers) {
        if (!adIdentifierToAdList.containsKey(adIdentifiers)) {
            return null;
        }
        return adIdentifierToAdList.get(adIdentifiers)
                .values()
                .stream()
                .toList();
    }

    public void updateAd(Ad ad) {
        AdIdentifiers identifiers = ad.getAdIdentifiers();
        if (!adIdentifierToAdList.containsKey(identifiers)) {
            adIdentifierToAdList.put(identifiers, new ConcurrentHashMap<>());
        }
        adIdentifierToAdList.get(identifiers).put(ad.getId(), ad);
    }
}
