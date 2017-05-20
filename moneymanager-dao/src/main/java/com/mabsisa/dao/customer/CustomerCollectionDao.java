/**
 * 
 */
package com.mabsisa.dao.customer;

import java.util.List;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import com.mabsisa.common.model.CustomerCollectionDetail;
import com.mabsisa.common.util.CommonConstant;

/**
 * @author abhinab
 *
 */
public interface CustomerCollectionDao {

	@Retryable(maxAttempts = CommonConstant.DB_RETRY_COUNT, value = DataAccessResourceFailureException.class, backoff = @Backoff(delay = CommonConstant.DB_RETRY_DELAY))
	List<CustomerCollectionDetail> findAll();

}