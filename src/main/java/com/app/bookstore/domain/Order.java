package com.app.bookstore.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ananth Shanmugam
 * Entity class for Order
 */
@Data
@Entity
@Table(name = "orders")
public class Order {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();
    
    private BigDecimal totalAmount = new BigDecimal(0.00);
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    private String shippingAddress;
    
    private String billingAddress;
    
    private String paymentMethod;
    
    private String paymentInfo;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.NEW;
    
    private LocalDateTime orderedDate;
    
    private LocalDateTime endDate;
    
    private Boolean usingPoints = false;

    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
    }
}
