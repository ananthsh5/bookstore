package com.app.bookstore.domain.dto;

import java.time.LocalDateTime;

import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * @author Ananth Shanmugam
 */
@Data
public class PromotionDTO {

    private Long id;
    
    private String title;
    
    private String description;

    private String image;

    @Transient
    @JsonIgnore
    private MultipartFile imageUpload;
    
    private String url;
    
    private Integer count;
        
    private LocalDateTime dateCreated;
    
    private LocalDateTime dateExpired;

}
