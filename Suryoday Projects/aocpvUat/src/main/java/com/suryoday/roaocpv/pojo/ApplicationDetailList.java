package com.suryoday.roaocpv.pojo;

public class ApplicationDetailList {

	private String applicationId;
	private String name;
	private String mobileNo;
	private String status;
	private String requiredAmount;
	private String appNoWithProductCode;

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

	public String getRequiredAmount() {
		return requiredAmount;
	}

	public void setRequiredAmount(String requiredAmount) {
		this.requiredAmount = requiredAmount;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public ApplicationDetailList(String applicationId, String name, String mobileNo, String status,
			String requiredAmount, String appNoWithProductCode) {
		super();
		this.applicationId = applicationId;
		this.name = name;
		this.mobileNo = mobileNo;
		this.status = status;
		this.requiredAmount = requiredAmount;
		this.appNoWithProductCode = appNoWithProductCode;
	}

	public ApplicationDetailList() {
		super();
		// TODO Auto-generated constructor stub
	}

}
