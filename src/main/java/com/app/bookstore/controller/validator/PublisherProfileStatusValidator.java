package com.app.bookstore.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.app.bookstore.domain.dto.PublisherDto;
import com.app.bookstore.domain.dto.UserDTO;
import com.app.bookstore.service.UserService;

/**
 * @author Ananth Shanmugam
 * Class to validate email for publisher profile update
 */
@Component
public class PublisherProfileStatusValidator implements Validator {

	
    
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return PublisherDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
		PublisherDto publisherDto = (PublisherDto) target;

		UserDTO existsUser = userService.findByEmail(publisherDto.getUserDTO().getEmail());
		if (existsUser != null) {
			// reject with error when email is not unique
			errors.rejectValue("userDTO.email", "email.exists",
					"There is already a user registered with the email provided.");
		}

    }
}
