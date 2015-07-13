/*
 * Copyright (c) 2015 Nokia. All rights reserved.
 */
package com.purber.rest.dto;

public class ErrorInfoDto {
    


    private String errorMessage;
    
    public ErrorInfoDto(String errorMessage) {
        super();
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }    
    
}
