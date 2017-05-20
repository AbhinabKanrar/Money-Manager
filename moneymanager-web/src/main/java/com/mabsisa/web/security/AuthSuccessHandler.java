/**
 * 
 */
package com.mabsisa.web.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.mabsisa.common.model.UserStatus;
import com.mabsisa.common.util.CommonConstant;


/**
 * @author abhinab
 *
 */
@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.web.authentication.
	 * AuthenticationSuccessHandler#onAuthenticationSuccess(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * org.springframework.security.core.Authentication)
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {
		handle(request, response, authentication);
	}
	
	protected void handle(HttpServletRequest request, 
		      HttpServletResponse response, Authentication authentication) throws IOException {
		UserStatus userStatus = (UserStatus) WebUtils.getSessionAttribute(request, "userStatusToken");
		if (userStatus != null) {
			if (userStatus == UserStatus.ACTIVE) {
				redirectStrategy.sendRedirect(request, response, CommonConstant.URL_DEFAULT_SUCCESS);
			} else {
				request.getSession().invalidate();
				if (userStatus == UserStatus.DISABLED) {
					redirectStrategy.sendRedirect(request, response, CommonConstant.URL_LOGIN_ERROR_DISABLED);
				} else if (userStatus == UserStatus.DEACTIVE) {
					redirectStrategy.sendRedirect(request, response, CommonConstant.URL_LOGIN_ERROR_DEACTIVE);
				}
			}
		} else {
			request.getSession().invalidate();
			redirectStrategy.sendRedirect(request, response, CommonConstant.URL_LOGIN_ERROR_INVALID);
		}
	}

}
