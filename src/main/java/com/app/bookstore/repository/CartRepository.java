package com.app.bookstore.repository;

import com.app.bookstore.domain.CartItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Ananth Shanmugam
 */
@Repository
public interface CartRepository extends CrudRepository<CartItem, Long> {
    @Query("select ci from CartItem ci where ci.customer.id = :customerId")
    public List<CartItem> getCartItemByCustomerId(Long customerId);
}
