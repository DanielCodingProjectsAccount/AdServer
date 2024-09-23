package AdServer;

import AdServer.DBs.Cache.EventCache;
import AdServer.DBs.Cache.EventCacheUpdater;
import AdServer.Objects.Ad;
import AdServer.DBs.AdDB;
import AdServer.Objects.AdIdentifiers;
import AdServer.Requests.AdEventRequest;
import AdServer.Score.Selectors.MedianThresholdAdSelectorByScore;
import AdServer.Score.SimpleAdEventScoreCalculator;
import AdServer.Services.AdEventService;
import AdServer.Services.AdPlacementService;
import AdServer.Services.AggregatorService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class AdServerService {
    private final EventCache eventCache = EventCache.getInstance();
    private final SimpleAdEventScoreCalculator simpleAdEventScoreCalculator = new SimpleAdEventScoreCalculator();
    private final EventCacheUpdater eventCacheUpdater = EventCacheUpdater.getInstance(simpleAdEventScoreCalculator);
    private final MedianThresholdAdSelectorByScore medianSelector =
            new MedianThresholdAdSelectorByScore();
    private final AdDB adDB = AdDB.getInstance();
    private final int eventServicePoolSize = 4;
    private final AdPlacementService adPlacementService = AdPlacementService.getInstance(eventCache, adDB, medianSelector);
    private final AggregatorService aggregatorService = AggregatorService.getInstance();
    private final AdEventService adEventService = new AdEventService(eventServicePoolSize, aggregatorService);
    private final BlockingQueue<AdEventRequest> eventRequestQueue = new LinkedBlockingQueue<>();

    public AdServerService() {
        adEventService.startConsumers(eventRequestQueue);
    }

    public Ad getAd(int size, String language, String country) {
        try {
            AdIdentifiers adIdentifiers = new AdIdentifiers(size, language, country);
            return adPlacementService.chooseAd(adIdentifiers);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public void postAdEvent(int adId, String eventType) {
        try {
            AdEventRequest request = new AdEventRequest(adId, eventType);
            eventRequestQueue.offer(request);
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public static List<AdIdentifiers> initAdDb(int MAX_AD_ID,
                                               List<Integer> sizeList,
                                               List<String> countryList,
                                               List<String> languageList,
                                               List<String> adDataList) {
        List<AdIdentifiers> possibleIdentifiers = new ArrayList<>();

        Random random = new Random();
        AdDB adDbToFill = AdDB.getInstance();

        for (int i = 1; i <= MAX_AD_ID; ++i) {
            int size = sizeList.get(random.nextInt(sizeList.size()));
            String country = countryList.get(random.nextInt(countryList.size()));
            String language = languageList.get(random.nextInt(languageList.size()));
            AdIdentifiers adIdentifiers = new AdIdentifiers(size, language, country);

            if (!possibleIdentifiers.contains(adIdentifiers)) {
                possibleIdentifiers.add(adIdentifiers);
            }

            String adData = adDataList.get(random.nextInt(adDataList.size())) + ", " + adIdentifiers.toString();


            Ad ad = new Ad(i, adData, adIdentifiers);
            adDbToFill.updateAd(ad);
        }

        System.out.println("Initialized AdDB");
        return possibleIdentifiers;
    }
}
