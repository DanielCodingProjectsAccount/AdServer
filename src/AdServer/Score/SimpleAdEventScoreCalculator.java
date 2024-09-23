package AdServer.Score;

import AdServer.Objects.AdEventData;

public class SimpleAdEventScoreCalculator implements IEventScoreCalculator {

    public SimpleAdEventScoreCalculator() {

    }

    public double calculateScore(AdEventData adEventData) {
        int impressions = adEventData.getImpressions();
        int clicks = adEventData.getClicks();
        int registers = adEventData.getRegisters();

        double clicksToImpressions = 0.0;
        double registersToClicks = 0.0;
        if (impressions != 0.0) {
            clicksToImpressions = ((double) clicks / impressions);
        }
        if (clicks != 0.0) {
            registersToClicks =  ((double) registers / clicks);
        }
        double result =  0.7 * clicksToImpressions + 0.3 * registersToClicks;
        return result;
    }
}
