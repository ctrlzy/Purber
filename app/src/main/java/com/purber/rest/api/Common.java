/*
 * Copyright (c) 2015 Nokia. All rights reserved.
 */
package com.purber.rest.api;

import javax.ws.rs.core.MediaType;

public class Common {
    
    public static enum QueryParam{
        uid,
        version,
        wait_time,
        vid
    }
    
    
    
    public static final int ORDER_STATUS_INITIAL   = 0;
    public static final int ORDER_STATUS_BOOKING   = 1;
    public static final int ORDER_STATUS_ACCEPTED  = 2;
    public static final int ORDER_STATUS_REJECTED  = 3;
    public static final int ORDER_STATUS_CONFIRMED = 4;
    public static final int ORDER_STATUS_COMPLETED = 5;
    public static final int ORDER_STATUS_CANCELED  = 6;
    public static final int ORDER_STATUS_EVALUATED = 7;
    public static final int ORDER_STATUS_UNREACHABLE = 8;
    
    
    public static final String SERVER_URI = "http://localhost:8080/purber_server/rest/v1/";
    public static final String MEDIA_TYPE =  MediaType.APPLICATION_JSON;
    public static final String ORDER_PATH = "/orders/";
    
    public enum ActionCode{
        initial, book, accept, reject, confirm, complete, cancel, close, unknown
    }

}
