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
	private String customerName;
	private String region;
	private String building;
	private String address;
	private String floor;
	private Long collectorId;
	private String collectorName;
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
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getBuilding() {
		return building;
	}
	public void setBuilding(String building) {
		this.building = building;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
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
