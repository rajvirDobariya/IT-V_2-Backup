package com.suryoday.aocpv.pojo;

import java.time.LocalDate;

public class LoanDetail {

	private long mobilePhone;
	private String referenceNo;
	private Long customerID;
	private String memberName;
	private String state;
	private long branchId;
	private double amount;
	private String status;
	private LocalDate createDate;
	private long applicationNo;
	private String productCode;

	public LoanDetail() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LoanDetail(long mobilePhone, String referenceNo, Long customerID, String memberName, String state,
			long branchId, double amount, String status, LocalDate createDate, String productCode) {
		super();
		this.mobilePhone = mobilePhone;
		this.referenceNo = referenceNo;
		this.customerID = customerID;
		this.memberName = memberName;
		this.state = state;
		this.branchId = branchId;
		this.amount = amount;
		this.status = status;
		this.createDate = createDate;
		this.productCode = productCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public long getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(long mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public Long getCustomerID() {
		return customerID;
	}

	public void setCustomerID(Long customerID) {
		this.customerID = customerID;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public long getBranchId() {
		return branchId;
	}

	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public long getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(long applicationNo) {
		this.applicationNo = applicationNo;
	}

	@Override
	public String toString() {
		return "LoanDetail [mobilePhone=" + mobilePhone + ", referenceNo=" + referenceNo + ", customerID=" + customerID
				+ ", memberName=" + memberName + ", state=" + state + ", branchId=" + branchId + ", amount=" + amount
				+ ", status=" + status + ", createDate=" + createDate + ", applicationNo=" + applicationNo + "]";
	}

}
