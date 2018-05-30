package com.sensennetworks.vehicles.detection.client.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Headers Object
 *
 * @author Sreekanth Reddy B
 * @since 2017-06-01
 */
public class Headers {

    @SerializedName("content-type")
    private String contentType;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

}
