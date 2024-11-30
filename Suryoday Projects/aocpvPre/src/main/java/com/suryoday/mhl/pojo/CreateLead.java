package com.suryoday.mhl.pojo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_LEAD")
public class CreateLead {
	@Id
	private String applicationNo;
	private String product;
	private String loanPurpose;
	private String requiredLoanAmount;
	private String tenure;
	private String sourceType;
	private String sourceName;
	private String empId;
	private String createdBy;
	private String createdName;
	private String name;
	private String status;
	private String branchId;
	private String branchName;
	private LocalDateTime createdDate; 
	private LocalDateTime updatedDate;
	
	
	public String getApplicationNo() {
		return applicationNo;
	}
	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product.toUpperCase();
	}
	public String getLoanPurpose() {
		return loanPurpose;
	}
	public void setLoanPurpose(String loanPurpose) {
		this.loanPurpose = loanPurpose.toUpperCase();
	}
	public String getRequiredLoanAmount() {
		return requiredLoanAmount;
	}
	public void setRequiredLoanAmount(String requiredLoanAmount) {
		this.requiredLoanAmount = requiredLoanAmount;
	}
	public String getTenure() {
		return tenure;
	}
	public void setTenure(String tenure) {
		this.tenure = tenure;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType.toUpperCase();
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName.toUpperCase();
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy.toUpperCase();
	}
	public String getCreatedName() {
		return createdName;
	}
	public void setCreatedName(String createdName) {
		this.createdName = createdName.toUpperCase();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name.toUpperCase();
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status.toUpperCase();
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName.toUpperCase();
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
	@Override
	public String toString() {
		return "CreateLead [applicationNo=" + applicationNo + ", product=" + product + ", loanPurpose=" + loanPurpose
				+ ", requiredLoanAmount=" + requiredLoanAmount + ", tenure=" + tenure + ", sourceType=" + sourceType
				+ ", sourceName=" + sourceName + ", empId=" + empId + ", createdBy=" + createdBy + ", createdName="
				+ createdName + ", name=" + name + ", status=" + status + ", branchId=" + branchId + ", branchName="
				+ branchName + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + "]";
	} 
	
	
}
