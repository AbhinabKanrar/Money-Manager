/**
 * 
 */
package com.mabsisa.web;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.mabsisa.common.model.Role;
import com.mabsisa.common.model.User;
import com.mabsisa.common.model.UserStatus;
import com.mabsisa.service.ApplicationServiceContext;
import com.mabsisa.service.user.UserService;

/**
 * @author abhinab
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.mabsisa.web")
@Import(ApplicationServiceContext.class)
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class, JmsAutoConfiguration.class })
public class BootLoader implements CommandLineRunner {

	@Autowired
	private UserService userService;
	
	@Override
	public void run(String... args) throws Exception {
		User user = new User();
		user.setUserId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
		user.setUsername("admin");
		user.setPassword("$2a$10$trT3.R/Nfey62eczbKEnueTcIbJXW.u1ffAo/XfyLpofwNDbEB86O");
		user.setRole(Role.ADMIN);
		user.setMail("abhinabkanrar@gmail.com");
		user.setPhoneNumber("+919836334099");
		user.setUserStatus(UserStatus.ACTIVE);
		try {
			user = userService.save(user);
		} catch(Exception e) {}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(BootLoader.class, args);
	}

}
