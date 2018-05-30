package com.sensennetworks.vehicles.detection.client.pojo;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * EventFramesPacket Object
 *
 * @author Sreekanth Reddy B
 * @since 2017-05-30
 */
public class EventFramesPacket {

    private Headers headers;
    private String key;
//    private String regionCode;      // not used in request message
    private List<EventFrames> eventFrames;
    private RequestModeFlags requestModeFlags;

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /*public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }*/

    public List<EventFrames> getEventFrames() {
        return eventFrames;
    }

    public void setEventFrames(List<EventFrames> eventFrames) {
        this.eventFrames = eventFrames;
    }

    
    public RequestModeFlags getRequestModeFlags() {
        return requestModeFlags;
    }

   
    public void setRequestModeFlags(RequestModeFlags requestModeFlags) {
        this.requestModeFlags = requestModeFlags;
    }

    
}
