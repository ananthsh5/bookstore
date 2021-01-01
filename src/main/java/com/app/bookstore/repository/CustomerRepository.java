package com.app.bookstore.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.bookstore.domain.Customer;
import com.app.bookstore.domain.User;

/**
 * @author Ananth Shanmugam
 */
@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
    Customer findCustomerByUser(User user);

}
