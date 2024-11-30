package com.suryoday.aocpv.pojo;

public class RetrieveReportResponse {
	private long mobilePhone;
	private long customerID;
	private String memberName;
	private String dateOfBirth;
	private String address;
	private String state;
	private String postal;
	private String branchId;
	private String	status;
	private String createDate;
	private String updateDate;
	private String RequestLoan;
	private double maxEmieligibility;
	private String tenure;
	private String InterestRate;
	public RetrieveReportResponse(long mobilePhone, long customerID, String memberName, String dateOfBirth,
			String address, String state, String postal, String branchId, String status, String createDate,
			String updateDate, String requestLoan, double maxEmieligibility, String tenure, String interestRate) {
		super();
		this.mobilePhone = mobilePhone;
		this.customerID = customerID;
		this.memberName = memberName;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
		this.state = state;
		this.postal = postal;
		this.branchId = branchId;
		this.status = status;
		this.createDate = createDate;
		this.updateDate = updateDate;
		RequestLoan = requestLoan;
		this.maxEmieligibility = maxEmieligibility;
		this.tenure = tenure;
		InterestRate = interestRate;
	}
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
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getRequestLoan() {
		return RequestLoan;
	}
	public void setRequestLoan(String requestLoan) {
		RequestLoan = requestLoan;
	}
	public double getMaxEmieligibility() {
		return maxEmieligibility;
	}
	public void setMaxEmieligibility(double maxEmieligibility) {
		this.maxEmieligibility = maxEmieligibility;
	}
	public String getTenure() {
		return tenure;
	}
	public void setTenure(String tenure) {
		this.tenure = tenure;
	}
	public String getInterestRate() {
		return InterestRate;
	}
	public void setInterestRate(String interestRate) {
		InterestRate = interestRate;
	}
	@Override
	public String toString() {
		return "RetrieveReportResponse [mobilePhone=" + mobilePhone + ", customerID=" + customerID + ", memberName="
				+ memberName + ", dateOfBirth=" + dateOfBirth + ", address=" + address + ", state=" + state
				+ ", postal=" + postal + ", branchId=" + branchId + ", status=" + status + ", createDate=" + createDate
				+ ", updateDate=" + updateDate + ", RequestLoan=" + RequestLoan + ", maxEmieligibility="
				+ maxEmieligibility + ", tenure=" + tenure + ", InterestRate=" + InterestRate + "]";
	}
	
}
