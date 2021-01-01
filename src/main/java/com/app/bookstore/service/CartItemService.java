package com.app.bookstore.service;


import java.util.List;

import com.app.bookstore.domain.dto.CartItemDTO;

/**
 * @author Ananth Shanmugam
 */
public interface CartItemService {
    public CartItemDTO saveCartItem(CartItemDTO cartItemDTO);

    public List<CartItemDTO> getCartItems();

    public CartItemDTO getCartItemById(Long id);
}
