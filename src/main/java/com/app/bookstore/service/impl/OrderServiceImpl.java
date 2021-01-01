package com.app.bookstore.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.bookstore.domain.Order;
import com.app.bookstore.domain.OrderItemStatus;
import com.app.bookstore.domain.OrderStatus;
import com.app.bookstore.domain.dto.CartItemDTO;
import com.app.bookstore.domain.dto.CustomerDTO;
import com.app.bookstore.domain.dto.OrderDTO;
import com.app.bookstore.domain.dto.OrderItemDTO;
import com.app.bookstore.repository.OrderRepository;
import com.app.bookstore.service.CartService;
import com.app.bookstore.service.OrderItemService;
import com.app.bookstore.service.OrderService;
import com.app.bookstore.transformer.OrderTransformer;
import com.app.bookstore.util.PdfGenerator;

/**
 * @author Ananth Shanmugam
 */
@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderRepository orderRepository;

	@Autowired	
	OrderItemService orderItemService;
	
	@Autowired
	private CartService cartService;

	@Autowired
	private PdfGenerator pdfGenerator;

	// private final static Logger LOGGER =
	// LoggerFactory.getLogger(OrderServiceImpl.class);

	@Override
	public OrderDTO getOrderById(Long id) { /* Get order by id from db  */
		return OrderTransformer.transform(orderRepository.findById(id).get());
	}

	@Override
	public OrderItemDTO getOrderItemById(Long itemId) {  /* Get order item by id from db  */
		return orderItemService.getOrderItemById(itemId);
	}

	@Override
	public OrderDTO saveOrder(CustomerDTO customer, OrderDTO order) {  /* Save the order  to db */ 
		List<CartItemDTO> cartItems = (List<CartItemDTO>) cartService.getCartByCustomerId(customer.getId());
		List<OrderItemDTO> resultList = new ArrayList<OrderItemDTO>();
		BigDecimal totalAmount = new BigDecimal(0.00);
		for (CartItemDTO ci : cartItems) {				/* iterate through the cart items and calc the total amount and add to order */
			OrderItemDTO oi = new OrderItemDTO();
			oi.setBookDTO(ci.getBookDTO());
			oi.setQuantity(ci.getQuantity());
			resultList.add(oi);
			oi.setOrderDTO(order);
			totalAmount = totalAmount.add(ci.getBookDTO().getPrice().multiply(new BigDecimal(ci.getQuantity())));
			cartService.removeCartItem(ci.getId());
		}
		order.setOrderItems(resultList);

		if (order.getUsingPoints() == true) {  /* If customer is using reward points */
			totalAmount = totalAmount.subtract(new BigDecimal(customer.getPoints()));
			if (totalAmount.compareTo(BigDecimal.ZERO) > 0) {
				customer.setPoints(0);
			} else {
				customer.setPoints(totalAmount.abs().intValue());
			}
		}

		order.setTotalAmount(totalAmount);
		String info = order.getPaymentInfo();
		String last4Digits;
		if (info.length() <= 4) {
			last4Digits = info;
		} else {
			last4Digits = info.substring(info.length() - 4);
		}

		info = "Card number XXXX XXXX XXXX " + last4Digits +" used for payment";
		order.setPaymentInfo(info);
		order.setCustomerDTO(customer);
		order.setOrderedDate(LocalDateTime.now());
		customer.addOrder(order);

		Order savedOder = orderRepository.save(OrderTransformer.transform(order));
		order.setId(savedOder.getId());
		return OrderTransformer.transform(savedOder);
	}

	public OrderDTO updateOrder(OrderDTO order) { /* Update the order in db */ 
		Order savedOder = orderRepository.save(OrderTransformer.transform(order));
		return OrderTransformer.transform(savedOder);
	}


	@Override
	public void completeOrder(OrderDTO order) { /* Complete the order and add reward points to customer in db */ 
		order.setStatus(OrderStatus.COMPLETED);
		Integer points = order.getTotalAmount().divide(new BigDecimal(100)).intValue();
		order.getCustomerDTO().setPoints(order.getCustomerDTO().getPoints() + points);
		orderRepository.save(OrderTransformer.transform(order));
	}

	@Override
	public OrderItemDTO saveOrderItem(OrderItemDTO orderItem) {   /* Save the order item in db */ 
		return orderItemService.saveOrderItem(orderItem);
	}

	@Override
	public void cancelOrder(OrderDTO order) {	/* Cancel the order in db */ 
		order.setStatus(OrderStatus.CANCELED);
		order.setEndDate(LocalDateTime.now());
		order.setTotalAmount(new BigDecimal(0));
		order.setPaymentInfo("");
		List<OrderItemDTO> orderItems = order.getOrderItems();
		for (OrderItemDTO i : orderItems) {
			i.setOrderStatus(OrderItemStatus.CANCELED);
		}
		if (orderItems != null) {
			orderItems.stream().forEach(orderItemObj -> orderItemObj.setOrderStatus(OrderItemStatus.CANCELED));
		}
		orderRepository.save(OrderTransformer.transform(order));
	}

	@Override
	public File downloadReceipt(OrderDTO orderDTO) throws Exception { /* Create the pdf for download */
		Map<String, OrderDTO> data = new HashMap<String, OrderDTO>();
		data.put("order", orderDTO);
		return pdfGenerator.createPdf("customer/PDF", data);

	}

	@Override
	public List<OrderItemDTO> getOrderItemsByPublisher(Long publisherId) { /* get the order item by publisher */
		return orderItemService.getOrderItemsByPublisher(publisherId);
	}

	@Override
	public List<OrderDTO> getAll() {/* get the all the orders */
		final List<Order> results = (List<Order>) orderRepository.findAll();
		List<OrderDTO> resultList = new ArrayList<OrderDTO>();
		if (results != null) {
			results.stream().forEach(orderObj -> resultList.add(OrderTransformer.transform(orderObj)));
		}
		return resultList;
	}

	@Override
	public List<OrderItemDTO> getDeliveredOrderItemsByOrder(Long orderId) {	/* get the delivered orders only */
		return orderItemService.getDeliveredOrderItemsByOrder(orderId);

	}

}
