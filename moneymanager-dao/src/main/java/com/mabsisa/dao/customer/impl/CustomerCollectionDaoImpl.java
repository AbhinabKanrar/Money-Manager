/**
 * 
 */
package com.mabsisa.dao.customer.impl;

import java.math.BigDecimal;
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

import com.mabsisa.common.model.CollectorCollection;
import com.mabsisa.common.model.CustomerAssignmentCollector;
import com.mabsisa.common.model.CustomerCollectionDetail;
import com.mabsisa.common.model.CustomerCollectionDetailAudit;
import com.mabsisa.common.model.CustomerPerRegion;
import com.mabsisa.common.model.RevenueByRegion;
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
			+ "audit_id,customer_id,collector_id,location,reason,collection_ts" + ") VALUES ("
			+ ":audit_id,:customer_id,:collector_id,:location,:reason,now()" + ")";

	private static final String RETRIEVE_SQL = "SELECT collection.collection_id,cust.customer_id,collection.collector_id,"
			+ "cust.region,cust.building,cust.address,cust.client,cust.name,cust.floor,cust.fee,cust.mahal,cust.telephone,"
			+ "cust.left_travel,cust.note,collection.jan_fee,collection.feb_fee,collection.mar_fee,collection.apr_fee,"
			+ "collection.may_fee,collection.jun_fee,collection.jul_fee,collection.aug_fee,collection.sep_fee,"
			+ "collection.oct_fee,collection.nov_fee,collection.dec_fee " + "FROM "
			+ "mm.customer_detail cust join mm.customer_collection_detail collection "
			+ "on cust.customer_id=collection.customer_id";

	private static final String RETRIEVE_SQL_BY_ID = "SELECT collection.collection_id,cust.customer_id,collection.collector_id,"
			+ "cust.region,cust.building,cust.address,cust.client,cust.name,cust.floor,cust.fee,cust.mahal,cust.telephone,"
			+ "cust.left_travel,cust.note,collection.jan_fee,collection.feb_fee,collection.mar_fee,collection.apr_fee,"
			+ "collection.may_fee,collection.jun_fee,collection.jul_fee,collection.aug_fee,collection.sep_fee,"
			+ "collection.oct_fee,collection.nov_fee,collection.dec_fee " + "FROM "
			+ "mm.customer_detail cust join mm.customer_collection_detail collection "
			+ "on cust.customer_id=collection.customer_id " + "where collection.collection_id = :collection_id";

	private static final String RETRIEVE_AUDIT_SQL = "SELECT cca.audit_id,cca.customer_id,cd.name,cd.region,cd.building,cd.address,"
			+ "cd.floor,cca.collector_id,uad.username,cca.location,cca.reason,cca.collection_ts "
			+ "FROM mm.customer_collection_detail_audit cca join mm.user_auth_detail uad "
			+ "on cca.collector_id=uad.user_id "
			+ "join mm.customer_detail cd "
			+ "on cca.customer_id=cd.customer_id";

	private static final String RETRIEVE_CUSTOMER_PER_REGION_SQL = "SELECT region,count(customer_id),"
			+ "(count(customer_id))*100/(SELECT count(customer_id) FROM mm.customer_detail) as percentage, "
			+ "(SELECT count(customer_id) FROM mm.customer_detail) as total FROM mm.customer_detail group by region";

	private static final String RETRIEVE_REVENUE_SQL = "SELECT region,sum(ccd.jan_fee)  jan,"
			+ "sum(ccd.feb_fee) feb,sum(ccd.mar_fee) mar,sum(ccd.apr_fee) apr,sum(ccd.may_fee) may,"
			+ "sum(ccd.jun_fee) jun,sum(ccd.jul_fee) jul,sum(ccd.aug_fee) aug,sum(ccd.sep_fee) sep,"
			+ "sum(ccd.oct_fee) oct,sum(ccd.nov_fee) nov,sum(ccd.dec_fee) as dec "
			+ "FROM mm.customer_detail cd join mm.customer_collection_detail ccd "
			+ "on cd.customer_id=ccd.customer_id group by cd.region";

	private static final String RETRIEVE_ASSIGNMENT_BY_COLLECTOR_SQL = "SELECT ccd.collector_id,uad.username,count(cd.customer_id) "
			+ "FROM mm.customer_detail cd join mm.customer_collection_detail ccd "
			+ "on cd.customer_id=ccd.customer_id "
			+ "join mm.user_auth_detail uad "
			+ "on ccd.collector_id=uad.user_id "
			+ "group by ccd.collector_id,uad.username";

	private static final String RETRIEVE_COLLECTION_BY_COLLECTOR_SQL = "SELECT ccd.collector_id,uad.username,"
			+ "sum(ccd.jan_fee) + sum(ccd.feb_fee) + sum(ccd.mar_fee) + sum(ccd.apr_fee) + sum(ccd.may_fee) + "
			+ "sum(ccd.jun_fee) + sum(ccd.jul_fee) + sum(ccd.aug_fee) + sum(ccd.sep_fee) + sum(ccd.oct_fee) + "
			+ "sum(ccd.nov_fee) + sum(ccd.dec_fee) as sum "
			+ "FROM mm.customer_detail cd join mm.customer_collection_detail ccd "
			+ "on cd.customer_id=ccd.customer_id " + "join mm.user_auth_detail uad "
			+ "on uad.user_id=ccd.collector_id " + "where ccd.collector_id is not null "
			+ "group by ccd.collector_id,uad.username";

	private static final String RETRIEVE_COLLECTION_OF_TODAY_SQL = "SELECT * FROM mm.collector_collection";

	private static final String UPDATE_SQL = "select * from mm.customer_collection_detail where collection_id = ? for update";

	private static final String UPDATE_AUDIT_SQL = "SELECT * FROM mm.collector_collection where collector_id=? FOR UPDATE";

	private static final String UPDATE_REFRESH_AUDIT_SQL = "update mm.collector_collection set amount = :amount";

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

				if (customerCollectionDetail.getCollectorId() != null) {
					rs.updateLong("collector_id", customerCollectionDetail.getCollectorId());
				}
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

		for (CustomerCollectionDetail customerCollectionDetail : customerCollectionDetails) {
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

		updateCollectorAudit(customerCollectionDetail);

		if (customerCollectionDetail.getDue().compareTo(customerCollectionDetail.getActual()) > 0
				&& customerCollectionDetail.getReasonCode() != null
				&& !customerCollectionDetail.getReasonCode().trim().isEmpty()) {
			updateAudit(customerCollectionDetail);
		}

		return customerCollectionDetail;
	}

	private void updateCollectorAudit(CustomerCollectionDetail customerCollectionDetail) {
		PreparedStatementCreatorFactory queryFactory = new PreparedStatementCreatorFactory(UPDATE_AUDIT_SQL,
				Arrays.asList(new SqlParameter(Types.BIGINT)));
		queryFactory.setUpdatableResults(true);
		queryFactory.setResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE);
		PreparedStatementCreator psc = queryFactory
				.newPreparedStatementCreator(new Object[] { customerCollectionDetail.getCollectorId() });
		final CollectorCollection collectorCollection = new CollectorCollection();
		RowCallbackHandler rowHandler = new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {

				collectorCollection.setId(rs.getLong("id"));
				collectorCollection.setCollectorId(rs.getLong("collector_id"));
				collectorCollection.setCollectorName(rs.getString("collector_name"));
				collectorCollection.setCollectionAmount(rs.getBigDecimal("amount"));
				rs.updateBigDecimal("amount",
						customerCollectionDetail.getActual().add(collectorCollection.getCollectionAmount()));

				rs.updateRow();
			}

		};

		jdbcTemplate.query(psc, rowHandler);

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
		CustomerCollectionDetail customerCollectionDetail = jdbcNTemplate.queryForObject(RETRIEVE_SQL_BY_ID, param,
				new RowMapper<CustomerCollectionDetail>() {
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

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	public List<CustomerCollectionDetailAudit> findAllCustomerCollectionDetailAudit() {
		List<CustomerCollectionDetailAudit> customerCollectionDetailAudits = jdbcTemplate.query(RETRIEVE_AUDIT_SQL,
				new RowMapper<CustomerCollectionDetailAudit>() {

					@Override
					public CustomerCollectionDetailAudit mapRow(ResultSet rs, int rowNum) throws SQLException {
						final CustomerCollectionDetailAudit customerCollectionDetailAudit = new CustomerCollectionDetailAudit();

						customerCollectionDetailAudit.setAuditId(rs.getLong("audit_id"));
						customerCollectionDetailAudit.setCustomerId(rs.getLong("customer_id"));
						customerCollectionDetailAudit.setCustomerName(rs.getString("name"));
						customerCollectionDetailAudit.setRegion(rs.getString("region"));
						customerCollectionDetailAudit.setBuilding(rs.getString("building"));
						customerCollectionDetailAudit.setAddress(rs.getString("address"));
						customerCollectionDetailAudit.setFloor(rs.getString("floor"));
						customerCollectionDetailAudit.setCollectorId(rs.getLong("collector_id"));
						customerCollectionDetailAudit.setCollectorName(rs.getString("username"));
						customerCollectionDetailAudit.setLocation(rs.getString("location"));
						customerCollectionDetailAudit.setReason(rs.getString("reason"));
						customerCollectionDetailAudit.setCollectionTs(rs.getTimestamp("collection_ts"));

						return customerCollectionDetailAudit;
					}

				});

		if (customerCollectionDetailAudits == null || customerCollectionDetailAudits.isEmpty()) {
			return null;
		}

		return customerCollectionDetailAudits;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	public List<CustomerPerRegion> findAllCustomerPerRegion() {
		List<CustomerPerRegion> customerPerRegions = jdbcTemplate.query(RETRIEVE_CUSTOMER_PER_REGION_SQL,
				new RowMapper<CustomerPerRegion>() {

					@Override
					public CustomerPerRegion mapRow(ResultSet rs, int rowNum) throws SQLException {
						final CustomerPerRegion customerPerRegion = new CustomerPerRegion();

						customerPerRegion.setRegion(rs.getString("region"));
						customerPerRegion.setNumberOfCustomer(rs.getInt("count"));
						customerPerRegion.setPercentage(rs.getInt("percentage"));
						customerPerRegion.setTotal(rs.getInt("total"));

						return customerPerRegion;
					}

				});

		if (customerPerRegions == null || customerPerRegions.isEmpty()) {
			return null;
		}

		return customerPerRegions;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	public List<RevenueByRegion> findAllRevenueByRegion() {
		List<RevenueByRegion> revenueByRegions = jdbcTemplate.query(RETRIEVE_REVENUE_SQL,
				new RowMapper<RevenueByRegion>() {

					@Override
					public RevenueByRegion mapRow(ResultSet rs, int rowNum) throws SQLException {
						final RevenueByRegion revenueByRegion = new RevenueByRegion();

						revenueByRegion.setRegion(rs.getString("region"));
						revenueByRegion.setJanRevenue(rs.getBigDecimal("jan"));
						revenueByRegion.setFebRevenue(rs.getBigDecimal("feb"));
						revenueByRegion.setMarRevenue(rs.getBigDecimal("mar"));
						revenueByRegion.setAprRevenue(rs.getBigDecimal("apr"));
						revenueByRegion.setMayRevenue(rs.getBigDecimal("may"));
						revenueByRegion.setJunRevenue(rs.getBigDecimal("jun"));
						revenueByRegion.setJulRevenue(rs.getBigDecimal("jul"));
						revenueByRegion.setAugRevenue(rs.getBigDecimal("aug"));
						revenueByRegion.setSepRevenue(rs.getBigDecimal("sep"));
						revenueByRegion.setOctRevenue(rs.getBigDecimal("oct"));
						revenueByRegion.setNovRevenue(rs.getBigDecimal("nov"));
						revenueByRegion.setDecRevenue(rs.getBigDecimal("dec"));

						return revenueByRegion;
					}

				});

		if (revenueByRegions == null || revenueByRegions.isEmpty()) {
			return null;
		}

		return revenueByRegions;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	public List<CustomerAssignmentCollector> findAllAssignmentByCollector() {
		List<CustomerAssignmentCollector> customerAssignmentCollectors = jdbcTemplate
				.query(RETRIEVE_ASSIGNMENT_BY_COLLECTOR_SQL, new RowMapper<CustomerAssignmentCollector>() {

					@Override
					public CustomerAssignmentCollector mapRow(ResultSet rs, int rowNum) throws SQLException {
						final CustomerAssignmentCollector customerAssignmentCollector = new CustomerAssignmentCollector();

						customerAssignmentCollector.setCollectorId(rs.getLong("collector_id"));
						customerAssignmentCollector.setCollectorName(rs.getString("username"));
						customerAssignmentCollector.setCustomerCount(rs.getInt("count"));

						return customerAssignmentCollector;
					}

				});

		if (customerAssignmentCollectors == null || customerAssignmentCollectors.isEmpty()) {
			return null;
		}

		return customerAssignmentCollectors;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	public List<CollectorCollection> findAllCollectionByCollector() {
		List<CollectorCollection> collectorCollections = jdbcTemplate.query(RETRIEVE_COLLECTION_BY_COLLECTOR_SQL,
				new RowMapper<CollectorCollection>() {

					@Override
					public CollectorCollection mapRow(ResultSet rs, int rowNum) throws SQLException {
						final CollectorCollection collectorCollection = new CollectorCollection();

						collectorCollection.setCollectorId(rs.getLong("collector_id"));
						collectorCollection.setCollectorName(rs.getString("username"));
						collectorCollection.setCollectionAmount(rs.getBigDecimal("sum"));

						return collectorCollection;
					}

				});

		if (collectorCollections == null || collectorCollections.isEmpty()) {
			return null;
		}

		return collectorCollections;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	public List<CollectorCollection> findAllCollectionOfToday() {
		List<CollectorCollection> collectorCollections = jdbcTemplate.query(RETRIEVE_COLLECTION_OF_TODAY_SQL,
				new RowMapper<CollectorCollection>() {

					@Override
					public CollectorCollection mapRow(ResultSet rs, int rowNum) throws SQLException {
						final CollectorCollection collectorCollection = new CollectorCollection();

						collectorCollection.setId(rs.getLong("id"));
						collectorCollection.setCollectorId(rs.getLong("collector_id"));
						collectorCollection.setCollectorName(rs.getString("collector_name"));
						collectorCollection.setCollectionAmount(rs.getBigDecimal("amount"));

						return collectorCollection;
					}

				});

		if (collectorCollections == null || collectorCollections.isEmpty()) {
			return null;
		}

		return collectorCollections;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void refresh() {
		Map<String, Object> params = new HashMap<>(1);
		params.put("amount", new BigDecimal("0.00"));
		jdbcNTemplate.update(UPDATE_REFRESH_AUDIT_SQL, params);
	}

}
