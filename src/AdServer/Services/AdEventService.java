package AdServer.Services;

import AdServer.Requests.AdEventRequest;

import java.util.concurrent.*;

public class AdEventService {

    private final ExecutorService executorService;
    private final int poolSize;
    private final AggregatorService aggregatorService;

    public AdEventService(int poolSize, AggregatorService aggregatorService) {
        this.poolSize = poolSize;
        this.executorService = Executors.newFixedThreadPool(poolSize);
        this.aggregatorService = aggregatorService;
    }

    public void startConsumers(BlockingQueue<AdEventRequest> eventRequestQueue) {
        for (int i = 0; i < poolSize; i++) {
            executorService.submit(() -> {
                while (true) {
                    try {
                        AdEventRequest request = eventRequestQueue.take();
                        aggregatorService.recordEvent(request.getAdId(), request.getEventType());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
    }
}
