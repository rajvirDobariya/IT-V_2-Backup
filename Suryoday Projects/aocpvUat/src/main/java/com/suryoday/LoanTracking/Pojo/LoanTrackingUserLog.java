package com.suryoday.LoanTracking.Pojo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_loantracking_userlog")
public class LoanTrackingUserLog {
	@Id
	private long id;
	private long applicationNo;
	private String currentStage;
	private String remarks;
	private String createdBy;
	private String assignTo;
	private String fromStatus;
	private String toStatus;
	private String appNoWithProductCode;
	private String productType;
	private LocalDateTime startDate;
	private LocalDateTime endDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(long applicationNo) {
		this.applicationNo = applicationNo;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getAssignTo() {
		return assignTo;
	}

	public void setAssignTo(String assignTo) {
		this.assignTo = assignTo;
	}

	public String getFromStatus() {
		return fromStatus;
	}

	public void setFromStatus(String fromStatus) {
		this.fromStatus = fromStatus;
	}

	public String getToStatus() {
		return toStatus;
	}

	public void setToStatus(String toStatus) {
		this.toStatus = toStatus;
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

	@Override
	public String toString() {
		return "LoanTrackingUserLog [id=" + id + ", applicationNo=" + applicationNo + ", currentStage=" + currentStage
				+ ", remarks=" + remarks + ", createdBy=" + createdBy + ", assignTo=" + assignTo + ", fromStatus="
				+ fromStatus + ", toStatus=" + toStatus + ", appNoWithProductCode=" + appNoWithProductCode
				+ ", productType=" + productType + ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}
}
