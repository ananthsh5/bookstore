package com.app.bookstore.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.bookstore.domain.Book;
import com.app.bookstore.domain.dto.BookDTO;
import com.app.bookstore.domain.dto.BookcategoryDTO;
import com.app.bookstore.domain.dto.PublisherDto;
import com.app.bookstore.repository.BookRepository;
import com.app.bookstore.service.BookService;
import com.app.bookstore.transformer.BookTransformer;
import com.app.bookstore.transformer.BookcategoryTransformer;
import com.app.bookstore.transformer.PublisherTransformer;

/**
 * @author Ananth Shanmugam
 */
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

//	private final static Logger LOGGER = LoggerFactory.getLogger(BookServiceImpl.class);

    @Override
    public List<BookDTO> getAll() { /* Get the all the books  */  
        final List<Book> results = (List<Book>) bookRepository.findAll();
        List<BookDTO> resultList = new ArrayList<BookDTO>();
        if ( results != null ) {
			results.stream().forEach(bookObj -> resultList.add(BookTransformer.transform(bookObj)));
        }
        return resultList;
    }

    @Override
    public BookDTO findById(Long id) { /* Get the book by book id */  
        return BookTransformer.transform(bookRepository.findById(id).get());
    }

    @Override
    public BookDTO save(BookDTO book) { /* Save the book to db */  
    	Book entitybook = BookTransformer.transform(book);
    	if(book.getId() ==null) {
            Book savedBook = bookRepository.save(entitybook);
            return BookTransformer.transform(savedBook);
    		
    	}else {
    		Book dbbook = 	bookRepository.findById(book.getId()).get();
    		dbbook.setAvailable(book.getAvailable());
    		dbbook.setPrice(book.getPrice());
    		dbbook.setDescription(book.getDescription());
    		dbbook.setName(book.getName());
    		dbbook.setBookcategory(BookcategoryTransformer.transform(book.getBookcategoryDTO()));
    		dbbook.setImage(book.getImage());
            Book savedBook = bookRepository.save(dbbook);
            return BookTransformer.transform(savedBook);

    	}
    }

    @Override
    public void delete(BookDTO book) {  /* Delete the book in db */ 
        bookRepository.delete(BookTransformer.transform(book));
    }

    @Override
    public List<BookDTO> getBooksByBookcategory(BookcategoryDTO bookcategory) { /* Get the books by book category from db */ 
        
        final List<Book> results = bookRepository.findBooksByBookcategory(BookcategoryTransformer.transform(bookcategory));
        List<BookDTO> resultList = new ArrayList<BookDTO>();
        if ( results != null ) {
			results.stream().forEach(bookObj -> resultList.add(BookTransformer.transform(bookObj)));
        }
        return resultList;
    }

    @Override
    public List<BookDTO> getBooksByPublisher(PublisherDto publisher) { /* Get the books by publisher from db */
        
        final List<Book> results = bookRepository.findBooksByPublisher(PublisherTransformer.transform(publisher));
        List<BookDTO> resultList = new ArrayList<BookDTO>();
        if ( results != null ) {
			results.stream().forEach(bookObj -> resultList.add(BookTransformer.transform(bookObj)));
        }
        return resultList;
    }

    @Override
    public List<BookDTO> getBooksByName(String name) {
       
        final List<Book> results = bookRepository.findBooksByName(name);
        List<BookDTO> resultList = new ArrayList<BookDTO>();
        if ( results != null ) {
			results.stream().forEach(bookObj -> resultList.add(BookTransformer.transform(bookObj)));

        }
        return resultList;
    }
}
