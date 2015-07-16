/*
 * Copyright (c) 2015 Nokia. All rights reserved.
 */
package com.purber.client.restimpl;

import java.util.Collection;
import java.util.Collections;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.purber.rest.api.Common;
import com.purber.rest.api.Common.ActionCode;
import com.purber.rest.api.OrderRequest;
import com.purber.rest.dto.OrderDto;

public class OrderRequestImpl implements OrderRequest {
    
    
    private Client client;
    private WebTarget baseTarget;
    
      
    public OrderRequestImpl() {
        super();
        client = ClientBuilder.newClient().register(JacksonJsonProvider.class);
        baseTarget = client.target(Common.SERVER_URI).path(Common.ORDER_PATH); 
    }
    
    public OrderRequestImpl(String uri) {
        super();
        client = ClientBuilder.newClient().register(JacksonJsonProvider.class);
        baseTarget = client.target(uri); 
    }
    

    @Override
    public Collection<OrderDto> getOrderListByUid(int uid) {
        
        Collection<OrderDto> result = Collections.emptyList();
        WebTarget target = baseTarget;
        if (uid > 0)  target = baseTarget.queryParam(Common.QueryParam.uid.name(), uid);
        
        try {
            Response response = target.request(Common.MEDIA_TYPE).get();
            if (response.getStatus() == Status.OK.getStatusCode()) {
                result = response.readEntity(new GenericType<Collection<OrderDto>>(){});
            }
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    @Override
    public Collection<OrderDto> getAllOrders() {        
        return getOrderListByUid(0);
    }
    
    

    @Override
    public StatusType clean() {
        try {
            Response response = baseTarget.request(Common.MEDIA_TYPE).delete();
            return response.getStatusInfo();
        } catch (Exception e) {
            return Status.SERVICE_UNAVAILABLE;
        }
    }

    @Override
    public StatusType removeByOid(int id) {        
        try {
            Response response = baseTarget.path(String.valueOf(id)).request(Common.MEDIA_TYPE).delete();
            return response.getStatusInfo();
        } catch (Exception e) {
            return Status.SERVICE_UNAVAILABLE;
        }
    }

    @Override
    public OrderDto getByOid(int id) {        
        return getByOid( id,  0,  0);
    }

    @Override
    public OrderDto getByOid(int id, int wait_time, int version) {
        
        WebTarget target = baseTarget.path(String.valueOf(id))
                .queryParam(Common.QueryParam.wait_time.name(), wait_time)
                .queryParam(Common.QueryParam.version.name(), version);
        OrderDto result = null;
        
        try {
            Response response = target.request(Common.MEDIA_TYPE).get();
            if (response.getStatus() == Status.OK.getStatusCode()) {
                result = response.readEntity(OrderDto.class);                
            }
            
        } catch (Exception e) {
            
        }
        return result;
    }

    @Override
    public OrderDto book(OrderDto order) {
        
        OrderDto result = order;
        
        try {
            Response response = baseTarget.request(Common.MEDIA_TYPE).post(Entity.json(order));
            if (response.getStatus() == Status.OK.getStatusCode()) {
                result = response.readEntity(OrderDto.class);                
            }else
            {
                result.setStatus(Common.ORDER_STATUS_UNREACHABLE);
            }
                
        } catch (Exception e) {            
            result.setStatus(Common.ORDER_STATUS_UNREACHABLE);
        }
        return result;
    }

    @Override
    public OrderDto update(int id, ActionCode action, int vendor_id) {
        
        OrderDto result = new OrderDto();
        WebTarget target = baseTarget.path(String.valueOf(id)).path(action.name())
                .queryParam(Common.QueryParam.vid.name(), vendor_id);                
        
        try {
            Response response = target.request(Common.MEDIA_TYPE).put(Entity.json(""));
            if (response.getStatus() == Status.OK.getStatusCode()) {
                result = response.readEntity(OrderDto.class);                
            }else
            {
                result.setStatus(Common.ORDER_STATUS_UNREACHABLE);
            }                
        } catch (Exception e) {            
            result.setStatus(Common.ORDER_STATUS_UNREACHABLE);
        }
        return result;
    }


    @Override
    public OrderDto cancel(OrderDto order) {
        if ( order == null ) return order;
        return update(order.getId(), ActionCode.cancel, 0);
    }


    @Override
    public OrderDto confirm(OrderDto order) {
        if ( order == null ) return order;
        return update(order.getId(), ActionCode.confirm, order.getVendor_id());
    }


    @Override
    public OrderDto complete(OrderDto order) {
        if ( order == null ) return order;
        return update(order.getId(), ActionCode.complete, order.getVendor_id());
    }    

}
