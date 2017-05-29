/**
 * 
 */
package com.mabsisa.common.model;

import java.io.Serializable;

/**
 * @author abhinab
 *
 */
public class CustomerPerRegion implements Serializable {

	private static final long serialVersionUID = -7014989335407612319L;
	
	private String region;
	private int numberOfCustomer;
	private float percentage;
	private int total;

	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public int getNumberOfCustomer() {
		return numberOfCustomer;
	}
	public void setNumberOfCustomer(int numberOfCustomer) {
		this.numberOfCustomer = numberOfCustomer;
	}
	public float getPercentage() {
		return percentage;
	}
	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}

}
