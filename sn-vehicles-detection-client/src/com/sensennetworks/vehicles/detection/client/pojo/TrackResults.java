package com.sensennetworks.vehicles.detection.client.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * TrackResults Object
 *
 * @author Sreekanth Reddy B
 * @since 2017-06-01
 */
public class TrackResults {

    @SerializedName("processing-time")
    private ProcessingTime processingTime;
    @SerializedName("track-ocr")
    private TrackOcr trackOcr;
    @SerializedName("geometry")
    private Geometry geometry;

    public ProcessingTime getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(ProcessingTime processingTime) {
        this.processingTime = processingTime;
    }

    public TrackOcr getTrackOcr() {
        return trackOcr;
    }

    public void setTrackOcr(TrackOcr trackOcr) {
        this.trackOcr = trackOcr;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
    
}
