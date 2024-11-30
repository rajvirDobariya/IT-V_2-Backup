package com.suryoday.aocpv.pojo;

import java.time.LocalDate;

public class PreApprovedList {
	
	private long mobilePhone;
	private String referenceNo;
	private Long customerID;
	private String memberName;
	private long branchId;
	private double amount;
	private String status;
	private LocalDate createDate;
	private LocalDate updatedDate;
	private String city;
	private String state;
	private String area;
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
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
	public LocalDate getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(LocalDate updatedDate) {
		this.updatedDate = updatedDate;
	}
	public PreApprovedList(long mobilePhone, String referenceNo, Long customerID, String memberName, long branchId,
			double amount, String status, LocalDate createDate, LocalDate updatedDate,String city,String state,String area) {
		super();
		this.mobilePhone = mobilePhone;
		this.referenceNo = referenceNo;
		this.customerID = customerID;
		this.memberName = memberName;
		this.branchId = branchId;
		this.amount = amount;
		this.status = status;
		this.createDate = createDate;
		this.updatedDate = updatedDate;
		this.city = city;
		this.state = state;
		this.area = area;
	}
	@Override
	public String toString() {
		return "PreApprovedList [mobilePhone=" + mobilePhone + ", referenceNo=" + referenceNo + ", customerID="
				+ customerID + ", memberName=" + memberName + ", branchId=" + branchId + ", amount=" + amount
				+ ", status=" + status + ", createDate=" + createDate + ", updatedDate=" + updatedDate + "]";
	}
	
	
}
