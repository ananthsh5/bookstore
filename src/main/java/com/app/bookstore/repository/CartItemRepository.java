package com.app.bookstore.repository;

import com.app.bookstore.domain.CartItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Ananth Shanmugam
 */
@Repository
public interface CartItemRepository extends CrudRepository<CartItem, Long> {
}
