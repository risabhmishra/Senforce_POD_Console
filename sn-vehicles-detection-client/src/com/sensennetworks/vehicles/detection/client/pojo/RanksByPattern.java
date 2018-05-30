package com.sensennetworks.vehicles.detection.client.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * RanksByPattern Object
 *
 * @author Sreekanth Reddy B
 * @since 2017-06-01
 */
public class RanksByPattern {

    @SerializedName("ocr")
    private String ocr;

    public String getOcr() {
        return ocr;
    }

    public void setOcr(String ocr) {
        this.ocr = ocr;
    }

}
