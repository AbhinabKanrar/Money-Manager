/**
 * 
 */
package com.mabsisa.dao.customer.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Calendar;
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

import com.mabsisa.common.model.CustomerCollectionDetail;
import com.mabsisa.dao.customer.CustomerCollectionDao;

/**
 * @author abhinab
 *
 */
@Repository
@EnableRetry
public class CustomerCollectionDaoImpl implements CustomerCollectionDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcNTemplate;

	private static final String INSERT_SQL = "INSERT INTO mm.customer_collection_detail_audit ("
			+ "audit_id,customer_id,collector_id,location,reason,collection_ts"
			+ ") VALUES ("
			+ ":audit_id,:customer_id,:collector_id,:location,:reason,now()"
			+ ")"; 
	
	private static final String RETRIEVE_SQL = "SELECT collection.collection_id,cust.customer_id,collection.collector_id,"
			+ "cust.region,cust.building,cust.address,cust.client,cust.name,cust.floor,cust.fee,cust.mahal,cust.telephone,"
			+ "cust.left_travel,cust.note,collection.jan_fee,collection.feb_fee,collection.mar_fee,collection.apr_fee,"
			+ "collection.may_fee,collection.jun_fee,collection.jul_fee,collection.aug_fee,collection.sep_fee,"
			+ "collection.oct_fee,collection.nov_fee,collection.dec_fee "
			+ "FROM "
			+ "mm.customer_detail cust join mm.customer_collection_detail collection "
			+ "on cust.customer_id=collection.customer_id";

	private static final String RETRIEVE_SQL_BY_ID = "SELECT collection.collection_id,cust.customer_id,collection.collector_id,"
			+ "cust.region,cust.building,cust.address,cust.client,cust.name,cust.floor,cust.fee,cust.mahal,cust.telephone,"
			+ "cust.left_travel,cust.note,collection.jan_fee,collection.feb_fee,collection.mar_fee,collection.apr_fee,"
			+ "collection.may_fee,collection.jun_fee,collection.jul_fee,collection.aug_fee,collection.sep_fee,"
			+ "collection.oct_fee,collection.nov_fee,collection.dec_fee "
			+ "FROM "
			+ "mm.customer_detail cust join mm.customer_collection_detail collection "
			+ "on cust.customer_id=collection.customer_id "
			+ "where collection.collection_id = :collection_id";
	
	private static final String UPDATE_SQL = "select * from mm.customer_collection_detail where collection_id = ? for update";
	private static final String BATCH_UPDATE_SQL = "update mm.customer_collection_detail set collector_id=:collector_id where customer_id=:customer_id";

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public CustomerCollectionDetail update(CustomerCollectionDetail customerCollectionDetail) {
		PreparedStatementCreatorFactory queryFactory = new PreparedStatementCreatorFactory(UPDATE_SQL,
				Arrays.asList(new SqlParameter(Types.BIGINT)));
		queryFactory.setUpdatableResults(true);
		queryFactory.setResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE);
		PreparedStatementCreator psc = queryFactory
				.newPreparedStatementCreator(new Object[] { customerCollectionDetail.getCollectionId() });
		final CustomerCollectionDetail oldCustomerCollectionDetail = new CustomerCollectionDetail();
		RowCallbackHandler rowHandler = new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				oldCustomerCollectionDetail.setCollectionId(rs.getLong("collection_id"));
				oldCustomerCollectionDetail.setCustomerId(rs.getLong("customer_id"));
				oldCustomerCollectionDetail.setCollectorId(rs.getLong("collector_id"));
				oldCustomerCollectionDetail.setJanFee(rs.getBigDecimal("jan_fee"));
				oldCustomerCollectionDetail.setFebFee(rs.getBigDecimal("feb_fee"));
				oldCustomerCollectionDetail.setMarFee(rs.getBigDecimal("mar_fee"));
				oldCustomerCollectionDetail.setAprFee(rs.getBigDecimal("apr_fee"));
				oldCustomerCollectionDetail.setMayFee(rs.getBigDecimal("may_fee"));
				oldCustomerCollectionDetail.setJunFee(rs.getBigDecimal("jun_fee"));
				oldCustomerCollectionDetail.setJulFee(rs.getBigDecimal("jul_fee"));
				oldCustomerCollectionDetail.setAugFee(rs.getBigDecimal("aug_fee"));
				oldCustomerCollectionDetail.setSepFee(rs.getBigDecimal("sep_fee"));
				oldCustomerCollectionDetail.setOctFee(rs.getBigDecimal("oct_fee"));
				oldCustomerCollectionDetail.setNovFee(rs.getBigDecimal("nov_fee"));
				oldCustomerCollectionDetail.setDecFee(rs.getBigDecimal("dec_fee"));

				rs.updateLong("collector_id", customerCollectionDetail.getCollectorId());
				rs.updateBigDecimal("jan_fee", customerCollectionDetail.getJanFee());
				rs.updateBigDecimal("feb_fee", customerCollectionDetail.getFebFee());
				rs.updateBigDecimal("mar_fee", customerCollectionDetail.getMarFee());
				rs.updateBigDecimal("apr_fee", customerCollectionDetail.getAprFee());
				rs.updateBigDecimal("may_fee", customerCollectionDetail.getMayFee());
				rs.updateBigDecimal("jun_fee", customerCollectionDetail.getJunFee());
				rs.updateBigDecimal("jul_fee", customerCollectionDetail.getJulFee());
				rs.updateBigDecimal("aug_fee", customerCollectionDetail.getAugFee());
				rs.updateBigDecimal("sep_fee", customerCollectionDetail.getSepFee());
				rs.updateBigDecimal("oct_fee", customerCollectionDetail.getOctFee());
				rs.updateBigDecimal("nov_fee", customerCollectionDetail.getNovFee());
				rs.updateBigDecimal("dec_fee", customerCollectionDetail.getDecFee());

				rs.updateRow();
			}

		};

		jdbcTemplate.query(psc, rowHandler);
		
		return customerCollectionDetail;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public List<CustomerCollectionDetail> update(List<CustomerCollectionDetail> customerCollectionDetails) {
		int counter = 0;

		@SuppressWarnings("unchecked")
		Map<String, Object>[] params = new Map[customerCollectionDetails.size()];
		
		for(CustomerCollectionDetail customerCollectionDetail : customerCollectionDetails) {
			Map<String, Object> param = new HashMap<>(2);
			param.put("customer_id", customerCollectionDetail.getCustomerId());
			param.put("collector_id", customerCollectionDetail.getCollectorId());
			
			params[counter] = param;
			counter++;
		}
		
		jdbcNTemplate.batchUpdate(BATCH_UPDATE_SQL, params);
		
		return customerCollectionDetails;
	}

	
	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public CustomerCollectionDetail update(CustomerCollectionDetail customerCollectionDetail, int month) {
		PreparedStatementCreatorFactory queryFactory = new PreparedStatementCreatorFactory(UPDATE_SQL,
				Arrays.asList(new SqlParameter(Types.BIGINT)));
		queryFactory.setUpdatableResults(true);
		queryFactory.setResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE);
		PreparedStatementCreator psc = queryFactory
				.newPreparedStatementCreator(new Object[] { customerCollectionDetail.getCollectionId() });
		final CustomerCollectionDetail oldCustomerCollectionDetail = new CustomerCollectionDetail();
		RowCallbackHandler rowHandler = new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				oldCustomerCollectionDetail.setCollectionId(rs.getLong("collection_id"));
				oldCustomerCollectionDetail.setCustomerId(rs.getLong("customer_id"));
				oldCustomerCollectionDetail.setCollectorId(rs.getLong("collector_id"));
				oldCustomerCollectionDetail.setJanFee(rs.getBigDecimal("jan_fee"));
				oldCustomerCollectionDetail.setFebFee(rs.getBigDecimal("feb_fee"));
				oldCustomerCollectionDetail.setMarFee(rs.getBigDecimal("mar_fee"));
				oldCustomerCollectionDetail.setAprFee(rs.getBigDecimal("apr_fee"));
				oldCustomerCollectionDetail.setMayFee(rs.getBigDecimal("may_fee"));
				oldCustomerCollectionDetail.setJunFee(rs.getBigDecimal("jun_fee"));
				oldCustomerCollectionDetail.setJulFee(rs.getBigDecimal("jul_fee"));
				oldCustomerCollectionDetail.setAugFee(rs.getBigDecimal("aug_fee"));
				oldCustomerCollectionDetail.setSepFee(rs.getBigDecimal("sep_fee"));
				oldCustomerCollectionDetail.setOctFee(rs.getBigDecimal("oct_fee"));
				oldCustomerCollectionDetail.setNovFee(rs.getBigDecimal("nov_fee"));
				oldCustomerCollectionDetail.setDecFee(rs.getBigDecimal("dec_fee"));

				if (month == Calendar.JANUARY) {
					rs.updateBigDecimal("jan_fee", customerCollectionDetail.getActual());
				} else if (month == Calendar.FEBRUARY) {
					rs.updateBigDecimal("feb_fee", customerCollectionDetail.getActual());
				} else if (month == Calendar.MARCH) {
					rs.updateBigDecimal("mar_fee", customerCollectionDetail.getActual());
				} else if (month == Calendar.APRIL) {
					rs.updateBigDecimal("apr_fee", customerCollectionDetail.getActual());
				} else if (month == Calendar.MAY) {
					rs.updateBigDecimal("may_fee", customerCollectionDetail.getActual());
				} else if (month == Calendar.JUNE) {
					rs.updateBigDecimal("jun_fee", customerCollectionDetail.getActual());
				} else if (month == Calendar.JULY) {
					rs.updateBigDecimal("jul_fee", customerCollectionDetail.getActual());
				} else if (month == Calendar.AUGUST) {
					rs.updateBigDecimal("aug_fee", customerCollectionDetail.getActual());
				} else if (month == Calendar.SEPTEMBER) {
					rs.updateBigDecimal("sep_fee", customerCollectionDetail.getActual());
				} else if (month == Calendar.OCTOBER) {
					rs.updateBigDecimal("oct_fee", customerCollectionDetail.getActual());
				} else if (month == Calendar.NOVEMBER) {
					rs.updateBigDecimal("nov_fee", customerCollectionDetail.getActual());
				} else if (month == Calendar.DECEMBER) {
					rs.updateBigDecimal("dec_fee", customerCollectionDetail.getActual());
				}
				
				rs.updateRow();
			}
		};

		jdbcTemplate.query(psc, rowHandler);
		
		if (customerCollectionDetail.getDue().compareTo(customerCollectionDetail.getActual()) > 0 && customerCollectionDetail.getReasonCode() != null && !customerCollectionDetail.getReasonCode().trim().isEmpty()) {
			updateAudit(customerCollectionDetail);
		}
		
		return customerCollectionDetail;
	}
	
	private CustomerCollectionDetail updateAudit(CustomerCollectionDetail customerCollectionDetail) {

		Map<String, Object> params = new HashMap<>(5);

		params.put("audit_id", UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
		params.put("customer_id", customerCollectionDetail.getCustomerId());
		params.put("collector_id", customerCollectionDetail.getCollectorId());
		params.put("location", customerCollectionDetail.getLocation());
		params.put("reason", customerCollectionDetail.getReasonCode());

		jdbcNTemplate.update(INSERT_SQL, params);

		return customerCollectionDetail;
	}
	
	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	public List<CustomerCollectionDetail> findAll() {
		List<CustomerCollectionDetail> customerCollectionDetails = jdbcTemplate.query(RETRIEVE_SQL,
				new RowMapper<CustomerCollectionDetail>() {

					@Override
					public CustomerCollectionDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
						final CustomerCollectionDetail customerCollectionDetail = new CustomerCollectionDetail();

						customerCollectionDetail.setCollectionId(rs.getLong("collection_id"));
						customerCollectionDetail.setCustomerId(rs.getLong("customer_id"));
						customerCollectionDetail.setCollectorId(rs.getLong("collector_id"));
						customerCollectionDetail.setRegion(rs.getString("region"));
						customerCollectionDetail.setBuilding(rs.getString("building"));
						customerCollectionDetail.setAddress(rs.getString("address"));
						customerCollectionDetail.setClient(rs.getString("client"));
						customerCollectionDetail.setName(rs.getString("name"));
						customerCollectionDetail.setFloor(rs.getString("floor"));
						customerCollectionDetail.setFee(rs.getBigDecimal("fee"));
						customerCollectionDetail.setMahal(rs.getString("mahal"));
						customerCollectionDetail.setTelephone(rs.getString("telephone"));
						customerCollectionDetail.setLeftTravel(rs.getString("left_travel"));
						customerCollectionDetail.setNote(rs.getString("note"));
						customerCollectionDetail.setJanFee(rs.getBigDecimal("jan_fee"));
						customerCollectionDetail.setFebFee(rs.getBigDecimal("feb_fee"));
						customerCollectionDetail.setMarFee(rs.getBigDecimal("mar_fee"));
						customerCollectionDetail.setAprFee(rs.getBigDecimal("apr_fee"));
						customerCollectionDetail.setMayFee(rs.getBigDecimal("may_fee"));
						customerCollectionDetail.setJunFee(rs.getBigDecimal("jun_fee"));
						customerCollectionDetail.setJulFee(rs.getBigDecimal("jul_fee"));
						customerCollectionDetail.setAugFee(rs.getBigDecimal("aug_fee"));
						customerCollectionDetail.setSepFee(rs.getBigDecimal("sep_fee"));
						customerCollectionDetail.setOctFee(rs.getBigDecimal("oct_fee"));
						customerCollectionDetail.setNovFee(rs.getBigDecimal("nov_fee"));
						customerCollectionDetail.setDecFee(rs.getBigDecimal("dec_fee"));
		
						return customerCollectionDetail;
					}

				});

		if (customerCollectionDetails == null || customerCollectionDetails.isEmpty()) {
			return null;
		}

		return customerCollectionDetails;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	public CustomerCollectionDetail findByCollectionId(long collectionId) {
		Map<String, Object> param = new HashMap<>(1);
		param.put("collection_id", collectionId);
		CustomerCollectionDetail customerCollectionDetail = jdbcNTemplate.queryForObject(RETRIEVE_SQL_BY_ID, param, new RowMapper<CustomerCollectionDetail>() {
			@Override
			public CustomerCollectionDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
				CustomerCollectionDetail customerCollectionDetail = new CustomerCollectionDetail();
				
				customerCollectionDetail.setCollectionId(rs.getLong("collection_id"));
				customerCollectionDetail.setCustomerId(rs.getLong("customer_id"));
				customerCollectionDetail.setCollectorId(rs.getLong("collector_id"));
				customerCollectionDetail.setRegion(rs.getString("region"));
				customerCollectionDetail.setBuilding(rs.getString("building"));
				customerCollectionDetail.setAddress(rs.getString("address"));
				customerCollectionDetail.setClient(rs.getString("client"));
				customerCollectionDetail.setName(rs.getString("name"));
				customerCollectionDetail.setFloor(rs.getString("floor"));
				customerCollectionDetail.setFee(rs.getBigDecimal("fee"));
				customerCollectionDetail.setMahal(rs.getString("mahal"));
				customerCollectionDetail.setTelephone(rs.getString("telephone"));
				customerCollectionDetail.setLeftTravel(rs.getString("left_travel"));
				customerCollectionDetail.setNote(rs.getString("note"));
				customerCollectionDetail.setJanFee(rs.getBigDecimal("jan_fee"));
				customerCollectionDetail.setFebFee(rs.getBigDecimal("feb_fee"));
				customerCollectionDetail.setMarFee(rs.getBigDecimal("mar_fee"));
				customerCollectionDetail.setAprFee(rs.getBigDecimal("apr_fee"));
				customerCollectionDetail.setMayFee(rs.getBigDecimal("may_fee"));
				customerCollectionDetail.setJunFee(rs.getBigDecimal("jun_fee"));
				customerCollectionDetail.setJulFee(rs.getBigDecimal("jul_fee"));
				customerCollectionDetail.setAugFee(rs.getBigDecimal("aug_fee"));
				customerCollectionDetail.setSepFee(rs.getBigDecimal("sep_fee"));
				customerCollectionDetail.setOctFee(rs.getBigDecimal("oct_fee"));
				customerCollectionDetail.setNovFee(rs.getBigDecimal("nov_fee"));
				customerCollectionDetail.setDecFee(rs.getBigDecimal("dec_fee"));
				
				return customerCollectionDetail;
			}
		});
		return customerCollectionDetail;
	}

}
