package com.app.bookstore.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.bookstore.BookStoreAppLauncher;
import com.app.bookstore.domain.dto.BookDTO;
import com.app.bookstore.domain.dto.BookcategoryDTO;
import com.app.bookstore.domain.dto.CustomerDTO;
import com.app.bookstore.domain.dto.PublisherDto;
import com.app.bookstore.domain.dto.UserDTO;
import com.app.bookstore.service.BookService;
import com.app.bookstore.service.BookcategoryService;
import com.app.bookstore.service.MessageService;
import com.app.bookstore.service.PublisherService;
import com.app.bookstore.service.UserService;

/**
 * @author Ananth Shanmugam
 */
@Controller
public class BookPublisherController {

	private final String PUBLISHER_BOOK_LIST_PAGE = "/publisher/bookList";

	private final String PUBLISHER_EDIT_BOOK_PAGE = "/publisher/bookEdit";

	private final String REDIRECT_TO_PUBLISHER_BOOK_PAGE = "redirect:/publisher/book";
	
	private final String REDIRECT_TO_ACCOUNT_LOGIN_PAGE = "redirect:/account/login";

	private final String SAVE_IMAGE_ERROR_MSG =	"Error in saving book image";
	
	private final String BOOK_IMAGE_DIR ="\\static\\img\\books";

	private final String SAVE_BOOK_IMAGE_DIR ="/img/books/";
	
	private final String ANONYMOUS_USER ="anonymousUser";

	
	private final static Logger LOGGER = LoggerFactory.getLogger(BookPublisherController.class);

	@Autowired
	private BookService bookService;

	@Autowired
	private UserService userService;

	@Autowired
	private BookcategoryService bookcategoryService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private PublisherService publisherService;


	@GetMapping(value = { "/publisher/book" })
	public String getBookList(Model model) {		/* Get the book list associated with this publisher */
		
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }
		UserDTO user = userService.findByEmail(authentication.getName());
		PublisherDto publisher = publisherService.getPublisherByUser(user);
		model.addAttribute("books", bookService.getBooksByPublisher(publisher));
		return PUBLISHER_BOOK_LIST_PAGE;
	}

	@GetMapping(value = { "/publisher/book/delete/{id}" }) /* Delete the book by book id associated with this publisher */
	public String deleteBook(@PathVariable(value = "id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }

		BookDTO book = bookService.findById(id);
		if (book != null)
			bookService.delete(book);
		return REDIRECT_TO_PUBLISHER_BOOK_PAGE;
	}

	@GetMapping(value = { "/publisher/book/add" }) /* Get the new book add page */
	public String addBookForm(Model model, RedirectAttributes rd) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }

		BookDTO bookDTO = new BookDTO();
		model.addAttribute("book", bookDTO);
		model.addAttribute("categories", bookcategoryService.getBookcategories());
		return PUBLISHER_EDIT_BOOK_PAGE;
	}

	@GetMapping(value = { "/publisher/book/{id}" }) /* Get the update book page */ 
	public String editBook(@PathVariable(value = "id", required = false) Long id, Model model, RedirectAttributes rd) {

		if (id != null) {
			model.addAttribute("book", bookService.findById(id));
			model.addAttribute("categories", bookcategoryService.getBookcategories());
		}
		return PUBLISHER_EDIT_BOOK_PAGE;
	}

	@PostMapping(value = { "/publisher/book/{id}" })  /* Save/Update existing book of publisher in db */ 
	public String saveorupdateBook(@Valid BookDTO book, BindingResult bindingresult,
			@PathVariable(value = "id", required = false) Long id, RedirectAttributes rd) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }

		uploadBookImage(book, bindingresult);
		if (bindingresult.hasErrors()) {
			return PUBLISHER_EDIT_BOOK_PAGE;
		}

		BookcategoryDTO bookcategory = bookcategoryService.getBookcategoryById(book.getBookcategoryDTO().getId());

		if (id != null) {
			BookDTO updateBook = bookService.findById(id);
			updateBook.setName(book.getName());
			updateBook.setDescription(book.getDescription());
			updateBook.setPrice(book.getPrice());
			if (book.getImage() != null &&  book.getUpload()!=null) {
				updateBook.setImage(book.getImage());
			}
			updateBook.setAvailable(book.getAvailable());
			updateBook.setBookcategoryDTO(bookcategory);
			saveToRepository(updateBook);
		} else {
			saveToRepository(book);
		}

		return REDIRECT_TO_PUBLISHER_BOOK_PAGE;
	}

	@PostMapping(value = { "/publisher/book" }) /* Save new book to this publisher in db */ 
	public String saveBook(@Valid BookDTO book, BindingResult bindingresult, RedirectAttributes rd) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }
		uploadBookImage(book, bindingresult);
		
		if (bindingresult.hasErrors()) {
			return PUBLISHER_EDIT_BOOK_PAGE;
		}
		saveToRepository(book);
		return REDIRECT_TO_PUBLISHER_BOOK_PAGE;
	}
	
	private void saveToRepository(BookDTO bookDTO) { /*Helper method to save book to db */
		LOGGER.info("book category id: "+ bookDTO.getBookcategoryDTO().getId());
		BookcategoryDTO bookcategory = bookcategoryService.getBookcategoryById(bookDTO.getBookcategoryDTO().getId());

		PublisherDto publisher = getBookPublisherDto();
		bookDTO.setBookcategoryDTO(bookcategory);
		bookDTO.setPublisherDto(publisher);
		bookService.save(bookDTO);
		/* send subscription info message to subscribers */
		List<CustomerDTO> subscribers = publisher.getCustomers();
		if (subscribers != null ) {
			String message = "New book added by" + publisher.getName() + " Publisher.";
			subscribers.stream().forEach(subscriberObj -> messageService.sendMessageToUser(subscriberObj.getUserDTO(), message));
        }

	}
	
	private void uploadBookImage(BookDTO book, BindingResult bindingresult) { /*Helper method to upload image */
		MultipartFile upload = book.getUpload();

		if (upload != null && !upload.isEmpty()) {
			
			String homeUrl = new ApplicationHome(BookStoreAppLauncher.class).getDir() + BOOK_IMAGE_DIR;
			Path rootLocation = Paths.get(homeUrl);

			if (!Files.exists(rootLocation)) {
				try {
					Files.createDirectory(rootLocation);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try {
				String imageName = UUID.randomUUID().toString() + "."
						+ FilenameUtils.getExtension(upload.getOriginalFilename());
				Files.copy(upload.getInputStream(), rootLocation.resolve(imageName));
				book.setImage(SAVE_BOOK_IMAGE_DIR + imageName);
			} catch (Exception ex) {
				bindingresult.rejectValue("upload", "", SAVE_IMAGE_ERROR_MSG);
			}
		}
	}
	
	private PublisherDto getBookPublisherDto() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		PublisherDto publisher = null;
		if (authentication != null) {
			UserDTO user = userService.findByEmail(authentication.getName());
			if (user != null) {
				publisher = user.getPublisherDto();
			}

		}
		return publisher;
	}
}
