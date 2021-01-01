package com.app.bookstore.service;

import java.util.List;

import com.app.bookstore.domain.Customer;
import com.app.bookstore.domain.dto.CustomerDTO;
import com.app.bookstore.domain.dto.OrderDTO;
import com.app.bookstore.domain.dto.PublisherDto;
import com.app.bookstore.domain.dto.PublisherInfo;
import com.app.bookstore.domain.dto.UserDTO;

/**
 * @author Ananth Shanmugam
 */
public interface CustomerService {
	CustomerDTO saveCustomerWithUser(UserDTO userDTO);
	CustomerDTO saveCustomer(CustomerDTO customerDTO);

	CustomerDTO updateCustomer(CustomerDTO customer);
    Customer getCustomerById(Long id);
    CustomerDTO getCustomerByUser();

    void subscribePublisher(CustomerDTO customer, PublisherDto publisher);
    void unsubscribePublisher(CustomerDTO customer, PublisherDto publisher);
    List<OrderDTO> getOrdersByCustomerId(Long customerId);
    List<PublisherInfo> getSubscribers(Long customerId);
    int getCustomerActiveSubscriptions(PublisherDto publisherDto);
    
}
