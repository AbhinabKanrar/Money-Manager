/**
 * 
 */
package com.mabsisa.service.customer.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mabsisa.common.model.CustomerCollectionDetail;
import com.mabsisa.common.model.User;
import com.mabsisa.common.util.CommonUtils;
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
			if (allCustomerCollectionDetails != null && !allCustomerCollectionDetails.isEmpty()) {
				customerCollectionDetails = allCustomerCollectionDetails.stream().filter(
						customerCollectionDetail -> customerCollectionDetail.getCollectorId() == user.getUserId())
						.collect(Collectors.toList());
			}
		}
		return customerCollectionDetails;
	}

	@Override
	public CustomerCollectionDetail findCollectionByCollectionId(long collectionId) {
		CustomerCollectionDetail customerCollectionDetail = new CustomerCollectionDetail();
		customerCollectionDetail = customerCollectionDao.findByCollectionId(collectionId);
		if (customerCollectionDetail != null) {
			BigDecimal fee = customerCollectionDetail.getFee();
			BigDecimal janFee = customerCollectionDetail.getJanFee();
			BigDecimal febFee = customerCollectionDetail.getFebFee();
			BigDecimal marFee = customerCollectionDetail.getMarFee();
			BigDecimal aprFee = customerCollectionDetail.getAprFee();
			BigDecimal mayFee = customerCollectionDetail.getMayFee();
			BigDecimal junFee = customerCollectionDetail.getJunFee();
			BigDecimal julFee = customerCollectionDetail.getJulFee();
			BigDecimal augFee = customerCollectionDetail.getAugFee();
			BigDecimal sepFee = customerCollectionDetail.getSepFee();
			BigDecimal octFee = customerCollectionDetail.getOctFee();
			BigDecimal novFee = customerCollectionDetail.getNovFee();
			BigDecimal decFee = customerCollectionDetail.getDecFee();

			BigDecimal expectedFee = fee.multiply(new BigDecimal(CommonUtils.getCurrentMonth()));

			BigDecimal pastFee = getDueMonthlyFee(janFee, Calendar.JANUARY)
					.add(getDueMonthlyFee(febFee, Calendar.FEBRUARY))
					.add(getDueMonthlyFee(marFee, Calendar.MARCH))
					.add(getDueMonthlyFee(aprFee, Calendar.APRIL))
					.add(getDueMonthlyFee(mayFee, Calendar.MAY))
					.add(getDueMonthlyFee(junFee, Calendar.JUNE))
					.add(getDueMonthlyFee(julFee, Calendar.JULY))
					.add(getDueMonthlyFee(augFee, Calendar.AUGUST))
					.add(getDueMonthlyFee(sepFee, Calendar.SEPTEMBER))
					.add(getDueMonthlyFee(octFee, Calendar.OCTOBER))
					.add(getDueMonthlyFee(novFee, Calendar.NOVEMBER))
					.add(getDueMonthlyFee(decFee, Calendar.DECEMBER));

			customerCollectionDetail.setDue(expectedFee.subtract(pastFee));
			
			if (customerCollectionDetail.getDue().compareTo(BigDecimal.ZERO) < 0) {
				customerCollectionDetail.setDue(BigDecimal.ZERO);
			}
			
		}

		return customerCollectionDetail;
	}

	private BigDecimal getDueMonthlyFee(BigDecimal monthlyFee, int month) {
		if (month > (CommonUtils.getCurrentMonth() - 1)) {
			return BigDecimal.ZERO;
		}
		return monthlyFee;
	}

}
