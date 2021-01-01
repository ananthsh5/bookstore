package com.app.bookstore;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.app.bookstore.controller.BookPublisherController;
import com.app.bookstore.domain.Accstatus;
import com.app.bookstore.domain.Role;
import com.app.bookstore.domain.dto.BookDTO;
import com.app.bookstore.domain.dto.BookcategoryDTO;
import com.app.bookstore.domain.dto.MessageDTO;
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
@WebMvcTest(BookPublisherController.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class BookPublisherControllerTest {

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
	BookPublisherController bookPublisherController;
	
	@WithMockUser(username="publisher@bookstore.com",password="qwerty123S")
	@Test
	public void doPostSaveBookWithId() throws Exception
	{
		PublisherDto publisherDto = new PublisherDto();
		publisherDto.setName("Apress");
		publisherDto.setDescription("");
		publisherDto.setAccstatus(Accstatus.APPROVED);
		publisherDto.setCustomers(null);

		PublisherDto publisherDto2 = new PublisherDto();
		publisherDto2.setName("Apress");
		publisherDto2.setDescription("");
		publisherDto2.setAccstatus(Accstatus.APPROVED);
		
		BookcategoryDTO bookcategoryDTO = new BookcategoryDTO();
		bookcategoryDTO.setId(Long.valueOf(2));
		bookcategoryDTO.setName("Spring");
		//bookcategoryDTO.setBooks(null);
		
		BookDTO bookDTO = new BookDTO();
		bookDTO.setName("Pro JPA 2");
		bookDTO.setDescription("Mastering Spring  persistence basics and core topics.");
		bookDTO.setPrice(new BigDecimal("30.00"));
		bookDTO.setAvailable(Double.valueOf(1));
		bookDTO.setPublisherDto(publisherDto);
		bookDTO.setBookcategoryDTO(bookcategoryDTO);

		LocalDateTime ldt = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
		Timestamp current = Timestamp.valueOf(ldt);
		bookDTO.setCreatedDate(current);
		
		List<MessageDTO> messages = null;

		UserDTO userDTO = new UserDTO();
		userDTO.setAddress("Kuala Lumpur");
		userDTO.setEmail("publisher@bookstore.com");
		userDTO.setFirstName("BookStore");
		userDTO.setLastName("Publisher 1");
		userDTO.setPhone("123456789");
		userDTO.setRegisterDate(LocalDate.now());
		userDTO.setRole(Role.PUBLISHER);
		userDTO.setPassword("$2a$10$jmiSOFYc1v46UwHq6V6QOOebRLaGnH9KCsO7TaNYiRQg4y8mWQGhu");
		userDTO.setFullName(userDTO.getFirstName() + " " + userDTO.getLastName());
		userDTO.setMessages(messages);
		userDTO.setPublisherDto(publisherDto2);
		
		List<OrderItemDTO> orderItems = new ArrayList<OrderItemDTO>();

		Mockito.when(orderItemService.getOrderItems()).thenReturn(orderItems);

		Mockito.when(bookcategoryService.getBookcategoryById(Long.valueOf(2))).thenReturn(bookcategoryDTO);

		Mockito.when(userservice.findByEmail("publisher@bookstore.com")).thenReturn(userDTO);
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/publisher/book", bookDTO)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.flashAttr("bookDTO", bookDTO))
				.andExpect(status().is(HttpStatus.SC_MOVED_TEMPORARILY))
				.andReturn();
		
		System.out.println("Response status received is "+result.getResponse().getStatus());
		System.out.println("Response content type received is "+result.getResponse().getContentType());

//		assertEquals(result.getResponse().getStatus(), HttpStatus.SC_OK);

	}
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
}
