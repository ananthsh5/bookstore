package com.app.bookstore.transformer;

import java.util.Optional;

import com.app.bookstore.domain.Message;
import com.app.bookstore.domain.User;
import com.app.bookstore.domain.dto.MessageDTO;
import com.app.bookstore.domain.dto.UserDTO;
import com.google.common.base.Function;

/**
 * @author Ananth Shanmugam
 * Converter Class to 
 * 1) convert Message dto to entity, 
 * 2) convert Message entity to dto,
 */
public class MessageTransformer extends AbstractTransformer implements Function<Message, MessageDTO> {

	@Override
	public MessageDTO apply(Message message) {
		return transform(message);
	}

	public static MessageDTO transform(Message message) {
		Optional<MessageDTO> optionalMessageDTO = convertDbMessageToMessageDTO(message);
		if (optionalMessageDTO.isPresent()) {
			MessageDTO messageDTO = optionalMessageDTO.get();
			Optional<UserDTO> optionalUserDTO = convertDbUserToUserDTO(message.getUser());
			optionalUserDTO.ifPresent(optionalUserDTOObj -> messageDTO.setUserDTO(optionalUserDTOObj));
			return messageDTO;
		}
		return null;
	}

	public static Message transform(final MessageDTO messageDTO) {
		Optional<Message> optionalMessage = convertMessageDTOToDbMessage(messageDTO);
		if (optionalMessage.isPresent()) {
			final Message message = optionalMessage.get();
			Optional<User> optionalUser = convertUserDTOToDbUser(messageDTO.getUserDTO());
			optionalUser.ifPresent(optionalUserObj -> message.setUser(optionalUserObj));
			return message;
		}
		return null;
	}

}
