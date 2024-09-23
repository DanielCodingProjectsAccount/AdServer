package AdServer.Score.Selectors;

import AdServer.Objects.Ad;
import AdServer.Objects.AdWithScore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class ThresholdAdSelectorByScore implements IAdSelectorByScores {
    protected static final double HIGH_AD_PROBABILITY = 0.8;
    protected final Random random = new Random();

    @Override
    public Ad selectAd(Map<Integer, AdWithScore> scoresMap, List<Ad> availableAds) {
        List<Ad> greatAds = new ArrayList<>();
        List<Ad> okAds = new ArrayList<>();
        double thresholdScore = calculateThreshold(scoresMap.values());

        fillAdLists(scoresMap, greatAds, okAds, thresholdScore);

        if (!greatAds.isEmpty() && (okAds.isEmpty() || random.nextDouble() < HIGH_AD_PROBABILITY)) {
            return greatAds.get(random.nextInt(greatAds.size()));
        } else if (!okAds.isEmpty()) {
            return okAds.get(random.nextInt(okAds.size()));
        }

        return availableAds.get(random.nextInt(availableAds.size()));
    }

    protected abstract double calculateThreshold(Collection<AdWithScore> scores);

    private void fillAdLists(Map<Integer, AdWithScore> scoresMap,
                             List<Ad> greatAds,
                             List<Ad> okAds,
                             double thresholdScore) {
        for (Map.Entry<Integer, AdWithScore> entry : scoresMap.entrySet()) {
            int adId = entry.getKey();
            double score = entry.getValue().getScore();

            if (score > thresholdScore) {
                greatAds.add(entry.getValue().getAd());
            } else {
                okAds.add(entry.getValue().getAd());
            }
        }
    }
}
