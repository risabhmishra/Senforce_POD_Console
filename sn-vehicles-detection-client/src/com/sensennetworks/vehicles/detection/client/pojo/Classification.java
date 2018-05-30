package com.sensennetworks.vehicles.detection.client.pojo;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Classification Object
 *
 * @author Sreekanth Reddy B
 * @since 2017-05-29
 */
public class Classification {

    
    private List<Images> images;
   
    
    public List<Images> getImages() {
        return images;
    }
    
    public void setImages(List<Images> images) {
        this.images = images;
    }
    
    
    /*@SerializedName("car")
    private String car;
    @SerializedName("numberPlate")
    private String numberPlate;
    @SerializedName("plateImg")
    private String plateImg;
    private List<String> metadata;
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
    }*/


}
