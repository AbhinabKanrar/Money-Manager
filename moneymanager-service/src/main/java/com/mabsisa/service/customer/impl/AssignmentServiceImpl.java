/**
 * 
 */
package com.mabsisa.service.customer.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mabsisa.common.model.CustomerCollectionDetail;
import com.mabsisa.common.model.SearchParam;
import com.mabsisa.common.util.CommonUtils;
import com.mabsisa.dao.customer.CustomerCollectionDao;
import com.mabsisa.service.customer.AssignmentService;

/**
 * @author abhinab
 *
 */
@Service
public class AssignmentServiceImpl implements AssignmentService {

	@Autowired
	private CustomerCollectionDao customerCollectionDao;
	
	@Override
	public boolean assignCustomer(String[] selectors, long collectorId, SearchParam searchParam) {
		boolean flag = false;
		
		List<CustomerCollectionDetail> customerCollectionDetails = customerCollectionDao.findAll();
		List<CustomerCollectionDetail> selectedCustomerCollectionDetails = new ArrayList<CustomerCollectionDetail>(customerCollectionDetails.size());
		
		if (customerCollectionDetails != null && !customerCollectionDetails.isEmpty()) {
			if (searchParam == SearchParam.REGION) {
				List<String> regions = Arrays.asList(selectors);
				for (String region : regions) {
					for(CustomerCollectionDetail customerCollectionDetail : customerCollectionDetails) {
						if (customerCollectionDetail.getRegion().equalsIgnoreCase(region)) {
							customerCollectionDetail.setCollectorId(collectorId);
							selectedCustomerCollectionDetails.add(customerCollectionDetail);
						}
					}
				}
			} else if (searchParam == SearchParam.BUILDING) {
				List<String> buildingRegions = Arrays.asList(selectors);
				for (String buildingRegion : buildingRegions) {
					String[] buildingRegionArray= buildingRegion.split(",");
					String building = buildingRegionArray[0];
					String region = buildingRegionArray[1];
					for(CustomerCollectionDetail customerCollectionDetail : customerCollectionDetails) {
						if (customerCollectionDetail.getBuilding().equals(building) && 
								customerCollectionDetail.getRegion().equalsIgnoreCase(region)) {
							customerCollectionDetail.setCollectorId(collectorId);
							selectedCustomerCollectionDetails.add(customerCollectionDetail);
						}
					}
				}
			} else if (searchParam == SearchParam.MANUAL) {
				List<String> customerIds = Arrays.asList(selectors);
				for (String customerId : customerIds) {
					for(CustomerCollectionDetail customerCollectionDetail : customerCollectionDetails) {
						if (customerCollectionDetail.getCustomerId().equals(Long.valueOf(customerId))) {
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
							
							if (customerCollectionDetail.getDue().compareTo(BigDecimal.ZERO) >= 0) {
								customerCollectionDetail.setCollectorId(collectorId);
								selectedCustomerCollectionDetails.add(customerCollectionDetail);
							}
							
						}
					}
				}
			}
			customerCollectionDao.update(selectedCustomerCollectionDetails);
			flag = true;
		}
		
		return flag;
	}

	private BigDecimal getDueMonthlyFee(BigDecimal monthlyFee, int month) {
		if (month > (CommonUtils.getCurrentMonth() - 1)) {
			return BigDecimal.ZERO;
		}
		return monthlyFee;
	}
	
}
