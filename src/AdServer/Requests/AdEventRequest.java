package AdServer.Requests;

import java.util.Objects;

public class AdEventRequest {
    private final int adId;
    private final String eventType; // can be "impression", "click", or "register"

    public AdEventRequest(int adId, String eventType) {
        this.adId = adId;
        this.eventType = eventType;
    }

    public int getAdId() {
        return adId;
    }

    public String getEventType() {
        return eventType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdEventRequest that = (AdEventRequest) o;
        return adId == that.adId && Objects.equals(eventType, that.eventType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adId, eventType);
    }

    @Override
    public String toString() {
        return "AdServer.Requests.AdEventRequest{" +
                "adId=" + adId +
                ", eventType='" + eventType + '\'' +
                '}';
    }
}
