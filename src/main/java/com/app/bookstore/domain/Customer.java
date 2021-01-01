package com.app.bookstore.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;

/**
 * @author Ananth Shanmugam
 * Entity class for customer
 */
@Data
@Entity
public class Customer {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.PERSIST)
    private List<Order> orders = new ArrayList<Order>();
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.PERSIST)
    private List<CartItem> cartItems = new ArrayList<CartItem>();
    
    @ManyToMany
    @JoinTable(name = "subscriber", joinColumns = {@JoinColumn(name = "customer_id")}, inverseJoinColumns = {@JoinColumn(name = "publisher_id")})
    private List<Publisher> publishers = new ArrayList<Publisher>();

    private Integer points = 0;
    
    public void subscribePublisher(Publisher publisher) {
        publishers.add(publisher);
    }

    public void unsubscribePublisher(Publisher publisher) {
        publishers.remove(publisher);
    }

    public void addCartItem(CartItem item) {
        cartItems.add(item);
    }

    public void removeCartItem(CartItem item) {
        cartItems.remove(item);
    }

    public void addOrder(Order order) {
        orders.add(order);
    }
}
