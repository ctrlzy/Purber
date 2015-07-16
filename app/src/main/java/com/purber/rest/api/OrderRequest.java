/*
 * Copyright (c) 2015 Nokia. All rights reserved.
 */
package com.purber.rest.api;

import java.util.Collection;



import javax.ws.rs.core.Response.StatusType;

import com.purber.rest.api.Common.ActionCode;
import com.purber.rest.dto.OrderDto;

public interface OrderRequest {
    
    public Collection<OrderDto> getOrderListByUid(int uid);
    
    public Collection<OrderDto> getAllOrders();
    
    public StatusType clean();
    
    public StatusType removeByOid(int id);    
    
    public OrderDto getByOid(int id);
    
    public OrderDto getByOid(int id,int wait_time, int version);
    
    public OrderDto book(OrderDto order);
    
    public OrderDto update(int id, ActionCode action, int vendor_id);
    
    public OrderDto cancel(OrderDto order);
    
    public OrderDto confirm(OrderDto order);
    
    public OrderDto complete(OrderDto order);

}
