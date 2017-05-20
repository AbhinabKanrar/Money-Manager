/**
 * 
 */
package com.mabsisa.dao.user;

import java.util.List;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import com.mabsisa.common.model.User;
import com.mabsisa.common.util.CommonConstant;

/**
 * @author abhinab
 *
 */
public interface UserDao {

	@Retryable(maxAttempts=CommonConstant.DB_RETRY_COUNT,value=DataAccessResourceFailureException.class,backoff=@Backoff(delay = CommonConstant.DB_RETRY_DELAY))
	User save(User user);
	
	@Retryable(maxAttempts=CommonConstant.DB_RETRY_COUNT,value=DataAccessResourceFailureException.class,backoff=@Backoff(delay = CommonConstant.DB_RETRY_DELAY))
	User update(User user);
	
	@Retryable(maxAttempts=CommonConstant.DB_RETRY_COUNT,value=DataAccessResourceFailureException.class,backoff=@Backoff(delay = CommonConstant.DB_RETRY_DELAY))
	List<User> findAll();
	
	@Retryable(maxAttempts=CommonConstant.DB_RETRY_COUNT,value=DataAccessResourceFailureException.class,backoff=@Backoff(delay = CommonConstant.DB_RETRY_DELAY))
	User findUserByUserId(Long userId);
	
	@Retryable(maxAttempts=CommonConstant.DB_RETRY_COUNT,value=DataAccessResourceFailureException.class,backoff=@Backoff(delay = CommonConstant.DB_RETRY_DELAY))
	User findUserByUsername(String username);
	
	@Retryable(maxAttempts=CommonConstant.DB_RETRY_COUNT,value=DataAccessResourceFailureException.class,backoff=@Backoff(delay = CommonConstant.DB_RETRY_DELAY))
	void update(String username, String password);
	
}
