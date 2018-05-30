package com.sensennetworks.vehicles.detection.client.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Geometry Object
 *
 * @author Sreekanth Reddy B
 * @since 2017-06-02
 */
public class Geometry {

    @SerializedName("plate-height")
    private String plateHeight;
    @SerializedName("plate-woh")
    private String plateWoh;
    @SerializedName("char-median-width")
    private String charMedianWidth;
    @SerializedName("num-of-chars")
    private String numOfChars;
    @SerializedName("plate-angle")
    private String plateAngle;
    @SerializedName("char-median-height")
    private String charMedianHeight;
    @SerializedName("plate-width")
    private String plateWidth;

    public String getPlateHeight() {
        return plateHeight;
    }

    public void setPlateHeight(String plateHeight) {
        this.plateHeight = plateHeight;
    }

    public String getPlateWoh() {
        return plateWoh;
    }

    public void setPlateWoh(String plateWoh) {
        this.plateWoh = plateWoh;
    }

    public String getCharMedianWidth() {
        return charMedianWidth;
    }

    public void setCharMedianWidth(String charMedianWidth) {
        this.charMedianWidth = charMedianWidth;
    }

    public String getNumOfChars() {
        return numOfChars;
    }

    public void setNumOfChars(String numOfChars) {
        this.numOfChars = numOfChars;
    }

    public String getPlateAngle() {
        return plateAngle;
    }

    public void setPlateAngle(String plateAngle) {
        this.plateAngle = plateAngle;
    }

    public String getCharMedianHeight() {
        return charMedianHeight;
    }

    public void setCharMedianHeight(String charMedianHeight) {
        this.charMedianHeight = charMedianHeight;
    }

    public String getPlateWidth() {
        return plateWidth;
    }

    public void setPlateWidth(String plateWidth) {
        this.plateWidth = plateWidth;
    }
    
}
