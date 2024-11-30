package com.suryoday.dsaOnboard.pojo;

import java.time.LocalDateTime;
import java.util.List;

public class LoginResponse {
	private long applicationNo;
	private String entity;
	private String companyName;
	private String emailId;
	private boolean emailIdVerify;
	private String mobileNo;
	private boolean mobileNoVerify;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	private String status;
	private String flowStatus;
	private String branchId;
	private String leadId;
	private String leadName;
	private String typeOfRelationship;
	private List<String> productType;
	private String noOfPartner;
	private String constitutionType;

//	private String sessionId;
//	private String userRoleName;
//	private List<PermissionDto> permissions;
	public long getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(long applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public boolean isEmailIdVerify() {
		return emailIdVerify;
	}

	public void setEmailIdVerify(boolean emailIdVerify) {
		this.emailIdVerify = emailIdVerify;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public boolean isMobileNoVerify() {
		return mobileNoVerify;
	}

	public void setMobileNoVerify(boolean mobileNoVerify) {
		this.mobileNoVerify = mobileNoVerify;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
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

	public String getLeadId() {
		return leadId;
	}

	public void setLeadId(String leadId) {
		this.leadId = leadId;
	}

	public String getLeadName() {
		return leadName;
	}

	public void setLeadName(String leadName) {
		this.leadName = leadName;
	}

	public String getTypeOfRelationship() {
		return typeOfRelationship;
	}

	public void setTypeOfRelationship(String typeOfRelationship) {
		this.typeOfRelationship = typeOfRelationship;
	}

	public List<String> getProductType() {
		return productType;
	}

	public void setProductType(List<String> productType) {
		this.productType = productType;
	}

	public String getNoOfPartner() {
		return noOfPartner;
	}

	public void setNoOfPartner(String noOfPartner) {
		this.noOfPartner = noOfPartner;
	}

	public String getConstitutionType() {
		return constitutionType;
	}

	public void setConstitutionType(String constitutionType) {
		this.constitutionType = constitutionType;
	}

}
