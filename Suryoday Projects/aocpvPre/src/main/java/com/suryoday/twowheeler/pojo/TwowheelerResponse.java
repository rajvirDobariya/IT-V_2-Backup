package com.suryoday.twowheeler.pojo;

import java.time.LocalDateTime;

public class TwowheelerResponse {

	private String applicationNo;
	private String appNoWithProductCode;
	private LocalDateTime createdTimestamp;
	private LocalDateTime updatedTimestamp;
	private String status;
	private String salesBranchId;
	private String name;
	private String mobileNo;
	private String customerId;
	private String stage;
	private String flowStatus;
	private String salesCreatedBy;
	private String listType;
	private String preApprovalAmount;
	
	public String getPreApprovalAmount() {
		return preApprovalAmount;
	}
	public void setPreApprovalAmount(String preApprovalAmount) {
		this.preApprovalAmount = preApprovalAmount;
	}
	public String getListType() {
		return listType;
	}
	public void setListType(String listType) {
		this.listType = listType;
	}
	public String getSalesCreatedBy() {
		return salesCreatedBy;
	}
	public void setSalesCreatedBy(String salesCreatedBy) {
		this.salesCreatedBy = salesCreatedBy;
	}
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	public String getFlowStatus() {
		return flowStatus;
	}
	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}
	public String getApplicationNo() {
		return applicationNo;
	}
	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}
	public String getAppNoWithProductCode() {
		return appNoWithProductCode;
	}
	public void setAppNoWithProductCode(String appNoWithProductCode) {
		this.appNoWithProductCode = appNoWithProductCode;
	}
	public LocalDateTime getCreatedTimestamp() {
		return createdTimestamp;
	}
	public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	public LocalDateTime getUpdatedTimestamp() {
		return updatedTimestamp;
	}
	public void setUpdatedTimestamp(LocalDateTime updatedTimestamp) {
		this.updatedTimestamp = updatedTimestamp;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSalesBranchId() {
		return salesBranchId;
	}
	public void setSalesBranchId(String salesBranchId) {
		this.salesBranchId = salesBranchId;
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
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public TwowheelerResponse(String applicationNo, String appNoWithProductCode, LocalDateTime createdTimestamp,
			LocalDateTime updatedTimestamp, String status, String salesBranchId, String name, String mobileNo,
			String customerId, String stage, String flowStatus,String salesCreatedBy,String listType,String preApprovalAmount) {
		super();
		this.applicationNo = applicationNo;
		this.appNoWithProductCode = appNoWithProductCode;
		this.createdTimestamp = createdTimestamp;
		this.updatedTimestamp = updatedTimestamp;
		this.status = status;
		this.salesBranchId = salesBranchId;
		this.name = name;
		this.mobileNo = mobileNo;
		this.customerId = customerId;
		this.stage = stage;
		this.flowStatus = flowStatus;
		this.salesCreatedBy = salesCreatedBy;
		this.listType = listType;
		this.preApprovalAmount = preApprovalAmount;
	}
	public TwowheelerResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
