package com.app.bookstore.transformer;

import java.util.Optional;

import com.app.bookstore.domain.Publisher;
import com.app.bookstore.domain.dto.PublisherDto;
import com.app.bookstore.domain.dto.PublisherInfo;
import com.app.bookstore.domain.dto.UserDTO;
import com.google.common.base.Function;

/**
 * @author Ananth Shanmugam
 * Converter Class to 
 * 1) convert Publisher entity to PublisherInfo dto,
 */
public class PublisherInfoTransformer extends AbstractTransformer implements Function<Publisher, PublisherInfo> {

	@Override
	public PublisherInfo apply(Publisher publisher) {
		return transform(publisher);
	}

	public static PublisherInfo transform(Publisher publisher) {

		Optional<PublisherDto> optionalPublisherDto = convertDbPublisherToPublisherDTO(publisher);

		if (optionalPublisherDto.isPresent()) {
			PublisherDto publisherDto = optionalPublisherDto.get();
			
			Optional<UserDTO> optionalUserDTO = convertDbUserToUserDTO(publisher.getUser());
			optionalUserDTO.ifPresent(optionalUserDTOObj -> publisherDto.setUserDTO(optionalUserDTOObj));
			PublisherInfo publisherInfo = new PublisherInfo();
            publisherInfo.setId( publisherDto.getId());
            publisherInfo.setName(publisherDto.getName());
            publisherInfo.setDescription(publisherDto.getDescription());
            publisherInfo.setPhone(publisherDto.getUserDTO().getPhone());
            publisherInfo.setEmail(publisherDto.getUserDTO().getEmail());
            publisherInfo.setAddress(publisherDto.getUserDTO().getAddress());

			return publisherInfo;
		}
		return null;
	}


}
