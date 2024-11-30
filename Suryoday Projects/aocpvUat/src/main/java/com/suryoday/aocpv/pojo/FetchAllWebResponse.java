package com.suryoday.aocpv.pojo;

public class FetchAllWebResponse {

	private long applicationNo;
	private long customerId;
	private String name;
	private long mobileNo;
	private String dob;
	private String createBy;
	private String creationDate;
	private String updatedate;
	private String status;
	private String appNoWithProductCode;
	private String branchId;

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getAppNoWithProductCode() {
		return appNoWithProductCode;
	}

	public void setAppNoWithProductCode(String appNoWithProductCode) {
		this.appNoWithProductCode = appNoWithProductCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUpdatedate() {
		return updatedate;
	}

	public void setUpdatedate(String updatedate) {
		this.updatedate = updatedate;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public long getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(long applicationNo) {
		this.applicationNo = applicationNo;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(long mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public FetchAllWebResponse(long applicationNo, long customerId, String name, long mobileNo, String dob,
			String createBy, String creationDate, String updateDate, String status, String appNoWithProductCode,
			String branchId) {
		super();
		this.applicationNo = applicationNo;
		this.customerId = customerId;
		this.name = name;
		this.mobileNo = mobileNo;
		this.dob = dob;
		this.createBy = createBy;
		this.creationDate = creationDate;
		this.updatedate = updateDate;
		this.status = status;
		this.appNoWithProductCode = appNoWithProductCode;
		this.branchId = branchId;
	}

	public FetchAllWebResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "FetchAllWebResponse [applicationNo=" + applicationNo + ", customerId=" + customerId + ", name=" + name
				+ ", mobileNo=" + mobileNo + ", dob=" + dob + ", createBy=" + createBy + ", creationDate="
				+ creationDate + ", updatedate=" + updatedate + "]";
	}

}
