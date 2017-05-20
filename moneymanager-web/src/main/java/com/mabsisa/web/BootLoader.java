/**
 * 
 */
package com.mabsisa.web;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.mabsisa.service.ApplicationServiceContext;

/**
 * @author abhinab
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.mabsisa.web")
@Import(ApplicationServiceContext.class)
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class, JmsAutoConfiguration.class })
public class BootLoader implements CommandLineRunner {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
	 */
	@Override
	public void run(String... args) throws Exception {

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(BootLoader.class, args);
	}

}
