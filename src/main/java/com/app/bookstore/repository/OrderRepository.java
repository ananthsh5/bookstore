package com.app.bookstore.repository;

import org.springframework.data.repository.CrudRepository;

import com.app.bookstore.domain.Order;

/**
 * @author Ananth Shanmugam
 */
public interface OrderRepository extends CrudRepository<Order, Long> {

}
