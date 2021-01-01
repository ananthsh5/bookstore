package com.app.bookstore.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.bookstore.domain.Bookcategory;

/**
 * @author Ananth Shanmugam
 */
@Repository
public interface BookcategoryRepository extends CrudRepository<Bookcategory, Long> {

}
