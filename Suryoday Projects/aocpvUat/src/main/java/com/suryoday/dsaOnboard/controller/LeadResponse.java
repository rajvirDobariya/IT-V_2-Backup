package com.suryoday.dsaOnboard.controller;

public class LeadResponse {
	private String userId;
	private String userName;
	private long branchId;
	private long isActive;
	private String designation;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getBranchId() {
		return branchId;
	}

	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}

	public long getIsActive() {
		return isActive;
	}

	public void setIsActive(long isActive) {
		this.isActive = isActive;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public LeadResponse(String userId, String userName, long branchId, long isActive, String designation) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.branchId = branchId;
		this.isActive = isActive;
		this.designation = designation;
	}

	public LeadResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

}
