package com.app.bookstore.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.app.bookstore.domain.dto.BookDTO;
import com.app.bookstore.domain.dto.CartItemDTO;
import com.app.bookstore.domain.dto.OrderItemDTO;
import com.app.bookstore.domain.dto.PublisherDto;
import com.app.bookstore.service.BookService;
import com.app.bookstore.service.CartItemService;
import com.app.bookstore.service.CustomerService;
import com.app.bookstore.service.OrderItemService;

/**
 * @author Ananth Shanmugam
 */
@Controller
public class BookController {

	private final String BOOK_PAGE = "book";
	
	private final String REDIRECT_TO_ACCOUNT_LOGIN_PAGE = "redirect:/account/login";

	private final String ANONYMOUS_USER ="anonymousUser";

	@Autowired
	private BookService bookService;

	@Autowired
	private OrderItemService orderItemService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CartItemService cartItemService;

	@GetMapping("/book/{bookId}")
	public String loadBook(@PathVariable("bookId") Long id, Model model) { /* load the book page by book id */
		BookDTO book = bookService.findById(id);

		model.addAttribute("book", book);
		List<OrderItemDTO> orderItems = orderItemService.getOrderItems().stream()
				.filter(orderItem -> orderItem.getBookDTO().equals(book)).collect(Collectors.toList());
		model.addAttribute("orderItems", orderItems);

		PublisherDto publisher = book.getPublisherDto();
		model.addAttribute("publisher", publisher);
		model.addAttribute("subscribe", customerService.getCustomerActiveSubscriptions(publisher));
		return BOOK_PAGE;
	}

	@GetMapping("/book/addToCart/{bookId}")
	public String addToCart(@PathVariable("bookId") Long bookId) {  /* Book id is added to cart */
		BookDTO newBook = bookService.findById(bookId);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth.getPrincipal().equals(ANONYMOUS_USER) || (auth!=null && auth.getPrincipal().equals(ANONYMOUS_USER))) {
			return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
		}
		CartItemDTO newCartItem = new CartItemDTO();
		newCartItem.setBookDTO(newBook);
		newCartItem.setCustomerDTO(customerService.getCustomerByUser());
		newCartItem.setQuantity(1);
		cartItemService.saveCartItem(newCartItem);
		return "redirect:/";
	}
}
