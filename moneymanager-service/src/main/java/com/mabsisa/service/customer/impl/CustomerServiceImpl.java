package com.mabsisa.service.customer.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mabsisa.common.model.Customer;
import com.mabsisa.common.util.CommonUtils;
import com.mabsisa.dao.customer.CustomerDao;
import com.mabsisa.service.customer.CustomerService;

/**
 * @author abhinab
 *
 */
@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerDao customerDao;

	@Override
	public Customer save(Customer customer) {
		return customerDao.save(customer);
	}

	@Override
	public List<Customer> save(File file) throws InvalidFormatException, IOException {
		List<Customer> customers = CommonUtils.generateCollectionDetails(file);
		return customerDao.save(customers);
	}

	@Override
	public Customer update(Customer customer) {
		return customerDao.update(customer);
	}

	@Override
	public Customer delete(Customer customer) {
		return customerDao.delete(customer);
	}

	@Override
	public List<Customer> findAll() {
		return customerDao.findAll();
	}

	@Override
	public Customer findByCustomerId(long customerId) {
		return customerDao.findByCustomerId(customerId);
	}

}
