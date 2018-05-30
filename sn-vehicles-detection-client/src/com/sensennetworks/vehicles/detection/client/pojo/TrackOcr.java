package com.sensennetworks.vehicles.detection.client.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * TrackOcr Object
 *
 * @author Sreekanth Reddy B
 * @since 2017-06-02
 */
public class TrackOcr {

    @SerializedName("ocr")
    private String ocr;
    @SerializedName("score")
    private String score;

    public String getOcr() {
        return ocr;
    }

    public void setOcr(String ocr) {
        this.ocr = ocr;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

}
