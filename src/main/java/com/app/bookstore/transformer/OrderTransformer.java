package com.app.bookstore.transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.bookstore.domain.Book;
import com.app.bookstore.domain.Customer;
import com.app.bookstore.domain.Message;
import com.app.bookstore.domain.Order;
import com.app.bookstore.domain.OrderItem;
import com.app.bookstore.domain.User;
import com.app.bookstore.domain.dto.BookDTO;
import com.app.bookstore.domain.dto.CustomerDTO;
import com.app.bookstore.domain.dto.MessageDTO;
import com.app.bookstore.domain.dto.OrderDTO;
import com.app.bookstore.domain.dto.OrderItemDTO;
import com.app.bookstore.domain.dto.UserDTO;
import com.google.common.base.Function;

/**
 * @author Ananth Shanmugam
 * Converter Class to 
 * 1) convert Order dto to Order entity, 
 * 2) convert Order entity to Order dto,
 */
public class OrderTransformer extends AbstractTransformer implements Function<Order, OrderDTO> {
	private final static Logger LOGGER = LoggerFactory.getLogger(OrderTransformer.class);

	@Override
	public OrderDTO apply(Order order) {
		return transform(order);
	}

	public static OrderDTO transform(Order order) {
		OrderDTO orderDTO = null;
		LOGGER.info("OrderTransformer ");
		Optional<OrderDTO> optionalOrderDTO = convertDbOrderToOrderDTO(order);
		if (optionalOrderDTO.isPresent()) {

			orderDTO = optionalOrderDTO.get();
			Customer customer = order.getCustomer();
			if (customer != null) {
				CustomerDTO customerDTO;
				Optional<CustomerDTO> optionalCustomerDTO = convertDbCustomerToCustomerDTO(customer);
				if (optionalCustomerDTO.isPresent()) {
					customerDTO = optionalCustomerDTO.get();
					User user = order.getCustomer().getUser();
					Optional<UserDTO> optionalUserDTO = convertDbUserToUserDTO(user);

					if (optionalUserDTO.isPresent()) {
						UserDTO userDTO = optionalUserDTO.get();
						Optional<List<MessageDTO>> messageDTOList = convertDbMessageLstToMessageDTOLst(
								user.getMessages());
						messageDTOList.ifPresent(messageDTOListObj -> userDTO.setMessages(messageDTOListObj));
						customerDTO.setUserDTO(userDTO);
					}
					orderDTO.setCustomerDTO(customerDTO);
				}
			}
			List<OrderItem> orderItems = order.getOrderItems();
			List<OrderItemDTO> orderItemBoList = new ArrayList<OrderItemDTO>();

			if (orderItems != null && !orderItems.isEmpty()) {
				for (OrderItem orderItem : orderItems) {
					Optional<OrderItemDTO> optionalOrderItemDTO = convertDbOrderItemToOrderItemDTO(orderItem);
					if (optionalOrderItemDTO.isPresent()) {
						OrderItemDTO orderItemDTO = optionalOrderItemDTO.get();
						LOGGER.info("OrderItem iss:" + orderItem.getBook().getName());

						Optional<BookDTO> optionalBookDTO = convertDbBookToBookDTO(orderItem.getBook());
						optionalBookDTO.ifPresent(bookDTOObj -> orderItemDTO.setBookDTO(bookDTOObj));
						orderItemBoList.add(orderItemDTO);
					}
				}
			}
			orderDTO.setOrderItems(orderItemBoList);
		}

		return orderDTO;
	}

	public static Order transform(final OrderDTO orderDTO) {
		Order order = null;

		Optional<Order> optionalOrder = convertOrderDTOToDbOrder(orderDTO);
		if (optionalOrder.isPresent()) {
			order = optionalOrder.get();

			Optional<Customer> optionalCustomer = convertCustomerDTOToDbCustomer(orderDTO.getCustomerDTO());
			if (optionalCustomer.isPresent()) {
				Customer customer = optionalCustomer.get();
				UserDTO userDTO = orderDTO.getCustomerDTO().getUserDTO();
				Optional<User> optionalUser = convertUserDTOToDbUser(userDTO);

				if (optionalUser.isPresent()) {
					User user = optionalUser.get();
					Optional<List<Message>> optionalMessageList = convertMessageDTOLstToDbMessageLst(
							userDTO.getMessages());
					optionalMessageList.ifPresent(optionalMessageListObj -> user.setMessages(optionalMessageListObj));
					customer.setUser(user);
				}

				order.setCustomer(customer);
			}

			List<OrderItem> orderItems = new ArrayList<OrderItem>();
			List<OrderItemDTO> orderItemBoList = orderDTO.getOrderItems();

			if (orderItemBoList != null && !orderItemBoList.isEmpty()) {
				for (OrderItemDTO orderItemDTO : orderItemBoList) {
					Optional<OrderItem> optionalOrderItemDTO = convertOrderItemDTOToDbOrderItem(orderItemDTO);
					if (optionalOrderItemDTO.isPresent()) {

						OrderItem orderItem = optionalOrderItemDTO.get();
						BookDTO bookDTO = orderItemDTO.getBookDTO();

						Optional<Book> optionalBook = convertBookDTOToDbBook(bookDTO);
						optionalBook.ifPresent(bookObj -> orderItem.setBook(bookObj));
						orderItem.setOrder(order);
						orderItems.add(orderItem);
					}
				}
			}
			order.setOrderItems(orderItems);
		}
		return order;
	}

}
