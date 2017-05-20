/**
 * 
 */
package com.mabsisa.dao.user.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.mabsisa.common.model.Role;
import com.mabsisa.common.model.User;
import com.mabsisa.common.model.UserStatus;
import com.mabsisa.dao.user.UserDao;

/**
 * @author abhinab
 *
 */
@Repository
@EnableRetry
public class UserDaoImpl implements UserDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate jdbcNTemplate;

	private static final String INSERT_SQL = "INSERT INTO mm.user_auth_detail ("
			+ "user_id,username,password,role,mail,phone_number,user_status" + ") VALUES ("
			+ ":user_id,:username,:password,:role,:mail,:phone_number,:user_status" + ")";
	private static final String RETRIEVE_SQL_BY_USERNAME = "SELECT * FROM mm.user_auth_detail where username =:username";
	private static final String RETRIEVE_SQL = "select * from mm.user_auth_detail";
	private static final String RETRIEVE_SQL_BY_ID = "select * from mm.user_auth_detail where user_id =:user_id";
	private static final String UPDATE_SQL = "select * from mm.user_auth_detail where user_id = ? for update";
	private static final String DELETE_SQL = "delete from mm.user_auth_detail where user_id = ?";

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public User save(User user) {
		Map<String, Object> params = new HashMap<>(7);
		user.setUserId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
		params.put("user_id", user.getUserId());
		params.put("username", user.getUsername());
		params.put("password", user.getPassword());
		params.put("role", user.getRole().name());
		params.put("mail", user.getMail());
		params.put("phone_number", user.getPhoneNumber());
		params.put("user_status", user.getUserStatus().name());
		
		jdbcNTemplate.update(INSERT_SQL, params);
		
		return user;
	}
	
	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void update(String username, String password) {
		
	}
	
	@Override
	public User update(User user) {
		PreparedStatementCreatorFactory queryFactory = new PreparedStatementCreatorFactory(UPDATE_SQL,
				Arrays.asList(new SqlParameter(Types.BIGINT)));
		queryFactory.setUpdatableResults(true);
		queryFactory.setResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE);
		PreparedStatementCreator psc = 
				queryFactory.newPreparedStatementCreator(
	            new Object[] {user.getUserId()});
		final User oldUser = new User();
		RowCallbackHandler rowHandler = new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				oldUser.setUserId(rs.getLong("user_id"));
				oldUser.setUsername(rs.getString("username"));
				oldUser.setRole(Role.valueOf(rs.getString("role")));
				oldUser.setMail(rs.getString("mail"));
				oldUser.setPhoneNumber(rs.getString("phone_number"));
				oldUser.setUserStatus(UserStatus.valueOf(rs.getString("user_status")));
				
				rs.updateString("username", user.getUsername());
				rs.updateString("role", user.getRole().name());
				rs.updateString("mail", user.getMail());
				rs.updateString("phone_number", user.getPhoneNumber());
				rs.updateString("user_status", user.getUserStatus().name());
				
				rs.updateRow();
			}
		};
		
		jdbcTemplate.query(psc, rowHandler);
		
		return user;
	}
	
	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	public User findUserByUsername(String username) {
		Map<String, Object> param = new HashMap<>(1);
		param.put("username", username);

		User user = jdbcNTemplate.query(RETRIEVE_SQL_BY_USERNAME, param, new ResultSetExtractor<User>() {
			@Override
			public User extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					User user = new User();
					user.setUserId(rs.getLong("user_id"));
					user.setUsername(rs.getString("username"));
					user.setPassword(rs.getString("password"));
					user.setRole(Role.valueOf(rs.getString("role")));
					user.setMail(rs.getString("mail"));
					user.setPhoneNumber(rs.getString("phone_number"));
					user.setUserStatus(UserStatus.valueOf(rs.getString("user_status")));
					return user;
				} else {
					return null;
				}
			}
		});

		return user;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	public List<User> findAll() {
		List<User> users = jdbcTemplate.query(RETRIEVE_SQL, new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				final User user = new User();
				user.setUserId(rs.getLong("user_id"));
				user.setUsername(rs.getString("username"));
				user.setRole(Role.valueOf(rs.getString("role")));
				user.setMail(rs.getString("mail"));
				user.setPhoneNumber(rs.getString("phone_number"));
				user.setUserStatus(UserStatus.valueOf(rs.getString("user_status")));
				return user;
			}
		});

		if (users == null || users.isEmpty()) {
			return null;
		}

		return users;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	public User findUserByUserId(Long userId) {
		Map<String, Object> param = new HashMap<>(1);
		param.put("user_id", userId);
		User user = jdbcNTemplate.queryForObject(RETRIEVE_SQL_BY_ID, param, new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User();
				user.setUserId(rs.getLong("user_id"));
				user.setUsername(rs.getString("username"));
				user.setRole(Role.valueOf(rs.getString("role")));
				user.setMail(rs.getString("mail"));
				user.setPhoneNumber(rs.getString("phone_number"));
				user.setUserStatus(UserStatus.valueOf(rs.getString("user_status")));
				return user;
			}
		});
		return user;
	}

}
