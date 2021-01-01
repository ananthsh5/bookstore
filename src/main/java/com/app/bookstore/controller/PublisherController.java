package com.app.bookstore.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.bookstore.controller.validator.PublisherStoreImageFileValidator;
import com.app.bookstore.domain.OrderItemStatus;
import com.app.bookstore.domain.OrderStatus;
import com.app.bookstore.domain.dto.BookcategoryDTO;
import com.app.bookstore.domain.dto.CustomerDTO;
import com.app.bookstore.domain.dto.OrderItemDTO;
import com.app.bookstore.domain.dto.PublisherDto;
import com.app.bookstore.domain.dto.UserDTO;
import com.app.bookstore.service.BookcategoryService;
import com.app.bookstore.service.MessageService;
import com.app.bookstore.service.OrderService;
import com.app.bookstore.service.PublisherService;
import com.app.bookstore.service.UserService;

/**
 * @author Ananth Shanmugam
 */
@Controller
@RequestMapping(value = {"/publisher"})
public class PublisherController {
	
	private final String PUBLISHER_ORDER_PAGE = "/publisher/Orders";

	private final String PUBLISHER_DASHBOARD_PAGE = "publisher/dashboard";

	private final String PUBLISHER_ORDER_DETAIL_PAGE = "/publisher/OrderItemDetail";
	
	private final String REDIRECT_TO_PUBLISHER_ORDER_PAGE = "redirect:/publisher/orders";

	private final String REDIRECT_TO_PUBLISHER_STORE_PAGE = "redirect:/publisher/store";

	private final String PUBLISHER_STORE_INFO_PAGE = "/publisher/storeInfo";

	private final String PUBLISHER_SUBSRIBERS_PAGE = "/publisher/Subscribers";
	
    private final String PUBLISHER_INFO_UPDATE_MSG = "Publisher Information Updated";

	private final String REDIRECT_TO_ACCOUNT_LOGIN_PAGE = "redirect:/account/login";

	private final String ANONYMOUS_USER ="anonymousUser";

	@Autowired
    private PublisherService publisherService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookcategoryService bookcategoryService;

    @ModelAttribute("categories")
    public List<BookcategoryDTO> getBookCategories(){
        return bookcategoryService.getBookcategories();
    }
  
    @Autowired
    private MessageService messageService;
    
    @Autowired
    PublisherStoreImageFileValidator publisherStoreImageFileValidator;
    

    @GetMapping(value = {"", "/", "/dashboard"})  /* Get the publishers dashboard page */
    public String getPublisherIndex() {
        return PUBLISHER_DASHBOARD_PAGE;
    }

    @GetMapping("/orders")
    public String getOrdersByPublisher(Model model) {   /* Get the publishers customer orders page */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }
        UserDTO user = userService.findByEmail(authentication.getName());
        PublisherDto publisher = publisherService.getPublisherByUser(user);
        List<OrderItemDTO> orderItems = orderService.getOrderItemsByPublisher(publisher.getId());
        model.addAttribute("orderItems", orderItems);
        return PUBLISHER_ORDER_PAGE;
    }

    @GetMapping("/orders/{itemId}")    /* Get the customer order by id page */
    public String getOrderItem(@PathVariable("itemId") Long itemId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }

        OrderItemDTO item = orderService.getOrderItemById(itemId);
        BigDecimal totalAmount = item.getBookDTO().getPrice().multiply(new BigDecimal(item.getQuantity()));
        model.addAttribute("item", item);
        model.addAttribute("totalAmount", totalAmount);
        return PUBLISHER_ORDER_DETAIL_PAGE;
    }

    @PostMapping("/orders/{itemId}/status")  /* Update the customer order status by id */
    public String updateOrderStatus(@PathVariable("itemId") Long itemId, @Valid OrderItemDTO item, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER)))  {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }

    	OrderItemDTO orderItemDTO = orderService.getOrderItemById(itemId);
        BigDecimal totalAmount = orderItemDTO.getBookDTO().getPrice().multiply(new BigDecimal(orderItemDTO.getQuantity()));
        if(orderItemDTO != null){
            orderItemDTO.setOrderStatus(item.getOrderStatus());
            if (orderItemDTO.getOrderStatus() == OrderItemStatus.CANCELED) {
                String message = "The order for " + orderItemDTO.getBookDTO().getName() + " is canceled";
                messageService.sendMessageToUser(orderItemDTO.getOrderDTO().getCustomerDTO().getUserDTO(), message);
            }
            if (orderItemDTO.getOrderDTO().getStatus() == OrderStatus.NEW) {
                if (orderItemDTO.getOrderStatus() != OrderItemStatus.ORDERED && orderItemDTO.getOrderStatus() != OrderItemStatus.CANCELED) {
                    orderItemDTO.getOrderDTO().setStatus(OrderStatus.PROCESSING);
                }
            }
            if (orderItemDTO.getOrderStatus() == OrderItemStatus.CANCELED || orderItemDTO.getOrderStatus() == OrderItemStatus.RETURNED) {
                orderItemDTO.getOrderDTO().setTotalAmount(orderItemDTO.getOrderDTO().getTotalAmount().subtract(totalAmount));
            }
            orderService.saveOrderItem(orderItemDTO);
        }
        model.addAttribute("item", orderItemDTO);
        model.addAttribute("totalAmount", totalAmount);
        return REDIRECT_TO_PUBLISHER_ORDER_PAGE;
    }


    @GetMapping("/subscribers")   /* Get the customer subscribers  page */
    public String getSubsribers(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }
        UserDTO user = userService.findByEmail(authentication.getName());
        PublisherDto publisher = publisherService.getPublisherByUser(user);
        List<CustomerDTO> customers = publisherService.getSubscribers(publisher.getId());
        model.addAttribute("customers", customers);
        return PUBLISHER_SUBSRIBERS_PAGE;
    }

    
    @GetMapping(value = {"/store"})  /* Get the publisher store info page */
    public String getStoreForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }
        if (authentication != null) {
            UserDTO user = userService.findByEmail(authentication.getName());
            if (user != null) {
                PublisherDto dto = new PublisherDto();
                dto.setId(user.getPublisherDto().getId());
                dto.setName(user.getPublisherDto().getName());
                dto.setDescription(user.getPublisherDto().getDescription());
                dto.setPicture(user.getPublisherDto().getPicture() != null ? user.getPublisherDto().getPicture() : "data:image/svg+xml;charset=UTF-8,%3Csvg%20width%3D%22893%22%20height%3D%22180%22%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20viewBox%3D%220%200%20893%20180%22%20preserveAspectRatio%3D%22none%22%3E%3Cdefs%3E%3Cstyle%20type%3D%22text%2Fcss%22%3E%23holder_16c89944157%20text%20%7B%20fill%3Argba(255%2C255%2C255%2C.75)%3Bfont-weight%3Anormal%3Bfont-family%3AHelvetica%2C%20monospace%3Bfont-size%3A45pt%20%7D%20%3C%2Fstyle%3E%3C%2Fdefs%3E%3Cg%20id%3D%22holder_16c89944157%22%3E%3Crect%20width%3D%22893%22%20height%3D%22180%22%20fill%3D%22%23777%22%3E%3C%2Frect%3E%3Cg%3E%3Ctext%20x%3D%22331.4000015258789%22%20y%3D%22110.15999908447266%22%3E893x180%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3C%2Fsvg%3E");
                dto.setCreated(user.getRegisterDate());
                dto.setAccstatus(user.getPublisherDto().getAccstatus());
                model.addAttribute("publisher", dto);
            }
        }
        return PUBLISHER_STORE_INFO_PAGE;
    }

    @PostMapping(value = {"/store"}) /* Validate and then update the publisher store info page */
    public String updateStoreInfo(@Valid PublisherDto dto, BindingResult bindingresult, RedirectAttributes rd) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }
        publisherStoreImageFileValidator.validate(dto, bindingresult);
        if (bindingresult.hasErrors()) {
            return PUBLISHER_STORE_INFO_PAGE;
        }
        if (authentication != null) {
            UserDTO userUpdate = userService.findByEmail(authentication.getName());
            if (userUpdate != null) {
                PublisherDto publisher = userUpdate.getPublisherDto();
                publisher.setName(dto.getName());
                publisher.setDescription(dto.getDescription());
                publisher.setPicture(dto.getPicture());
                publisherService.updatePublisher(publisher);
            }
        }

        rd.addFlashAttribute("success", PUBLISHER_INFO_UPDATE_MSG); /* Add the update success msg */
        return REDIRECT_TO_PUBLISHER_STORE_PAGE;
    }
}
