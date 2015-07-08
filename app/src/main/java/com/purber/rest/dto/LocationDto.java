/*
 * Copyright (c) 2015 Nokia. All rights reserved.
 */
package com.purber.rest.dto;

public class LocationDto {
    
    private double longitude;
    
    private double latitude;
    
    public LocationDto() {
        super();        
        this.latitude = 0;
        this.longitude = 0;
    }

    public double getLongitude() {
        return longitude;
    }

    public LocationDto setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public LocationDto setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }   
    

}
