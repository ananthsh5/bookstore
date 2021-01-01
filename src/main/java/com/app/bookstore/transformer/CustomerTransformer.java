package com.app.bookstore.transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.app.bookstore.domain.CartItem;
import com.app.bookstore.domain.Customer;
import com.app.bookstore.domain.Message;
import com.app.bookstore.domain.Order;
import com.app.bookstore.domain.Publisher;
import com.app.bookstore.domain.User;
import com.app.bookstore.domain.dto.BookDTO;
import com.app.bookstore.domain.dto.CartItemDTO;
import com.app.bookstore.domain.dto.CustomerDTO;
import com.app.bookstore.domain.dto.MessageDTO;
import com.app.bookstore.domain.dto.OrderDTO;
import com.app.bookstore.domain.dto.PublisherDto;
import com.app.bookstore.domain.dto.UserDTO;
import com.google.common.base.Function;

/**
 * @author Ananth Shanmugam
 * Converter Class to 
 * 1) convert Customer dto to Customer entity, 
 * 2) convert Customer entity to Customer dto,
 */
public class CustomerTransformer extends AbstractTransformer implements Function<Customer, CustomerDTO> {

	@Override
	public CustomerDTO apply(Customer customer) {
		return transform(customer);
	}

	public static CustomerDTO transform(Customer customer) {

		Optional<CustomerDTO> optionalCustomerDTO = convertDbCustomerToCustomerDTO(customer);
		if (optionalCustomerDTO.isPresent()) {
			CustomerDTO customerDTO = optionalCustomerDTO.get();

			User user = customer.getUser();
			Optional<UserDTO> optionalUserDTO = convertDbUserToUserDTO(user);

			if (optionalUserDTO.isPresent()) {
				UserDTO userDTO = optionalUserDTO.get();

				Optional<List<MessageDTO>> messageDTOList = convertDbMessageLstToMessageDTOLst(user.getMessages());
				if (messageDTOList.isPresent()) {
					userDTO.setMessages(messageDTOList.get());
				}
				customerDTO.setUserDTO(userDTO);
			}

			Optional<List<OrderDTO>> orderBoList = convertDbOrderLstToOrderDTOLst(customer.getOrders());//
			if (orderBoList.isPresent()) {
				customerDTO.setOrderDTOs(orderBoList.get());//
			}
			List<CartItem> cartItems = customer.getCartItems();
			List<CartItemDTO> cartItemBoList = new ArrayList<CartItemDTO>();
			if (cartItems != null)
				// LOGGER.info("cartItems size is "+ cartItems.size());
				if (cartItems != null && !cartItems.isEmpty()) {
					for (CartItem cartItem : cartItems) {

						Optional<CartItemDTO> optionalCartItemDTO = convertDbCartItemToCartItemDTO(cartItem);
						if (optionalCartItemDTO.isPresent()) {
							CartItemDTO cartItemDTO = optionalCartItemDTO.get();

							Optional<BookDTO> optionalBookDTO = convertDbBookToBookDTO(cartItem.getBook());
							optionalBookDTO.ifPresent(bookDTOObj -> cartItemDTO.setBookDTO(bookDTOObj));

							Optional<CustomerDTO> optionalCartItemCustomerDTO = convertDbCustomerToCustomerDTO(
									cartItem.getCustomer());
							optionalCartItemCustomerDTO.ifPresent(
									optionalCustomerDTOObj -> cartItemDTO.setCustomerDTO(optionalCustomerDTOObj));
							cartItemBoList.add(cartItemDTO);
						}
					}
					customerDTO.setCartItems(cartItemBoList);

					Optional<List<PublisherDto>> publisherDtoList = convertDbPublisherLstToPublisherDTOLst(
							customer.getPublishers());
					if (publisherDtoList.isPresent()) {
						customerDTO.setPublisherDtos(publisherDtoList.get());
					}
				}
			return customerDTO;
		}
		return null;
	}

	public static Customer transform(final CustomerDTO customerDTO) {

		Optional<Customer> optionalCustomer = convertCustomerDTOToDbCustomer(customerDTO);
		if (optionalCustomer.isPresent()) {
			Customer customer = optionalCustomer.get();

			UserDTO userDTO = customerDTO.getUserDTO();
			Optional<User> optionalUser = convertUserDTOToDbUser(userDTO);

			if (optionalUser.isPresent()) {
				User user = optionalUser.get();
				Optional<List<Message>> messageList = convertMessageDTOLstToDbMessageLst(userDTO.getMessages());
				if (messageList.isPresent()) {
					user.setMessages(messageList.get());
				}
				customer.setUser(user);
			}
			Optional<List<Order>> orderList = convertOrderDTOLstToDbOrderLst(customerDTO.getOrderDTOs());
			if (orderList.isPresent()) {
				customer.setOrders(orderList.get());
			}

			List<CartItemDTO> cartItemDTOs = customerDTO.getCartItems();
			Optional<List<CartItem>> cartItems = convertCartItemDTOLstToDbCartItemLst(cartItemDTOs);
			if (cartItems.isPresent()) {
				customer.setCartItems(cartItems.get());
			}
			Optional<List<Publisher>> publishers = convertPublisherDTOLstToDbPublisherLst(
					customerDTO.getPublisherDtos());
			if (publishers.isPresent()) {
				customer.setPublishers(publishers.get());
			}
			return customer;
		}
		return null;
	}

}
