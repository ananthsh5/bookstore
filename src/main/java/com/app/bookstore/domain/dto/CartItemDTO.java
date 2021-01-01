package com.app.bookstore.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * @author Ananth Shanmugam
 */
@Data
public class CartItemDTO {

	private Long id;

	private int quantity;

	private BookDTO bookDTO;

	@JsonIgnore
	private CustomerDTO customerDTO;
}
