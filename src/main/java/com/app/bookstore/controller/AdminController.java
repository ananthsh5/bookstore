package com.app.bookstore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.bookstore.domain.Accstatus;
import com.app.bookstore.domain.OrderStatus;
import com.app.bookstore.domain.dto.OrderDTO;
import com.app.bookstore.domain.dto.PublisherDto;
import com.app.bookstore.service.BookService;
import com.app.bookstore.service.OrderService;
import com.app.bookstore.service.PromotionService;
import com.app.bookstore.service.PublisherService;

/**
 * @author Ananth Shanmugam
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private PublisherService publisherService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private BookService bookService;

	@Autowired
	private PromotionService promotionService;

	private final String ADMIN_DASHBOARD_PAGE = "/admin/dashboard";

	private final String ADMIN_PUBLISHERS_PAGE = "/admin/Publisher";

	private final String REDIRECT_TO_ADMIN_PUBLISHERS_PAGE = "redirect:/admin/publishers";

	private final String REDIRECT_TO_ACCOUNT_LOGIN_PAGE = "redirect:/account/login";

	private final String ANONYMOUS_USER ="anonymousUser";

	@GetMapping("/publishers")
	public String getPublishers(Model model) {  /* Get the publishers page */
		model.addAttribute("publishers", publisherService.getAllPublishers());
		return ADMIN_PUBLISHERS_PAGE;
	}

	@GetMapping(value = { "/dashboard", "/", "" })
	public String adminHomepage(Model model) { /* Get the statistics data for dash board */

		
		List<OrderDTO> orderList = orderService.getAll();

		Long totalOrders = orderList.stream().count();

		Long totalCompletedOrders = orderList.stream().filter(order -> order.getStatus() == OrderStatus.COMPLETED).count();

		Long totalCancelOrders = orderList.stream().filter(order -> order.getStatus() == OrderStatus.CANCELED).count();

		Long totalProcessingOrders = orderList.stream().filter(order -> order.getStatus() == OrderStatus.PROCESSING).count();

		model.addAttribute("totalOrders", totalOrders);
		model.addAttribute("totalCompletedOrders", totalCompletedOrders);
		model.addAttribute("totalCancelOrders", totalCancelOrders);
		model.addAttribute("totalProcessingOrders", totalProcessingOrders);

		int totalBooks = bookService.getAll().size(); 		/* get total books.*/
		model.addAttribute("totalBooks", totalBooks);

		// get total publishers
		Long totalPublishers = publisherService.getAllPublishers().stream() /* get publisher count.*/
				.filter(x -> x.getAccstatus() == Accstatus.APPROVED).count();

		model.addAttribute("totalActivePublishers", totalPublishers); /* get active publisher count.*/

		// get total promos
		int totalPromos = promotionService.getAll().size();
		model.addAttribute("totalPromos", totalPromos);  /* get promo count.*/

		return ADMIN_DASHBOARD_PAGE;

	}

	@PostMapping("/publishers/{publisherId}/approve")
	public String approvePublisher(@PathVariable("publisherId") Long publisherId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }

		PublisherDto s = publisherService.getPublisherById(publisherId);
		if (s != null) {
			s.setAccstatus(Accstatus.APPROVED); /* Set publisher as approved. Triggered by admin.*/
			publisherService.save(s);
		}
		model.addAttribute("publisher", s);
		return REDIRECT_TO_ADMIN_PUBLISHERS_PAGE;
	}

	@PostMapping("/publishers/{publisherId}/reject")
	public String rejectPublisher(@PathVariable("publisherId") Long publisherId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }

		PublisherDto s = publisherService.getPublisherById(publisherId);
		if (s != null) {
			s.setAccstatus(Accstatus.REJECTED); /* Set publisher as rejected. Triggered by admin.*/
			publisherService.save(s);
		}
		model.addAttribute("publisher", s);
		return REDIRECT_TO_ADMIN_PUBLISHERS_PAGE;
	}

}
