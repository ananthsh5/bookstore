package com.app.bookstore.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.bookstore.domain.OrderItem;
import com.app.bookstore.domain.dto.OrderItemDTO;
import com.app.bookstore.repository.OrderItemRepository;
import com.app.bookstore.service.OrderItemService;
import com.app.bookstore.transformer.OrderItemTransformer;

/**
 * @author Ananth Shanmugam
 */
@Service
public class OrderItemServiceImpl implements OrderItemService {
    @Autowired
    OrderItemRepository orderItemRepository;

    @Override
    public OrderItemDTO saveOrderItem(OrderItemDTO orderItemDTO) { /* Save the order item to db */ 
    	OrderItem orderItem = orderItemRepository.save(OrderItemTransformer.transform(orderItemDTO));
    	return OrderItemTransformer.transform(orderItem);
    }

    @Override
    public List<OrderItemDTO> getOrderItems() {  /* Get all order items from db  */
        final List<OrderItem> results = (List<OrderItem>) orderItemRepository.findAll();
        List<OrderItemDTO> resultList = new ArrayList<OrderItemDTO>();
        if ( results != null ) {
			results.stream().forEach(orderItemObj -> resultList.add(OrderItemTransformer.transform(orderItemObj)));
        }
        return resultList;
    }

    @Override
    public OrderItemDTO getOrderItemById(Long id) { /* Get order item by id from db  */
        return OrderItemTransformer.transform(orderItemRepository.findById(id).get());
    }

	@Override
	public List<OrderItemDTO> getDeliveredOrderItemsByOrder(Long orderId) { /* Get the order items delivered by order id */
		final List<OrderItem> results = orderItemRepository.getDeliveredOrderItemsByOrder(orderId);
		List<OrderItemDTO> resultList = new ArrayList<OrderItemDTO>();
		if (results != null) {
			results.stream().forEach(orderItemObj -> resultList.add(OrderItemTransformer.transform(orderItemObj)));
		}
		return resultList;

	}

	@Override
	public List<OrderItemDTO> getOrderItemsByPublisher(Long publisherId) {
		final List<OrderItem> results = orderItemRepository.getOrderItemsByPublisher(publisherId);
		List<OrderItemDTO> resultList = new ArrayList<OrderItemDTO>();
		if (results != null) {
			results.stream().forEach(orderItemObj -> resultList.add(OrderItemTransformer.transform(orderItemObj)));
		}
		return resultList;

	}
}
