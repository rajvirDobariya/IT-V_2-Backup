package com.suryoday.aocpv.pojo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "AOCPV_LOAN_CREATION")
public class AocpvLoanCreation {
	@Id
	private long applicationNo;
	private long customerId;
	private String customerName;
	private String sanctionedLoanAmount;
	private String rateOfInterest;
	private String tenure;
	private String schemeCode;
	private LocalDate createdDate;
	private LocalDate updetedDate;
	private String status;
	private String loan_status;
	private String loanAccoutNumber;
	private LocalDate loanCreationDate;
	private String targetAccount;
	private String DisbursementAmount;
	private String ExistingLoanBalance;
	private String creditAmount;
	private String disbursalStatus;
	private LocalDate disbursalDate;
	private String branchId;
	private String sancationEMI;
	private long mobileNo;
	private String disbrustTranId;
	private String AcctName;
	private String aadharNo;
	private String cifNumber;
	private String cifMessage;
	private LocalDateTime cifTimeStamp;
	private String accountNumber;
	private String accountMessage;
	private LocalDateTime accountOpeningTimeStamp;
	private String loanAccountMessage;
	private String disbursementMessage;
	@Lob
	private String leegalityResponse;
	private String documentId;
	private String sendAgreement;
	private String isVerify;
	private String documentVerify;
	private String sanctionLetterIsVerify;
	private String aggreementLetterIsVerify;
	private String upload_sancation_letter;
	private String upload_aggreement_letter;
	@Lob
	private String accountData;
	private String clientId;
	
	
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getSanctionLetterIsVerify() {
		return sanctionLetterIsVerify;
	}

	public void setSanctionLetterIsVerify(String sanctionLetterIsVerify) {
		this.sanctionLetterIsVerify = sanctionLetterIsVerify;
	}

	public String getAggreementLetterIsVerify() {
		return aggreementLetterIsVerify;
	}

	public void setAggreementLetterIsVerify(String aggreementLetterIsVerify) {
		this.aggreementLetterIsVerify = aggreementLetterIsVerify;
	}

	public String getAccountData() {
		return accountData;
	}

	public void setAccountData(String accountData) {
		this.accountData = accountData;
	}

	public String getUpload_sancation_letter() {
		return upload_sancation_letter;
	}

	public void setUpload_sancation_letter(String upload_sancation_letter) {
		this.upload_sancation_letter = upload_sancation_letter;
	}

	public String getUpload_aggreement_letter() {
		return upload_aggreement_letter;
	}

	public void setUpload_aggreement_letter(String upload_aggreement_letter) {
		this.upload_aggreement_letter = upload_aggreement_letter;
	}

	public String getSendAgreement() {
		return sendAgreement;
	}

	public void setSendAgreement(String sendAgreement) {
		this.sendAgreement = sendAgreement;
	}

	public String getIsVerify() {
		return isVerify;
	}

	public void setIsVerify(String isVerify) {
		this.isVerify = isVerify;
	}

	public String getDocumentVerify() {
		return documentVerify;
	}

	public void setDocumentVerify(String documentVerify) {
		this.documentVerify = documentVerify;
	}

	public String getLeegalityResponse() {
		return leegalityResponse;
	}

	public void setLeegalityResponse(String leegalityResponse) {
		this.leegalityResponse = leegalityResponse;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public LocalDateTime getCifTimeStamp() {
		return cifTimeStamp;
	}

	public void setCifTimeStamp(LocalDateTime cifTimeStamp) {
		this.cifTimeStamp = cifTimeStamp;
	}

	public LocalDateTime getAccountOpeningTimeStamp() {
		return accountOpeningTimeStamp;
	}

	public void setAccountOpeningTimeStamp(LocalDateTime accountOpeningTimeStamp) {
		this.accountOpeningTimeStamp = accountOpeningTimeStamp;
	}

	public String getAadharNo() {
		return aadharNo;
	}

	public void setAadharNo(String aadharNo) {
		this.aadharNo = aadharNo;
	}

	public String getAcctName() {
		return AcctName;
	}

	public void setAcctName(String acctName) {
		AcctName = acctName.toUpperCase();
	}

	public String getDisbrustTranId() {
		return disbrustTranId;
	}

	public void setDisbrustTranId(String disbrustTranId) {
		this.disbrustTranId = disbrustTranId;
	}

	public long getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(long mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getSancationEMI() {
		return sancationEMI;
	}

	public void setSancationEMI(String sancationEMI) {
		this.sancationEMI = sancationEMI;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public long getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(long applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName.toUpperCase();
	}

	public String getSanctionedLoanAmount() {
		return sanctionedLoanAmount;
	}

	public void setSanctionedLoanAmount(String sanctionedLoanAmount) {
		this.sanctionedLoanAmount = sanctionedLoanAmount;
	}

	public String getRateOfInterest() {
		return rateOfInterest;
	}

	public void setRateOfInterest(String rateOfInterest) {
		this.rateOfInterest = rateOfInterest;
	}

	public String getTenure() {
		return tenure;
	}

	public void setTenure(String tenure) {
		this.tenure = tenure;
	}

	public String getSchemeCode() {
		return schemeCode;
	}

	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDate getUpdetedDate() {
		return updetedDate;
	}

	public void setUpdetedDate(LocalDate updetedDate) {
		this.updetedDate = updetedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status.toUpperCase();
	}

	public String getLoan_status() {
		return loan_status;
	}

	public void setLoan_status(String loan_status) {
		this.loan_status = loan_status.toUpperCase();
	}

	public String getLoanAccoutNumber() {
		return loanAccoutNumber;
	}

	public void setLoanAccoutNumber(String loanAccoutNumber) {
		this.loanAccoutNumber = loanAccoutNumber;
	}

	public LocalDate getLoanCreationDate() {
		return loanCreationDate;
	}

	public void setLoanCreationDate(LocalDate loanCreationDate) {
		this.loanCreationDate = loanCreationDate;
	}

	public String getTargetAccount() {
		return targetAccount;
	}

	public void setTargetAccount(String targetAccount) {
		this.targetAccount = targetAccount;
	}

	public String getDisbursementAmount() {
		return DisbursementAmount;
	}

	public void setDisbursementAmount(String disbursementAmount) {
		DisbursementAmount = disbursementAmount;
	}

	public String getExistingLoanBalance() {
		return ExistingLoanBalance;
	}

	public void setExistingLoanBalance(String existingLoanBalance) {
		ExistingLoanBalance = existingLoanBalance;
	}

	public String getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(String creditAmount) {
		this.creditAmount = creditAmount;
	}

	public String getDisbursalStatus() {
		return disbursalStatus;
	}

	public void setDisbursalStatus(String disbursalStatus) {
		this.disbursalStatus = disbursalStatus.toUpperCase();
	}

	public LocalDate getDisbursalDate() {
		return disbursalDate;
	}

	public void setDisbursalDate(LocalDate disbursalDate) {
		this.disbursalDate = disbursalDate;
	}

	public String getCifNumber() {
		return cifNumber;
	}

	public void setCifNumber(String cifNumber) {
		this.cifNumber = cifNumber;
	}

	public String getCifMessage() {
		return cifMessage;
	}

	public void setCifMessage(String cifMessage) {
		this.cifMessage = cifMessage;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountMessage() {
		return accountMessage;
	}

	public void setAccountMessage(String accountMessage) {
		this.accountMessage = accountMessage;
	}

	public String getLoanAccountMessage() {
		return loanAccountMessage;
	}

	public void setLoanAccountMessage(String loanAccountMessage) {
		this.loanAccountMessage = loanAccountMessage;
	}

	public String getDisbursementMessage() {
		return disbursementMessage;
	}

	public void setDisbursementMessage(String disbursementMessage) {
		this.disbursementMessage = disbursementMessage;
	}

	@Override
	public String toString() {
		return "AocpvLoanCreation [applicationNo=" + applicationNo + ", customerId=" + customerId + ", customerName="
				+ customerName + ", sanctionedLoanAmount=" + sanctionedLoanAmount + ", rateOfInterest=" + rateOfInterest
				+ ", tenure=" + tenure + ", schemeCode=" + schemeCode + ", createdDate=" + createdDate
				+ ", updetedDate=" + updetedDate + ", status=" + status + ", loan_status=" + loan_status
				+ ", loanAccoutNumber=" + loanAccoutNumber + ", loanCreationDate=" + loanCreationDate
				+ ", targetAccount=" + targetAccount + ", DisbursementAmount=" + DisbursementAmount
				+ ", ExistingLoanBalance=" + ExistingLoanBalance + ", creditAmount=" + creditAmount
				+ ", disbursalStatus=" + disbursalStatus + ", disbursalDate=" + disbursalDate + ", branchId=" + branchId
				+ ", sancationEMI=" + sancationEMI + ", mobileNo=" + mobileNo + ", disbrustTranId=" + disbrustTranId
				+ ", AcctName=" + AcctName + ", aadharNo=" + aadharNo + ", cifNumber=" + cifNumber + ", cifMessage="
				+ cifMessage + ", accountNumber=" + accountNumber + ", accountMessage=" + accountMessage
				+ ", loanAccountMessage=" + loanAccountMessage + ", disbursementMessage=" + disbursementMessage + "]";
	}

}
