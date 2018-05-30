package com.sensennetworks.vehicles.detection.client.pojo;

/**
 * EventFrames Object
 *
 * @author Sreekanth Reddy B
 * @since 2017-05-30
 */
public class EventFrames {

    private String base64image;
    private String frameType;
    // added
    private String uid; 
    

    public String getBase64image() {
        return base64image;
    }

    public void setBase64image(String base64image) {
        this.base64image = base64image;
    }

    public String getFrameType() {
        return frameType;
    }

    public void setFrameType(String frameType) {
        this.frameType = frameType;
    }

    /**
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

}
