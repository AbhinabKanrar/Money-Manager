/**
 * 
 */
package com.mabsisa.service.report.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mabsisa.common.model.CollectorCollection;
import com.mabsisa.common.model.CustomerAssignmentCollector;
import com.mabsisa.common.model.CustomerCollectionDetail;
import com.mabsisa.common.model.CustomerCollectionDetailAudit;
import com.mabsisa.common.model.CustomerPerRegion;
import com.mabsisa.common.model.RevenueByRegion;
import com.mabsisa.common.util.CommonConstant;
import com.mabsisa.common.util.CommonUtils;
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
		int SIZE  = 0;
		Map<String, Integer> records = new HashMap<String, Integer>();
		List<CustomerCollectionDetail> customerCollectionDetails = customerCollectionDao.findAll();
		if (customerCollectionDetails != null && !customerCollectionDetails.isEmpty()) {
			SIZE = customerCollectionDetails.size();
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

	@Override
	public List<CustomerPerRegion> findAllCustomerPerRegion() {
		return customerCollectionDao.findAllCustomerPerRegion();
	}

	@Override
	public List<RevenueByRegion> findAllRevenueByRegion() {
		return customerCollectionDao.findAllRevenueByRegion();
	}

	@Override
	public Map<String, Object> findPaidUnPaidCustomerPerMonth() {
		int janPaid = 0;
		int janUnPaid = 0;
		int febPaid = 0;
		int febUnPaid = 0;
		int marPaid = 0;
		int marUnPaid = 0;
		int aprPaid = 0;
		int aprUnPaid = 0;
		int mayPaid = 0;
		int mayUnPaid = 0;
		int junPaid = 0;
		int junUnPaid = 0;
		int julPaid = 0;
		int julUnPaid = 0;
		int augPaid = 0;
		int augUnPaid = 0;
		int sepPaid = 0;
		int sepUnPaid = 0;
		int octPaid = 0;
		int octUnPaid = 0;
		int novPaid = 0;
		int novUnPaid = 0;
		int decPaid = 0;
		int decUnPaid = 0;
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Map<String, Integer>> result = new HashMap<String, Map<String, Integer>>(); 
		List<CustomerCollectionDetail> customerCollectionDetails = customerCollectionDao.findAll();
		
		if (customerCollectionDetails != null && !customerCollectionDetails.isEmpty()) {
			for (CustomerCollectionDetail customerCollectionDetail : customerCollectionDetails) {
				result.put(customerCollectionDetail.getRegion(), null);
			}
			for (CustomerCollectionDetail customerCollectionDetail : customerCollectionDetails) {
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
				
				if (fee.subtract(janFee).compareTo(BigDecimal.ZERO) >= 0) {
					janPaid++;
				} else {
					janUnPaid++;
				}
				
				if (fee.subtract(febFee).compareTo(BigDecimal.ZERO) >= 0) {
					febPaid++;
				} else {
					febUnPaid++;
				}
				
				if (fee.subtract(marFee).compareTo(BigDecimal.ZERO) >= 0) {
					marPaid++;
				} else {
					marUnPaid++;
				}
				
				if (fee.subtract(aprFee).compareTo(BigDecimal.ZERO) >= 0) {
					aprPaid++;
				} else {
					aprUnPaid++;
				}
				
				if (fee.subtract(mayFee).compareTo(BigDecimal.ZERO) >= 0) {
					mayPaid++;
				} else {
					mayUnPaid++;
				}
				if (fee.subtract(junFee).compareTo(BigDecimal.ZERO) >= 0) {
					junPaid++;
				} else {
					junUnPaid++;
				}
				
				if (fee.subtract(julFee).compareTo(BigDecimal.ZERO) >= 0) {
					julPaid++;
				} else {
					julUnPaid++;
				}
				
				if (fee.subtract(augFee).compareTo(BigDecimal.ZERO) >= 0) {
					augPaid++;
				} else {
					augUnPaid++;
				}
				
				if (fee.subtract(sepFee).compareTo(BigDecimal.ZERO) >= 0) {
					sepPaid++;
				} else {
					sepUnPaid++;
				}
				
				if (fee.subtract(octFee).compareTo(BigDecimal.ZERO) >= 0) {
					octPaid++;
				} else {
					octUnPaid++;
				}
				
				if (fee.subtract(novFee).compareTo(BigDecimal.ZERO) >= 0) {
					novPaid++;
				} else {
					novUnPaid++;
				}
				
				if (fee.subtract(decFee).compareTo(BigDecimal.ZERO) >= 0) {
					decPaid++;
				} else {
					decUnPaid++;
				}
				
				
				
//				BigDecimal expectedFee = fee.multiply(new BigDecimal(CommonUtils.getCurrentMonth()));
//				
//				BigDecimal pastFee = getDueMonthlyFee(janFee, Calendar.JANUARY)
//						.add(getDueMonthlyFee(febFee, Calendar.FEBRUARY)).add(getDueMonthlyFee(marFee, Calendar.MARCH))
//						.add(getDueMonthlyFee(aprFee, Calendar.APRIL)).add(getDueMonthlyFee(mayFee, Calendar.MAY))
//						.add(getDueMonthlyFee(junFee, Calendar.JUNE)).add(getDueMonthlyFee(julFee, Calendar.JULY))
//						.add(getDueMonthlyFee(augFee, Calendar.AUGUST)).add(getDueMonthlyFee(sepFee, Calendar.SEPTEMBER))
//						.add(getDueMonthlyFee(octFee, Calendar.OCTOBER)).add(getDueMonthlyFee(novFee, Calendar.NOVEMBER))
//						.add(getDueMonthlyFee(decFee, Calendar.DECEMBER));
//				
//				customerCollectionDetail.setDue(expectedFee.subtract(pastFee));
//				
//				if (customerCollectionDetail.getDue().compareTo(BigDecimal.ZERO) >= 0) {
//					unpaidCustomerNumber++;
//				} else {
//					paidCustomerNumber++;
//				}
				
			}
//			data.put(CommonConstant.KEY_TOTAL_PAID_CUSTOMER, paidCustomerNumber);
//			data.put(CommonConstant.KEY_TOTAL_UNPAID_CUSTOMER, unpaidCustomerNumber);
			data.put(CommonConstant.KEY_TOTAL_CUSTOMER, customerCollectionDetails.size());
		}
		return data;
	}

	private BigDecimal getDueMonthlyFee(BigDecimal monthlyFee, int month) {
		if (month > (CommonUtils.getCurrentMonth() - 1)) {
			return BigDecimal.ZERO;
		}
		return monthlyFee;
	}

	@Override
	public List<CustomerAssignmentCollector> findAllAssignmentByCollector() {
		return customerCollectionDao.findAllAssignmentByCollector();
	}

	@Override
	public List<CollectorCollection> findAllCollectionByCollector() {
		return customerCollectionDao.findAllCollectionByCollector();
	}
	
}
