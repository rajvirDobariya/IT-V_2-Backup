package com.suryoday.aocpv.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EmiResponseMaster {
	
	@Id
	private long id;
	private String amountRequested;
	private String term;
	private String paymentAmount;
	private String totalPayableAmount;
	private String interestAmount;
	private String productCode;
	
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getInterestAmount() {
		return interestAmount;
	}
	public void setInterestAmount(String interestAmount) {
		this.interestAmount = interestAmount;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAmountRequested() {
		return amountRequested;
	}
	public void setAmountRequested(String amountRequested) {
		this.amountRequested = amountRequested;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getTotalPayableAmount() {
		return totalPayableAmount;
	}
	public void setTotalPayableAmount(String totalPayableAmount) {
		this.totalPayableAmount = totalPayableAmount;
	}
	
	
}
