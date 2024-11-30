package com.suryoday.dsaOnboard.pojo;

import java.time.LocalDateTime;

public class BankDetailsResponse {
	
	private long applicationNo;
	private String ifscCode;
	private String bankAccountNo;
	private String bankName;
	private String branchName;
	private String accountholderName;
	private LocalDateTime updatedDate;
	private String status;
	private String flowStatus;
	private String bankDetailsVerify;
	
	public String getBankDetailsVerify() {
		return bankDetailsVerify;
	}
	public void setBankDetailsVerify(String bankDetailsVerify) {
		this.bankDetailsVerify = bankDetailsVerify;
	}
	public long getApplicationNo() {
		return applicationNo;
	}
	public void setApplicationNo(long applicationNo) {
		this.applicationNo = applicationNo;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getBankAccountNo() {
		return bankAccountNo;
	}
	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getAccountholderName() {
		return accountholderName;
	}
	public void setAccountholderName(String accountholderName) {
		this.accountholderName = accountholderName;
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
	
}
