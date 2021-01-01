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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.bookstore.domain.OrderItemStatus;
import com.app.bookstore.domain.dto.CustomerDTO;
import com.app.bookstore.domain.dto.OrderDTO;
import com.app.bookstore.domain.dto.OrderItemDTO;
import com.app.bookstore.domain.dto.PublisherDto;
import com.app.bookstore.domain.dto.PublisherInfo;
import com.app.bookstore.service.CartService;
import com.app.bookstore.service.CustomerService;
import com.app.bookstore.service.OrderService;
import com.app.bookstore.service.PublisherService;

/**
 * @author Ananth Shanmugam
 */
@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private PublisherService publisherService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;
    
    
//	private final static Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);


    private final String CUSTOMER_DASHBOARD_PAGE = "/customer/dashboard";

    private final String CUSTOMER_FORM_PAGE = "/customer/CustomerForm";

    private final String CUSTOMER_PROFILE_PAGE = "/customer/CustomerProfile";

    private final String CUSTOMER_PROFILE_UPDATE_PAGE = "/customer/UpdateCustomer";
    
    private final String CUSTOMER_SUBSCRIBERS_PAGE = "/customer/Subscriber";
  
    private final String CUSTOMER_ORDER_HISTORY_PAGE = "/customer/OrderHistory";
    
    private final String CUSTOMER_CHECKOUT_PAGE= "/customer/Checkout";
    
    private final String REDIRECT_CUSTOMER_TO_ORDER_PAGE = "redirect:/customer/orders/"; 
    
	private final String REDIRECT_TO_ACCOUNT_LOGIN_PAGE = "redirect:/account/login";

	private final String ANONYMOUS_USER ="anonymousUser";

    @GetMapping(value = {"/customer", CUSTOMER_DASHBOARD_PAGE})
    public String customerDashboardPage() { /* Get the customer dashboard page */
        return CUSTOMER_DASHBOARD_PAGE;
    }

    @GetMapping("/register/customer")  /* Get the customer register page */
    public String inputCustomer(@ModelAttribute("customer") CustomerDTO customer) {
        return CUSTOMER_FORM_PAGE;
    }

    @PostMapping("/register/customer")  /* Validate and save the customer to db */
    public String saveNewCustomer(@Valid CustomerDTO customerDTO, BindingResult bindingresult) {
        if (bindingresult.hasErrors()) {
            return CUSTOMER_FORM_PAGE;
        }
        customerService.saveCustomer(customerDTO);
        Long customerId = customerDTO.getId();
        return "redirect:/customer/" + customerId + "/profile";
    }

    @GetMapping("/customer/profile")   /* Get the customer profile page */
    public String getCustomerProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }

        model.addAttribute("customer", customerService.getCustomerByUser());
        return CUSTOMER_PROFILE_PAGE;
    }

    @GetMapping("/customer/profile/update")  /* Get the customer update profile page */
    public String updateCustomer(@PathVariable Long customerId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }

        CustomerDTO customer = customerService.getCustomerByUser();
        customer.getUserDTO().setConfirmPassword(customer.getUserDTO().getPassword());
        model.addAttribute("customer", customer);
        return CUSTOMER_PROFILE_UPDATE_PAGE;
    }

    @PostMapping("/customer/profile/update") /* Update the customer profile in db */
    public String saveCustomer(@Valid CustomerDTO customerDTO, BindingResult result) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }

        if (result.hasErrors()) {
            return CUSTOMER_PROFILE_UPDATE_PAGE;
        }
        customerService.updateCustomer(customerDTO);
        return "redirect:"+CUSTOMER_PROFILE_PAGE;
    }

    @GetMapping("/customer/subscribing")   /* Get the customer subscription page */
    public String getSubsriber(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }

        return CUSTOMER_SUBSCRIBERS_PAGE;
    }

    @GetMapping("/customer/subscribers")  /* Get the customer subscription messages */
    @ResponseBody
    public List<PublisherInfo> getSubsribers(Model model) {
       
        CustomerDTO customer = customerService.getCustomerByUser();
        List<PublisherInfo> subsribers = customerService.getSubscribers(customer.getId());
        return subsribers;
    }

    @DeleteMapping("/customer/subscribe/unsubscribe/{publisherId}")
    @ResponseBody					 /* Unsubscribe the customer subscription from publisher */
    public Boolean removeCartItem(@PathVariable Long publisherId) {
        CustomerDTO customer = customerService.getCustomerByUser();
        PublisherDto publisher = publisherService.getPublisherById(publisherId);
        customerService.unsubscribePublisher(customer, publisher);
        return true;
    }

    @PostMapping("/customer/subscribe/{action}/{publisherId}")
    @ResponseBody					 /* Subscribe the customer subscription from publisher */
    public Boolean subsriberPublisher(@PathVariable("publisherId") Long publisherId, @PathVariable("action") String action){
    	CustomerDTO customer = customerService.getCustomerByUser();
        PublisherDto publisher = publisherService.getPublisherById(publisherId);
        if(action.equals("Subsribe")){

        customerService.subscribePublisher(customer, publisher);
        return true;
        } else {
        customerService.unsubscribePublisher(customer, publisher);
        return false;

        }
    }

    @GetMapping("/customer/orders")   /* Get the customer order history from db */
    public String getOrderHistory(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }

    	CustomerDTO customer = customerService.getCustomerByUser();
        model.addAttribute("orders", customerService.getOrdersByCustomerId(customer.getId()));
        return CUSTOMER_ORDER_HISTORY_PAGE;
    }

    @GetMapping("/customer/checkout")  /* Get the customer checkout page */
    public String getCheckout(@ModelAttribute("order") OrderDTO order, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }

    	CustomerDTO customer = customerService.getCustomerByUser();
        model.addAttribute("cart", cartService.getCartByCustomerId(customer.getId()));
        model.addAttribute("totalAmount", cartService.getTotalAmount(customer.getId()));
        return CUSTOMER_CHECKOUT_PAGE;
    }

    @PostMapping("/customer/order") /* Save the order placed by customer */
    public String placeOrder(@Valid OrderDTO order, Model model) {
    	CustomerDTO customer = customerService.getCustomerByUser();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }

        order =  orderService.saveOrder(customer, order);
        model.addAttribute("orders", order.getOrderItems());
        Long orderId = order.getId();
        return REDIRECT_CUSTOMER_TO_ORDER_PAGE + orderId;
    }

    @PostMapping("/customer/item/{itemId}/cancel") /* Cancel the customer order item by order item id */
    public String cancelOrderItem(@PathVariable("itemId") Long itemId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }

        OrderItemDTO orderItem = orderService.getOrderItemById(itemId);
        BigDecimal totalAmount = orderItem.getBookDTO().getPrice().multiply(new BigDecimal(orderItem.getQuantity()));
        if(orderItem != null){
            orderItem.setOrderStatus(OrderItemStatus.CANCELED);
            orderItem.getOrderDTO().setTotalAmount(orderItem.getOrderDTO().getTotalAmount().subtract(totalAmount));
            orderService.saveOrderItem(orderItem);
        }
        model.addAttribute("item", orderItem);
        Long orderId = orderItem.getOrderDTO().getId();
        return REDIRECT_CUSTOMER_TO_ORDER_PAGE + orderId;
    }

}
