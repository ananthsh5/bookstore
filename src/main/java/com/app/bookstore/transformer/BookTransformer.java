package com.app.bookstore.transformer;

import java.util.Optional;

import com.app.bookstore.domain.Book;
import com.app.bookstore.domain.Bookcategory;
import com.app.bookstore.domain.Publisher;
import com.app.bookstore.domain.dto.BookDTO;
import com.app.bookstore.domain.dto.BookcategoryDTO;
import com.app.bookstore.domain.dto.PublisherDto;
import com.google.common.base.Function;

/**
 * @author Ananth Shanmugam
 * Converter Class to 
 * 1) convert book dto to book entity, 
 * 2) convert book entity to book dto,
 */
public class BookTransformer extends AbstractTransformer implements Function<Book, BookDTO> {

	// private final static Logger LOGGER =
	// LoggerFactory.getLogger(BookTransformer.class);

	@Override
	public BookDTO apply(Book book) {
		return transform(book);
	}

	public static BookDTO transform(Book book) {
		Optional<BookDTO> optionalBookDTO = convertDbBookToBookDTO(book);
		if (optionalBookDTO.isPresent()) {
			BookDTO bookDTO = optionalBookDTO.get();

			Optional<BookcategoryDTO> optionalBookcategoryDTO = convertDbBookcategoryToBookcategoryDTO(
					book.getBookcategory());
			optionalBookcategoryDTO.ifPresent(optionalBookcategoryDTOObj -> bookDTO.setBookcategoryDTO(optionalBookcategoryDTOObj));

			Optional<PublisherDto> optionalPublisherDto = convertDbPublisherToPublisherDTO(book.getPublisher());
			optionalPublisherDto.ifPresent(optionalPublisherDtoObj -> bookDTO.setPublisherDto(optionalPublisherDtoObj));
//			LOGGER.info("publisher from bookentity id is "+publisher.getId());

			return bookDTO;
		}
		return null;
	}

	public static Book transform(final BookDTO bookDTO) {
		
		Optional<Book> optionalBook = convertBookDTOToDbBook(bookDTO);
		if (optionalBook.isPresent()) {
			Book book = optionalBook.get();

			Optional<Bookcategory> optionalBookcategory = convertBookcategoryDTOToDbBookcategory(bookDTO.getBookcategoryDTO());
			optionalBookcategory.ifPresent(optionalBookcategoryObj -> book.setBookcategory(optionalBookcategoryObj));

			Optional<Publisher> optionalPublisher = convertPublisherDTOToDbPublisher(bookDTO.getPublisherDto());
			optionalPublisher.ifPresent(optionalPublisherObj -> book.setPublisher(optionalPublisherObj));
//			LOGGER.info("publisher from bookentity id is "+publisher.getId());

			return book;
		}
		return null;
	}

}
