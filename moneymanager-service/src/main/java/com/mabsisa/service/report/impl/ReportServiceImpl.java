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

	private int paidCounter = 0;
	private int unpaidCounter = 0;
	
	@Autowired
	private CustomerCollectionDao customerCollectionDao;

	@Override
	public Map<String, Integer> findAssignmentRecord() {
		int SIZE  = 0;
		paidCounter = 0;
		unpaidCounter = 0;
		Map<String, Integer> records = new HashMap<String, Integer>();
		List<CustomerCollectionDetail> customerCollectionDetails = customerCollectionDao.findAll();
		if (customerCollectionDetails != null && !customerCollectionDetails.isEmpty()) {
			SIZE = customerCollectionDetails.size();
			records.put(CommonConstant.KEY_TOTAL_CUSTOMER, SIZE);
			records.put(CommonConstant.KEY_TOTAL_ASSIGNED_CUSTOMER, (int) customerCollectionDetails.stream()
					.filter(customerCollectionDetail -> customerCollectionDetail.getCollectorId() != 0).count());
			records.put(CommonConstant.KEY_TOTAL_UNASSIGNED_CUSTOMER, (int) customerCollectionDetails.stream()
					.filter(customerCollectionDetail -> customerCollectionDetail.getCollectorId() == 0).count());
			customerCollectionDetails.forEach(customerCollectionDetail -> {
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
				
				if (expectedFee.compareTo(pastFee) > 0) {
					unpaidCounter++;
				} else {
					paidCounter++;
				}
				
			});
			records.put(CommonConstant.KEY_TOTAL_PAID_CUSTOMER, paidCounter);
			records.put(CommonConstant.KEY_TOTAL_UNPAID_CUSTOMER, unpaidCounter);
		} else {
			records.put(CommonConstant.KEY_TOTAL_CUSTOMER, 0);
			records.put(CommonConstant.KEY_TOTAL_ASSIGNED_CUSTOMER, 0);
			records.put(CommonConstant.KEY_TOTAL_UNASSIGNED_CUSTOMER, 0);
			records.put(CommonConstant.KEY_TOTAL_PAID_CUSTOMER, 0);
			records.put(CommonConstant.KEY_TOTAL_UNPAID_CUSTOMER, 0);
		}
		return records;
	}

	@Override
	public List<CustomerCollectionDetailAudit> findAllCustomerCollectionDetailAudit() {
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

//	@Override
//	public Map<String, Object> findPaidUnPaidCustomerPerMonth() {
//		Map<String, Object> data = new HashMap<String, Object>();
//		Map<String, Map<String, Integer>> result = new HashMap<String, Map<String, Integer>>(); 
//		List<CustomerCollectionDetail> customerCollectionDetails = customerCollectionDao.findAll();
//		
//		if (customerCollectionDetails != null && !customerCollectionDetails.isEmpty()) {
//			for (CustomerCollectionDetail customerCollectionDetail : customerCollectionDetails) {
//				result.put(customerCollectionDetail.getRegion(), null);
//			}
//			for (CustomerCollectionDetail customerCollectionDetail : customerCollectionDetails) {
//				BigDecimal fee = customerCollectionDetail.getFee();
//				BigDecimal janFee = customerCollectionDetail.getJanFee();
//				BigDecimal febFee = customerCollectionDetail.getFebFee();
//				BigDecimal marFee = customerCollectionDetail.getMarFee();
//				BigDecimal aprFee = customerCollectionDetail.getAprFee();
//				BigDecimal mayFee = customerCollectionDetail.getMayFee();
//				BigDecimal junFee = customerCollectionDetail.getJunFee();
//				BigDecimal julFee = customerCollectionDetail.getJulFee();
//				BigDecimal augFee = customerCollectionDetail.getAugFee();
//				BigDecimal sepFee = customerCollectionDetail.getSepFee();
//				BigDecimal octFee = customerCollectionDetail.getOctFee();
//				BigDecimal novFee = customerCollectionDetail.getNovFee();
//				BigDecimal decFee = customerCollectionDetail.getDecFee();
//				
//				if (fee.subtract(janFee).compareTo(BigDecimal.ZERO) >= 0) {
//				} else {
//				}
//				
//				if (fee.subtract(febFee).compareTo(BigDecimal.ZERO) >= 0) {
//				} else {
//				}
//				
//				if (fee.subtract(marFee).compareTo(BigDecimal.ZERO) >= 0) {
//				} else {
//				}
//				
//				if (fee.subtract(aprFee).compareTo(BigDecimal.ZERO) >= 0) {
//				} else {
//				}
//				
//				if (fee.subtract(mayFee).compareTo(BigDecimal.ZERO) >= 0) {
//				} else {
//				}
//				if (fee.subtract(junFee).compareTo(BigDecimal.ZERO) >= 0) {
//				} else {
//				}
//				
//				if (fee.subtract(julFee).compareTo(BigDecimal.ZERO) >= 0) {
//				} else {
//				}
//				
//				if (fee.subtract(augFee).compareTo(BigDecimal.ZERO) >= 0) {
//				} else {
//				}
//				
//				if (fee.subtract(sepFee).compareTo(BigDecimal.ZERO) >= 0) {
//				} else {
//				}
//				
//				if (fee.subtract(octFee).compareTo(BigDecimal.ZERO) >= 0) {
//				} else {
//				}
//				
//				if (fee.subtract(novFee).compareTo(BigDecimal.ZERO) >= 0) {
//				} else {
//				}
//				
//				if (fee.subtract(decFee).compareTo(BigDecimal.ZERO) >= 0) {
//				} else {
//				}
//				
//				
//				
////				BigDecimal expectedFee = fee.multiply(new BigDecimal(CommonUtils.getCurrentMonth()));
////				
////				BigDecimal pastFee = getDueMonthlyFee(janFee, Calendar.JANUARY)
////						.add(getDueMonthlyFee(febFee, Calendar.FEBRUARY)).add(getDueMonthlyFee(marFee, Calendar.MARCH))
////						.add(getDueMonthlyFee(aprFee, Calendar.APRIL)).add(getDueMonthlyFee(mayFee, Calendar.MAY))
////						.add(getDueMonthlyFee(junFee, Calendar.JUNE)).add(getDueMonthlyFee(julFee, Calendar.JULY))
////						.add(getDueMonthlyFee(augFee, Calendar.AUGUST)).add(getDueMonthlyFee(sepFee, Calendar.SEPTEMBER))
////						.add(getDueMonthlyFee(octFee, Calendar.OCTOBER)).add(getDueMonthlyFee(novFee, Calendar.NOVEMBER))
////						.add(getDueMonthlyFee(decFee, Calendar.DECEMBER));
////				
////				customerCollectionDetail.setDue(expectedFee.subtract(pastFee));
////				
////				if (customerCollectionDetail.getDue().compareTo(BigDecimal.ZERO) >= 0) {
////					unpaidCustomerNumber++;
////				} else {
////					paidCustomerNumber++;
////				}
//				
//			}
////			data.put(CommonConstant.KEY_TOTAL_PAID_CUSTOMER, paidCustomerNumber);
////			data.put(CommonConstant.KEY_TOTAL_UNPAID_CUSTOMER, unpaidCustomerNumber);
//			data.put(CommonConstant.KEY_TOTAL_CUSTOMER, customerCollectionDetails.size());
//		}
//		return data;
//	}

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

	@Override
	public List<CollectorCollection> findAllCollectionOfToday() {
		return customerCollectionDao.findAllCollectionOfToday();
	}

	@Override
	public Map<String, Integer> findPaidUnpaidInfo() {
		Map<String, Integer> records = new HashMap<String, Integer>();
		List<CustomerCollectionDetail> customerCollectionDetails = customerCollectionDao.findAll();
		if (customerCollectionDetails != null && !customerCollectionDetails.isEmpty()) {
			int month = CommonUtils.getCurrentMonth() - 1;
			for (CustomerCollectionDetail customerCollectionDetail : customerCollectionDetails) {
				if (month == Calendar.JANUARY) {
					if (customerCollectionDetail.getFee().compareTo(customerCollectionDetail.getJanFee()) > 0) {
						
					}
				}
				records.put(CommonConstant.KEY_TOTAL_CUSTOMER, customerCollectionDetails.size());
			}
		} else {
			records.put(CommonConstant.KEY_TOTAL_CUSTOMER, customerCollectionDetails.size());
		}
		return records;
	}
	
}
