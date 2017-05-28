/**
 * 
 */
package com.mabsisa.service.report.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mabsisa.common.model.CustomerCollectionDetail;
import com.mabsisa.common.model.CustomerCollectionDetailAudit;
import com.mabsisa.common.util.CommonConstant;
import com.mabsisa.dao.customer.CustomerCollectionDao;
import com.mabsisa.service.report.ReportService;

/**
 * @author abhinab
 *
 */
@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private CustomerCollectionDao customerCollectionDao;

	@Override
	public Map<String, Integer> findAssignmentRecord() {
		Map<String, Integer> records = new HashMap<String, Integer>();
		List<CustomerCollectionDetail> customerCollectionDetails = customerCollectionDao.findAll();
		int SIZE = customerCollectionDetails.size();
		if (SIZE > 0) {
			records.put(CommonConstant.KEY_TOTAL_CUSTOMER, SIZE);
			records.put(CommonConstant.KEY_TOTAL_ASSIGNED_CUSTOMER, (int) customerCollectionDetails.stream()
					.filter(customerCollectionDetail -> customerCollectionDetail.getCollectorId() != 0).count());
			records.put(CommonConstant.KEY_TOTAL_UNASSIGNED_CUSTOMER, (int) customerCollectionDetails.stream()
					.filter(customerCollectionDetail -> customerCollectionDetail.getCollectorId() == 0).count());
		} else {
			records.put(CommonConstant.KEY_TOTAL_CUSTOMER, 0);
			records.put(CommonConstant.KEY_TOTAL_ASSIGNED_CUSTOMER, 0);
			records.put(CommonConstant.KEY_TOTAL_UNASSIGNED_CUSTOMER, 0);
		}
		return records;
	}

	@Override
	public List<CustomerCollectionDetailAudit> findAll() {
		return customerCollectionDao.findAllCustomerCollectionDetailAudit();
	}

}
