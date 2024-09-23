package AdServer.Score.Selectors;

import AdServer.Objects.AdWithScore;

import java.util.Collection;
import java.util.List;

public class MedianThresholdAdSelectorByScore extends ThresholdAdSelectorByScore {

    @Override
    protected double calculateThreshold(Collection<AdWithScore> scores) {
        List<Double> sortedScores = scores.stream()
                .map(AdWithScore::getScore)
                .sorted()
                .toList();
        int size = sortedScores.size();
        return (size % 2 == 0)
                ? (sortedScores.get(size / 2 - 1) + sortedScores.get(size / 2)) / 2
                : sortedScores.get(size / 2);
    }
}
