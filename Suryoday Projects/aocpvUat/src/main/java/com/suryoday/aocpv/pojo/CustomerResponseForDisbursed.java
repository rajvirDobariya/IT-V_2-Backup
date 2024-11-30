package com.suryoday.aocpv.pojo;

import java.time.LocalDate;

public class CustomerResponseForDisbursed {

	private long mobilePhone;
	private long customerID;
	private String memberName;
	private String dateOfBirth;
	private String address;
	private String state;
	private String postal;
	private String branchId;
	private String amount;
	private String status;
	private String createDate;
	private String updateDate;
	private String sanctionedLoanAmount;
	private String loanAccoutNumber;
	private LocalDate loanCreationDate;

	public String getSanctionedLoanAmount() {
		return sanctionedLoanAmount;
	}

	public void setSanctionedLoanAmount(String sanctionedLoanAmount) {
		this.sanctionedLoanAmount = sanctionedLoanAmount;
	}

	public String getLoanAccoutNumber() {
		return loanAccoutNumber;
	}

	public void setLoanAccoutNumber(String loanAccoutNumber) {
		this.loanAccoutNumber = loanAccoutNumber;
	}

	public LocalDate getLoanCreationDate() {
		return loanCreationDate;
	}

	public void setLoanCreationDate(LocalDate loanCreationDate) {
		this.loanCreationDate = loanCreationDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	private long applicationNo;

	public long getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(long mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public long getCustomerID() {
		return customerID;
	}

	public void setCustomerID(long customerID) {
		this.customerID = customerID;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostal() {
		return postal;
	}

	public void setPostal(String postal) {
		this.postal = postal;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public long getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(long applicationNo) {
		this.applicationNo = applicationNo;
	}

	public CustomerResponseForDisbursed(long mobilePhone, long customerID, String memberName, String dateOfBirth,
			String address, String state, String postal, String branchId, String amount, String status,
			String createDate, long applicationNo, String updateDate) {
		super();
		this.mobilePhone = mobilePhone;
		this.customerID = customerID;
		this.memberName = memberName;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
		this.state = state;
		this.postal = postal;
		this.branchId = branchId;
		this.amount = amount;
		this.status = status;
		this.createDate = createDate;
		this.applicationNo = applicationNo;
		this.updateDate = updateDate;
	}

	public CustomerResponseForDisbursed() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "CustomerResponse [mobilePhone=" + mobilePhone + ", customerID=" + customerID + ", memberName="
				+ memberName + ", dateOfBirth=" + dateOfBirth + ", address=" + address + ", state=" + state
				+ ", postal=" + postal + ", branchId=" + branchId + ", amount=" + amount + ", status=" + status
				+ ", createDate=" + createDate + ", applicationNo=" + applicationNo + "]";
	}

}
