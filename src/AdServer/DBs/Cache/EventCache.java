package AdServer.DBs.Cache;

import AdServer.Objects.Ad;
import AdServer.Objects.AdWithScore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class EventCache {

    private static volatile EventCache instance;
    private final ConcurrentMap<Integer, Double> adScores = new ConcurrentHashMap<>();

    private EventCache() {
    }

    public static EventCache getInstance() {
        if (instance == null) {
            synchronized (EventCache.class) {
                if (instance == null) {
                    instance = new EventCache();
                }
            }
        }
        return instance;
    }

    public void updateAdScore(int adId, double score) {
        adScores.put(adId, score);
    }

    public Map<Integer, AdWithScore> getScoresMap(List<Ad> ads) {
        Map<Integer, AdWithScore> scoresMap = new HashMap<>();
        for (Ad ad : ads) {
            int adId = ad.getId();
            scoresMap.put(adId, new AdWithScore(ad, this.adScores.get(adId)));
        }
        return scoresMap;
    }

    public int getNumberOfAdsInCache() {
        return this.adScores.size();
    }

    public int size() {
        return this.adScores.size();
    }

    @Override
    public String toString() {
        try {
            return adScores.entrySet()
                    .stream()
                    .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                    .map(entry -> String.format("%d : %.3f", entry.getKey(), entry.getValue()))
                    .collect(Collectors.joining(", "));
        } catch (Exception e) {
            System.out.println(e);
        }
        return "";
    }
}
