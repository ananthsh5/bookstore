package com.app.bookstore.repository;

import com.app.bookstore.domain.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Ananth Shanmugam
 */
@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {
}
