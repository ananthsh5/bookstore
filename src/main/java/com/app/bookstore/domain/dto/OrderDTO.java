package com.app.bookstore.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.app.bookstore.domain.OrderStatus;

import lombok.Data;

/**
 * @author Ananth Shanmugam
 */
@Data
public class OrderDTO {

	private Long id;
	
	private List<OrderItemDTO> orderItems = new ArrayList<OrderItemDTO>();
	
	private BigDecimal totalAmount = new BigDecimal(0.00);
	
	private CustomerDTO customerDTO;
	
	private String shippingAddress;
	
	private String billingAddress;
	
	private String paymentMethod;
	
	private String paymentInfo;

	private LocalDateTime orderedDate;
	
	private LocalDateTime endDate;

	private Boolean usingPoints = false;

	private OrderStatus status = OrderStatus.NEW;
	
    public void addOrderItemDTO(OrderItemDTO item) {
        orderItems.add(item);
    }

}
