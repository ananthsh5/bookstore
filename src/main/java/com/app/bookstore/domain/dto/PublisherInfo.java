package com.app.bookstore.domain.dto;

import lombok.Data;

/**
 * @author Ananth Shanmugam
 */
@Data
public class PublisherInfo {
    private Long id;
    
    private String name;
    
    private String description;
    
    private String phone;
    
    private String email;
    
    private String address;
}
