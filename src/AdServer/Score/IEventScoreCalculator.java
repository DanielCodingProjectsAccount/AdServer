package AdServer.Score;

import AdServer.Objects.AdEventData;

public interface IEventScoreCalculator {
    double calculateScore(AdEventData adEventData);
}
