/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sensennetworks.vehicles.detection.client.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Person Object
 * 
 * @author Amit Kumar
 * @since 30/05/2018
 */
public class Person {
    
    @SerializedName("x")
    private float x;
    @SerializedName("y")
    private float y;
    @SerializedName("w")
    private float w;
    @SerializedName("h")
    private float h;

   
    public float getX() {
        return x;
    }
  
    public void setX(float x) {
        this.x = x;
    }
 
    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
    }
    
}
