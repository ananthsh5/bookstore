package com.app.bookstore.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.bookstore.controller.validator.ChangePasswordPolicyValidator;
import com.app.bookstore.controller.validator.UserEmailValidator;
import com.app.bookstore.domain.dto.ChangePasswordDto;
import com.app.bookstore.domain.dto.MessageDTO;
import com.app.bookstore.domain.dto.UpdateProfileDto;
import com.app.bookstore.domain.dto.UserDTO;
import com.app.bookstore.service.MessageService;
import com.app.bookstore.service.UserService;

/**
 * @author Ananth Shanmugam
 */
@Controller
@RequestMapping("/account")
public class AccountController {

	private UserService userService;

	private final String ACCOUNT_MESSAGES_PAGE = "/account/messages";

	private final String ACCOUNT_PROFILE_PAGE = "/account/profile";

	private final String REDIRECT_TO_ACCOUNT_PROFILE_PAGE = "redirect:/account/profile";

	private final String ACCOUNT_LOGIN_PAGE = "/account/login";

	private final String ACCOUNT_REGISTER_PAGE = "/account/register";

	private final String ACCOUNT_REGISTER_SUCCESS_PAGE = "/account/register-success";

	private final String REDIRECT_TO_ACCOUNT_MESSAGE_PAGE = "redirect:/account/message";

	private final String REDIRECT_TO_ACCOUNT_LOGIN_PAGE = "redirect:/account/login";

	private final String REDIRECT_TO_HOME_PAGE = "redirect:/";

	private final String PASSWORD_UPDATED = "Profile Updated";
	
	private final String ANONYMOUS_USER ="anonymousUser";

	private final static Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private MessageService messageService;

	@Autowired
	ChangePasswordPolicyValidator changePasswordPolicyValidator;

	@Autowired
	UserEmailValidator userEmailValidator;

	public AccountController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping(value = { "/message" })
	public String getMessageListForm(Model model) { /* get the current publisher messages */
		
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }
        else {
			UserDTO user = userService.findByEmail(authentication.getName());
			if (user != null) {
				List<MessageDTO> messages = user.getMessages();
				model.addAttribute("messages", messages);
			}
		}
		return ACCOUNT_MESSAGES_PAGE;
	}

	@GetMapping(value = { "/profile" })
	public String getProfileForm(Model model) {  /* get the current user profile */
		
		LOGGER.info("inside getProfileForm method");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }
		LOGGER.info("inside getProfileForm method 2");

		if (!model.containsAttribute("profile")) {
				UpdateProfileDto profileDto = userService.getLoggedInUserProfile();
                model.addAttribute("profile", profileDto);
		}

		if (!model.containsAttribute("changePasswordDto")) {
			model.addAttribute("changePasswordDto", new ChangePasswordDto());
		}

		return ACCOUNT_PROFILE_PAGE;
	}

	@GetMapping(value = { "/login" })
	public String getLoginPage() { 		/* get the login page */
		return ACCOUNT_LOGIN_PAGE;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return REDIRECT_TO_HOME_PAGE; 
	}

	@GetMapping(value = { "/register" })
	public String getRegisterPage() {
		return ACCOUNT_REGISTER_PAGE;
	}

	@GetMapping(value = { "/register-success" })
	public String getSuccessPage() {
		return ACCOUNT_REGISTER_SUCCESS_PAGE;
	}

	@PostMapping(value = { "/profile/info" })
	public String updateProfile(@Valid @ModelAttribute("profile") UpdateProfileDto profile, BindingResult bindingResult,
			RedirectAttributes rd, HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }
		UserDTO currentUserDTO = null;

		if (authentication != null) {
			currentUserDTO = userService.findByEmail(authentication.getName());
			if (currentUserDTO != null && !currentUserDTO.getEmail().equals(profile.getEmail())) {
				currentUserDTO.setEmail(profile.getEmail());
				userEmailValidator.validate(currentUserDTO, bindingResult); /* validate if email is different */
			}
		}
		
        currentUserDTO.setFirstName(profile.getFirstName());
        currentUserDTO.setLastName(profile.getLastName());
        currentUserDTO.setEmail(profile.getEmail());
        currentUserDTO.setPhone(profile.getPhone());
        currentUserDTO.setAddress(profile.getAddress());
        //LOGGER.info("currentUserDTO id is" + currentUserDTO.getId());
        
		if (bindingResult.hasErrors()) { 		/* if got errors, add the errors and redirect */
			rd.addFlashAttribute("org.springframework.validation.BindingResult.profile", bindingResult);
			rd.addFlashAttribute("profile", profile);
			return REDIRECT_TO_ACCOUNT_PROFILE_PAGE;
		}

		userService.updateUser(currentUserDTO);

		rd.addFlashAttribute("updateProfileSuccess", PASSWORD_UPDATED);

		return REDIRECT_TO_ACCOUNT_PROFILE_PAGE;
	}

	@PostMapping(value = { "/profile/security" })
	public String changePassword(@Valid @ModelAttribute("changePasswordDto") ChangePasswordDto changePasswordDto,
			BindingResult bindingResult, RedirectAttributes rd) { /* Update user password  */

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }

		changePasswordPolicyValidator.validate(changePasswordDto, bindingResult);

		if (bindingResult.hasErrors()) {
			rd.addFlashAttribute("org.springframework.validation.BindingResult.changePasswordDto", bindingResult);
			rd.addFlashAttribute("changePasswordDto", changePasswordDto);
			return REDIRECT_TO_ACCOUNT_PROFILE_PAGE;
		}
		if (authentication != null) {

			UserDTO user = userService.findByEmail(authentication.getName());
			if (user != null) {
				LOGGER.info("userdto getpassword is "+user.getPassword());

				userService.changePassword(changePasswordDto.getNewPassword(), user);
			}
		}
		rd.addFlashAttribute("changePasswordSuccess", PASSWORD_UPDATED);
		return REDIRECT_TO_ACCOUNT_PROFILE_PAGE;
	}

	@DeleteMapping(value = {
			"/messages/read/{id}" }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody boolean setMessageRead(@PathVariable(value = "id") Long id) {
		messageService.setMessageRead(id);  /* Set user message as read, this is for jquery call  */
		return true;
	}

	@GetMapping(value = { "/message/read/{id}" })
	public String readMessage(@PathVariable(value = "id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }

		MessageDTO message = messageService.getMessageById(id);
		message.setRead(true); /* Set user message as read  */
		messageService.saveMessage(message);
		return REDIRECT_TO_ACCOUNT_MESSAGE_PAGE;
	}

	@GetMapping(value = { "/message/delete/{id}" })
	public String deleteMessage(@PathVariable(value = "id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }

		MessageDTO message = messageService.getMessageById(id);  /* Set user message as read  */
		messageService.delete(message);
		return REDIRECT_TO_ACCOUNT_MESSAGE_PAGE;
	}

}
