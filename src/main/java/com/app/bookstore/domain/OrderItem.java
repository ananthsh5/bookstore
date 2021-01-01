package com.app.bookstore.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Ananth Shanmugam
 * Entity class for OrderItem
 */
@Data
@Entity
public class OrderItem {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "book_id")
    private Book book;
    
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "order_id")
    
    private Order order;
    
    private int rating = 0;
    
    @Enumerated(EnumType.STRING)
    private OrderItemStatus orderStatus = OrderItemStatus.ORDERED;
    
    private LocalDateTime shippingDate;
    
    private LocalDateTime deliveredDate;
}
