package com.suryoday.aocpv.pojo;

public class FetchByCustResponse {
 private long applicationNo;
 private long customerID;
 private String memberName;
 private long mobilePhone;
 private String branchId;
 private String status;
 private String createDate;
 private String updateDate;
 private String appNoWithProductCode;
 
public String getAppNoWithProductCode() {
	return appNoWithProductCode;
}
public void setAppNoWithProductCode(String appNoWithProductCode) {
	this.appNoWithProductCode = appNoWithProductCode;
}
public long getApplicationNo() {
	return applicationNo;
}
public void setApplicationNo(long applicationNo) {
	this.applicationNo = applicationNo;
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
public long getMobilePhone() {
	return mobilePhone;
}
public void setMobilePhone(long mobilePhone) {
	this.mobilePhone = mobilePhone;
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
public FetchByCustResponse(long applicationNo, long customerID, String memberName, long mobilePhone, String branchId,
		String status, String createDate, String updateDate,String appNoWithProductCode) {
	super();
	this.applicationNo = applicationNo;
	this.customerID = customerID;
	this.memberName = memberName;
	this.mobilePhone = mobilePhone;
	this.branchId = branchId;
	this.status = status;
	this.createDate = createDate;
	this.updateDate = updateDate;
	this.appNoWithProductCode = appNoWithProductCode;
}
public FetchByCustResponse() {
	super();
	// TODO Auto-generated constructor stub
}
@Override
public String toString() {
	return "FetchByCustResponse [applicationNo=" + applicationNo + ", customerID=" + customerID + ", memberName="
			+ memberName + ", mobilePhone=" + mobilePhone + ", branchId=" + branchId + ", status=" + status
			+ ", createDate=" + createDate + ", updateDate=" + updateDate + "]";
}

 
}
