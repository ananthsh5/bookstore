package com.app.bookstore.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ananth Shanmugam
 * Entity class for book category
 */
@Data
@Entity
public class Bookcategory {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;

    @OneToMany(mappedBy = "bookcategory", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    List<Book> books = new ArrayList<Book>();

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }
}
