package com.hub.backend.db.dao;

import java.util.List;

import com.hub.backend.models.Order;
import com.hub.backend.models.OrderItem;

public interface OrderRepoInterface {
    
    List<Order> getHistory(int athleteId);
    List<Order> getActiveOrders(int athleteId);
    List<OrderItem> getOrderItems(int orderId);
    boolean cancelOrder(int orderId);

}
