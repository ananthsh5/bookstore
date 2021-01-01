package com.app.bookstore.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.app.bookstore.domain.OrderItemStatus;
import com.app.bookstore.domain.OrderStatus;
import com.app.bookstore.domain.dto.OrderDTO;
import com.app.bookstore.domain.dto.OrderItemDTO;
import com.app.bookstore.service.OrderService;

/**
 * @author Ananth Shanmugam
 */
@Controller
public class CustomerOrderController {
    @Autowired
    private OrderService orderService;
    
    private final String REDIRECT_TO_CUSTOMER_ORDER_PAGE = "redirect:/customer/orders/";

    private final String CUSTOMER_ORDER_DETAIL_PAGE = "/customer/OrderDetail";

	private final String REDIRECT_TO_ACCOUNT_LOGIN_PAGE = "redirect:/account/login";
	
	private final String ANONYMOUS_USER ="anonymousUser";

    @GetMapping("/customer/orders/{orderId}")   /* Get the customer order details by order id */
    public String getOrder(@PathVariable("orderId") Long orderId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }

        OrderDTO order = orderService.getOrderById(orderId);
        if (order.getStatus() == OrderStatus.PROCESSING) {
            for (OrderItemDTO item : order.getOrderItems()) {
                if (item.getOrderStatus() == OrderItemStatus.ORDERED || item.getOrderStatus() == OrderItemStatus.SHIPPED) {
                    model.addAttribute("order", order);
                    return CUSTOMER_ORDER_DETAIL_PAGE;
                }
            }
            order.setStatus(OrderStatus.COMPLETED);
            order.setEndDate(LocalDateTime.now());
            order.getCustomerDTO().setPoints(order.getCustomerDTO().getPoints() + order.getTotalAmount().divide(new BigDecimal(100)).intValue());
            orderService.updateOrder(order);
        }
        model.addAttribute("order", order);
        model.addAttribute("deliveredOrderItems", orderService.getDeliveredOrderItemsByOrder(orderId));
        return CUSTOMER_ORDER_DETAIL_PAGE;
    }

    @PostMapping("/customer/orders/{orderId}/cancel")  /* Cancel the customer order by order id */
    public String cancelOrder(@PathVariable("orderId") Long orderId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }

        OrderDTO order = orderService.getOrderById(orderId);
        orderService.cancelOrder(order);
        return REDIRECT_TO_CUSTOMER_ORDER_PAGE + orderId;
    }

    @PostMapping("/customer/orders/{orderId}/download")  /* Download the customer order details by order id */
    public String downloadReceipt(@PathVariable("orderId") Long orderId, Model model, HttpServletResponse response) throws Exception {
        OrderDTO order = orderService.getOrderById(orderId);
        List<OrderItemDTO> deliveredOrderItems = orderService.getDeliveredOrderItemsByOrder(orderId);
        order.setOrderItems(deliveredOrderItems);
        File file = orderService.downloadReceipt(order);
        if (file.exists()) {
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=order.pdf");
            ServletOutputStream stream = null;
            BufferedInputStream buf = null;
            try {
                stream = response.getOutputStream();
                FileInputStream os = new FileInputStream(file);
                int readBytes = 0;
                byte [] buffer = new byte [4096];
                while ((readBytes = os.read (buffer,0,4096)) != -1) {
                    stream.write (buffer,0,readBytes);
                }
                os.close();
            } catch (IOException ioe) {
                throw new ServletException(ioe.getMessage());
            } finally {
                if(stream != null)
                    stream.close();
                if(buf != null)
                    buf.close();
            }
        }
        return REDIRECT_TO_CUSTOMER_ORDER_PAGE + orderId;
    }

  
}
