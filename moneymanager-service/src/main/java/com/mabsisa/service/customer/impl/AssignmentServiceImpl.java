/**
 * 
 */
package com.mabsisa.service.customer.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mabsisa.common.model.CustomerCollectionDetail;
import com.mabsisa.common.model.SearchParam;
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
						if (customerCollectionDetail.getCustomerId() == Long.valueOf(customerId)) {
							customerCollectionDetail.setCollectorId(collectorId);
							selectedCustomerCollectionDetails.add(customerCollectionDetail);
						}
					}
				}
			}
			customerCollectionDao.update(selectedCustomerCollectionDetails);
			flag = true;
		}
		
		return flag;
	}

}
