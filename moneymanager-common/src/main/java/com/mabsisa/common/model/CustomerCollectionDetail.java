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
public class CustomerCollectionDetail implements Serializable {

	private static final long serialVersionUID = -7726287508210513844L;

	private Long collectionId;
	private Long customerId;
	private long collectorId;
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
	private String note;
	private BigDecimal janFee;
	private BigDecimal febFee;
	private BigDecimal marFee;
	private BigDecimal aprFee;
	private BigDecimal mayFee;
	private BigDecimal junFee;
	private BigDecimal julFee;
	private BigDecimal augFee;
	private BigDecimal sepFee;
	private BigDecimal octFee;
	private BigDecimal novFee;
	private BigDecimal decFee;
	private BigDecimal due;
	private BigDecimal actual;
	private String reasonCode = " ";
	private String location;

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
	public long getCollectorId() {
		return collectorId;
	}
	public void setCollectorId(long collectorId) {
		this.collectorId = collectorId;
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
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public BigDecimal getJanFee() {
		return janFee;
	}
	public void setJanFee(BigDecimal janFee) {
		this.janFee = janFee;
	}
	public BigDecimal getFebFee() {
		return febFee;
	}
	public void setFebFee(BigDecimal febFee) {
		this.febFee = febFee;
	}
	public BigDecimal getMarFee() {
		return marFee;
	}
	public void setMarFee(BigDecimal marFee) {
		this.marFee = marFee;
	}
	public BigDecimal getAprFee() {
		return aprFee;
	}
	public void setAprFee(BigDecimal aprFee) {
		this.aprFee = aprFee;
	}
	public BigDecimal getMayFee() {
		return mayFee;
	}
	public void setMayFee(BigDecimal mayFee) {
		this.mayFee = mayFee;
	}
	public BigDecimal getJunFee() {
		return junFee;
	}
	public void setJunFee(BigDecimal junFee) {
		this.junFee = junFee;
	}
	public BigDecimal getJulFee() {
		return julFee;
	}
	public void setJulFee(BigDecimal julFee) {
		this.julFee = julFee;
	}
	public BigDecimal getAugFee() {
		return augFee;
	}
	public void setAugFee(BigDecimal augFee) {
		this.augFee = augFee;
	}
	public BigDecimal getSepFee() {
		return sepFee;
	}
	public void setSepFee(BigDecimal sepFee) {
		this.sepFee = sepFee;
	}
	public BigDecimal getOctFee() {
		return octFee;
	}
	public void setOctFee(BigDecimal octFee) {
		this.octFee = octFee;
	}
	public BigDecimal getNovFee() {
		return novFee;
	}
	public void setNovFee(BigDecimal novFee) {
		this.novFee = novFee;
	}
	public BigDecimal getDecFee() {
		return decFee;
	}
	public void setDecFee(BigDecimal decFee) {
		this.decFee = decFee;
	}
	public BigDecimal getDue() {
		return due;
	}
	public void setDue(BigDecimal due) {
		this.due = due;
	}
	public BigDecimal getActual() {
		return actual;
	}
	public void setActual(BigDecimal actual) {
		this.actual = actual;
	}
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

}
