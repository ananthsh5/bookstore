package com.app.bookstore.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.bookstore.controller.validator.ChangePasswordPolicyValidator;
import com.app.bookstore.controller.validator.UserEmailValidator;
import com.app.bookstore.domain.Role;
import com.app.bookstore.domain.dto.CustomerDTO;
import com.app.bookstore.domain.dto.UserDTO;
import com.app.bookstore.service.CustomerService;
import com.app.bookstore.service.UserService;

/**
 * @author Ananth Shanmugam
 */
@Controller
@RequestMapping("/account")
public class CustomerAccountController {

//	private final static Logger LOGGER = LoggerFactory.getLogger(CustomerAccountController.class);

	private UserService userService;
	private CustomerService customerService;

	private final String ACCOUNT_CUSTOMER_REGISTER_PAGE = "/account/customerRegister";

	private final String REDIRECT_TO_ACCOUNT_REGISTER_SUCCESS_PAGE = "redirect:/account/register-success";
	
	@Autowired
	ChangePasswordPolicyValidator changePasswordPolicyValidator;

	@Autowired
	UserEmailValidator userEmailValidator;

	public CustomerAccountController(UserService userService, CustomerService customerService) {
		this.userService = userService;
		this.customerService = customerService;
	}

	@GetMapping(value = { "/customer-register" }) /* Get the customer register page */
	public String getCustomerRegisterForm(@ModelAttribute("customerDTO") CustomerDTO customerDTO) {
		return ACCOUNT_CUSTOMER_REGISTER_PAGE;
	}



	@PostMapping(value = { "/customer-register" }) /* Validate and then save the new customer details */
	public String postCustomerRegister(@Valid CustomerDTO customerDTO, BindingResult bindingResult) {

		userEmailValidator.validate(customerDTO.getUserDTO(), bindingResult);

		if (bindingResult.hasErrors()) {
			return ACCOUNT_CUSTOMER_REGISTER_PAGE;
		}
		UserDTO newUser = customerDTO.getUserDTO();

		newUser.setRole(Role.CUSTOMER);

		UserDTO userDTO = userService.addUser(newUser);
		customerService.saveCustomerWithUser(userDTO);

		return REDIRECT_TO_ACCOUNT_REGISTER_SUCCESS_PAGE;
	}


}
