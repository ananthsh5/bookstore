package com.app.bookstore.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.bookstore.domain.dto.BookDTO;
import com.app.bookstore.domain.dto.BookcategoryDTO;
import com.app.bookstore.domain.dto.CartItemDTO;
import com.app.bookstore.domain.dto.CustomerDTO;
import com.app.bookstore.domain.dto.PromotionDTO;
import com.app.bookstore.domain.dto.UserDTO;
import com.app.bookstore.service.BookService;
import com.app.bookstore.service.BookcategoryService;
import com.app.bookstore.service.CartService;
import com.app.bookstore.service.CustomerService;
import com.app.bookstore.service.PromotionService;
import com.app.bookstore.service.UserService;

/**
 * @author Ananth Shanmugam
 */
@Controller
public class HomeController {

    @Autowired
    private BookService bookService;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CartService cartService;

    @Autowired
    private BookcategoryService bookcategoryService;
    
    private final static Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    private final String INDEX_PAGE = "index";

    private final String FAQ_PAGE = "faq";

    private final String ABOUT_PAGE = "about";

    private final String REDIRECT_TO_ACCOUNT_LOGIN_PAGE = "redirect:/account/login";

    private final String REDIRECT_TO_CUSTOMER_CART_PAGE = "redirect:/customer/cart";

    private final String ERROR_PAGE_403 = "403";

    private final String ERROR_PAGE_404 = "404";
    
    private final String PERMISSION_ERROR_MSG = "You do not have permission to access this page!";
    
	private final String ANONYMOUS_USER ="anonymousUser";

    @GetMapping(value = {"/"})  /* Get the entry page of book store */
    public String index(Model model) {
        List<BookDTO> books = bookService.getAll();		/* Get all the books */
        addShuffledBooksAndPromotionsAndCategories(books,model);

        return INDEX_PAGE;
    }

    @GetMapping(value = {"/faq"}) /* Get the faq page of book store */
    public String faq(Model model) {
        List<BookDTO> books = bookService.getAll();
        addShuffledBooksAndPromotionsAndCategories(books,model);
        return FAQ_PAGE;
    }

    @GetMapping(value = {"/about"}) /* Get the about page of book store */
    public String about(Model model) {
        List<BookDTO> books = bookService.getAll();
        addShuffledBooksAndPromotionsAndCategories(books,model);

        return ABOUT_PAGE;
    }


    @GetMapping("/search")   /* Get the search results of book store - front end not done */
    public String indexSearch(@RequestParam("searchWord") String searchWord ,Model model) {
        //brings books
        List<BookDTO> books = bookService.getAll().stream()
                .filter(x -> x.getName().toLowerCase().contains(searchWord.toLowerCase())).collect(Collectors.toList());

        addShuffledBooksAndPromotionsAndCategories(books,model);

        return INDEX_PAGE;
    }


    @GetMapping("/category")  /* Get the categories by id */
    public String getBooksByCategoryPage(@RequestParam("id") Long categoryId, Model model) {
        BookcategoryDTO bookcategory = bookcategoryService.getBookcategoryById(categoryId);
        List<BookDTO> books = bookService.getBooksByBookcategory(bookcategory);

        addShuffledBooksAndPromotionsAndCategories(books,model);
        model.addAttribute("currentCategoryId", bookcategory.getId());
        return INDEX_PAGE;
    }

  
    @PostMapping(value = {"/book/addToCart"},
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public boolean addBookToCart(@RequestBody String id) {		  /* Add book by book id to book store cart. Jquery call */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            UserDTO user = userService.findByEmail(authentication.getName());
            if(user == null)
                return false;
            BookDTO book = bookService.findById(Long.parseLong(id));
            CustomerDTO customer = customerService.getCustomerByUser();
            List<CartItemDTO> cartItems = customer.getCartItems(); /* Get the current customer cart */
            CartItemDTO cartItem = new CartItemDTO();

            for(CartItemDTO item : cartItems){
                if(item.getBookDTO().getId().equals(book.getId())){ /* Increase quantity by 1 for existing item in cart*/
                    cartItem = item;
                    cartItem.setQuantity(cartItem.getQuantity() + 1);
                    break;
                }
            }
            if(cartItem.getId() == null){ //new book added to cart
                cartItem.setBookDTO(book);
                cartItem.setCustomerDTO(customer);
                cartItem.setQuantity(1);
            }
            cartService.saveCartItem(customer, cartItem);
        }else{
            return false;
        }
        return true;
    }

   
    @GetMapping("/404")
    public String get404() {
        return ERROR_PAGE_404;
    }

    @GetMapping("/403")
    public String get403(Principal user, Model model) {
        model.addAttribute("msg", PERMISSION_ERROR_MSG);
        return ERROR_PAGE_403;
    }

    @GetMapping("/error")	/*Generic error page mapping*/
    public String getError() {
        return "error";
    }

    @RequestMapping(value = {"/book/{id}/cart"})
    public String addBookToCart(@PathVariable(value = "id") Long id){    /* Add book by book id to book store cart. And redirect to cart page */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))){
            return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }

        UserDTO user = userService.findByEmail(authentication.getName());
        if(user == null){
            return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }
        LOGGER.info("addBookToCart id : "+ id);

        BookDTO book = bookService.findById(id);
        CustomerDTO customer = customerService.getCustomerByUser();

        List<CartItemDTO> cartItems = customer.getCartItems();
        CartItemDTO cartItem = new CartItemDTO();

        for(CartItemDTO item : cartItems){  /* Increase quantity by 1 for existing item in cart*/
            if(item.getBookDTO().getId().equals(book.getId())){
                cartItem = item;
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                break;
            }
        }

        if(cartItem.getId() == null){  //new book added to cart
            cartItem.setBookDTO(book);
            cartItem.setCustomerDTO(customer);
            cartItem.setQuantity(1);
        }
        cartService.addCartItem(cartItem);
        return REDIRECT_TO_CUSTOMER_CART_PAGE;
    }

    /*Helper method to shuffle books, and then add books, categories and promos */
    private void addShuffledBooksAndPromotionsAndCategories(List<BookDTO> books,Model model) {
        Collections.shuffle(books, new Random());

        LOGGER.info("addShuffledBooksAndPromotionsAndCategories");
        model.addAttribute("books", books);

        /* Get the promos */
        List<PromotionDTO> promotions = promotionService.getAll();
        model.addAttribute("promos", promotions);

        /* Get categories */
        List<BookcategoryDTO> bookCategories = bookcategoryService.getBookcategories();
        model.addAttribute("categories", bookCategories);
    }
}
