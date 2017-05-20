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
public class Customer implements Serializable {

	private static final long serialVersionUID = 6702238399457510993L;

	private Long customerId;
	private String region;
	private String building;
	private String address;
	private String client;
	private String name;
	private String floor;
	private BigDecimal fee;
	private String mahal;
	private String telephone;
	private String leftTravel;
	private long collectorId;
	private String note;

	public Customer() {}
	
	public Customer(BigDecimal fee) {
		this.fee = fee;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
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

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public String getMahal() {
		return mahal;
	}

	public void setMahal(String mahal) {
		this.mahal = mahal;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getLeftTravel() {
		return leftTravel;
	}

	public void setLeftTravel(String leftTravel) {
		this.leftTravel = leftTravel;
	}

	public long getCollectorId() {
		return collectorId;
	}

	public void setCollectorId(long collectorId) {
		this.collectorId = collectorId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
