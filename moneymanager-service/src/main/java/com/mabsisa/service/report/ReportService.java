/**
 * 
 */
package com.mabsisa.service.report;

import java.util.List;
import java.util.Map;

import com.mabsisa.common.model.CollectorCollection;
import com.mabsisa.common.model.CustomerAssignmentCollector;
import com.mabsisa.common.model.CustomerCollectionDetailAudit;
import com.mabsisa.common.model.CustomerPerRegion;
import com.mabsisa.common.model.RevenueByRegion;

/**
 * @author abhinab
 *
 */
public interface ReportService {

	Map<String, Integer> findAssignmentRecord();
	
	Map<String, Integer> findPaidUnpaidInfo();
	
	List<CustomerCollectionDetailAudit> findAll();
	
	List<CustomerPerRegion> findAllCustomerPerRegion();

	List<RevenueByRegion> findAllRevenueByRegion();
	
	Map<String, Object> findPaidUnPaidCustomerPerMonth();
	
	List<CustomerAssignmentCollector> findAllAssignmentByCollector();
	
	List<CollectorCollection> findAllCollectionByCollector();
	
	List<CollectorCollection> findAllCollectionOfToday();
	
}
