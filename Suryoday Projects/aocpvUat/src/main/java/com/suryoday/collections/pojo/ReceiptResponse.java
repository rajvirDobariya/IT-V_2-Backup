package com.suryoday.collections.pojo;

public class ReceiptResponse {

	private String currentDate;
	private String receiptNo;
	private String totalCount;
	private String accountNo;
	private String custName;
	private String paymanetStatus;

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getPaymanetStatus() {
		return paymanetStatus;
	}

	public void setPaymanetStatus(String paymanetStatus) {
		this.paymanetStatus = paymanetStatus;
	}

	public ReceiptResponse(String currentDate, String receiptNo, String totalCount, String accountNo, String custName,
			String paymanetStatus) {
		super();
		this.currentDate = currentDate;
		this.receiptNo = receiptNo;
		this.totalCount = totalCount;
		this.accountNo = accountNo;
		this.custName = custName;
		this.paymanetStatus = paymanetStatus;
	}

	@Override
	public String toString() {
		return "ReceiptResponse [currentDate=" + currentDate + ", receiptNo=" + receiptNo + ", totalCount=" + totalCount
				+ ", accountNo=" + accountNo + ", custName=" + custName + ", paymanetStatus=" + paymanetStatus + "]";
	}

}
