package com.app.bookstore.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.app.bookstore.domain.dto.ChangePasswordDto;
import com.app.bookstore.domain.dto.UserDTO;
import com.app.bookstore.service.UserService;

/**
 * @author Ananth Shanmugam
 * Class to validate front end request fields for password change
 */
@Component
public class ChangePasswordPolicyValidator implements Validator {


	@Autowired
	private UserService userService;

	@Override
	public boolean supports(Class<?> clazz) {
		return ChangePasswordDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		ChangePasswordDto changePasswordDto = (ChangePasswordDto) target;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {

			UserDTO user = userService.findByEmail(authentication.getName());
			if (user != null) {

				if (changePasswordDto.getNewPassword() != "" || changePasswordDto.getCurrentPassword() != ""
						|| changePasswordDto.getConfirmNewPassword() != "") {
					if (changePasswordDto.getNewPassword() != "") {
						if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmNewPassword())) {
							errors.rejectValue("confirmNewPassword", "",
									"New Password and Confirm New Password fields miss matched.");
						}

						if (changePasswordDto.getCurrentPassword() == "") {
							errors.rejectValue("currentPassword", "", "Please provide your current password.");
						}
					}
					if (changePasswordDto.getCurrentPassword() != "") {
						// check the current password is correct.
						boolean valid = userService.validatePassword(changePasswordDto.getCurrentPassword(), user);

						if (!valid) {
							errors.rejectValue("currentPassword", "", "Current Password is incorrect.");
						}
					}

				}
			}
		}
	}
}
