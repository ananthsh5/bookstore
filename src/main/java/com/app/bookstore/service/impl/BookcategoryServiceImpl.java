package com.app.bookstore.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.bookstore.domain.Bookcategory;
import com.app.bookstore.domain.dto.BookcategoryDTO;
import com.app.bookstore.repository.BookcategoryRepository;
import com.app.bookstore.service.BookcategoryService;
import com.app.bookstore.transformer.BookcategoryTransformer;

/**
 * @author Ananth Shanmugam
 */
@Service
public class BookcategoryServiceImpl implements BookcategoryService {
    @Autowired
    private BookcategoryRepository bookcategoryRepository;

    @Override
    public List<BookcategoryDTO> getBookcategories() {    /* Get the all the book categories  */      
        final List<Bookcategory> results = (List<Bookcategory>) bookcategoryRepository.findAll();
        List<BookcategoryDTO> resultList = new ArrayList<BookcategoryDTO>();
        if ( results != null ) {
   			results.stream().forEach(bookcategoryObj -> resultList.add(BookcategoryTransformer.transform(bookcategoryObj)));
        }
        return resultList;
    }

    @Override
    public BookcategoryDTO getBookcategoryById(Long id) {  /* Get the book category by id */
        return BookcategoryTransformer.transform(bookcategoryRepository.findById(id).get());
    }
}
