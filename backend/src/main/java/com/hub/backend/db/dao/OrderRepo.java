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
        
        List<OrderItem> items = new ArrayList<>();
        String query =  "select oi.id, oi.orderId, e.id as eId, e.name as eName, e.imageUrl as eImage, oi.quantity, oi.priceAtPurchase " +
                        "from orderItem oi join equipment e on e.id = oi.equipmentId where oi.orderId = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(query);
        ){
            stm.setInt(1, orderId);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                OrderItem oi = new OrderItem(
                    rs.getInt("id"),
                    rs.getInt("orderId"),
                    rs.getInt("eId"),
                    rs.getString("eName"),
                    rs.getString("eImage"),
                    rs.getInt("quantity"),
                    rs.getFloat("priceAtPurchase")
                );
                items.add(oi);
            }

            return items;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return items;
    }

    @Override
    public boolean cancelOrder(int orderId) {
        
        String updateOrderQuery = "UPDATE `Order` SET status = 'CANCELLED' WHERE id = ? AND status = 'ACCEPTED'";
        String selectItemsQuery = "SELECT equipmentId, quantity FROM OrderItem WHERE orderId = ?";
        String updateStockQuery = "UPDATE Equipment SET stock = stock + ? WHERE id = ?";

        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement updateOrderStm = conn.prepareStatement(updateOrderQuery);
            PreparedStatement selectItemsStm = conn.prepareStatement(selectItemsQuery);
            PreparedStatement updateStockStm = conn.prepareStatement(updateStockQuery);
        ){
            conn.setAutoCommit(false);

            //update order to cancelled
            updateOrderStm.setInt(1, orderId);
            int rowsUpdated = updateOrderStm.executeUpdate();
            if (rowsUpdated == 0) {
                conn.rollback();
                return false;
            }

            //get all equipment ids that were cancelled and its quantities
            selectItemsStm.setInt(1, orderId);
            ResultSet rs = selectItemsStm.executeQuery();
            while (rs.next()) {
                updateStockStm.setInt(1, rs.getInt("quantity"));
                updateStockStm.setInt(2, rs.getInt("equipmentId"));
                updateStockStm.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
}
