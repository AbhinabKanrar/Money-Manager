package com.mabsisa.service.customer.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mabsisa.common.model.Customer;
import com.mabsisa.common.model.CustomerCollectionDetail;
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
		List<CustomerCollectionDetail> customerCollectionDetails = CommonUtils.generateCollectionDetails(file);
		List<Customer> customers = new ArrayList<Customer>();
		for (CustomerCollectionDetail customerCollectionDetail : customerCollectionDetails) {
			Customer customer = new Customer();
			customer.setCustomerId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
			customer.setRegion(customerCollectionDetail.getRegion());
			customer.setBuilding(customerCollectionDetail.getBuilding());
			customer.setAddress(customerCollectionDetail.getAddress());
			customer.setClient(customerCollectionDetail.getClient());
			customer.setName(customerCollectionDetail.getName());
			customer.setFloor(customerCollectionDetail.getFloor());
			customer.setFee(customerCollectionDetail.getFebFee());
			customer.setMahal(customerCollectionDetail.getMahal());
			customer.setTelephone(customerCollectionDetail.getTelephone());
			customer.setLeftTravel(customerCollectionDetail.getLeftTravel());
			customer.setNote(customerCollectionDetail.getNote());
			
			customerCollectionDetail.setCustomerId(customer.getCustomerId());
			
			customers.add(customer);
		}

		return customerDao.save(customers, customerCollectionDetails);
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
