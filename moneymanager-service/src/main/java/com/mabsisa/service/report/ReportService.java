/**
 * 
 */
package com.mabsisa.service.report;

import java.util.List;
import java.util.Map;

import com.mabsisa.common.model.CustomerCollectionDetailAudit;

/**
 * @author abhinab
 *
 */
public interface ReportService {

	Map<String, Integer> findAssignmentRecord();
	
	List<CustomerCollectionDetailAudit> findAll();
	
}
