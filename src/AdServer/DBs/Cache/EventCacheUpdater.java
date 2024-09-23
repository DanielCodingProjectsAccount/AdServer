package AdServer.DBs.Cache;

import AdServer.DBs.AdDB;
import AdServer.DBs.EventDB;
import AdServer.Objects.AdEventData;
import AdServer.Score.IEventScoreCalculator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EventCacheUpdater {
    private static volatile EventCacheUpdater instance;

    private final AdDB adDB = AdDB.getInstance();
    private final EventDB eventDB = EventDB.getInstance();
    private final EventCache eventCache = EventCache.getInstance();
    private final int cacheUpdateSchedulerPoolSize = 1;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(cacheUpdateSchedulerPoolSize);

    private IEventScoreCalculator eventScoreCalculator;

    public EventCacheUpdater(IEventScoreCalculator eventScoreCalculator) {
        this.eventScoreCalculator = eventScoreCalculator;
        scheduler.scheduleAtFixedRate(this::updateCache, 0, 10, TimeUnit.SECONDS);
    }

    public static EventCacheUpdater getInstance(IEventScoreCalculator eventScoreCalculator) {
        if (instance == null) {
            synchronized (EventCacheUpdater.class) {
                if (instance == null) {
                    instance = new EventCacheUpdater(eventScoreCalculator);
                }
            }
        }
        return instance;
    }


    private void updateCache() {
        for (int adId : eventDB.getAllAdIds()) {
            try {
                AdEventData adEventData = eventDB.getAdEventData(adId);
                double score = eventScoreCalculator.calculateScore(adEventData);
                eventCache.updateAdScore(adId, score);
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        System.out.println("Ad Event Cache ( size = " + eventCache.size() + " ) " + " is : \n" + eventCache.toString());
    }

}
