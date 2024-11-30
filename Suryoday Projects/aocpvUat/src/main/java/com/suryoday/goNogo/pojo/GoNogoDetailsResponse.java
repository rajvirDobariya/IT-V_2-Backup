package com.suryoday.goNogo.pojo;

public class GoNogoDetailsResponse {

	private String name;
	private String dateOfBirth;
	private String mobileNo;
	private String status;
	private String flowStatus;
	private String branchId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public GoNogoDetailsResponse(String name, String dateOfBirth, String mobileNo, String status, String flowStatus,
			String branchId) {
		super();
		this.name = name;
		this.dateOfBirth = dateOfBirth;
		this.mobileNo = mobileNo;
		this.status = status;
		this.flowStatus = flowStatus;
		this.branchId = branchId;
	}

	public GoNogoDetailsResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

}
