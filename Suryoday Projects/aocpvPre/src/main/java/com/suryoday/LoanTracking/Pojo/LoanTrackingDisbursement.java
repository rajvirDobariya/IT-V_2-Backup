package com.suryoday.LoanTracking.Pojo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tbl_loantracking_disbursement")
public class LoanTrackingDisbursement {
	@Id
	private long applicationId;
	private String createdBy;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private String currentStage;
	private String remarks;
	private String name;
	private String panNo;
	private String isVerify;
	private String loanAmount;
	private String branchId;
	private String assignTo;
	private String assignRole;
	private String productType;
	private String city;
	private String state;
	private String appNoWithProductCode;
	private String isLocked;
	public long getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(long applicationId) {
		this.applicationId = applicationId;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public LocalDateTime getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}
	public LocalDateTime getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}
	public String getCurrentStage() {
		return currentStage;
	}
	public void setCurrentStage(String currentStage) {
		this.currentStage = currentStage;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPanNo() {
		return panNo;
	}
	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}
	public String getIsVerify() {
		return isVerify;
	}
	public void setIsVerify(String isVerify) {
		this.isVerify = isVerify;
	}
	public String getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getAssignTo() {
		return assignTo;
	}
	public void setAssignTo(String assignTo) {
		this.assignTo = assignTo;
	}
	public String getAssignRole() {
		return assignRole;
	}
	public void setAssignRole(String assignRole) {
		this.assignRole = assignRole;
	}
	public String getAppNoWithProductCode() {
		return appNoWithProductCode;
	}
	public void setAppNoWithProductCode(String appNoWithProductCode) {
		this.appNoWithProductCode = appNoWithProductCode;
	}
	
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
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
	public String getIsLocked() {
		return isLocked;
	}
	public void setIsLocked(String isLocked) {
		this.isLocked = isLocked;
	}
	@Override
	public String toString() {
		return "LoanTrackingDisbursement [applicationId=" + applicationId + ", createdBy=" + createdBy + ", startDate="
				+ startDate + ", endDate=" + endDate + ", currentStage=" + currentStage + ", remarks=" + remarks
				+ ", name=" + name + ", panNo=" + panNo + ", isVerify=" + isVerify + ", loanAmount=" + loanAmount
				+ ", branchId=" + branchId + ", assignTo=" + assignTo + ", assignRole=" + assignRole + ", productType="
				+ productType + ", city=" + city + ", state=" + state + ", appNoWithProductCode=" + appNoWithProductCode
				+ ", isLocked=" + isLocked + "]";
	}
	
	
			
}
