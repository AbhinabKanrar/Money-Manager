/**
 * 
 */
package com.mabsisa.common.model;

import java.io.Serializable;

/**
 * @author abhinab
 *
 */
public class CustomerAssignmentCollector implements Serializable {

	private static final long serialVersionUID = 1458650305857688142L;
	
	private Long collectorId;
	private String collectorName;
	private int customerCount;

	public Long getCollectorId() {
		return collectorId;
	}
	public void setCollectorId(Long collectorId) {
		this.collectorId = collectorId;
	}
	public String getCollectorName() {
		return collectorName;
	}
	public void setCollectorName(String collectorName) {
		this.collectorName = collectorName;
	}
	public int getCustomerCount() {
		return customerCount;
	}
	public void setCustomerCount(int customerCount) {
		this.customerCount = customerCount;
	}

}
