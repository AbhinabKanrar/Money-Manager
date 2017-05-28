/**
 * 
 */
package com.mabsisa.common.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author abhinab
 *
 */
public class CustomerCollectionDetailAudit implements Serializable {

	private static final long serialVersionUID = -1215360197084480134L;
	
	private Long auditId;
	private Long customerId;
	private Long collectorId;
	private String location;
	private String reason;
	private Timestamp collectionTs;

	public Long getAuditId() {
		return auditId;
	}
	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public long getCollectorId() {
		return collectorId;
	}
	public void setCollectorId(long collectorId) {
		this.collectorId = collectorId;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Timestamp getCollectionTs() {
		return collectionTs;
	}
	public void setCollectionTs(Timestamp collectionTs) {
		this.collectionTs = collectionTs;
	}
	
}
