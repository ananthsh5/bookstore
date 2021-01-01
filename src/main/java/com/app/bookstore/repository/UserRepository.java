package com.app.bookstore.repository;

import com.app.bookstore.domain.Message;
import com.app.bookstore.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Ananth Shanmugam
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    // find user by email
    User findByEmail(String email);

    // get last 5 unread notify message of user by email.
    @Query(value = "select m from Message m join User u on m.user = u.id where m.read = 0 and  u.email = ?1 and rownum < 3 order by m.receivedDate desc")
    List<Message> getLast3UnreadNotifyMessageByUserEmail(String email);

}
