package com.sensennetworks.vehicles.detection.client.pojo;

/**
 * Response Object
 *
 * @author Sreekanth Reddy B
 * @since 2017-08-12
 */
public class Response {

    private ClassificationPacket ClassificationPacket;

    public ClassificationPacket getClassificationPacket() {
        return ClassificationPacket;
    }

    public void setClassificationPacket(ClassificationPacket ClassificationPacket) {
        this.ClassificationPacket = ClassificationPacket;
    }

}
