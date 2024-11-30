package com.suryoday.collections.pojo;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "update_trail")
public class UpdateTrailBean {
	
	 @Id
	 @GeneratedValue
	 @Column(name = "id")
	
	 private int id;
	 private String customerId;
	 private String accountNo;
	 private String customerName;
	 private String customerMate;
	 private String dispositionCode;
	 private String shortDescp;
	 private String ptpnextactionDate;
	 private String nextaction;
	 private String ptpAmount;
	 private String emiAmount;
	 private String assignTo;
	 private String reasonforAssigning;
	 private String newcontactNo;
	 private String reasonforNonpayment;
	 private String remarks;
	 private String branchId;
	 private LocalDate createdDate;
	 private LocalDate updatedDate;
	 private LocalDate dueDate;
	 
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerMate() {
		return customerMate;
	}
	public void setCustomerMate(String customerMate) {
		this.customerMate = customerMate;
	}
	public String getDispositionCode() {
		return dispositionCode;
	}
	public void setDispositionCode(String dispositionCode) {
		this.dispositionCode = dispositionCode;
	}
	public String getShortDescp() {
		return shortDescp;
	}
	public void setShortDescp(String shortDescp) {
		this.shortDescp = shortDescp;
	}
	public String getPtpnextactionDate() {
		return ptpnextactionDate;
	}
	public void setPtpnextactionDate(String ptpnextactionDate) {
		this.ptpnextactionDate = ptpnextactionDate;
	}
	public String getNextaction() {
		return nextaction;
	}
	public void setNextaction(String nextaction) {
		this.nextaction = nextaction;
	}
	public String getPtpAmount() {
		return ptpAmount;
	}
	public void setPtpAmount(String ptpAmount) {
		this.ptpAmount = ptpAmount;
	}
	public String getEmiAmount() {
		return emiAmount;
	}
	public void setEmiAmount(String emiAmount) {
		this.emiAmount = emiAmount;
	}
	public String getAssignTo() {
		return assignTo;
	}
	public void setAssignTo(String assignTo) {
		this.assignTo = assignTo;
	}
	public String getReasonforAssigning() {
		return reasonforAssigning;
	}
	public void setReasonforAssigning(String reasonforAssigning) {
		this.reasonforAssigning = reasonforAssigning;
	}
	public String getNewcontactNo() {
		return newcontactNo;
	}
	public void setNewcontactNo(String newcontactNo) {
		this.newcontactNo = newcontactNo;
	}
	public String getReasonforNonpayment() {
		return reasonforNonpayment;
	}
	public void setReasonforNonpayment(String reasonforNonpayment) {
		this.reasonforNonpayment = reasonforNonpayment;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public LocalDate getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}
	public LocalDate getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(LocalDate updatedDate) {
		this.updatedDate = updatedDate;
	}
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	
	@Override
	public String toString() {
		return "UpdateTrailBean [id=" + id + ", customerId=" + customerId + ", accountNo=" + accountNo
				+ ", customerName=" + customerName + ", customerMate=" + customerMate + ", dispositionCode="
				+ dispositionCode + ", shortDescp=" + shortDescp + ", ptpnextactionDate=" + ptpnextactionDate
				+ ", nextaction=" + nextaction + ", ptpAmount=" + ptpAmount + ", emiAmount=" + emiAmount + ", assignTo="
				+ assignTo + ", reasonforAssigning=" + reasonforAssigning + ", newcontactNo=" + newcontactNo
				+ ", reasonforNonpayment=" + reasonforNonpayment + ", remarks=" + remarks + ", branchId=" + branchId
				+ ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + ", dueDate=" + dueDate + "]";
	}
}
