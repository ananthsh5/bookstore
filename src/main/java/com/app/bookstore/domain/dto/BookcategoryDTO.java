package com.app.bookstore.domain.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * @author Ananth Shanmugam
 */
@Data
public class BookcategoryDTO {
	
    private Long id;
    
    private String name;

    List<BookDTO> books = new ArrayList<BookDTO>();

    public void addBook(BookDTO book) {
        books.add(book);
    }

    public void removeBook(BookDTO book) {
        books.remove(book);
    }

}
