/**
 * 
 */
package com.mabsisa.service.user.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mabsisa.common.model.User;
import com.mabsisa.dao.user.UserDao;
import com.mabsisa.service.user.UserService;

/**
 * @author abhinab
 *
 */
@Service
public class UserServiceImpl implements UserService {

	private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public User save(User user) {
		return userDao.save(generatePasswordEncodeduser(user));
	}

	@Override
	public User update(User user) {
		return userDao.update(user);
	}

	@Override
	public void update(String username, String password) {
		userDao.update(username, password);
	}
	
	@Override
	public List<User> findAll() {
		return userDao.findAll();
	}

	@Override
	public User findUserByUserId(Long userId) {
		return userDao.findUserByUserId(userId);
	}

	
	@Override
	public User findUserByUsername(String username) {
		return userDao.findUserByUsername(username);
	}

	private User generatePasswordEncodeduser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return user;
	}
	
}
