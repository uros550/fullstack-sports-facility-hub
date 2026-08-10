package com.hub.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hub.backend.db.dao.OrderRepo;
import com.hub.backend.models.Order;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {
    
    @GetMapping("/history/{athleteId}")
    public List<Order> getHistory(@PathVariable int athleteId) {
        return new OrderRepo().getHistory(athleteId);
    }

    @GetMapping("/active/{athleteId}")
    public List<Order> getActiveOrders(@PathVariable int athleteId) {
        return new OrderRepo().getActiveOrders(athleteId);
    }

    @PutMapping("/cancel/{orderId}")
    public boolean cancelOrder(@PathVariable int orderId) {
        return new OrderRepo().cancelOrder(orderId);
    }

}
