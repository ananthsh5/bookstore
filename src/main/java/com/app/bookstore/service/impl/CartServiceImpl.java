package com.app.bookstore.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.bookstore.domain.CartItem;
import com.app.bookstore.domain.dto.CartItemDTO;
import com.app.bookstore.domain.dto.CustomerDTO;
import com.app.bookstore.repository.CartItemRepository;
import com.app.bookstore.repository.CartRepository;
import com.app.bookstore.service.BookService;
import com.app.bookstore.service.CartService;
import com.app.bookstore.transformer.CartItemTransformer;

/**
 * @author Ananth Shanmugam
 */
@Service
public class CartServiceImpl implements CartService {

//	private final static Logger LOGGER = LoggerFactory.getLogger(CartServiceImpl.class);

	@Autowired
	CartRepository cartRepository;

	@Autowired
	CartItemRepository cartItemRepository;

	@Autowired
	BookService bookService;

	@Override
	public void addCartItem(CartItemDTO item) {  /* Save cart item of customer to db */

		CartItem cartItem = CartItemTransformer.transform(item);
		cartItem.getCustomer().getCartItems().add(cartItem);

		cartRepository.save(cartItem);
	}

	@Override
	public CartItemDTO saveCartItem(CustomerDTO customer, CartItemDTO item) { /* Save cart item of customer to db */
		CartItem cartItem = cartItemRepository.findById(item.getId()).get();
		cartItem.getCustomer().getCartItems().add(cartItem);
		cartItem.setQuantity(item.getQuantity());
		cartRepository.save(cartItem);

		return CartItemTransformer.transform(cartItem);
	}

	@Override
	public void removeCartItem(Long id) {  /* Remove cart item from customer cart in db */
		CartItem item = cartRepository.findById(id).get();
		item.getCustomer().removeCartItem(item);
		cartRepository.delete(item);
	}

	@Override
	public List<CartItemDTO> getCartByCustomerId(Long customerId) {  /* Get the customer cart from db */
		final List<CartItem> results = (List<CartItem>) cartRepository.getCartItemByCustomerId(customerId);
		List<CartItemDTO> resultList = new ArrayList<CartItemDTO>();
		if (results != null) {
			results.stream().forEach(cartItemObj -> resultList.add(CartItemTransformer.transform(cartItemObj)));
		}
		return resultList;
	}

	@Override
	public BigDecimal getTotalAmount(Long customerId) {  /* Calculate the total amount of items in customer cart */
		List<CartItem> cartItemsList = (List<CartItem>) cartRepository.getCartItemByCustomerId(customerId);
		BigDecimal totalAmount = new BigDecimal(0.00);
		if (cartItemsList != null) {
			for (CartItem ci : cartItemsList) {
				totalAmount = totalAmount.add(ci.getBook().getPrice().multiply(new BigDecimal(ci.getQuantity())));
			}
		}
		return totalAmount;
	}
}
