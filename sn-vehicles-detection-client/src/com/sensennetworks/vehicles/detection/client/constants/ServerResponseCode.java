package com.sensennetworks.vehicles.detection.client.constants;

/**
 * Response codes sent by ML server.
 * @author Faram
 * @since 2017-07-07
 */
public enum ServerResponseCode {
    OK(200),
    BAD_REQUEST(400),
    INTERNAL_SERVER_ERROR(500);
    
    private int val;

    private ServerResponseCode(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
    
}
