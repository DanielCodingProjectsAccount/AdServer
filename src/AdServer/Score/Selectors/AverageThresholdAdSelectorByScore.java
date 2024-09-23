package AdServer.Score.Selectors;

import AdServer.Objects.AdWithScore;

import java.util.Collection;

public class AverageThresholdAdSelectorByScore extends ThresholdAdSelectorByScore {

    @Override
    protected double calculateThreshold(Collection<AdWithScore> scores) {
        return scores.stream()
                .mapToDouble(AdWithScore::getScore)
                .average()
                .orElse(0.0);
    }
}
