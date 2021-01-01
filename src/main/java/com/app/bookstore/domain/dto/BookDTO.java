package com.app.bookstore.domain.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * @author Ananth Shanmugam
 */
@Data
public class BookDTO {
	
	private Long id;
    
	@NotBlank
    private String name;
    
    private String description;

    @Transient
    @JsonIgnore
    private MultipartFile upload;
    private Double available;

    private BigDecimal price;

    private String image;

    @JsonIgnore
    private PublisherDto publisherDto;

    private BookcategoryDTO bookcategoryDTO;
    
    private Timestamp createdDate;

    private String createdBy;


}
