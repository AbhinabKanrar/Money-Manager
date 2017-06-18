package com.mabsisa.dao.customer.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.mabsisa.common.model.Customer;
import com.mabsisa.common.model.CustomerCollectionDetail;
import com.mabsisa.dao.customer.CustomerDao;

/**
 * @author abhinab
 *
 */
@Repository
@EnableRetry
public class CustomerDaoImpl implements CustomerDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate jdbcNTemplate;

	private static final String INSERT_SQL = "INSERT INTO mm.customer_detail ("
			+ "customer_id,region,building,address,client,name,floor,fee,mahal,telephone,left_travel,note"
			+ ") VALUES ("
			+ ":customer_id, :region, :building, :address, :client, :name, :floor, :fee, :mahal, :telephone, :left_travel, :note"
			+ ")";
	private static final String INSERT_COLLECTION_SQL = "INSERT INTO mm.customer_collection_detail ("
			+ "collection_id,customer_id,collector_id,jan_fee,feb_fee,mar_fee,apr_fee,may_fee,jun_fee,jul_fee,aug_fee,sep_fee,oct_fee,nov_fee,dec_fee"
			+ ") VALUES ("
			+ ":collection_id,:customer_id,:collector_id,:jan_fee,:feb_fee,:mar_fee,:apr_fee,:may_fee,:jun_fee,:jul_fee,:aug_fee,:sep_fee,:oct_fee,:nov_fee,:dec_fee"
			+ ")";
	private static final String RETRIEVE_SQL = "SELECT * FROM mm.customer_detail";
	private static final String RETRIEVE_SQL_BY_ID = "select * from mm.customer_detail where customer_id =:customer_id";
	private static final String UPDATE_SQL = "select * from mm.customer_detail where customer_id = ? for update";
	private static final String DELETE_SQL = "delete from mm.customer_detail where customer_id = ?";

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public Customer save(Customer customer) {

		Map<String, Object> params = new HashMap<>(13);

		customer.setCustomerId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);

		params.put("customer_id", customer.getCustomerId());
		params.put("region", customer.getRegion());
		params.put("building", customer.getBuilding());
		params.put("address", customer.getAddress());
		params.put("client", customer.getClient());
		params.put("name", customer.getName());
		params.put("floor", customer.getFloor());
		params.put("fee", customer.getFee());
		params.put("mahal", customer.getMahal());
		params.put("telephone", customer.getTelephone());
		params.put("left_travel", customer.getLeftTravel());
		params.put("note", customer.getNote());

		jdbcNTemplate.update(INSERT_SQL, params);

		 CustomerCollectionDetail customerCollectionDetail = new
		 CustomerCollectionDetail();
		 customerCollectionDetail.setCustomerId(customer.getCustomerId());
		 saveCustomerToCollectionMapping(customerCollectionDetail);

		return customer;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public List<Customer> save(List<Customer> customers, List<CustomerCollectionDetail> customerCollectionDetails) {

		int counter = 0;

		@SuppressWarnings("unchecked")
		Map<String, Object>[] params = new Map[customers.size()];
//		List<CustomerCollectionDetail> customerCollectionDetails = new ArrayList<CustomerCollectionDetail>();
//		CustomerCollectionDetail customerCollectionDetail;

		for (Customer customer : customers) {
			Map<String, Object> param = new HashMap<String, Object>(13);

//			customer.setCustomerId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);

			param.put("customer_id", customer.getCustomerId());
			param.put("region", customer.getRegion());
			param.put("building", customer.getBuilding());
			param.put("address", customer.getAddress());
			param.put("client", customer.getClient());
			param.put("name", customer.getName());
			param.put("floor", customer.getFloor());
			param.put("fee", customer.getFee());
			param.put("mahal", customer.getMahal());
			param.put("telephone", customer.getTelephone());
			param.put("left_travel", customer.getLeftTravel());
			param.put("note", customer.getNote());

//			customerCollectionDetail = new CustomerCollectionDetail();
//			customerCollectionDetail.setCustomerId(customer.getCustomerId());
//			customerCollectionDetails.add(customerCollectionDetail);

			params[counter] = param;
			counter++;
		}

		jdbcNTemplate.batchUpdate(INSERT_SQL, params);

		saveCustomerToCollectionMapping(customerCollectionDetails);

		return customers;
	}

	public CustomerCollectionDetail saveCustomerToCollectionMapping(CustomerCollectionDetail customerCollectionDetail) {

		Map<String, Object> param = new HashMap<>(15);

		customerCollectionDetail.setCollectionId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);

		param.put("collection_id", customerCollectionDetail.getCollectionId());
		param.put("customer_id", customerCollectionDetail.getCustomerId());
		param.put("collector_id", null);
		param.put("jan_fee", BigDecimal.ZERO);
		param.put("feb_fee", BigDecimal.ZERO);
		param.put("mar_fee", BigDecimal.ZERO);
		param.put("apr_fee", BigDecimal.ZERO);
		param.put("may_fee", BigDecimal.ZERO);
		param.put("jun_fee", BigDecimal.ZERO);
		param.put("jul_fee", BigDecimal.ZERO);
		param.put("aug_fee", BigDecimal.ZERO);
		param.put("sep_fee", BigDecimal.ZERO);
		param.put("oct_fee", BigDecimal.ZERO);
		param.put("nov_fee", BigDecimal.ZERO);
		param.put("dec_fee", BigDecimal.ZERO);

		jdbcNTemplate.update(INSERT_COLLECTION_SQL, param);

		return customerCollectionDetail;
	}

	public List<CustomerCollectionDetail> saveCustomerToCollectionMapping(
			List<CustomerCollectionDetail> customerCollectionDetails) {
		int counter = 0;

		@SuppressWarnings("unchecked")
		Map<String, Object>[] params = new Map[customerCollectionDetails.size()];
		
		for (CustomerCollectionDetail customerCollectionDetail : customerCollectionDetails) {
			Map<String, Object> param = new HashMap<>(15);

			customerCollectionDetail.setCollectionId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);

			param.put("collection_id", customerCollectionDetail.getCollectionId());
			param.put("customer_id", customerCollectionDetail.getCustomerId());
			param.put("collector_id", null);
			param.put("jan_fee", customerCollectionDetail.getJanFee() == null ? BigDecimal.ZERO
					: customerCollectionDetail.getJanFee());
			param.put("feb_fee", customerCollectionDetail.getFebFee() == null ? BigDecimal.ZERO
					: customerCollectionDetail.getFebFee());
			param.put("mar_fee", customerCollectionDetail.getMarFee() == null ? BigDecimal.ZERO
					: customerCollectionDetail.getMarFee());
			param.put("apr_fee", customerCollectionDetail.getAprFee() == null ? BigDecimal.ZERO
					: customerCollectionDetail.getAprFee());
			param.put("may_fee", customerCollectionDetail.getMayFee() == null ? BigDecimal.ZERO
					: customerCollectionDetail.getMayFee());
			param.put("jun_fee", customerCollectionDetail.getJunFee() == null ? BigDecimal.ZERO
					: customerCollectionDetail.getJunFee());
			param.put("jul_fee", customerCollectionDetail.getJulFee() == null ? BigDecimal.ZERO
					: customerCollectionDetail.getJulFee());
			param.put("aug_fee", customerCollectionDetail.getAugFee() == null ? BigDecimal.ZERO
					: customerCollectionDetail.getAugFee());
			param.put("sep_fee", customerCollectionDetail.getSepFee() == null ? BigDecimal.ZERO
					: customerCollectionDetail.getSepFee());
			param.put("oct_fee", customerCollectionDetail.getOctFee() == null ? BigDecimal.ZERO
					: customerCollectionDetail.getOctFee());
			param.put("nov_fee", customerCollectionDetail.getNovFee() == null ? BigDecimal.ZERO
					: customerCollectionDetail.getNovFee());
			param.put("dec_fee", customerCollectionDetail.getDecFee() == null ? BigDecimal.ZERO
					: customerCollectionDetail.getDecFee());

			params[counter] = param;
			counter++;
		}

		jdbcNTemplate.batchUpdate(INSERT_COLLECTION_SQL, params);

		return customerCollectionDetails;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public Customer update(Customer customer) {
		PreparedStatementCreatorFactory queryFactory = new PreparedStatementCreatorFactory(UPDATE_SQL,
				Arrays.asList(new SqlParameter(Types.BIGINT)));
		queryFactory.setUpdatableResults(true);
		queryFactory.setResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE);
		PreparedStatementCreator psc = queryFactory
				.newPreparedStatementCreator(new Object[] { customer.getCustomerId() });
		final Customer oldCustomer = new Customer();
		RowCallbackHandler rowHandler = new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				oldCustomer.setCustomerId(rs.getLong("customer_id"));
				oldCustomer.setRegion(rs.getString("region"));
				oldCustomer.setBuilding(rs.getString("building"));
				oldCustomer.setAddress(rs.getString("address"));
				oldCustomer.setClient(rs.getString("client"));
				oldCustomer.setName(rs.getString("name"));
				oldCustomer.setFloor(rs.getString("floor"));
				oldCustomer.setFee(rs.getBigDecimal("fee"));
				oldCustomer.setMahal(rs.getString("mahal"));
				oldCustomer.setTelephone(rs.getString("telephone"));
				oldCustomer.setLeftTravel(rs.getString("left_travel"));
				oldCustomer.setNote(rs.getString("note"));

				rs.updateString("region", customer.getRegion());
				rs.updateString("building", customer.getBuilding());
				rs.updateString("address", customer.getAddress());
				rs.updateString("client", customer.getClient());
				rs.updateString("name", customer.getName());
				rs.updateString("floor", customer.getFloor());
				rs.updateBigDecimal("fee", customer.getFee());
				rs.updateString("mahal", customer.getMahal());
				rs.updateString("telephone", customer.getTelephone());
				rs.updateString("left_travel", customer.getLeftTravel());
				rs.updateString("note", customer.getNote());

				rs.updateRow();
			}

		};

		jdbcTemplate.query(psc, rowHandler);
		return customer;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public Customer delete(Customer customer) {
		Object[] params = { customer.getCustomerId() };
		int[] types = { Types.BIGINT };
		jdbcTemplate.update(DELETE_SQL, params, types);
		return customer;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	public List<Customer> findAll() {
		List<Customer> customers = jdbcTemplate.query(RETRIEVE_SQL, new RowMapper<Customer>() {

			@Override
			public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
				final Customer customer = new Customer();

				customer.setCustomerId(rs.getLong("customer_id"));
				customer.setRegion(rs.getString("region"));
				customer.setBuilding(rs.getString("building"));
				customer.setAddress(rs.getString("address"));
				customer.setClient(rs.getString("client"));
				customer.setName(rs.getString("name"));
				customer.setFloor(rs.getString("floor"));
				customer.setFee(rs.getBigDecimal("fee"));
				customer.setMahal(rs.getString("mahal"));
				customer.setTelephone(rs.getString("telephone"));
				customer.setLeftTravel(rs.getString("left_travel"));
				customer.setNote(rs.getString("note"));

				return customer;
			}

		});

		if (customers == null || customers.isEmpty()) {
			return null;
		}

		return customers;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	public Customer findByCustomerId(long customerId) {
		Map<String, Object> param = new HashMap<>(1);
		param.put("customer_id", customerId);
		Customer customer = jdbcNTemplate.queryForObject(RETRIEVE_SQL_BY_ID, param, new RowMapper<Customer>() {
			@Override
			public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
				Customer customerDetail = new Customer();
				customerDetail.setCustomerId(rs.getLong("customer_id"));
				customerDetail.setRegion(rs.getString("region"));
				customerDetail.setBuilding(rs.getString("building"));
				customerDetail.setAddress(rs.getString("address"));
				customerDetail.setClient(rs.getString("client"));
				customerDetail.setName(rs.getString("name"));
				customerDetail.setFloor(rs.getString("floor"));
				customerDetail.setFee(rs.getBigDecimal("fee"));
				customerDetail.setMahal(rs.getString("mahal"));
				customerDetail.setTelephone(rs.getString("telephone"));
				customerDetail.setLeftTravel(rs.getString("left_travel"));
				customerDetail.setNote(rs.getString("note"));
				return customerDetail;
			}
		});
		return customer;
	}

}
