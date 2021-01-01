package com.app.bookstore.domain.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * @author Ananth Shanmugam
 */
@Data
public class MessageDTO {

	
    private Long id;

    private String content;

    private Boolean read = false;

    private LocalDateTime receivedDate;

    @JsonIgnore
    private UserDTO userDTO;

}
