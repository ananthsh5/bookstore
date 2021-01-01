package com.app.bookstore.transformer;

import java.util.List;
import java.util.Optional;

import com.app.bookstore.domain.Book;
import com.app.bookstore.domain.CartItem;
import com.app.bookstore.domain.Customer;
import com.app.bookstore.domain.Message;
import com.app.bookstore.domain.Publisher;
import com.app.bookstore.domain.User;
import com.app.bookstore.domain.dto.BookDTO;
import com.app.bookstore.domain.dto.CartItemDTO;
import com.app.bookstore.domain.dto.CustomerDTO;
import com.app.bookstore.domain.dto.MessageDTO;
import com.app.bookstore.domain.dto.PublisherDto;
import com.app.bookstore.domain.dto.UserDTO;
import com.google.common.base.Function;

/**
 * @author Ananth Shanmugam
 * Converter Class to 
 * 1) convert CartItem dto to CartItem entity, 
 * 2) convert CartItem entity to CartItem dto,
 */
public class CartItemTransformer extends AbstractTransformer implements Function<CartItem, CartItemDTO> {

//	private final static Logger LOGGER = LoggerFactory.getLogger(CustomerAccountController.class);

	@Override
	public CartItemDTO apply(CartItem cartItem) {
		return transform(cartItem);
	}

	public static CartItemDTO transform(CartItem cartItem) {
		Optional<CartItemDTO> optionalCartItemDTO = convertDbCartItemToCartItemDTO(cartItem);
		if (optionalCartItemDTO.isPresent()) {
			CartItemDTO cartItemDTO = optionalCartItemDTO.get();

			Optional<BookDTO> optionalBookDTO = convertDbBookToBookDTO(cartItem.getBook());

			if (optionalBookDTO.isPresent()) {
				Optional<PublisherDto> optionalPublisherDto = convertDbPublisherToPublisherDTO(
						cartItem.getBook().getPublisher());

				BookDTO bookDTO = optionalBookDTO.get();
				optionalPublisherDto
						.ifPresent(optionalPublisherDtoObj -> bookDTO.setPublisherDto(optionalPublisherDtoObj));
				cartItemDTO.setBookDTO(bookDTO);
			}
			Customer customer = cartItem.getCustomer();
			Optional<CustomerDTO> optionalCustomerDTO = convertDbCustomerToCustomerDTO(customer);
			if (optionalCustomerDTO.isPresent()) {

				CustomerDTO customerDto = optionalCustomerDTO.get();
				Optional<List<CartItemDTO>> optionalCartItemLstDTO = convertDbCartItemLstToCartItemDTOLst(
						customer.getCartItems());
				optionalCartItemLstDTO
						.ifPresent(optionalCartItemDTOObj -> customerDto.setCartItems(optionalCartItemDTOObj));

				Optional<List<PublisherDto>> publisherDtoList = convertDbPublisherLstToPublisherDTOLst(
						customer.getPublishers());
				if (publisherDtoList.isPresent())
					customerDto.setPublisherDtos(publisherDtoList.get());

				User user = customer.getUser();
				Optional<UserDTO> optionalUserDTO = convertDbUserToUserDTO(user);

				if (optionalUserDTO.isPresent()) {
					UserDTO userDTO = optionalUserDTO.get();
					Optional<List<MessageDTO>> messageDTOList = convertDbMessageLstToMessageDTOLst(user.getMessages());
					if (messageDTOList.isPresent())
						userDTO.setMessages(messageDTOList.get());

					customerDto.setUserDTO(userDTO);
				}
				cartItemDTO.setCustomerDTO(customerDto);
			}
			return cartItemDTO;
		}
		return null;
	}

	public static CartItem transform(final CartItemDTO cartItemDTO) {

		Optional<CartItem> optionalCartItem = convertCartItemDTOToDbCartItem(cartItemDTO);
		if (optionalCartItem.isPresent()) {
			CartItem cartItem = optionalCartItem.get();
			Optional<Book> optionalBook = convertBookDTOToDbBook(cartItemDTO.getBookDTO());
			if (optionalBook.isPresent()) {
				Book book = optionalBook.get();

				Optional<Publisher> optionalPublisher = convertPublisherDTOToDbPublisher(
						cartItemDTO.getBookDTO().getPublisherDto());
				optionalPublisher.ifPresent(optionalPublisherObj -> book.setPublisher(optionalPublisherObj));
				cartItem.setBook(book);
			}
			CustomerDTO customerDTO = cartItemDTO.getCustomerDTO();

			Optional<Customer> optionalCustomer = convertCustomerDTOToDbCustomer(customerDTO);
			if (optionalCustomer.isPresent()) {

				Customer customer = optionalCustomer.get();

				List<CartItemDTO> cartItemDTOs = customerDTO.getCartItems();
				Optional<List<CartItem>> optionalCartItemList = convertCartItemDTOLstToDbCartItemLst(cartItemDTOs);
				optionalCartItemList.ifPresent(optionalCartItemsObj ->customer.setCartItems(optionalCartItemsObj));
	
				Optional<List<Publisher>> optionalPublisherList = convertPublisherDTOLstToDbPublisherLst(
						customerDTO.getPublisherDtos());
				optionalPublisherList.ifPresent(optionalpublisherListObj -> customer.setPublishers(optionalpublisherListObj));

				UserDTO userDTO = customerDTO.getUserDTO();
				Optional<User> optionalUser = convertUserDTOToDbUser(userDTO);

				if (optionalUser.isPresent()) {
					User user = optionalUser.get();
					Optional<List<Message>> optionalMessageList = convertMessageDTOLstToDbMessageLst(userDTO.getMessages());
					optionalMessageList.ifPresent(userObj -> user.setMessages(optionalMessageList.get()));

					customer.setUser(user);
				}
				cartItem.setCustomer(customer);
			}
			return cartItem;
		}
		return null;
	}

}
