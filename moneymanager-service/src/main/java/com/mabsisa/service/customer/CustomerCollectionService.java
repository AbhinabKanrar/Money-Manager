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

	CustomerCollectionDetail update(CustomerCollectionDetail customerCollectionDetail);
	List<CustomerCollectionDetail> findAll();
	CustomerCollectionDetail findByCollectionId(long collectionId);
	List<CustomerCollectionDetail> findByCollectorName(String collectorName);
	CustomerCollectionDetail findCollectionByCollectionId(long collectionId);
	
}
