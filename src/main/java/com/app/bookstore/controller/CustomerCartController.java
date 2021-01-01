package com.app.bookstore.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.bookstore.domain.dto.BookDTO;
import com.app.bookstore.domain.dto.CartDTO;
import com.app.bookstore.domain.dto.CartItemDTO;
import com.app.bookstore.domain.dto.CustomerDTO;
import com.app.bookstore.domain.dto.UserDTO;
import com.app.bookstore.service.BookService;
import com.app.bookstore.service.CartItemService;
import com.app.bookstore.service.CartService;
import com.app.bookstore.service.CustomerService;
import com.app.bookstore.service.UserService;

/**
 * @author Ananth Shanmugam
 */
@Controller
public class CustomerCartController {

	@Autowired
	private BookService bookService;

	@Autowired
	private CartService cartService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private UserService userService;

	@Autowired
	private CartItemService cartItemService;

	private final String CUSTOMER_SHOPPING_CART_PAGE = "/customer/shoppingCart";

	@GetMapping(value = { "/customer/cart" }) /* Get the customer cart page */
	public String getBookStoreCartForm() {
		return CUSTOMER_SHOPPING_CART_PAGE;
	}

	@GetMapping("/customer/shoppingCart")  /* Get the customer cart details */
	@ResponseBody
	public List<CartDTO> getCart() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			UserDTO user = userService.findByEmail(auth.getName());
			if (user != null) {
				CustomerDTO customerDTO = customerService.getCustomerByUser();
				if (customerDTO != null) {
					List<CartItemDTO> cartItems = cartService.getCartByCustomerId(customerDTO.getId());
					List<CartDTO> cartDTOitems = new ArrayList<>();
					if (cartItems != null) {
						cartItems.stream().forEach(cartDTOitemsObj -> {
							BookDTO book = cartDTOitemsObj.getBookDTO();
							cartDTOitems.add(new CartDTO(cartDTOitemsObj.getId(), book.getName(),
									book.getImage(), cartDTOitemsObj.getQuantity(),book.getPrice()));
						});
					}

					return cartDTOitems;
				}
			}
		}

		return null;
	}

	@PostMapping("/customer/cart/add")		
	@ResponseBody
	public CartItemDTO saveCartItem(@Valid CartItemDTO item) { /* Save the book to customer cart */
		CustomerDTO customer = customerService.getCustomerByUser();
		return cartService.saveCartItem(customer, item);
	}

	private final static Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

	@PutMapping(value = "/customer/cart/{id}/increaseQuantity", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public CartItemDTO increaseQuantity(@PathVariable Long id) { /* Increase the book quantity of customer in cart */
		CustomerDTO customer = customerService.getCustomerByUser();
		LOGGER.info("increaseQuantity id : " + id);

		CartItemDTO cartItem = cartItemService.getCartItemById(id);
		cartItem.setQuantity(cartItem.getQuantity() + 1);
		BookDTO book = bookService.findById(id);
		cartItem.setBookDTO(book);
		cartItem.setCustomerDTO(customer);

		CartItemDTO saved = cartService.saveCartItem(customer, cartItem);
		LOGGER.info("final here increaseQuantity");
		return saved;
	}

	@PutMapping(value = "/customer/cart/{id}/decreaseQuantity", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public CartItemDTO decreaseQuantity(@PathVariable Long id) {  /* Decrease the book quantity of customer in cart */
		CustomerDTO customer = customerService.getCustomerByUser();
		CartItemDTO i = cartItemService.getCartItemById(id); 
		if (i.getQuantity() > 1) {
			i.setQuantity(i.getQuantity() - 1);
		}
		return cartService.saveCartItem(customer, i);
	}

	@DeleteMapping(value = "/customer/cart/remove/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Boolean removeCartItem(@PathVariable Long id) {    /* Remove the book from the customer cart */
		cartService.removeCartItem(id);
		return true;
	}

}
