package com.mabsisa.service.customer;

import com.mabsisa.common.model.SearchParam;

public interface AssignmentService {

	boolean assignCustomer(String[] selectors, long collectorId, SearchParam searchParam);
	
}
