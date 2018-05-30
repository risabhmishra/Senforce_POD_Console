/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sensennetworks.vehicles.detection.client.pojo;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Images Object
 *
 * @author Amit Kumar   
 * @since 30/05/2018
 */
public class Images {
    
    @SerializedName("person")
    private List<Person> persons;
    @SerializedName("truck")
    private List<Truck> trucks;
    @SerializedName("car")
    private List<Car> cars;
    @SerializedName("bus")
    private List<Bus> buses;
    @SerializedName("motorcycle")
    private List<Motorcycle> motorcycles;
    @SerializedName("uid")
    private String uid;

    /**
     * @return the persons
     */
    public List<Person> getPersons() {
        return persons;
    }

    /**
     * @param persons the persons to set
     */
    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    /**
     * @return the trucks
     */
    public List<Truck> getTrucks() {
        return trucks;
    }

    /**
     * @param trucks the trucks to set
     */
    public void setTrucks(List<Truck> trucks) {
        this.trucks = trucks;
    }

    /**
     * @return the cars
     */
    public List<Car> getCars() {
        return cars;
    }

    /**
     * @param cars the cars to set
     */
    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    /**
     * @return the buses
     */
    public List<Bus> getBuses() {
        return buses;
    }

    /**
     * @param buses the buses to set
     */
    public void setBuses(List<Bus> buses) {
        this.buses = buses;
    }

    /**
     * @return the motorcycles
     */
    public List<Motorcycle> getMotorcycles() {
        return motorcycles;
    }

    /**
     * @param motorcycles the motorcycles to set
     */
    public void setMotorcycles(List<Motorcycle> motorcycles) {
        this.motorcycles = motorcycles;
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
