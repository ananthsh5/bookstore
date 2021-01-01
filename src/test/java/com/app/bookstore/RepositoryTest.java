package com.app.bookstore;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.app.bookstore.domain.Accstatus;
import com.app.bookstore.domain.Book;
import com.app.bookstore.domain.Bookcategory;
import com.app.bookstore.domain.Publisher;
import com.app.bookstore.repository.BookRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RepositoryTest {

	
	@Autowired
    private TestEntityManager entityManager;

	@Autowired
	BookRepository bookRepository;
	
	@Test
	public void testSaveBook()
	{

		Publisher publisher = new Publisher();
		publisher.setName("Apress");
		publisher.setDescription("");
		publisher.setAccstatus(Accstatus.APPROVED);

		Bookcategory bookcategory = new Bookcategory();
		bookcategory.setId(Long.valueOf(2));
		bookcategory.setName("Spring");
		
		Book book = new Book();
		book.setName("Pro JPA 2");
		book.setDescription("Mastering Spring  persistence basics and core topics.");
		book.setPrice(new BigDecimal("30.00"));
		book.setAvailable(Double.valueOf(1));
		book.setPublisher(publisher);
		book.setBookcategory(bookcategory);

		LocalDateTime ldt = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
		Timestamp current = Timestamp.valueOf(ldt);
		book.setCreatedDate(current);

		Book savedBook = entityManager.persist(book);
		Optional<Book> bookFromDB = bookRepository.findById(savedBook.getId());
		
		if(bookFromDB.isPresent())
		{
			assertThat(savedBook).isEqualTo(bookFromDB.get());
		}else {
			System.out.println("Error, book does not exist in repo. ");
		}

	}
	
	@Test
	public void testUpdateBook()
	{

		Publisher publisher = new Publisher();
		publisher.setName("Apress");
		publisher.setDescription("");
		publisher.setAccstatus(Accstatus.APPROVED);
		publisher.setCustomers(null);

		Bookcategory bookcategory = new Bookcategory();
		bookcategory.setId(Long.valueOf(2));
		bookcategory.setName("Spring");
		
		Book book = new Book();
		book.setName("Pro JPA 2");
		book.setDescription("Mastering Spring  persistence basics and core topics.");
		book.setPrice(new BigDecimal("30.00"));
		book.setAvailable(Double.valueOf(1));
		book.setPublisher(publisher);
		book.setBookcategory(bookcategory);

		LocalDateTime ldt = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
		Timestamp current = Timestamp.valueOf(ldt);
		book.setCreatedDate(current);

		Book savedBook = entityManager.persist(book);
		Optional<Book> bookFromDB = bookRepository.findById(savedBook.getId());
		
		if(bookFromDB.isPresent())
		{
			assertThat(savedBook).isEqualTo(bookFromDB.get());
			savedBook.setDescription("New Description coming soon");

		}else {
			System.out.println("Error, book does not exist in repo. ");
		}
		
		//check whether update is working
		Optional<Book> bookFromDBAgain = bookRepository.findById(savedBook.getId());
		if(bookFromDBAgain.isPresent())
		{
				assertThat(bookFromDBAgain.get().getDescription()).isEqualTo("New Description coming soon");
		}else {
			System.out.println("Error, book does not exist in repo.(2nd call) ");
		}
		
		
}
}
