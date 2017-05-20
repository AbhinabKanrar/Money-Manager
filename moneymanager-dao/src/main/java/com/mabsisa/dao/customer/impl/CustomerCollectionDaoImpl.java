/**
 * 
 */
package com.mabsisa.dao.customer.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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

	private static final String RETRIEVE_SQL = "SELECT collection.collection_id,cust.customer_id,collection.collector_id,"
			+ "cust.region,cust.building,cust.address,cust.client,cust.name,cust.floor,cust.fee,cust.mahal,cust.telephone,"
			+ "cust.left_travel,cust.note,collection.jan_fee,collection.feb_fee,collection.mar_fee,collection.apr_fee,"
			+ "collection.may_fee,collection.jun_fee,collection.jul_fee,collection.aug_fee,collection.sep_fee,"
			+ "collection.oct_fee,collection.nov_fee,collection.dec_fee "
			+ "FROM "
			+ "mm.customer_detail cust join mm.customer_collection_detail collection "
			+ "on cust.customer_id=collection.customer_id";

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
						customerCollectionDetail.setFebFee(rs.getBigDecimal("mar_fee"));
						customerCollectionDetail.setFebFee(rs.getBigDecimal("apr_fee"));
						customerCollectionDetail.setFebFee(rs.getBigDecimal("may_fee"));
						customerCollectionDetail.setFebFee(rs.getBigDecimal("jun_fee"));
						customerCollectionDetail.setFebFee(rs.getBigDecimal("jul_fee"));
						customerCollectionDetail.setFebFee(rs.getBigDecimal("aug_fee"));
						customerCollectionDetail.setFebFee(rs.getBigDecimal("sep_fee"));
						customerCollectionDetail.setFebFee(rs.getBigDecimal("oct_fee"));
						customerCollectionDetail.setFebFee(rs.getBigDecimal("nov_fee"));
						customerCollectionDetail.setFebFee(rs.getBigDecimal("dec_fee"));

						return customerCollectionDetail;
					}

				});

		if (customerCollectionDetails == null || customerCollectionDetails.isEmpty()) {
			return null;
		}

		return customerCollectionDetails;
	}

}
