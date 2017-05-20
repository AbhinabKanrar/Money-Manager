/**
 * 
 */
package com.mabsisa.dao.customer;

import java.util.List;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import com.mabsisa.common.model.Customer;
import com.mabsisa.common.model.CustomerCollectionDetail;
import com.mabsisa.common.util.CommonConstant;

/**
 * @author abhinab
 *
 */
public interface CustomerDao {

	@Retryable(maxAttempts = CommonConstant.DB_RETRY_COUNT, value = DataAccessResourceFailureException.class, backoff = @Backoff(delay = CommonConstant.DB_RETRY_DELAY))
	Customer save(Customer customer);
	
	@Retryable(maxAttempts = CommonConstant.DB_RETRY_COUNT, value = DataAccessResourceFailureException.class, backoff = @Backoff(delay = CommonConstant.DB_RETRY_DELAY))
	CustomerCollectionDetail save(CustomerCollectionDetail customerCollectionDetail);

	@Retryable(maxAttempts = CommonConstant.DB_RETRY_COUNT, value = DataAccessResourceFailureException.class, backoff = @Backoff(delay = CommonConstant.DB_RETRY_DELAY))
	List<Customer> save(List<Customer> customers);

	@Retryable(maxAttempts = CommonConstant.DB_RETRY_COUNT, value = DataAccessResourceFailureException.class, backoff = @Backoff(delay = CommonConstant.DB_RETRY_DELAY))
	Customer update(Customer customer);

	@Retryable(maxAttempts = CommonConstant.DB_RETRY_COUNT, value = DataAccessResourceFailureException.class, backoff = @Backoff(delay = CommonConstant.DB_RETRY_DELAY))
	Customer delete(Customer customer);

	@Retryable(maxAttempts = CommonConstant.DB_RETRY_COUNT, value = DataAccessResourceFailureException.class, backoff = @Backoff(delay = CommonConstant.DB_RETRY_DELAY))
	List<Customer> findAll();

	@Retryable(maxAttempts = CommonConstant.DB_RETRY_COUNT, value = DataAccessResourceFailureException.class, backoff = @Backoff(delay = CommonConstant.DB_RETRY_DELAY))
	Customer findByCustomerId(long customerId);

}
