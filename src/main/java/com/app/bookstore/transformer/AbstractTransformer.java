package com.app.bookstore.transformer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.app.bookstore.domain.Book;
import com.app.bookstore.domain.Bookcategory;
import com.app.bookstore.domain.CartItem;
import com.app.bookstore.domain.Customer;
import com.app.bookstore.domain.Message;
import com.app.bookstore.domain.Order;
import com.app.bookstore.domain.OrderItem;
import com.app.bookstore.domain.Promotion;
import com.app.bookstore.domain.Publisher;
import com.app.bookstore.domain.User;
import com.app.bookstore.domain.dto.BookDTO;
import com.app.bookstore.domain.dto.BookcategoryDTO;
import com.app.bookstore.domain.dto.CartItemDTO;
import com.app.bookstore.domain.dto.CustomerDTO;
import com.app.bookstore.domain.dto.MessageDTO;
import com.app.bookstore.domain.dto.OrderDTO;
import com.app.bookstore.domain.dto.OrderItemDTO;
import com.app.bookstore.domain.dto.PromotionDTO;
import com.app.bookstore.domain.dto.PublisherDto;
import com.app.bookstore.domain.dto.UserDTO;
import com.app.bookstore.util.TransformerUtil;

/**
 * @author Ananth Shanmugam
 * This class is the primary abstract converter class to convert 
 * 1) dto to entity, 
 * 2) entity to dto, 
 * 3) list of dto to list of entity and 
 * 4) list of entity to list of dto  
 */
public abstract class AbstractTransformer {

	public static Optional<BookDTO> convertDbBookToBookDTO(Book book) {
		return (Optional.ofNullable(book)).map(bookObj -> (BookDTO) TransformerUtil.copyAllUtility(bookObj, new BookDTO()));
	}

	public static Optional<Book> convertBookDTOToDbBook(BookDTO bookDTO) {
		return (Optional.ofNullable(bookDTO)).map(bookDTOObj -> (Book)TransformerUtil.copyAllUtility(bookDTOObj, new Book()));
	}

	public static Optional<CartItemDTO> convertDbCartItemToCartItemDTO(CartItem cartItem) {
		return (Optional.ofNullable(cartItem)).map(cartItemObj -> (CartItemDTO) TransformerUtil.copyAllUtility(cartItemObj, new CartItemDTO()));
	}

	public static Optional<CartItem> convertCartItemDTOToDbCartItem(CartItemDTO cartItemDTO) {
		return (Optional.ofNullable(cartItemDTO)).map(cartItemDTOObj -> (CartItem)TransformerUtil.copyAllUtility(cartItemDTOObj, new CartItem()));
	}

	public static Optional<BookcategoryDTO> convertDbBookcategoryToBookcategoryDTO(Bookcategory bookcategory) {
		return (Optional.ofNullable(bookcategory)).map(bookcategoryObj -> (BookcategoryDTO) TransformerUtil.copyAllUtility(bookcategoryObj, new BookcategoryDTO()));
	}

	public static Optional<Bookcategory> convertBookcategoryDTOToDbBookcategory(BookcategoryDTO bookcategoryDTO) {
		return (Optional.ofNullable(bookcategoryDTO)).map(bookcategoryDTOObj -> (Bookcategory)TransformerUtil.copyAllUtility(bookcategoryDTOObj, new Bookcategory()));
	}

	public static Optional<CustomerDTO> convertDbCustomerToCustomerDTO(Customer customer) {
		return (Optional.ofNullable(customer)).map(customerObj -> (CustomerDTO) TransformerUtil.copyAllUtility(customerObj, new CustomerDTO()));
	}

	public static Optional<Customer> convertCustomerDTOToDbCustomer(CustomerDTO customerDTO) {
		return (Optional.ofNullable(customerDTO)).map(customerDTOObj -> (Customer)TransformerUtil.copyAllUtility(customerDTOObj, new Customer()));
	}

	public static Optional<MessageDTO> convertDbMessageToMessageDTO(Message message) {
		return (Optional.ofNullable(message)).map(messageObj -> (MessageDTO) TransformerUtil.copyAllUtility(messageObj, new MessageDTO()));
	}

	public static Optional<Message> convertMessageDTOToDbMessage(MessageDTO messageDTO) {
		return (Optional.ofNullable(messageDTO)).map(messageDTOObj -> (Message)TransformerUtil.copyAllUtility(messageDTOObj, new Message()));
	}

	public static Optional<OrderItemDTO> convertDbOrderItemToOrderItemDTO(OrderItem orderItem) {
		return (Optional.ofNullable(orderItem)).map(orderItemObj -> (OrderItemDTO) TransformerUtil.copyAllUtility(orderItemObj, new OrderItemDTO()));
	}

	public static Optional<OrderItem> convertOrderItemDTOToDbOrderItem(OrderItemDTO orderItemDTO) {
		return (Optional.ofNullable(orderItemDTO)).map(orderItemDTOObj -> (OrderItem)TransformerUtil.copyAllUtility(orderItemDTO, new OrderItem()));
	}

	public static Optional<OrderDTO> convertDbOrderToOrderDTO(Order order) {
		return (Optional.ofNullable(order)).map(orderObj -> (OrderDTO) TransformerUtil.copyAllUtility(orderObj, new OrderDTO()));
	}

	public static Optional<Order> convertOrderDTOToDbOrder(OrderDTO orderDTO) {
		return (Optional.ofNullable(orderDTO)).map(orderDTOObj -> (Order)TransformerUtil.copyAllUtility(orderDTO, new Order()));
	}

	public static Optional<PromotionDTO> convertDbPromotionToPromotionDTO(Promotion promotion) {
		return (Optional.ofNullable(promotion)).map(promotionObj -> (PromotionDTO) TransformerUtil.copyAllUtility(promotionObj, new PromotionDTO()));
	}

	public static Optional<Promotion> convertPromotionDTOToDbPromotion(PromotionDTO promotionDTO) {
		return (Optional.ofNullable(promotionDTO)).map(promotionDTOObj -> (Promotion) TransformerUtil.copyAllUtility(promotionDTO, new Promotion()));
	}

	public static Optional<PublisherDto> convertDbPublisherToPublisherDTO(Publisher publisher) {
		return (Optional.ofNullable(publisher)).map(publisherObj -> (PublisherDto) TransformerUtil.copyAllUtility(publisherObj, new PublisherDto()));
	}

	public static Optional<Publisher> convertPublisherDTOToDbPublisher(PublisherDto publisherDto) {
		return (Optional.ofNullable(publisherDto)).map(publisherDTOObj -> (Publisher) TransformerUtil.copyAllUtility(publisherDTOObj, new Publisher()));
	}

	public static Optional<UserDTO> convertDbUserToUserDTO(User user) {
		return (Optional.ofNullable(user)).map(userObj -> (UserDTO) TransformerUtil.copyAllUtility(userObj, new UserDTO()));
	}

	public static Optional<User> convertUserDTOToDbUser(UserDTO userDTO) {
		return (Optional.ofNullable(userDTO)).map(userDTOObj -> (User) TransformerUtil.copyAllUtility(userDTOObj, new User()));
	}

	public static Optional<List<BookDTO>> convertDbBookLstToBookDTOLst(List<Book> bookList) {
		List<BookDTO> bookDtoList = null;
		if (bookList != null && !bookList.isEmpty()) {
			bookDtoList = bookList.stream().map(book -> (BookDTO) TransformerUtil.copyAllUtility(book, new BookDTO()))
					.collect(Collectors.toList());
		}
		return Optional.ofNullable(bookDtoList);
	}

	public static Optional<List<Book>> convertBookDTOLstToDbBookLst(List<BookDTO> bookDtoList) {
		List<Book> bookList = null;
		if (bookDtoList != null && !bookDtoList.isEmpty()) {
			bookList = bookDtoList.stream().map(bookDto -> (Book) TransformerUtil.copyAllUtility(bookDto, new Book()))
					.collect(Collectors.toList());
		}
		return Optional.ofNullable(bookList);
	}

	public static Optional<List<CartItemDTO>> convertDbCartItemLstToCartItemDTOLst(List<CartItem> cartItemList) {

		List<CartItemDTO> cartItemDtoList = null;
		if (cartItemList != null && !cartItemList.isEmpty()) {
			cartItemDtoList = cartItemList.stream()
					.map(cartItem -> (CartItemDTO) TransformerUtil.copyAllUtility(cartItem, new CartItemDTO()))
					.collect(Collectors.toList());
		}
		return Optional.ofNullable(cartItemDtoList);
	}

	public static Optional<List<CartItem>> convertCartItemDTOLstToDbCartItemLst(List<CartItemDTO> cartItemDtoList) {
		List<CartItem> cartItemList = null;
		if (cartItemDtoList != null && !cartItemDtoList.isEmpty()) {
			cartItemList = cartItemDtoList.stream()
					.map(cartItemDTO -> (CartItem) TransformerUtil.copyAllUtility(cartItemDTO, new Publisher()))
					.collect(Collectors.toList());
		}
		return Optional.ofNullable(cartItemList);
	}

	public static Optional<List<PublisherDto>> convertDbPublisherLstToPublisherDTOLst(List<Publisher> publisherList) {
		List<PublisherDto> publisherDtoList = null;
		if (publisherList != null && !publisherList.isEmpty()) {
			publisherDtoList = publisherList.stream()
					.map(publisher -> (PublisherDto) TransformerUtil.copyAllUtility(publisher, new PublisherDto()))
					.collect(Collectors.toList());
		}
		return Optional.ofNullable(publisherDtoList);
	}

	public static Optional<List<Publisher>> convertPublisherDTOLstToDbPublisherLst(
			List<PublisherDto> publisherDtoList) {
		List<Publisher> publisherList = null;
		if (publisherDtoList != null && !publisherDtoList.isEmpty()) {
			publisherList = publisherDtoList.stream()
					.map(publisher -> (Publisher) TransformerUtil.copyAllUtility(publisher, new Publisher()))
					.collect(Collectors.toList());
		}
		return Optional.ofNullable(publisherList);
	}

	public static Optional<List<MessageDTO>> convertDbMessageLstToMessageDTOLst(List<Message> messageList) {
		List<MessageDTO> messageDtoList = null;
		if (messageList != null && !messageList.isEmpty()) {
			messageDtoList = messageList.stream()
					.map(message -> (MessageDTO) TransformerUtil.copyAllUtility(message, new MessageDTO()))
					.collect(Collectors.toList());
		}
		return Optional.ofNullable(messageDtoList);
	}

	public static Optional<List<Message>> convertMessageDTOLstToDbMessageLst(List<MessageDTO> messageDtoList) {
		List<Message> messageList = null;
		if (messageDtoList != null && !messageDtoList.isEmpty()) {
			messageList = messageDtoList.stream()
					.map(message -> (Message) TransformerUtil.copyAllUtility(message, new Message()))
					.collect(Collectors.toList());
		}
		return Optional.ofNullable(messageList);
	}

	public static Optional<List<OrderDTO>> convertDbOrderLstToOrderDTOLst(List<Order> orderList) {
		List<OrderDTO> orderDtoList = null;
		if (orderList != null && !orderList.isEmpty()) {
			orderDtoList = orderList.stream()
					.map(order -> (OrderDTO) TransformerUtil.copyAllUtility(order, new OrderDTO()))
					.collect(Collectors.toList());
		}
		return Optional.ofNullable(orderDtoList);
	}

	public static Optional<List<Order>> convertOrderDTOLstToDbOrderLst(List<OrderDTO> orderDtoList) {
		List<Order> orderList = null;
		if (orderDtoList != null && !orderDtoList.isEmpty()) {
			orderList = orderDtoList.stream()
					.map(orderDTO -> (Order) TransformerUtil.copyAllUtility(orderDTO, new Order()))
					.collect(Collectors.toList());
		}
		return Optional.ofNullable(orderList);
	}

	public static Optional<List<OrderItem>> convertOrderItemDTOLstToDbOrderItemLst(List<OrderItemDTO> orderItemDtoList) {
		List<OrderItem> orderList = null;
		if (orderItemDtoList != null && !orderItemDtoList.isEmpty()) {
			orderList = orderItemDtoList.stream()
					.map(orderItemDTO -> (OrderItem) TransformerUtil.copyAllUtility(orderItemDTO, new OrderItem()))
					.collect(Collectors.toList());
		}
		return Optional.ofNullable(orderList);
	}

	public static Optional<List<CustomerDTO>> convertDbCustomerLstToCustomerDTOLst(List<Customer> customerList) {
		List<CustomerDTO> customerDtoList = null;
		if (customerList != null && !customerList.isEmpty()) {
			customerDtoList = customerList.stream()
					.map(customer -> (CustomerDTO) TransformerUtil.copyAllUtility(customer, new CustomerDTO()))
					.collect(Collectors.toList());
		}
		return Optional.ofNullable(customerDtoList);
	}

	public static Optional<List<Customer>> convertCustomerDTOLstToDbCustomerLst(List<CustomerDTO> customerDtoList) {
		List<Customer> customerList = null;
		if (customerDtoList != null && !customerDtoList.isEmpty()) {
			customerList = customerDtoList.stream()
					.map(customerDTO -> (Customer) TransformerUtil.copyAllUtility(customerDTO, new Customer()))
					.collect(Collectors.toList());
		}
		return Optional.ofNullable(customerList);
	}

}
