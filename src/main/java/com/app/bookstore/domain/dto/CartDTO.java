package com.app.bookstore.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Ananth Shanmugam
 */
@Data
@AllArgsConstructor
public class CartDTO {
    private Long id;
    
    private String bookName;
    
    private String picture;
    
    private int quantity;
    
    private BigDecimal bookPrice;
    

}
