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
public class RevenueByRegion implements Serializable {

	private static final long serialVersionUID = -91054165682859085L;
	
	private String region;
	private BigDecimal janRevenue;
	private BigDecimal febRevenue;
	private BigDecimal marRevenue;
	private BigDecimal aprRevenue;
	private BigDecimal mayRevenue;
	private BigDecimal junRevenue;
	private BigDecimal julRevenue;
	private BigDecimal augRevenue;
	private BigDecimal sepRevenue;
	private BigDecimal octRevenue;
	private BigDecimal novRevenue;
	private BigDecimal decRevenue;

	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public BigDecimal getJanRevenue() {
		return janRevenue;
	}
	public void setJanRevenue(BigDecimal janRevenue) {
		this.janRevenue = janRevenue;
	}
	public BigDecimal getFebRevenue() {
		return febRevenue;
	}
	public void setFebRevenue(BigDecimal febRevenue) {
		this.febRevenue = febRevenue;
	}
	public BigDecimal getMarRevenue() {
		return marRevenue;
	}
	public void setMarRevenue(BigDecimal marRevenue) {
		this.marRevenue = marRevenue;
	}
	public BigDecimal getAprRevenue() {
		return aprRevenue;
	}
	public void setAprRevenue(BigDecimal aprRevenue) {
		this.aprRevenue = aprRevenue;
	}
	public BigDecimal getMayRevenue() {
		return mayRevenue;
	}
	public void setMayRevenue(BigDecimal mayRevenue) {
		this.mayRevenue = mayRevenue;
	}
	public BigDecimal getJunRevenue() {
		return junRevenue;
	}
	public void setJunRevenue(BigDecimal junRevenue) {
		this.junRevenue = junRevenue;
	}
	public BigDecimal getJulRevenue() {
		return julRevenue;
	}
	public void setJulRevenue(BigDecimal julRevenue) {
		this.julRevenue = julRevenue;
	}
	public BigDecimal getAugRevenue() {
		return augRevenue;
	}
	public void setAugRevenue(BigDecimal augRevenue) {
		this.augRevenue = augRevenue;
	}
	public BigDecimal getSepRevenue() {
		return sepRevenue;
	}
	public void setSepRevenue(BigDecimal sepRevenue) {
		this.sepRevenue = sepRevenue;
	}
	public BigDecimal getOctRevenue() {
		return octRevenue;
	}
	public void setOctRevenue(BigDecimal octRevenue) {
		this.octRevenue = octRevenue;
	}
	public BigDecimal getNovRevenue() {
		return novRevenue;
	}
	public void setNovRevenue(BigDecimal novRevenue) {
		this.novRevenue = novRevenue;
	}
	public BigDecimal getDecRevenue() {
		return decRevenue;
	}
	public void setDecRevenue(BigDecimal decRevenue) {
		this.decRevenue = decRevenue;
	}

}
