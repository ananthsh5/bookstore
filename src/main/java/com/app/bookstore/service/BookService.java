package com.app.bookstore.service;

import java.util.List;

import com.app.bookstore.domain.dto.BookDTO;
import com.app.bookstore.domain.dto.BookcategoryDTO;
import com.app.bookstore.domain.dto.PublisherDto;

/**
 * @author Ananth Shanmugam
 */
public interface BookService {
    List<BookDTO> getAll();

    BookDTO findById(Long id);

    BookDTO save(BookDTO book);

    void delete(BookDTO book);

    List<BookDTO> getBooksByBookcategory(BookcategoryDTO bookcategory);

    List<BookDTO> getBooksByPublisher(PublisherDto publisher);

    List<BookDTO> getBooksByName(String name);
}
