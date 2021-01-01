package com.app.bookstore.transformer;

import java.util.List;
import java.util.Optional;

import com.app.bookstore.domain.Message;
import com.app.bookstore.domain.Publisher;
import com.app.bookstore.domain.User;
import com.app.bookstore.domain.dto.UpdateProfileDto;
import com.app.bookstore.domain.dto.UserDTO;
import com.google.common.base.Function;

/**
 * @author Ananth Shanmugam
 * Converter Class to 
 * 1) convert UserProfile dto to User entity, 
 * 2) convert User entity to UserProfile dto,
 */
public class UserProfileTransformer extends AbstractTransformer implements Function<User, UpdateProfileDto> {

	// private static final Logger logger =
	// LoggerFactory.getLogger(UserTransformer.class.getName());

	@Override
	public UpdateProfileDto apply(User user) {
		return transform(user);
	}

	public static UpdateProfileDto transform(User user) {
		Optional<UserDTO> optionalUserDTO = convertDbUserToUserDTO(user);

		if (optionalUserDTO.isPresent()) {
			UserDTO userDTO = optionalUserDTO.get();
			 UpdateProfileDto updateProfileDto = new UpdateProfileDto();
             updateProfileDto.setFirstName(userDTO.getFirstName());
             updateProfileDto.setLastName(userDTO.getLastName());
             updateProfileDto.setEmail(userDTO.getEmail());
             updateProfileDto.setPhone(userDTO.getPhone());
             updateProfileDto.setAddress(userDTO.getAddress());

			return updateProfileDto;
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
