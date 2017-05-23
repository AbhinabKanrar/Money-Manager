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
public class PaymentReceipt implements Serializable {

	private static final long serialVersionUID = 7848170834060722036L;
	
	private Long collectionId;
	private Long customerId;
	private Long collectorId;
	private BigDecimal dueAmount;
	private BigDecimal paidAmount;

	public Long getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(Long collectionId) {
		this.collectionId = collectionId;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Long getCollectorId() {
		return collectorId;
	}
	public void setCollectorId(Long collectorId) {
		this.collectorId = collectorId;
	}
	public BigDecimal getDueAmount() {
		return dueAmount;
	}
	public void setDueAmount(BigDecimal dueAmount) {
		this.dueAmount = dueAmount;
	}
	public BigDecimal getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

}
