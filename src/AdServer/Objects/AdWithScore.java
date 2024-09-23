package AdServer.Objects;

public class AdWithScore {
    private Ad ad;
    private double score;

    public AdWithScore(Ad ad, double score){
        this.ad = ad;
        this.score = score;
    }

    public Ad getAd() {
        return ad;
    }

    public double getScore() {
        return score;
    }
}
