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
	private Long collectorId;
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

}
