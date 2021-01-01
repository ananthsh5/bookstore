package com.app.bookstore.domain.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import com.app.bookstore.domain.Accstatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ananth Shanmugam
 */
@Data
@NoArgsConstructor
public class PublisherDto {
	
    private Long id;
    
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private Accstatus accstatus;
    
    private LocalDate created;
    
    @Transient
    @JsonIgnore
    private String picture;
    
	@JsonIgnore
    @Valid
    private UserDTO userDTO;

    private List<CustomerDTO> customers = new ArrayList<>();

    private List<BookDTO> bookDTOs = new ArrayList<>();

    @Transient
    @JsonIgnore
    private MultipartFile upload;
    
    public void addCustomer(CustomerDTO customer) {
        customers.add(customer);
    }

    public void removeCustomer(CustomerDTO customer) {
        customers.remove(customer);
    }

}
