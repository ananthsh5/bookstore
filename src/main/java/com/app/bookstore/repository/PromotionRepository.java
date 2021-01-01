package com.app.bookstore.repository;

import com.app.bookstore.domain.Promotion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Ananth Shanmugam
 */
@Repository
public interface PromotionRepository extends CrudRepository<Promotion, Long> {
}
