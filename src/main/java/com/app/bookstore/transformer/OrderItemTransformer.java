package com.app.bookstore.transformer;

import java.util.List;
import java.util.Optional;

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
 * 1) convert OrderItem dto to OrderItem entity, 
 * 2) convert OrderItem entity to  OrderItem dto,
 */
public class OrderItemTransformer extends AbstractTransformer implements Function<OrderItem, OrderItemDTO> {

	@Override
	public OrderItemDTO apply(OrderItem orderItem) {
		return transform(orderItem);
	}

	public static OrderItemDTO transform(OrderItem orderItem) {
		Optional<OrderItemDTO> optionalOrderItemDTO = convertDbOrderItemToOrderItemDTO(orderItem);
		if (optionalOrderItemDTO.isPresent()) {
			OrderItemDTO orderItemDTO = optionalOrderItemDTO.get();

			Optional<BookDTO> optionalBookDTO = convertDbBookToBookDTO(orderItem.getBook());
			optionalBookDTO.ifPresent(bookDTOObj -> orderItemDTO.setBookDTO(bookDTOObj));

			Order order = orderItem.getOrder();
			OrderDTO orderDTO = null;

			Optional<OrderDTO> optionalOrderDTO = convertDbOrderToOrderDTO(order);
			if (optionalOrderDTO.isPresent()) {
				orderDTO = optionalOrderDTO.get();

				orderItemDTO.setOrderDTO(orderDTO);
				Customer customer = orderItem.getOrder().getCustomer();

				Optional<CustomerDTO> optionalCustomerDTO = convertDbCustomerToCustomerDTO(customer);
				if (optionalCustomerDTO.isPresent()) {
					CustomerDTO customerDTO = optionalCustomerDTO.get();

					User user = orderItem.getOrder().getCustomer().getUser();
					Optional<UserDTO> optionalUserDTO = convertDbUserToUserDTO(user);

					if (optionalUserDTO.isPresent()) {
						UserDTO userDTO = optionalUserDTO.get();

						Optional<List<MessageDTO>> messageDTOList = convertDbMessageLstToMessageDTOLst(
								user.getMessages());
						if (messageDTOList.isPresent())
							userDTO.setMessages(messageDTOList.get());

						customerDTO.setUserDTO(userDTO);
						orderDTO.setCustomerDTO(customerDTO);
					}
				}
			}
			return orderItemDTO;

		}
		return null;
	}

	public static OrderItem transform(final OrderItemDTO orderItemDTO) {

		Optional<OrderItem> optionalOrderItemDTO = convertOrderItemDTOToDbOrderItem(orderItemDTO);
		if (optionalOrderItemDTO.isPresent()) {
			OrderItem orderItem = optionalOrderItemDTO.get();
			Optional<Book> optionalBook = convertBookDTOToDbBook(orderItemDTO.getBookDTO());
			optionalBook.ifPresent(optionalBookObj -> orderItem.setBook(optionalBook.get()));

			OrderDTO orderDTO = orderItemDTO.getOrderDTO();
			Optional<Order> optionalOrder = convertOrderDTOToDbOrder(orderDTO);
			if (optionalOrder.isPresent()) {
				Order order = optionalOrder.get();
				Optional<Customer> optionalCustomer = convertCustomerDTOToDbCustomer(orderDTO.getCustomerDTO());
				if (optionalCustomer.isPresent()) {

					Customer customer = optionalCustomer.get();
					UserDTO userDTO = orderDTO.getCustomerDTO().getUserDTO();
					Optional<User> optionalUser = convertUserDTOToDbUser(userDTO);

					if (optionalUser.isPresent()) {
						User user = optionalUser.get();
						Optional<List<Message>> optionalMessageList = convertMessageDTOLstToDbMessageLst(userDTO.getMessages());
						optionalMessageList.ifPresent(userObj -> user.setMessages(optionalMessageList.get()));

						customer.setUser(user);
					}
					order.setCustomer(customer);
				}

				orderItem.setOrder(order);
			}
			return orderItem;

		}
		return null;
	}

}
