/**
 * 
 */
package com.mabsisa.common.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author abhinab
 *
 */
public class CollectorCollection implements Serializable {

	private static final long serialVersionUID = -3780490965498442165L;
	
	private Long id;
	private Long collectorId;
	private String collectorName;
	private BigDecimal collectionAmount;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public BigDecimal getCollectionAmount() {
		return collectionAmount;
	}
	public void setCollectionAmount(BigDecimal collectionAmount) {
		this.collectionAmount = collectionAmount;
	}

}
