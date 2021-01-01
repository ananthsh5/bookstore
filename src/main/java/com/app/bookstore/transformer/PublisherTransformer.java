package com.app.bookstore.transformer;

import java.util.List;
import java.util.Optional;

import com.app.bookstore.domain.Book;
import com.app.bookstore.domain.Customer;
import com.app.bookstore.domain.Publisher;
import com.app.bookstore.domain.User;
import com.app.bookstore.domain.dto.BookDTO;
import com.app.bookstore.domain.dto.CustomerDTO;
import com.app.bookstore.domain.dto.PublisherDto;
import com.app.bookstore.domain.dto.UserDTO;
import com.google.common.base.Function;

/**
 * @author Ananth Shanmugam
 * Converter Class to 
 * 1) convert Publisher dto to Publisher entity, 
 * 2) convert Publisher entity to Publisher dto,
 */
public class PublisherTransformer extends AbstractTransformer implements Function<Publisher, PublisherDto> {

	@Override
	public PublisherDto apply(Publisher publisher) {
		return transform(publisher);
	}

	public static PublisherDto transform(Publisher publisher) {

		Optional<PublisherDto> optionalPublisherDto = convertDbPublisherToPublisherDTO(publisher);

		if (optionalPublisherDto.isPresent()) {
			PublisherDto publisherDto = optionalPublisherDto.get();

			Optional<UserDTO> optionalUserDTO = convertDbUserToUserDTO(publisher.getUser());
			optionalUserDTO.ifPresent(optionalUserDTOObj -> publisherDto.setUserDTO(optionalUserDTOObj));

			Optional<List<BookDTO>> optionalBookDTOList = convertDbBookLstToBookDTOLst(publisher.getBooks());
			optionalBookDTOList.ifPresent(bookDTOListObj -> publisherDto.setBookDTOs(bookDTOListObj));

			Optional<List<CustomerDTO>> optionalCustomerList = convertDbCustomerLstToCustomerDTOLst(
					publisher.getCustomers());
			optionalCustomerList
					.ifPresent(optionalCustomerListObj -> publisherDto.setCustomers(optionalCustomerListObj));
			return publisherDto;
		}
		return null;
	}

	public static Publisher transform(final PublisherDto publisherDto) {

		Optional<Publisher> optionalPublisher = convertPublisherDTOToDbPublisher(publisherDto);
		if (optionalPublisher.isPresent()) {
			final Publisher publisher = optionalPublisher.get();

			UserDTO userDTO = publisherDto.getUserDTO();
			Optional<User> optionalUser = convertUserDTOToDbUser(userDTO);
			optionalUser.ifPresent(optionalUserObj -> publisher.setUser(optionalUserObj));

			Optional<List<Book>> optionalBookList = convertBookDTOLstToDbBookLst(publisherDto.getBookDTOs());
			optionalBookList.ifPresent(bookDTOListObj -> publisher.setBooks(bookDTOListObj));

			Optional<List<Customer>> optionalCustomerList = convertCustomerDTOLstToDbCustomerLst(
					publisherDto.getCustomers());
			optionalCustomerList.ifPresent(optionalCustomerListObj -> publisher.setCustomers(optionalCustomerListObj));

			return publisher;
		}
		return null;
	}

}
