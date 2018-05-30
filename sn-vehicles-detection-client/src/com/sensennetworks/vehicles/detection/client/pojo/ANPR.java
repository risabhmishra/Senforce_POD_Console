/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sensennetworks.vehicles.detection.client.pojo;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * ANPR Object
 * 
 * @author Amit Kumar
 * @since 30/05/2018
 */
public class ANPR {
    
    @SerializedName("car")
    private String car;
    @SerializedName("numberPlate")
    private String numberPlate;
    @SerializedName("plateImg")
    private String plateImg;
    private List<String> metadata;
    
    private List<String> plateDetectionUIDs;
    //@SerializedName("metadata")
    //private Metadata metadata;

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getPlateImg() {
        return plateImg;
    }

    public void setPlateImg(String plateImg) {
        this.plateImg = plateImg;
    }

    public List<String> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<String> metadata) {
        this.metadata = metadata;
    }

    /**
     * @return the plateDetectionUIDs
     */
    public List<String> getPlateDetectionUIDs() {
        return plateDetectionUIDs;
    }

    /**
     * @param plateDetectionUIDs the plateDetectionUIDs to set
     */
    public void setPlateDetectionUIDs(List<String> plateDetectionUIDs) {
        this.plateDetectionUIDs = plateDetectionUIDs;
    }
}
