import AdServer.AdServerService;
import AdServer.Objects.Ad;
import AdServer.Objects.AdIdentifiers;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdServerTest {
    private static final int MAX_AD_ID = 12;
    private static final List<Integer> SIZE_LIST = Arrays.asList(5, 80);
    private static final List<String> COUNTRY_LIST = Arrays.asList("Israel", "US"); //TODO: make these an enum
    private static final List<String> LANGUAGE_LIST = Arrays.asList("English", "Hebrew");//TODO: make these an enum
    private static final List<String> AD_DATA_LIST = Arrays.asList("This is an ad ", "Commercial ", "Trading System ");
    private static final int NUM_THREADS = 10;
    private static final int NUM_ACTIONS = 5;
    private static final int SLEEP_DURATION_MS = 3000; // 5 seconds

    public static void main(String[] args) {
        List<AdIdentifiers> possibleIdentifiers = AdServerService.initAdDb(MAX_AD_ID,
                SIZE_LIST, COUNTRY_LIST, LANGUAGE_LIST, AD_DATA_LIST);
        AdServerService adServer = new AdServerService();

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                while (true) {
                    try {
                        Random random = new Random();
                        for (int j = 0; j < NUM_ACTIONS; j++) {
                            AdIdentifiers adIdentifiers = possibleIdentifiers.get(random.nextInt(possibleIdentifiers.size()));
                            Ad ad = adServer.getAd(adIdentifiers.getSize(), adIdentifiers.getLanguage(), adIdentifiers.getCountry());

                            if (ad != null) {
                                if (random.nextDouble() < 0.5) { // 50% chance to click
                                    if (random.nextDouble() < 0.5) { // 50% chance to register
                                        adServer.postAdEvent(ad.getId(), "register");
                                    } else {
                                        adServer.postAdEvent(ad.getId(), "click");
                                    }
                                } else {
                                    adServer.postAdEvent(ad.getId(), "impression");
                                }
                            } else {
                                System.out.println("Ad retrieved is null");
                            }
                        }
                        Thread.sleep(SLEEP_DURATION_MS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
        }

    }
}
