/**
 * 
 */
package com.mabsisa.dao.customer;

import java.util.List;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import com.mabsisa.common.model.CollectorCollection;
import com.mabsisa.common.model.CustomerAssignmentCollector;
import com.mabsisa.common.model.CustomerCollectionDetail;
import com.mabsisa.common.model.CustomerCollectionDetailAudit;
import com.mabsisa.common.model.CustomerPerRegion;
import com.mabsisa.common.model.RevenueByRegion;
import com.mabsisa.common.util.CommonConstant;

/**
 * @author abhinab
 *
 */
public interface CustomerCollectionDao {

	@Retryable(maxAttempts = CommonConstant.DB_RETRY_COUNT, value = DataAccessResourceFailureException.class, backoff = @Backoff(delay = CommonConstant.DB_RETRY_DELAY))
	CustomerCollectionDetail update(CustomerCollectionDetail customerCollectionDetail);
	
	@Retryable(maxAttempts = CommonConstant.DB_RETRY_COUNT, value = DataAccessResourceFailureException.class, backoff = @Backoff(delay = CommonConstant.DB_RETRY_DELAY))
	CustomerCollectionDetail update(CustomerCollectionDetail customerCollectionDetail, int month);
	
	@Retryable(maxAttempts = CommonConstant.DB_RETRY_COUNT, value = DataAccessResourceFailureException.class, backoff = @Backoff(delay = CommonConstant.DB_RETRY_DELAY))
	List<CustomerCollectionDetail> update(List<CustomerCollectionDetail> customerCollectionDetails);
	
	@Retryable(maxAttempts = CommonConstant.DB_RETRY_COUNT, value = DataAccessResourceFailureException.class, backoff = @Backoff(delay = CommonConstant.DB_RETRY_DELAY))
	List<CustomerCollectionDetail> findAll();
	
	@Retryable(maxAttempts = CommonConstant.DB_RETRY_COUNT, value = DataAccessResourceFailureException.class, backoff = @Backoff(delay = CommonConstant.DB_RETRY_DELAY))
	List<CustomerCollectionDetailAudit> findAllCustomerCollectionDetailAudit();
	
	@Retryable(maxAttempts = CommonConstant.DB_RETRY_COUNT, value = DataAccessResourceFailureException.class, backoff = @Backoff(delay = CommonConstant.DB_RETRY_DELAY))
	List<CustomerPerRegion> findAllCustomerPerRegion();
	
	@Retryable(maxAttempts = CommonConstant.DB_RETRY_COUNT, value = DataAccessResourceFailureException.class, backoff = @Backoff(delay = CommonConstant.DB_RETRY_DELAY))
	List<RevenueByRegion> findAllRevenueByRegion();
	
	@Retryable(maxAttempts = CommonConstant.DB_RETRY_COUNT, value = DataAccessResourceFailureException.class, backoff = @Backoff(delay = CommonConstant.DB_RETRY_DELAY))
	List<CustomerAssignmentCollector> findAllAssignmentByCollector();
	
	@Retryable(maxAttempts = CommonConstant.DB_RETRY_COUNT, value = DataAccessResourceFailureException.class, backoff = @Backoff(delay = CommonConstant.DB_RETRY_DELAY))
	List<CollectorCollection> findAllCollectionByCollector();
	
	@Retryable(maxAttempts = CommonConstant.DB_RETRY_COUNT, value = DataAccessResourceFailureException.class, backoff = @Backoff(delay = CommonConstant.DB_RETRY_DELAY))
	CustomerCollectionDetail findByCollectionId(long collectionId);

}
