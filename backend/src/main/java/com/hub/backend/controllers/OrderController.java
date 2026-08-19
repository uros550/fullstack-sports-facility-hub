package com.hub.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hub.backend.db.dao.OrderRepo;
import com.hub.backend.models.Order;
import com.hub.backend.models.OrderItem;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    @GetMapping()
    public List<Order> getAllOrders() {
        return new OrderRepo().getAllOrders();
    }
    
    @GetMapping("/history/{athleteId}")
    public List<Order> getHistory(@PathVariable int athleteId) {
        return new OrderRepo().getHistory(athleteId);
    }

    @GetMapping("/active/{athleteId}")
    public List<Order> getActiveOrders(@PathVariable int athleteId) {
        return new OrderRepo().getActiveOrders(athleteId);
    }

    @GetMapping("/{orderId}/items")
    public List<OrderItem> getOrderItems(@PathVariable int orderId) {
        return new OrderRepo().getOrderItems(orderId);
    }

    @PostMapping("/add/{athleteId}")
    public boolean addOrder(@PathVariable int athleteId, @RequestBody List<OrderItem> items) {
        return new OrderRepo().addOrder(athleteId, items);
    }

    @PutMapping("/cancel/{orderId}")
    public boolean cancelOrder(@PathVariable int orderId) {
        return new OrderRepo().cancelOrder(orderId);
    }

    @PutMapping("/picked/{orderId}")
    public boolean pickedOrder(@PathVariable int orderId) {
        return new OrderRepo().pickedOrder(orderId);
    }

}
