package com.app.bookstore.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.bookstore.domain.CartItem;
import com.app.bookstore.domain.dto.CartItemDTO;
import com.app.bookstore.repository.CartItemRepository;
import com.app.bookstore.service.CartItemService;
import com.app.bookstore.transformer.CartItemTransformer;

/**
 * @author Ananth Shanmugam
 */
@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    CartItemRepository cartItemRepository;

    @Override
    public CartItemDTO saveCartItem(CartItemDTO cartItemDTO) { /* Save the cart item to db */
        
    	CartItem savedCartItem = cartItemRepository.save(CartItemTransformer.transform(cartItemDTO));
    	return CartItemTransformer.transform(savedCartItem);
    }

    @Override
    public List<CartItemDTO> getCartItems() { /* Get all the cart items */
    	final List<CartItem> results  = (List<CartItem>) cartItemRepository.findAll();
    	List<CartItemDTO> resultList = new ArrayList<CartItemDTO>();
        if ( results != null ) {
			results.stream().forEach(cartItemObj -> resultList.add(CartItemTransformer.transform(cartItemObj)));

        }
        return resultList;

        
    }

    @Override
    public CartItemDTO getCartItemById(Long id) { /* Get the cart item by id from db */
        return CartItemTransformer.transform(cartItemRepository.findById(id).get());
    }
}
