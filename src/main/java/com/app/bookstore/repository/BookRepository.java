package com.app.bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.bookstore.domain.Book;
import com.app.bookstore.domain.Bookcategory;
import com.app.bookstore.domain.Publisher;

/**
 * @author Ananth Shanmugam
 */
@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
    List<Book> findBooksByBookcategory(Bookcategory bookcategory);

    List<Book> findBooksByPublisher(Publisher publisher);

    @Query("SELECT p FROM Book p WHERE p.name LIKE ?1 ")
    List<Book> findBooksByName(String name);
}
