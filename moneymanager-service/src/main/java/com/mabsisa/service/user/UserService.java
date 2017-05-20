/**
 * 
 */
package com.mabsisa.service.user;

import java.util.List;

import com.mabsisa.common.model.User;

/**
 * @author abhinab
 *
 */
public interface UserService {

	User save(User user);
	
	User update(User user);
	
	List<User> findAll();
	
	User findUserByUserId(Long userId);
	
	User findUserByUsername(String username);
	
	void update(String username, String password);
	
}
