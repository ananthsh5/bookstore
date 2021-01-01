package com.app.bookstore.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.app.bookstore.domain.dto.UserDTO;
import com.app.bookstore.service.UserService;

/**
 * @author Ananth Shanmugam
 * General interceptor class to add the userdto to the modelandview
 */
@Component
public class UserInterceptor implements HandlerInterceptor {

	@Autowired
	private UserService userService;


	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		// get current user principal
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			// check if the current user principal is valid.
			if (auth != null) {
				UserDTO userDTO = userService.findByEmail(auth.getName());
				if (userDTO != null) {
					// inject user details into the current view
					if (modelAndView != null && !modelAndView.getModelMap().containsKey("userDTO")) {
						modelAndView.getModelMap().addAttribute("userDTO", userDTO);
					}
				}
				

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
}
