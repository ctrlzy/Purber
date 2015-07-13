/*
 * Copyright (c) 2015 Nokia. All rights reserved.
 */
package com.purber.rest.dto;

public class OrderDto {

    private int id;
    
    private int status;
    
    private int customer_id;
    
    private int vendor_id;
    
    private LocationDto loc;
    
    private ServiceDto service;

    private int version;
    
    
    public OrderDto() {
        super();
        loc = new LocationDto();
        service = new ServiceDto();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(int vendor_id) {
        this.vendor_id = vendor_id;
    }

    public LocationDto getLoc() {
        return loc;
    }

    public void setLoc(LocationDto loc) {
        this.loc = loc;
    }

    public ServiceDto getService() {
        return service;
    }

    public void setService(ServiceDto service) {
        this.service = service;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void updateVesion()
    {
        this.version++;
    }
    
    
    
    

}
