package com.hub.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.hub.backend.db.DB;
import com.hub.backend.models.Order;
import com.hub.backend.models.OrderItem;

public class OrderRepo implements OrderRepoInterface {

    @Override
    public List<Order> getHistory(int athleteId) {
        
        List<Order> orders = new ArrayList<>();
        String query = "select * from `Order` where athleteId = ? and status in ('PICKED_UP', 'CANCELLED')";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setInt(1, athleteId);

            ResultSet rs = stm.executeQuery();
            while(rs.next()) {
                Timestamp orderTs = rs.getTimestamp("orderDate");
                LocalDateTime orderDate = (orderTs != null) ? orderTs.toLocalDateTime() : null;
                
                Order o = new Order(
                    rs.getInt("id"),
                    rs.getInt("athleteId"),
                    orderDate,
                    rs.getString("status")
                );
                orders.add(o);
            }
            return orders;
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }

    @Override
    public List<Order> getActiveOrders(int athleteId) {
        
         List<Order> orders = new ArrayList<>();
        String query = "select * from `Order` where athleteId = ? and status = 'ACCEPTED'";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setInt(1, athleteId);

            ResultSet rs = stm.executeQuery();
            while(rs.next()) {
                Timestamp orderTs = rs.getTimestamp("orderDate");
                LocalDateTime orderDate = (orderTs != null) ? orderTs.toLocalDateTime() : null;
                
                Order o = new Order(
                    rs.getInt("id"),
                    rs.getInt("athleteId"),
                    orderDate,
                    rs.getString("status")
                );
                orders.add(o);
            }
            return orders;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return orders;
    }

    @Override
    public List<OrderItem> getOrderItems(int orderId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOrderItems'");
    }

    @Override
    public boolean cancelOrder(int orderId) {
        
        String query = "update `Order` set status = 'CANCELLED' where id = ? and status = 'ACCEPTED'";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setInt(1, orderId);

            return stm.executeUpdate() > 0;    
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
}
