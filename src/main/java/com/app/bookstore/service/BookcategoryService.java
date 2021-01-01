package com.app.bookstore.service;

import java.util.List;

import com.app.bookstore.domain.dto.BookcategoryDTO;

/**
 * @author Ananth Shanmugam
 */	
public interface BookcategoryService {
    List<BookcategoryDTO> getBookcategories();
    BookcategoryDTO getBookcategoryById(Long id);
}
