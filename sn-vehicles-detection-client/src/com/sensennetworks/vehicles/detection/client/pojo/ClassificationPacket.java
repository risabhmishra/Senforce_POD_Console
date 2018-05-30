package com.sensennetworks.vehicles.detection.client.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * ClassificationPacket Object
 *
 * @author Sreekanth Reddy B
 * @since 2017-06-02
 */
public class ClassificationPacket {

    @SerializedName("classification")
    private Classification classification;
    private int responseCode;
    private String responseMessage;
    
    @SerializedName("anpr")
    private ANPR anpr;

    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    
    public ANPR getAnpr() {
        return anpr;
    }

    
    public void setAnpr(ANPR anpr) {
        this.anpr = anpr;
    }
}
