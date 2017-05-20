/**
 * 
 */
package com.mabsisa.web.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mabsisa.common.util.CommonConstant;
import com.mabsisa.common.util.CommonUtils;

/**
 * @author abhinab
 *
 */
@Controller
public class SecurityRouter {

	@GetMapping(CommonConstant.URL_LOGIN)
	public String login(@RequestParam(value = "error", required = false) String error, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			return "redirect:" + CommonConstant.URL_DEFAULT_SUCCESS;
		}
		if (error != null) {
			if (error.equalsIgnoreCase(CommonConstant.AUTH_CODE_LOGIN_DEACTIVE)) {
				model.addAttribute("msg", "Your account is deactivated");
			} else if (error.equalsIgnoreCase(CommonConstant.AUTH_CODE_LOGIN_DISABLED)) {
				model.addAttribute("msg", "Your account is disabled");
			} else if (error.equalsIgnoreCase(CommonConstant.AUTH_CODE_LOGIN_INVALID)) {
				model.addAttribute("msg", "Invalid username and/or password");
			}
		}
		return "login";
	}

	@GetMapping(CommonConstant.URL_DEFAULT_SUCCESS)
	public String loginSuccess(Model model) {
		model.addAttribute("access", CommonUtils.getLoggedInUserAccess());
		return "dashboard";
	}

}
