package com.suryoday.aocpv.pojo;

public class CustomerResponseWeb {

	private long mobilePhone;
	private long customerID;
	private String memberName;
	private String branchId;
	private String status;
	private String createDate;
	private String updateDate;
	private String listType;
	private String appNoWithProductCode;
	private String createdBy;
	private long applicationNo;

	public long getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(long applicationNo) {
		this.applicationNo = applicationNo;
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

	public String getListType() {
		return listType;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}

	public String getAppNoWithProductCode() {
		return appNoWithProductCode;
	}

	public void setAppNoWithProductCode(String appNoWithProductCode) {
		this.appNoWithProductCode = appNoWithProductCode;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public CustomerResponseWeb(long mobilePhone, long customerID, String memberName, String branchId, String status,
			String createDate, String updateDate, String listType, String appNoWithProductCode, String createdBy,
			long applicationNo) {
		super();
		this.mobilePhone = mobilePhone;
		this.customerID = customerID;
		this.memberName = memberName;
		this.branchId = branchId;
		this.status = status;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.listType = listType;
		this.appNoWithProductCode = appNoWithProductCode;
		this.createdBy = createdBy;
		this.applicationNo = applicationNo;
	}

	public CustomerResponseWeb() {
		super();
		// TODO Auto-generated constructor stub
	}

}
