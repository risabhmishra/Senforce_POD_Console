package com.sensennetworks.vehicles.detection.client.pojo;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Metadata Object
 *
 * @author Sreekanth Reddy B
 * @since 2017-06-01
 */
public class Metadata {

    @SerializedName("ranks-by-pattern")
    private List<RanksByPattern> ranksByPatternList;
    @SerializedName("ocr-score-max")
    private double ocrScoreMax;
    @SerializedName("track-results")
    private List<TrackResults> trackResultsList;
    @SerializedName("track-length")
    private int trackLength;
    @SerializedName("ocr-score-average")
    private double ocrScoreAverage;
    @SerializedName("voting-rank")
    private String votingRank;
    @SerializedName("ocr-score-min")
    private double ocrScoreMin;
    @SerializedName("fusion-rank")
    private String fusionRank;
    @SerializedName("ranks-by-score")
    private List<RanksByScore> ranksByScoresList;

    public List<RanksByPattern> getRanksByPatternList() {
        return ranksByPatternList;
    }

    public void setRanksByPatternList(List<RanksByPattern> ranksByPatternList) {
        this.ranksByPatternList = ranksByPatternList;
    }

    public double getOcrScoreMax() {
        return ocrScoreMax;
    }

    public void setOcrScoreMax(double ocrScoreMax) {
        this.ocrScoreMax = ocrScoreMax;
    }

    public List<TrackResults> getTrackResultsList() {
        return trackResultsList;
    }

    public void setTrackResultsList(List<TrackResults> trackResultsList) {
        this.trackResultsList = trackResultsList;
    }

    public int getTrackLength() {
        return trackLength;
    }

    public void setTrackLength(int trackLength) {
        this.trackLength = trackLength;
    }

    public double getOcrScoreAverage() {
        return ocrScoreAverage;
    }

    public void setOcrScoreAverage(double ocrScoreAverage) {
        this.ocrScoreAverage = ocrScoreAverage;
    }

    public String getVotingRank() {
        return votingRank;
    }

    public void setVotingRank(String votingRank) {
        this.votingRank = votingRank;
    }

    public double getOcrScoreMin() {
        return ocrScoreMin;
    }

    public void setOcrScoreMin(double ocrScoreMin) {
        this.ocrScoreMin = ocrScoreMin;
    }

    public String getFusionRank() {
        return fusionRank;
    }

    public void setFusionRank(String fusionRank) {
        this.fusionRank = fusionRank;
    }

    public List<RanksByScore> getRanksByScoresList() {
        return ranksByScoresList;
    }

    public void setRanksByScoresList(List<RanksByScore> ranksByScoresList) {
        this.ranksByScoresList = ranksByScoresList;
    }

}
