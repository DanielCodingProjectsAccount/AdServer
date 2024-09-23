package AdServer.DBs;

import AdServer.Objects.AdEventData;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EventDB {

    private static volatile EventDB instance;
    private final ConcurrentMap<Integer, AdEventData> eventData = new ConcurrentHashMap<>();

    private EventDB() {
    }

    public static EventDB getInstance() {
        if (instance == null) {
            synchronized (EventDB.class) {
                if (instance == null) {
                    instance = new EventDB();
                }
            }
        }
        return instance;
    }

    public AdEventData getAdEventData(int adId) {
        return new AdEventData(eventData.getOrDefault(adId, new AdEventData(adId)));
    }

    public void addOtherEventData(int adId, AdEventData adEventData) {
        eventData.compute(adId, (key, existingData) -> {
            if (existingData == null) {
                existingData = new AdEventData(adEventData);
                return existingData;
            }
            return AdEventData.combineEventData(adEventData, existingData);
        });
    }

    public List<Integer> getAllAdIds() {
        return eventData.keySet().stream().toList();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EventDB Contents:\n");
        eventData.forEach((adId, adEventData) -> {
            sb.append("Ad ID: ").append(adId)
                    .append(" -> ").append(adEventData)
                    .append("\n");
        });
        return sb.toString();
    }
}
