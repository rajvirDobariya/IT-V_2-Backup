package com.suryoday.dsaOnboard.pojo;

public class CfrReportResponse {

	private String bankname;
	private String detailsOFFraud;
	private String amount;
	private String registeredDate;
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public String getDetailsOFFraud() {
		return detailsOFFraud;
	}
	public void setDetailsOFFraud(String detailsOFFraud) {
		this.detailsOFFraud = detailsOFFraud;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getRegisteredDate() {
		return registeredDate;
	}
	public void setRegisteredDate(String registeredDate) {
		this.registeredDate = registeredDate;
	}
	
	
}
