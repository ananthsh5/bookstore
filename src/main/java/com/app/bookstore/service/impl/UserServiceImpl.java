package com.app.bookstore.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.bookstore.domain.Message;
import com.app.bookstore.domain.User;
import com.app.bookstore.domain.dto.CustomerDTO;
import com.app.bookstore.domain.dto.MessageDTO;
import com.app.bookstore.domain.dto.UpdateProfileDto;
import com.app.bookstore.domain.dto.UserDTO;
import com.app.bookstore.repository.UserRepository;
import com.app.bookstore.service.CustomerService;
import com.app.bookstore.service.UserService;
import com.app.bookstore.transformer.AbstractTransformer;
import com.app.bookstore.transformer.UserProfileTransformer;
import com.app.bookstore.transformer.UserTransformer;

/**
 * @author Ananth Shanmugam
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	CustomerService customerService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;


	// private final static Logger LOGGER =
	// LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public Boolean validatePassword(String password, UserDTO user) {  /* Validate the entered password */ 
		return bCryptPasswordEncoder.matches(password, user.getPassword());
	}

	@Override
	public UserDTO addUser(UserDTO userDTO) {  /* Save the new user to db */ 
		String hashPassword = bCryptPasswordEncoder.encode(userDTO.getPassword());
		userDTO.setPassword(hashPassword);
		userDTO.setConfirmPassword(hashPassword);
		userDTO.setRegisterDate(LocalDate.now());

		final User user = UserTransformer.transform(userDTO);
		user.setPublisher(null);
		userRepository.save(user);
		return UserTransformer.transform(user);
	}

	@Override
	public UserDTO updateUser(UserDTO userDTO) {  /* Update existing user to db */ 
		// does this user change the password.
		User existsUser = userRepository.findById(userDTO.getId()).get();

		existsUser.setAddress(userDTO.getAddress());
		existsUser.setFirstName(userDTO.getFirstName());
		existsUser.setLastName(userDTO.getLastName());
		existsUser.setPhone(userDTO.getPhone());

		existsUser = userRepository.save(existsUser);
		return UserTransformer.transform(existsUser);
	}

	@Override
	public UserDTO changePassword(String newRawPassword, UserDTO userDTO) { /* Change existing user password in db */ 
		User existsUser = userRepository.findById(userDTO.getId()).get();

		boolean isMatches = bCryptPasswordEncoder.matches(newRawPassword, userDTO.getPassword());
		if (!isMatches) {
			String hashPassword = bCryptPasswordEncoder.encode(userDTO.getPassword());
			existsUser.setPassword(hashPassword);
		}
		existsUser = userRepository.save(existsUser);
		return UserTransformer.transform(existsUser);
	}

	@Override
	public UserDTO findByEmail(String email) {  /* Find existing user email from db */ 
		final User user = userRepository.findByEmail(email);
		if (user != null) {
			UserDTO userDTO = UserTransformer.transform(user);
			return userDTO;
		}
		return null;

	}

	@Override
	public UpdateProfileDto getLoggedInUserProfile() { /* Return existing user in profile dto */ 
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final User user = userRepository.findByEmail(auth.getName());
		if (user != null) {
			UpdateProfileDto updateProfileDto = UserProfileTransformer.transform(user);
			CustomerDTO customerDTO = customerService.getCustomerByUser();
			if(customerDTO!=null) { //for publisher profile,customerdto will be null
				updateProfileDto.setPoints(customerDTO.getPoints()); // get the customer points
			}
			return updateProfileDto;
		}
		return null;

	}
	
	@Override
	public Optional<List<MessageDTO>> getLast3UnreadNotifyMessageByUserEmail(String email) {

		final List<Message> results = userRepository.getLast3UnreadNotifyMessageByUserEmail(email);
		return AbstractTransformer.convertDbMessageLstToMessageDTOLst(results);
	}


}
