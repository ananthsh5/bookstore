package com.app.bookstore.service;

import java.util.List;

import com.app.bookstore.domain.dto.OrderItemDTO;

/**
 * @author Ananth Shanmugam
 */
public interface OrderItemService {
    OrderItemDTO saveOrderItem(OrderItemDTO orderItem);

    List<OrderItemDTO> getOrderItems();

    OrderItemDTO getOrderItemById(Long id);

    
    List<OrderItemDTO> getDeliveredOrderItemsByOrder(Long orderId);
    
    List<OrderItemDTO> getOrderItemsByPublisher(Long publisherId);
}
