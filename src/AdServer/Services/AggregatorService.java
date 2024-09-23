package AdServer.Services;

import AdServer.DBs.EventDB;
import AdServer.Objects.AdEventData;

import java.util.Map;
import java.util.concurrent.*;

public class AggregatorService {

    private static volatile AggregatorService instance;
    private final ConcurrentMap<Integer, AdEventData> aggregatedData = new ConcurrentHashMap<>();
    private final int flusherSchedulerPoolSize = 1;
    private final ScheduledExecutorService flusherScheduler = Executors.newScheduledThreadPool(flusherSchedulerPoolSize);

    private AggregatorService() {
        flusherScheduler.scheduleAtFixedRate(this::flushAggregatedDataToDB, 0, 5, TimeUnit.SECONDS);
    }

    public static AggregatorService getInstance() {
        if (instance == null) {
            synchronized (AggregatorService.class) {
                if (instance == null) {
                    instance = new AggregatorService();
                }
            }
        }
        return instance;
    }

    public void recordEvent(Integer adId, String eventType) {
        aggregatedData.compute(adId, (key, existingData) -> {
            if (existingData == null) {
                existingData = new AdEventData(adId);
                return existingData;
            }
            existingData.updateEvent(eventType);
            return existingData;
        });
    }

    public void flushAggregatedDataToDB() {
//        System.out.println("Flushing Aggregated data to EventDB");
        for (Map.Entry<Integer, AdEventData> entry : aggregatedData.entrySet()) {
            try {
                int adId = entry.getKey();
                AdEventData aggregatedEventData = entry.getValue();
                EventDB.getInstance().addOtherEventData(adId, aggregatedEventData);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        // Clear aggregated data after flushing
        aggregatedData.clear();
    }
}
