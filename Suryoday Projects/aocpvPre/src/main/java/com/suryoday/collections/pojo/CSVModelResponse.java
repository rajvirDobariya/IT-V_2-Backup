package com.suryoday.collections.pojo;

public class CSVModelResponse {

	private String customerName;
	private String city;
	private String currentPOS;
	private String customerID;
	private String aggrementID;
	private String bucket;
	private String ptpDate;
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCurrentPOS() {
		return currentPOS;
	}
	public void setCurrentPOS(String currentPOS) {
		this.currentPOS = currentPOS;
	}
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	public String getAggrementID() {
		return aggrementID;
	}
	public void setAggrementID(String aggrementID) {
		this.aggrementID = aggrementID;
	}
	public String getBucket() {
		return bucket;
	}
	public void setBucket(String bucket) {
		this.bucket = bucket;
	}
	public String getPtpDate() {
		return ptpDate;
	}
	public void setPtpDate(String ptpDate) {
		this.ptpDate = ptpDate;
	}
	public CSVModelResponse(String customerName, String city, String currentPOS, String customerID, String aggrementID,
			String bucket, String ptpDate) {
		super();
		this.customerName = customerName;
		this.city = city;
		this.currentPOS = currentPOS;
		this.customerID = customerID;
		this.aggrementID = aggrementID;
		this.bucket = bucket;
		this.ptpDate = ptpDate;
	}
	@Override
	public String toString() {
		return "CSVModelResponse [customerName=" + customerName + ", city=" + city + ", currentPOS=" + currentPOS
				+ ", customerID=" + customerID + ", aggrementID=" + aggrementID + ", bucket=" + bucket + ", ptpDate="
				+ ptpDate + "]";
	}
	
}
