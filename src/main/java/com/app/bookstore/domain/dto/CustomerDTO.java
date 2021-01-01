package com.app.bookstore.domain.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * @author Ananth Shanmugam
 */
@Data
public class CustomerDTO {

    private Long id;
    
    private Integer points = 0;

    private UserDTO userDTO;
    
    private List<OrderDTO> orderDTOs = new ArrayList<OrderDTO>();
    
    private List<CartItemDTO> cartItems = new ArrayList<CartItemDTO>();
    
    @JsonIgnore
    private List<PublisherDto> publisherDtos = new ArrayList<PublisherDto>();

    public void subscribePublisher(PublisherDto publisherDto) {
        publisherDtos.add(publisherDto);
    }

    public void unsubscribePublisher(PublisherDto publisherDto) {
        publisherDtos.remove(publisherDto);
    }

    public void addCartItem(CartItemDTO item) {
        cartItems.add(item);
    }

    public void removeCartItem(CartItemDTO item) {
        cartItems.remove(item);
    }

    public void addOrder(OrderDTO order) {
        orderDTOs.add(order);
    }

}
