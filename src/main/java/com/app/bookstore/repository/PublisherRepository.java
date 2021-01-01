package com.app.bookstore.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.bookstore.domain.Publisher;
import com.app.bookstore.domain.User;

/**
 * @author Ananth Shanmugam
 */
@Repository
public interface PublisherRepository extends CrudRepository<Publisher, Long> {
	Publisher findPublisherByUser(User user);
}
