/**
 * 
 */
package com.mabsisa.service.customer;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.mabsisa.common.model.Customer;

/**
 * @author abhinab
 *
 */
public interface CustomerService {

	Customer save(Customer customer);

	List<Customer> save(File file) throws InvalidFormatException, IOException;

	Customer update(Customer customer);
	
	Customer delete(Customer customer);

	List<Customer> findAll();

	Customer findByCustomerId(long customerId);

}
