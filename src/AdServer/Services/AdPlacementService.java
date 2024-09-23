package AdServer.Services;

import AdServer.DBs.AdDB;
import AdServer.DBs.Cache.AdIdentifiersToAdsCache;
import AdServer.DBs.Cache.EventCache;
import AdServer.Objects.Ad;
import AdServer.Objects.AdIdentifiers;
import AdServer.Objects.AdWithScore;
import AdServer.Score.Selectors.IAdSelectorByScores;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class AdPlacementService {

    private static volatile AdPlacementService instance;
    private static final double THRESHOLD_TO_CHOOSE_CACHED_ADS = 0.75;
    private static final double THRESHOLD_RELATION_EVENT_CACHE = 0.3;

    private final AdDB adDB;
    private final EventCache eventCache;
    private final Random random = new Random();
    private final AdIdentifiersToAdsCache adCache = new AdIdentifiersToAdsCache();
    private final IAdSelectorByScores adSelectorByScores;

    private AdPlacementService(EventCache eventCache, AdDB adDB, IAdSelectorByScores adSelectorByScores) {
        this.eventCache = eventCache;
        this.adDB = adDB;
        this.adSelectorByScores = adSelectorByScores;
    }

    public static AdPlacementService getInstance(EventCache eventCache,
                                                 AdDB adDB,
                                                 IAdSelectorByScores adSelectorByScores) {
        if (instance == null) {
            synchronized (AdPlacementService.class) {
                if (instance == null) {
                    instance = new AdPlacementService(eventCache, adDB, adSelectorByScores);
                }
            }
        }
        return instance;
    }

    private boolean shouldChooseFromCachedAds(List<Ad> availableAds) {
        boolean hasHighProbability = random.nextDouble() < THRESHOLD_TO_CHOOSE_CACHED_ADS;
        boolean isEventCacheSufficient = ((double) eventCache.getNumberOfAdsInCache()
                / availableAds.size()) > THRESHOLD_RELATION_EVENT_CACHE;

        return hasHighProbability && isEventCacheSufficient;
    }

    public Ad chooseAd(AdIdentifiers adIdentifiers) {
        try {
            List<Ad> availableAds = fetchAdsFromCache(adIdentifiers);

            if (availableAds == null) {
                return null;
            }

            if (shouldChooseFromCachedAds(availableAds)) {
                Map<Integer, AdWithScore> scoresMap = eventCache.getScoresMap(availableAds);
                return adSelectorByScores.selectAd(scoresMap, availableAds);
            }

            return availableAds.isEmpty() ? null : availableAds.get(random.nextInt(availableAds.size()));
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    private List<Ad> fetchAdsFromCache(AdIdentifiers adIdentifiers) {
        List<Ad> cachedAds = adCache.getAds(adIdentifiers);
        if (cachedAds == null) {
            cachedAds = adDB.getAdsByIdentifiers(adIdentifiers);
            if (cachedAds != null) {
                adCache.putAds(adIdentifiers, cachedAds);
            }
        }
        return cachedAds;
    }
}
