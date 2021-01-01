package com.app.bookstore.transformer;

import java.util.List;
import java.util.Optional;

import com.app.bookstore.domain.Book;
import com.app.bookstore.domain.Bookcategory;
import com.app.bookstore.domain.dto.BookDTO;
import com.app.bookstore.domain.dto.BookcategoryDTO;
import com.google.common.base.Function;

/**
 * @author Ananth Shanmugam
 * Converter Class to 
 * 1) convert book category dto to book category entity, 
 * 2) convert book category entity to book category dto,
 */
public class BookcategoryTransformer extends AbstractTransformer implements Function<Bookcategory, BookcategoryDTO> {

	@Override
	public BookcategoryDTO apply(Bookcategory bookCategory) {
		return transform(bookCategory);
	}

	public static BookcategoryDTO transform(Bookcategory bookcategory) {
		BookcategoryDTO bookcategoryDTO = null;
		Optional<BookcategoryDTO> optionalBookcategoryDTO = convertDbBookcategoryToBookcategoryDTO(bookcategory);
		Optional<List<BookDTO>> boList = convertDbBookLstToBookDTOLst(bookcategory.getBooks());
		if (optionalBookcategoryDTO.isPresent()) {
			bookcategoryDTO = optionalBookcategoryDTO.get();

			if (boList.isPresent()) {
				bookcategoryDTO.setBooks(boList.get());
			}
		}
		return bookcategoryDTO;
	}

	public static Bookcategory transform(final BookcategoryDTO bo) {
		Bookcategory bookcategory = null;
		Optional<Bookcategory> optionalBookcategory = convertBookcategoryDTOToDbBookcategory(bo);
		Optional<List<Book>> bookList = convertBookDTOLstToDbBookLst(bo.getBooks());
		if(optionalBookcategory.isPresent()) {
			bookcategory = optionalBookcategory.get();
			if (bookList.isPresent()) {
				bookcategory.setBooks(bookList.get());
			}
		}
		return bookcategory;
	}

}
