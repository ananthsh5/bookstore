package com.app.bookstore.transformer;

import java.util.List;
import java.util.Optional;

import com.app.bookstore.domain.Message;
import com.app.bookstore.domain.Publisher;
import com.app.bookstore.domain.User;
import com.app.bookstore.domain.dto.CustomerDTO;
import com.app.bookstore.domain.dto.MessageDTO;
import com.app.bookstore.domain.dto.PublisherDto;
import com.app.bookstore.domain.dto.UserDTO;
import com.google.common.base.Function;

/**
 * @author Ananth Shanmugam
 * Converter Class to 
 * 1) convert User dto to User entity, 
 * 2) convert User entity to User dto,
 */
public class UserTransformer extends AbstractTransformer implements Function<User, UserDTO> {

	// private static final Logger logger =
	// LoggerFactory.getLogger(UserTransformer.class.getName());

	@Override
	public UserDTO apply(User user) {
		return transform(user);
	}

	public static UserDTO transform(User user) {
		Optional<UserDTO> optionalUserDTO = convertDbUserToUserDTO(user);

		if (optionalUserDTO.isPresent()) {
			UserDTO userDTO = optionalUserDTO.get();

			Optional<PublisherDto> optionalPublisherDto = convertDbPublisherToPublisherDTO(user.getPublisher());
			optionalPublisherDto.ifPresent(optionalPublisherDtoObj -> userDTO.setPublisherDto(optionalPublisherDtoObj));

			if(user.getPublisher()!=null) {
			Optional<List<CustomerDTO>> optionalCustomerList = convertDbCustomerLstToCustomerDTOLst(
					user.getPublisher().getCustomers());
			optionalCustomerList.ifPresent(optionalCustomerListObj -> userDTO.getPublisherDto().setCustomers(optionalCustomerListObj));
			}
			Optional<List<MessageDTO>> optionalMessageDTOList = convertDbMessageLstToMessageDTOLst(user.getMessages());
			optionalMessageDTOList.ifPresent(optionalMessageDTOListObj -> userDTO.setMessages(optionalMessageDTOListObj));
			
			return userDTO;
		}
		return null;
	}

	public static User transform(final UserDTO userDTO) {
		Optional<User> optionalUser = convertUserDTOToDbUser(userDTO);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			Optional<List<Message>> optionalMessageList = convertMessageDTOLstToDbMessageLst(userDTO.getMessages());
			optionalMessageList.ifPresent(optionalMessageListObj -> user.setMessages(optionalMessageListObj));

			Optional<Publisher> optionalPublisher = convertPublisherDTOToDbPublisher(userDTO.getPublisherDto());
			optionalPublisher.ifPresent(optionalPublisherObj -> user.setPublisher(optionalPublisherObj));

			return user;
		}
		return null;
	}

}
