package com.app.bookstore.service;

import java.math.BigDecimal;
import java.util.List;

import com.app.bookstore.domain.dto.CartItemDTO;
import com.app.bookstore.domain.dto.CustomerDTO;

/**
 * @author Ananth Shanmugam
 */
public interface CartService {
    void addCartItem(CartItemDTO item);
    CartItemDTO saveCartItem(CustomerDTO customer, CartItemDTO item);
    void removeCartItem(Long id);
    List<CartItemDTO> getCartByCustomerId(Long customerId);
    BigDecimal getTotalAmount(Long CustomerId);
}
