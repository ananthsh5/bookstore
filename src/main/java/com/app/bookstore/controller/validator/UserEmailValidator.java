package com.app.bookstore.controller.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.app.bookstore.domain.dto.UserDTO;
import com.app.bookstore.service.UserService;

/**
 * @author Ananth Shanmugam
 * Class to validate user entered email address with db
 */
@Component
public class UserEmailValidator implements Validator {

    
    @Autowired
    private UserService userService;
    
    private final static Logger LOGGER = LoggerFactory.getLogger(UserEmailValidator.class);


    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
    	UserDTO userDto = (UserDTO) target;

		UserDTO existsUser = userService.findByEmail(userDto.getEmail());
		if (existsUser != null) {
			LOGGER.info("existsUser is not null "+ userDto.getEmail());
			// reject with error when email is not unique
			errors.rejectValue("email", "email.exists",
					"There is already a user registered with the email provided.");
		}

    }
}
