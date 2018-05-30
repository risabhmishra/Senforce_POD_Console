package com.sensennetworks.vehicles.detection.client.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * ProcessingTime Object
 *
 * @author Sreekanth Reddy B
 * @since 2017-06-02
 */
public class ProcessingTime {

    @SerializedName("segmentation-time")
    private String segmentationTime;
    @SerializedName("total-time")
    private String totalTime;
    @SerializedName("ocr-time")
    private String ocrTime;

    public String getSegmentationTime() {
        return segmentationTime;
    }

    public void setSegmentationTime(String segmentationTime) {
        this.segmentationTime = segmentationTime;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getOcrTime() {
        return ocrTime;
    }

    public void setOcrTime(String ocrTime) {
        this.ocrTime = ocrTime;
    }

}
