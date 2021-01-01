package com.app.bookstore.domain.dto;

import java.time.LocalDateTime;

import com.app.bookstore.domain.OrderItemStatus;

import lombok.Data;

/**
 * @author Ananth Shanmugam
 */
@Data
public class OrderItemDTO {
    private Long id;
    private BookDTO bookDTO;
    private int quantity;
    private OrderDTO orderDTO;
    private int rating = 0;
    private OrderItemStatus orderStatus = OrderItemStatus.ORDERED;
    private LocalDateTime shippingDate;
    private LocalDateTime deliveredDate;

}
