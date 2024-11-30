package com.suryoday.hastakshar.pojo;

import java.time.LocalDateTime;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hastakshar_ReqStatus")
public class HastReqStatus {

	@Id
	@Column(nullable = false, unique = true)
	private String applictioNO;
	private String amount;
	private String department;
	private String transactionTypes;
	private String transactionDescription;
	
	@Column(name="transaction_description_v2")
	private String transactionDescriptionV2;
	private String nature;
	private String product;
	private byte[] attachment;
	private String policyNo;
	private String approver1;
	private String approver2;
	private String approver3;
	private String approver4;
	private String approver5;
	private String requestBy;
	private String remark;
	private String status;
	private String requestState;
	private String RequestFlow;
	private LocalDateTime createDate;
	private LocalDateTime updateDate;
	private String accountNo;
	private String keyword;
	private String CIF;
	private String lan;
	private String tat48Date;
	private Boolean emailSent;
	private String reportingManagerEmailId;
	private String mobileNo;
	private String linkToCIF;
	private String branchCode;
	private String margin;
	private String fdValue;

	public HastReqStatus() {
		super();
	}

	public HastReqStatus(String applictioNO, String amount, String department, String transactionTypes,
			String transactionDescription, String nature, String product, byte[] attachment, String policyNo,
			String approver1, String approver2, String approver3, String approver4, String approver5, String requestBy,
			String remark, String status, String requestState, String requestFlow, LocalDateTime createDate,
			LocalDateTime updateDate, String accountNo, String keyword, String cIF, String lan) {
		super();
		this.applictioNO = applictioNO;
		this.amount = amount;
		this.department = department;
		this.transactionTypes = transactionTypes;
		this.transactionDescription = transactionDescription;
		this.nature = nature;
		this.product = product;
		this.attachment = attachment;
		this.policyNo = policyNo;
		this.approver1 = approver1;
		this.approver2 = approver2;
		this.approver3 = approver3;
		this.approver4 = approver4;
		this.approver5 = approver5;
		this.requestBy = requestBy;
		this.remark = remark;
		this.status = status;
		this.requestState = requestState;
		RequestFlow = requestFlow;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.accountNo = accountNo;
		this.keyword = keyword;
		CIF = cIF;
		this.lan = lan;
	}

	public String getReportingManagerEmailId() {
		return reportingManagerEmailId;
	}

	public void setReportingManagerEmailId(String reportingManagerEmailId) {
		this.reportingManagerEmailId = reportingManagerEmailId;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getCIF() {
		return CIF;
	}

	public void setCIF(String cIF) {
		CIF = cIF;
	}

	public String getApplictioNO() {
		return applictioNO;
	}

	public void setApplictioNO(String applictioNO) {
		this.applictioNO = applictioNO;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDepartment() {
		return department;
	}

	public byte[] getAttachment() {
		return attachment;
	}

	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getTransactionTypes() {
		return transactionTypes;
	}

	public void setTransactionTypes(String transactionTypes) {
		this.transactionTypes = transactionTypes;
	}

	public String getTransactionDescription() {
		return transactionDescription;
	}

	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getApprover1() {
		return approver1;
	}

	public void setApprover1(String approver1) {
		this.approver1 = approver1;
	}

	public String getApprover2() {
		return approver2;
	}

	public void setApprover2(String approver2) {
		this.approver2 = approver2;
	}

	public String getApprover3() {
		return approver3;
	}

	public void setApprover3(String approver3) {
		this.approver3 = approver3;
	}

	public String getApprover4() {
		return approver4;
	}

	public void setApprover4(String approver4) {
		this.approver4 = approver4;
	}

	public String getApprover5() {
		return approver5;
	}

	public void setApprover5(String approver5) {
		this.approver5 = approver5;
	}

	public String getRequestBy() {
		return requestBy;
	}

	public void setRequestBy(String requestBy) {
		this.requestBy = requestBy;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRequestState() {
		return requestState;
	}

	public void setRequestState(String requestState) {
		this.requestState = requestState;
	}

	public String getRequestFlow() {
		return RequestFlow;
	}

	public void setRequestFlow(String requestFlow) {
		RequestFlow = requestFlow;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

	public String getLan() {
		return lan;
	}

	public void setLan(String lan) {
		this.lan = lan;
	}

	public String getTat48Date() {
		return tat48Date;
	}

	public void setTat48Date(String tat48Date) {
		this.tat48Date = tat48Date;
	}

	public Boolean getEmailSent() {
		return emailSent;
	}

	public void setEmailSent(Boolean emailSent) {
		this.emailSent = emailSent;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getLinkToCIF() {
		return linkToCIF;
	}

	public void setLinkToCIF(String linkToCIF) {
		this.linkToCIF = linkToCIF;
	}
	
	public String getTransactionDescriptionV2() {
		return transactionDescriptionV2;
	}

	public void setTransactionDescriptionV2(String transactionDescriptionV2) {
		this.transactionDescriptionV2 = transactionDescriptionV2;
	}
	
	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	
	public String getMargin() {
		return margin;
	}

	public void setMargin(String margin) {
		this.margin = margin;
	}

	public String getFdValue() {
		return fdValue;
	}

	public void setFdValue(String fdValue) {
		this.fdValue = fdValue;
	}

	@Override
	public String toString() {
		return "HastReqStatus [applictioNO=" + applictioNO + ", amount=" + amount + ", department=" + department
				+ ", transactionTypes=" + transactionTypes + ", transactionDescription=" + transactionDescription
				+ ", transactionDescriptionV2=" + transactionDescriptionV2 + ", nature=" + nature + ", product="
				+ product + ", attachment=" + Arrays.toString(attachment) + ", policyNo=" + policyNo + ", approver1="
				+ approver1 + ", approver2=" + approver2 + ", approver3=" + approver3 + ", approver4=" + approver4
				+ ", approver5=" + approver5 + ", requestBy=" + requestBy + ", remark=" + remark + ", status=" + status
				+ ", requestState=" + requestState + ", RequestFlow=" + RequestFlow + ", createDate=" + createDate
				+ ", updateDate=" + updateDate + ", accountNo=" + accountNo + ", keyword=" + keyword + ", CIF=" + CIF
				+ ", lan=" + lan + ", tat48Date=" + tat48Date + ", emailSent=" + emailSent
				+ ", reportingManagerEmailId=" + reportingManagerEmailId + ", mobileNo=" + mobileNo + ", linkToCIF="
				+ linkToCIF + ", branchCode=" + branchCode + ", margin=" + margin + ", fdValue=" + fdValue + "]";
	}

}