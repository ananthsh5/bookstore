package com.app.bookstore.service;

import java.io.File;
import java.util.List;

import com.app.bookstore.domain.dto.CustomerDTO;
import com.app.bookstore.domain.dto.OrderDTO;
import com.app.bookstore.domain.dto.OrderItemDTO;

/**
 * @author Ananth Shanmugam
 */
public interface OrderService {
    OrderDTO getOrderById(Long id);
    OrderDTO saveOrder(CustomerDTO customer, OrderDTO order);
    OrderDTO updateOrder(OrderDTO order);
    OrderItemDTO saveOrderItem(OrderItemDTO orderItem);
    void completeOrder(OrderDTO order);
    void cancelOrder(OrderDTO order);
    File downloadReceipt(OrderDTO orderDTO) throws Exception;

    OrderItemDTO getOrderItemById(Long itemId);
    List<OrderItemDTO> getOrderItemsByPublisher(Long publisherId);
   List<OrderDTO> getAll();
    List<OrderItemDTO> getDeliveredOrderItemsByOrder(Long orderId);
}
