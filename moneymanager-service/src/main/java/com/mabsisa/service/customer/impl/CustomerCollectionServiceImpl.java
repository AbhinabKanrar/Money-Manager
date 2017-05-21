/**
 * 
 */
package com.mabsisa.service.customer.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mabsisa.common.model.CustomerCollectionDetail;
import com.mabsisa.common.model.User;
import com.mabsisa.dao.customer.CustomerCollectionDao;
import com.mabsisa.service.customer.CustomerCollectionService;
import com.mabsisa.service.user.UserService;

/**
 * @author abhinab
 *
 */
@Service
public class CustomerCollectionServiceImpl implements CustomerCollectionService {

	@Autowired
	private CustomerCollectionDao customerCollectionDao;
	
	@Autowired
	private UserService userService;

	@Override
	public CustomerCollectionDetail update(CustomerCollectionDetail customerCollectionDetail) {
		return customerCollectionDao.update(customerCollectionDetail);
	}
	
	@Override
	public List<CustomerCollectionDetail> findAll() {
		return customerCollectionDao.findAll();
	}

	@Override
	public CustomerCollectionDetail findByCollectionId(long collectionId) {
		return customerCollectionDao.findByCollectionId(collectionId);
	}

	@Override
	public List<CustomerCollectionDetail> findByCollectorName(String collectorName) {
		User user = userService.findUserByUsername(collectorName);
		List<CustomerCollectionDetail> customerCollectionDetails = new ArrayList<CustomerCollectionDetail>();
		if (user != null) {
			List<CustomerCollectionDetail> allCustomerCollectionDetails = customerCollectionDao.findAll();
			customerCollectionDetails = allCustomerCollectionDetails.stream()
					.filter(customerCollectionDetail -> customerCollectionDetail.getCollectorId() == user.getUserId())
					.collect(Collectors.toList()); 
		}
		return customerCollectionDetails;
	}

	@Override
	public CustomerCollectionDetail findCollectionByCollectionId(long collectionId) {
		CustomerCollectionDetail customerCollectionDetail = new CustomerCollectionDetail();
		customerCollectionDetail = customerCollectionDao.findByCollectionId(collectionId);
		if (customerCollectionDetail != null) {
			BigDecimal fee  = customerCollectionDetail.getFee();
			BigDecimal janFee  = customerCollectionDetail.getJanFee();
			BigDecimal febFee  = customerCollectionDetail.getFebFee();
			BigDecimal marFee  = customerCollectionDetail.getMarFee();
			BigDecimal aprFee  = customerCollectionDetail.getAprFee();
			BigDecimal mayFee  = customerCollectionDetail.getMayFee();
			BigDecimal junFee  = customerCollectionDetail.getJunFee();
			BigDecimal julFee  = customerCollectionDetail.getJulFee();
			BigDecimal augFee  = customerCollectionDetail.getAugFee();
			BigDecimal sepFee  = customerCollectionDetail.getSepFee();
			BigDecimal octFee  = customerCollectionDetail.getOctFee();
			BigDecimal novFee  = customerCollectionDetail.getNovFee();
			BigDecimal decFee  = customerCollectionDetail.getDecFee();
			
			BigDecimal actualFee = getDueMonthlyFee(janFee, fee)
					.add(getDueMonthlyFee(febFee, fee))
					.add(getDueMonthlyFee(marFee, fee))
					.add(getDueMonthlyFee(aprFee, fee))
					.add(getDueMonthlyFee(mayFee, fee))
					.add(getDueMonthlyFee(junFee, fee))
					.add(getDueMonthlyFee(julFee, fee))
					.add(getDueMonthlyFee(augFee, fee))
					.add(getDueMonthlyFee(sepFee, fee))
					.add(getDueMonthlyFee(octFee, fee))
					.add(getDueMonthlyFee(novFee, fee))
					.add(getDueMonthlyFee(decFee, fee));
			
			customerCollectionDetail.setDue(actualFee);
		}
		
		return customerCollectionDetail;
	}
	
	private BigDecimal getDueMonthlyFee(BigDecimal monthlyFee, BigDecimal fee) {
		if (monthlyFee.compareTo(BigDecimal.ZERO) == 0) {
			return fee;
		} else if (monthlyFee.compareTo(fee) == -1) {
			return fee.subtract(monthlyFee);
		} else if (monthlyFee.compareTo(BigDecimal.ZERO) == 1) {
			return monthlyFee.subtract(fee);
		}
		return BigDecimal.ZERO;
	}

}
