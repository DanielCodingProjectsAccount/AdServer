package AdServer.Objects;

public class AdEventData {
    private final int adId;
    private int impressions;
    private int clicks;
    private int registers;

    public AdEventData(int adId) {
        this.adId = adId;
    }

    public AdEventData(int adId, int clicks, int impressions, int registers) {
        this.adId = adId;
        this.clicks = clicks;
        this.impressions = impressions;
        this.registers = registers;
    }

    public AdEventData(AdEventData other) {
        this.adId = other.adId;
        this.clicks = other.clicks;
        this.impressions = other.impressions;
        this.registers = other.registers;
    }

    public static AdEventData combineEventData(AdEventData eventData, AdEventData eventDataOther) {
        if (eventDataOther == null || eventData == null) {
            throw new IllegalArgumentException("Null AdEvenData as input");
        }
        if (eventDataOther.adId != eventData.adId) {
            throw new IllegalArgumentException("Trying to update eventData " + eventDataOther.adId
                    + " with " + eventData.adId);
        }
        return new AdEventData(eventData.adId,
                eventData.clicks + eventDataOther.clicks,
                eventData.impressions + eventDataOther.impressions,
                eventData.registers + eventDataOther.registers);
    }

    synchronized public AdEventData updateEvent(String eventType) {
        switch (eventType) {
            case "impression":
                this.incrementImpressions();
                break;
            case "click":
                this.incrementImpressions();
                this.incrementClicks();
                break;
            case "register":
                this.incrementImpressions();
                this.incrementClicks();
                this.incrementRegisters();
                break;
            default:
                throw new IllegalArgumentException("Unknown event type: " + eventType);
        }
        return this;
    }

    synchronized public AdEventData incrementImpressions() {
        impressions++;
        return this;
    }

    synchronized public AdEventData incrementClicks() {
        clicks++;
        return this;

    }

    synchronized public AdEventData incrementRegisters() {
        registers++;
        return this;

    }

    public int getImpressions() {
        return impressions;
    }

    public int getClicks() {
        return clicks;
    }

    public int getRegisters() {
        return registers;
    }
}
