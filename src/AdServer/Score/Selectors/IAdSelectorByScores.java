package AdServer.Score.Selectors;

import AdServer.Objects.Ad;
import AdServer.Objects.AdWithScore;

import java.util.List;
import java.util.Map;

public interface IAdSelectorByScores {
    Ad selectAd(Map<Integer, AdWithScore> scoresMap, List<Ad> availableAds);
}
