package com.app.bookstore.repository;

import com.app.bookstore.domain.OrderItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Ananth Shanmugam
 */
@Repository
public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {
    @Query("select i from OrderItem i " +
            "inner join Book p on i.book.id = p.id " +
            "inner join Publisher s on p.publisher.id = s.id " +
            "inner join Order o on i.order.id = o.id " +
            "where s.id = :publisherId")
    List<OrderItem> getOrderItemsByPublisher(Long publisherId);

    @Query(value = "select * from order_item i where i.order_status = 'DELIVERED' and i.order_id = ?", nativeQuery = true)
    List<OrderItem> getDeliveredOrderItemsByOrder(Long orderId);
}
