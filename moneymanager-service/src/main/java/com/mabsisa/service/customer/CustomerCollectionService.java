/**
 * 
 */
package com.mabsisa.service.customer;

import java.util.List;

import com.mabsisa.common.model.CustomerCollectionDetail;

/**
 * @author abhinab
 *
 */
public interface CustomerCollectionService {

	List<CustomerCollectionDetail> findAll();
	
}
