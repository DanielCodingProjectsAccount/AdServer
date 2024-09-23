package AdServer.DBs.Cache;

import AdServer.Objects.Ad;
import AdServer.Objects.AdIdentifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AdIdentifiersToAdsCache {
    private final Map<AdIdentifiers, CacheEntry> cache = new ConcurrentHashMap<>();
    private static final long CACHE_EXPIRY_TIME_MS = 3600000; // 1 hour in milliseconds

    public void putAds(AdIdentifiers adIdentifiers, List<Ad> ads) {
        cache.put(adIdentifiers, new CacheEntry(ads, System.currentTimeMillis()));
    }

    public List<Ad> getAds(AdIdentifiers adIdentifiers) {
        CacheEntry entry = cache.get(adIdentifiers);
        if (entry != null && !entry.isExpired()) {
            return entry.getAds();
        }
        return null;
    }

    private static class CacheEntry {
        private final List<Ad> ads;
        private final long timestamp;

        public CacheEntry(List<Ad> ads, long timestamp) {
            this.ads = ads;
            this.timestamp = timestamp;
        }

        public List<Ad> getAds() {
            return ads;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() - timestamp > CACHE_EXPIRY_TIME_MS;
        }
    }
}
