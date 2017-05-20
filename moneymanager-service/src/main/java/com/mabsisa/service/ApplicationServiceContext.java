/**
 * 
 */
package com.mabsisa.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mabsisa.dao.ApplicationDaoContext;
//import com.mabsisa.service.security.AuthenticationService;
//import com.mabsisa.service.security.impl.AuthenticationServiceImpl;

/**
 * @author abhinab
 *
 */
@Configuration
@ComponentScan(basePackages="com.mabsisa.service")
@Import(ApplicationDaoContext.class)
public class ApplicationServiceContext {
	
//	@Bean
//	public AuthenticationService authenticationService() {
//		return new AuthenticationServiceImpl();
//	}
	
}
