package com.app.bookstore.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.bookstore.controller.validator.PromotionImageFileValidator;
import com.app.bookstore.domain.dto.PromotionDTO;
import com.app.bookstore.service.PromotionService;

/**
 * @author Ananth Shanmugam
 */
@Controller
public class PromotionController {
	
	private final String ADMIN_PROMOTION_PAGE = "/admin/promotion";
	
    private final String REDIRECT_TO_PROMOTIONS_PAGE = "redirect:/admin/promos";

	private final String ADMIN_PROMOTION_FORM_PAGE = "/admin/promoForm";

	private final String REDIRECT_TO_ACCOUNT_LOGIN_PAGE = "redirect:/account/login";

	private final String ANONYMOUS_USER ="anonymousUser";

    @Autowired
    PromotionService promotionService;

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    PromotionImageFileValidator promotionImageFileValidator;

    @GetMapping("/admin/promos")  /* Get the promotion page */
    String showPromotionPanel(@ModelAttribute("promo") PromotionDTO promotion, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }

        List<PromotionDTO> promotions = promotionService.getAll();
        model.addAttribute("promos", promotions);

        return ADMIN_PROMOTION_PAGE;
    }

    @GetMapping("/admin/addPromo") /* Get the add promotion page */
    String promoForm(@ModelAttribute("promo") PromotionDTO promotion, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }

        return ADMIN_PROMOTION_FORM_PAGE;
    }

    @GetMapping("/admin/update/{promoId}")  /* Get existing promotion by id from db for update */
    String updatePromo(@PathVariable("promoId") Long promoId, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }

        PromotionDTO promotion = promotionService.getPromoById(promoId);
        model.addAttribute("promo", promotion);
        return ADMIN_PROMOTION_FORM_PAGE;
    }

    @PostMapping("/admin/updatePromo/{promoId}")  /* Validate and then update the existing promotion to db */
    String updateExistingPromo(@Valid PromotionDTO promotionDTO, BindingResult bindingResult, RedirectAttributes ra,
                     HttpServletRequest request, @PathVariable("promoId") Long promoId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }

        promotionImageFileValidator.validate(promotionDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return ADMIN_PROMOTION_PAGE;
        }
        promotionDTO.setId(promoId);
        promotionService.savePromo(promotionDTO);
        return REDIRECT_TO_PROMOTIONS_PAGE;
    }

    @PostMapping("/admin/addPromo")  /* Validate and then add the new promotion to db */
    String addPromo(@Valid PromotionDTO promotionDTO, BindingResult bindingResult, RedirectAttributes ra,
                     HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }

        promotionImageFileValidator.validate(promotionDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return ADMIN_PROMOTION_PAGE;
        }
        promotionService.savePromo(promotionDTO);
        return REDIRECT_TO_PROMOTIONS_PAGE;
    }
    
    @GetMapping("/admin/delete/{promoId}") /* Delete existing promotion by id in db */
    String deletePromo(@PathVariable("promoId") Long promoId){

    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || (authentication!=null && authentication.getPrincipal().equals(ANONYMOUS_USER))) {
        	return REDIRECT_TO_ACCOUNT_LOGIN_PAGE;
        }

        PromotionDTO promoToDelete = promotionService.getPromoById(promoId);
        promotionService.deletePromo(promoToDelete);
        return REDIRECT_TO_PROMOTIONS_PAGE;
    }

}
