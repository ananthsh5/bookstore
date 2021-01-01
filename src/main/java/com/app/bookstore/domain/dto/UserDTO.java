package com.app.bookstore.domain.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.app.bookstore.domain.Role;

import lombok.Data;

/**
 * @author Ananth Shanmugam
 */
@Data
public class UserDTO {

    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 4, max = 255)
    private String password;

    private String confirmPassword;

    @NotBlank
    private String phone;

    @NotBlank
    private String address;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate registerDate;

    private Role role;

    private List<MessageDTO> messages = new ArrayList<MessageDTO>();

    private PublisherDto publisherDto;
    
    @Transient
    private String fullName;


}
