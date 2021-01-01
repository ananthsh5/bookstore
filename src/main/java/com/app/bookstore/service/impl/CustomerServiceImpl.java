package com.app.bookstore.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.app.bookstore.domain.Customer;
import com.app.bookstore.domain.Order;
import com.app.bookstore.domain.Publisher;
import com.app.bookstore.domain.Role;
import com.app.bookstore.domain.User;
import com.app.bookstore.domain.dto.CustomerDTO;
import com.app.bookstore.domain.dto.OrderDTO;
import com.app.bookstore.domain.dto.PublisherDto;
import com.app.bookstore.domain.dto.PublisherInfo;
import com.app.bookstore.domain.dto.UserDTO;
import com.app.bookstore.repository.CustomerRepository;
import com.app.bookstore.service.CustomerService;
import com.app.bookstore.service.UserService;
import com.app.bookstore.transformer.CustomerTransformer;
import com.app.bookstore.transformer.OrderTransformer;
import com.app.bookstore.transformer.PublisherInfoTransformer;
import com.app.bookstore.transformer.PublisherTransformer;
import com.app.bookstore.transformer.UserTransformer;

/**
 * @author Ananth Shanmugam
 */
@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private UserService userService;

//	private final static Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Override
	public CustomerDTO saveCustomerWithUser(UserDTO userDTO) {  /* Save the customer and user to db */

		// create new customer
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setUserDTO(userDTO);
		customerDTO.getUserDTO().setRole(Role.CUSTOMER);
		Customer customer = CustomerTransformer.transform(customerDTO);
		Customer savedCustomer = customerRepository.save(customer);

		return CustomerTransformer.transform(savedCustomer);
	}

	@Override
	public CustomerDTO saveCustomer(CustomerDTO customerDTO) {  /* Save the customer to db */

		Customer customer = customerRepository.save(CustomerTransformer.transform(customerDTO));
		return CustomerTransformer.transform(customer);
	}

	@Override
	public CustomerDTO updateCustomer(CustomerDTO customerDTO) { /* Update the customer in db */
		Customer persistedCustomer = getCustomerById(customerDTO.getId());
		persistedCustomer.setUser(UserTransformer.transform(customerDTO.getUserDTO()));
		System.out.println(persistedCustomer);
		Customer savedCustomer = customerRepository.save(persistedCustomer);
		return CustomerTransformer.transform(savedCustomer);
	}

	@Override
	public Customer getCustomerById(Long id) {  /* Get the customer by id from db */
		return customerRepository.findById(id).get();
	}

	@Override
	public void subscribePublisher(CustomerDTO customer, PublisherDto publisherDto) {
		customer.subscribePublisher(publisherDto);   /* Update customer to subscribe from this publisher */
		publisherDto.addCustomer(customer);
		customerRepository.save(CustomerTransformer.transform(customer));
	}

	@Override
	public void unsubscribePublisher(CustomerDTO customer, PublisherDto publisherDto) {
		customer.unsubscribePublisher(publisherDto);/* Update customer to unsubscribe from this publisher */
		publisherDto.removeCustomer(customer);
		customerRepository.save(CustomerTransformer.transform(customer));
	}

	@Override
	public List<OrderDTO> getOrdersByCustomerId(Long customerId) {  /* Get the customer order history from db */
		final List<Order> results = customerRepository.findById(customerId).get().getOrders();
		List<OrderDTO> resultList = new ArrayList<OrderDTO>();
		if (results != null) {
			results.stream().forEach(orderObj -> resultList.add(OrderTransformer.transform(orderObj)));
		}
		return resultList;
	}

	@Override
	public List<PublisherInfo> getSubscribers(Long customerId) {
		final List<Publisher> results = customerRepository.findById(customerId).get().getPublishers();
		List<PublisherInfo> resultList = new ArrayList<PublisherInfo>();
		if (results != null) {
			results.stream().forEach(publisherObj -> resultList.add(PublisherInfoTransformer.transform(publisherObj)));
		}
		return resultList;
	}

	@Override
	public CustomerDTO getCustomerByUser() {
		// TODO Auto-generated method stub
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			UserDTO userDTO = userService.findByEmail(auth.getName());
			final User user = UserTransformer.transform(userDTO);
			if (user != null) {
				Customer customer = customerRepository.findCustomerByUser(user);
				return CustomerTransformer.transform(customer);
			}
		}
		return null;

	}

	@Override
	public int getCustomerActiveSubscriptions(PublisherDto publisherDto) {

		PublisherInfo publisherInfo = PublisherInfoTransformer.transform(PublisherTransformer.transform(publisherDto));
		CustomerDTO customerDTO = getCustomerByUser();
		if (customerDTO != null) {
			if (getSubscribers(customerDTO.getId()).contains(publisherInfo)) {
				return 1;
			} else {
				return 2;

			}
		} else {
			return 0;
		}
	}
}
