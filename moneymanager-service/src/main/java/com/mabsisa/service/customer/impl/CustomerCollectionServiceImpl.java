/**
 * 
 */
package com.mabsisa.service.customer.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mabsisa.common.model.CustomerCollectionDetail;
import com.mabsisa.dao.customer.CustomerCollectionDao;
import com.mabsisa.service.customer.CustomerCollectionService;

/**
 * @author abhinab
 *
 */
@Service
public class CustomerCollectionServiceImpl implements CustomerCollectionService {

	@Autowired
	private CustomerCollectionDao customerCollectionDao;
	
	@Override
	public List<CustomerCollectionDetail> findAll() {
		return customerCollectionDao.findAll();
	}

}
