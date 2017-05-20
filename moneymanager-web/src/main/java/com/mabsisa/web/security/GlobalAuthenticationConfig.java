/**
 * 
 */
package com.mabsisa.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import com.mabsisa.common.model.User;
import com.mabsisa.common.model.UserDetail;
import com.mabsisa.service.user.UserService;

/**
 * @author abhinab
 *
 */
@Configuration
public class GlobalAuthenticationConfig extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	private UserService userService;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
	}

	@Bean
	UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			User user;
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				user = userService.findUserByUsername(username);
				ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
						.currentRequestAttributes();
				if (user != null) {
					WebUtils.setSessionAttribute(attributes.getRequest(), "userStatusToken", user.getUserStatus());
					return new UserDetail(user.getUsername(), user.getPassword(), user.getRole().name());
				} else {
					throw new UsernameNotFoundException("user credential not found");
				}
			}
		};
	}

	private PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder(11);
		return encoder;
	}

}
