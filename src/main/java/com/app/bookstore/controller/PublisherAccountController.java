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
import com.app.bookstore.domain.Accstatus;
import com.app.bookstore.domain.Role;
import com.app.bookstore.domain.dto.PublisherDto;
import com.app.bookstore.domain.dto.UserDTO;
import com.app.bookstore.service.PublisherService;
import com.app.bookstore.service.UserService;

/**
 * @author Ananth Shanmugam
 */
@Controller
@RequestMapping("/account")
public class PublisherAccountController {

	private UserService userService;
	private PublisherService publisherService;

	private final String ACCOUNT_PUBLISHER_REGISTER_PAGE = "/account/publisherRegister";

	private final String REDIRECT_TO_ACCOUNT_REGISTER_SUCCESS_PAGE = "redirect:/account/register-success";


	@Autowired
	ChangePasswordPolicyValidator changePasswordPolicyValidator;

	@Autowired
	UserEmailValidator userEmailValidator;

	public PublisherAccountController(UserService userService, PublisherService publisherService) {
		this.userService = userService;
		this.publisherService = publisherService;
	}

	@GetMapping(value = { "/publisher-register" })  /* Get the publishers register page */
	public String getPublisherRegisterForm(@ModelAttribute("publisherDTO") PublisherDto publisher) {
		return ACCOUNT_PUBLISHER_REGISTER_PAGE;
	}

	@PostMapping(value = { "/publisher-register" }) /* Validate and then save the publisher detail to db */
	public String postPublisherRegister(@Valid PublisherDto publisherDto, BindingResult bindingresult) {

		userEmailValidator.validate(publisherDto.getUserDTO(), bindingresult);

		if (bindingresult.hasErrors()) {
			return ACCOUNT_PUBLISHER_REGISTER_PAGE;
		}

		UserDTO newUser = publisherDto.getUserDTO();
		newUser.setRole(Role.PUBLISHER);
		UserDTO saveUser = userService.addUser(newUser);
		publisherDto.setUserDTO(saveUser);
		publisherDto.setAccstatus(Accstatus.PENDING);
		publisherService.save(publisherDto);
		return REDIRECT_TO_ACCOUNT_REGISTER_SUCCESS_PAGE;
	}
}
