/*
 * Copyright (c) 2015 Nokia. All rights reserved.
 */
package com.purber.rest.dto;

public class ServiceDto {
    
    private int service_type;
    
    private int brand;
    
    private int model;

    public ServiceDto() {
        super();
    }

    public int getService_type() {
        return service_type;
    }

    public void setService_type(int service_type) {
        this.service_type = service_type;
    }

    public int getBrand() {
        return brand;
    }

    public void setBrand(int brand) {
        this.brand = brand;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }
    
    

}
