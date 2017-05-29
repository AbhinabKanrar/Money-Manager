/**
 * 
 */
package com.mabsisa.service.customer;

import java.util.List;

import com.mabsisa.common.model.CustomerCollectionDetail;
import com.mabsisa.common.model.CustomerPerRegion;
import com.mabsisa.common.model.PaymentReceipt;

/**
 * @author abhinab
 *
 */
public interface CustomerCollectionService {

	CustomerCollectionDetail update(CustomerCollectionDetail customerCollectionDetail);
	CustomerCollectionDetail collect(CustomerCollectionDetail customerCollectionDetail);
	List<CustomerCollectionDetail> findAll();
	CustomerCollectionDetail findByCollectionId(long collectionId);
	List<CustomerCollectionDetail> findByCollectorName(String collectorName);
	CustomerCollectionDetail findCollectionByCollectionId(long collectionId);
	PaymentReceipt generatePayementReceipt(long collectionId);
	
}
