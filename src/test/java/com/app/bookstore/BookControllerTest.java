package com.app.bookstore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.app.bookstore.controller.BookController;
import com.app.bookstore.domain.Accstatus;
import com.app.bookstore.domain.Role;
import com.app.bookstore.domain.dto.BookDTO;
import com.app.bookstore.domain.dto.CustomerDTO;
import com.app.bookstore.domain.dto.OrderItemDTO;
import com.app.bookstore.domain.dto.PublisherDto;
import com.app.bookstore.domain.dto.UserDTO;
import com.app.bookstore.service.BookService;
import com.app.bookstore.service.BookcategoryService;
import com.app.bookstore.service.CartItemService;
import com.app.bookstore.service.CartService;
import com.app.bookstore.service.CustomerService;
import com.app.bookstore.service.MessageService;
import com.app.bookstore.service.OrderItemService;
import com.app.bookstore.service.OrderService;
import com.app.bookstore.service.PromotionService;
import com.app.bookstore.service.PublisherService;
import com.app.bookstore.service.UserService;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@WebMvcTest(BookController.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class BookControllerTest {

	@MockBean
    private BCryptPasswordEncoder crypt;

	
	@Autowired
    private MockMvc mvc;
	
	@MockBean
	UserService userservice;

	@MockBean
	OrderItemService orderItemService;

	@MockBean
	BookService bookService;

	@MockBean
	CustomerService customerService;

	@MockBean
	CartItemService cartItemService;

	@MockBean
	MessageService messageService;

	@MockBean	
	PublisherService publisherService;

	@MockBean	
	OrderService orderService;
	
	@MockBean		
	PromotionService promotionService;
	
	@MockBean			
	CartService cartService;
	
	@MockBean			
	BookcategoryService bookcategoryService;
	
	@Autowired
	BookController bookController;
	

	@Test
	public void doGetBookWithId() throws Exception
	{

		PublisherDto publisherDto = new PublisherDto();
		publisherDto.setName("Apress");
		publisherDto.setDescription("");
		publisherDto.setAccstatus(Accstatus.APPROVED);
		
		BookDTO bookDTO = new BookDTO();
		bookDTO.setId(Long.valueOf(1));
		bookDTO.setName("Pro Spring 5 ");
		bookDTO.setDescription("Master Spring basics and core topics, and share the authors’ insights and real–world experiences with remoting, Hibernate, and EJB.");
		bookDTO.setPrice(new BigDecimal("28.49"));
		bookDTO.setAvailable(Double.valueOf(1));
		bookDTO.setPublisherDto(publisherDto);
		
		UserDTO userDTO = new UserDTO();
		userDTO.setAddress("Kuala Lumpur");
		userDTO.setEmail("customer@bookstore.com");
		userDTO.setFirstName("BookStore");
		userDTO.setLastName("Customer 1");
		userDTO.setPhone("123456789");
		userDTO.setRegisterDate(LocalDate.now());
		userDTO.setRole(Role.CUSTOMER);
		userDTO.setPassword("$2a$10$jmiSOFYc1v46UwHq6V6QOOebRLaGnH9KCsO7TaNYiRQg4y8mWQGhu");
		userDTO.setFullName(userDTO.getFirstName() + " " + userDTO.getLastName());
		
		CustomerDTO customerDTO  = new CustomerDTO();
		customerDTO.setUserDTO(userDTO);
		
		List<OrderItemDTO> orderItems = new ArrayList<OrderItemDTO>();

		Mockito.when(orderItemService.getOrderItems()).thenReturn(orderItems);

		Mockito.when(bookService.findById(Long.valueOf(1))).thenReturn(bookDTO);

		Mockito.when(customerService.getCustomerByUser()).thenReturn(customerDTO);

		
		Mockito.when(userservice.findByEmail("customer@bookstore.com")).thenReturn(userDTO);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/book/{bookId}", "1"))		
        		.andReturn();
		System.out.println("Response status received is "+result.getResponse().getStatus());
		System.out.println("Response content type received is "+result.getResponse().getContentType());

		assertEquals(result.getResponse().getStatus(), HttpStatus.SC_OK);

	}
	
	
	// BYE-PASS THE SECURITY 
    @Configuration
    @ComponentScan(basePackages = "com.app.bookstore.controller")
    @EnableWebSecurity
    static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception
        {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .anyRequest()
                    .permitAll();
        }
        

    }
	
	@Test
	public void getIndex() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/")
				.accept(MediaType.TEXT_HTML))
				.andExpect(status().isOk());
	}

	@Test
	public void getLogin() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/account/login")
				.accept(MediaType.TEXT_HTML))
				.andExpect(status().isOk());
			
	}
    
}

