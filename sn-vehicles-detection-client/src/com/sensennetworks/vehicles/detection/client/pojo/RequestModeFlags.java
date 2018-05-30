/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sensennetworks.vehicles.detection.client.pojo;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Amit Kumar
 * since 30/05/2018
 */
public class RequestModeFlags {
    
    @SerializedName("isClassification")
    private boolean isClassification;
    @SerializedName("isANPR")
    private boolean isANPR;

    /**
     * @return the isClassification
     */
    public boolean isIsClassification() {
        return isClassification;
    }

    /**
     * @param isClassification the isClassification to set
     */
    public void setIsClassification(boolean isClassification) {
        this.isClassification = isClassification;
    }

    /**
     * @return the isANPR
     */
    public boolean isIsANPR() {
        return isANPR;
    }

    /**
     * @param isANPR the isANPR to set
     */
    public void setIsANPR(boolean isANPR) {
        this.isANPR = isANPR;
    }
    
    
    
}
