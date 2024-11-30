package com.suryoday.twowheeler.pojo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class TwowheelerLoanCreation {
	@Id
	private String applicationNo;
	private LocalDateTime createdTimestamp;
	private LocalDateTime updatedTimestamp;
	private String status;
	private String customerId;
	private String sanctionedLoanAmount;
	private String disbursalAmount;
	private boolean cifCreation;
	private boolean accountOpening;
	private boolean loanCreation;
	private boolean collateralDetails;
	private boolean disbusrment;
	private boolean IMPS;
	private boolean CRM;
	private String loanAccoutNumber;
	private String targetAccount;
	private String collateralNo;
	private String disbursedTransId;
	private String listType;
	private String errorResponse;
	private Boolean spdc;
	private String spdcReferenceNumber;
	@Lob
	private String spdcRequest;
	@Lob
	private String spdcResponse;
	@Lob
	private String crmModificationRequest;
	@Lob
	private String loanCreationRequest;
	@Lob
	private String collateralDetailsRequest;
	@Lob
	private String disbursementRequest;
	@Lob
	private String crmModificationResponse;
	@Lob
	private String loanCreationResponse;
	@Lob
	private String collateralDetailsResponse;
	@Lob
	private String disbursementResponse;
	@Lob
	private String cifCreationRequest;
	@Lob
	private String cifCreationResponse;

	public String getSpdcReferenceNumber() {
		return spdcReferenceNumber;
	}

	public void setSpdcReferenceNumber(String spdcReferenceNumber) {
		this.spdcReferenceNumber = spdcReferenceNumber;
	}

	public Boolean getSpdc() {
		return spdc;
	}

	public void setSpdc(Boolean spdc) {
		this.spdc = spdc;
	}

	public String getSpdcRequest() {
		return spdcRequest;
	}

	public void setSpdcRequest(String spdcRequest) {
		this.spdcRequest = spdcRequest;
	}

	public String getSpdcResponse() {
		return spdcResponse;
	}

	public void setSpdcResponse(String spdcResponse) {
		this.spdcResponse = spdcResponse;
	}

	public String getCifCreationRequest() {
		return cifCreationRequest;
	}

	public void setCifCreationRequest(String cifCreationRequest) {
		this.cifCreationRequest = cifCreationRequest;
	}

	public String getCifCreationResponse() {
		return cifCreationResponse;
	}

	public void setCifCreationResponse(String cifCreationResponse) {
		this.cifCreationResponse = cifCreationResponse;
	}

	private LocalDate loanCreationDate;

	public LocalDate getLoanCreationDate() {
		return loanCreationDate;
	}

	public void setLoanCreationDate(LocalDate loanCreationDate) {
		this.loanCreationDate = loanCreationDate;
	}

	public String getCrmModificationResponse() {
		return crmModificationResponse;
	}

	public void setCrmModificationResponse(String crmModificationResponse) {
		this.crmModificationResponse = crmModificationResponse;
	}

	public String getLoanCreationResponse() {
		return loanCreationResponse;
	}

	public void setLoanCreationResponse(String loanCreationResponse) {
		this.loanCreationResponse = loanCreationResponse;
	}

	public String getCollateralDetailsResponse() {
		return collateralDetailsResponse;
	}

	public void setCollateralDetailsResponse(String collateralDetailsResponse) {
		this.collateralDetailsResponse = collateralDetailsResponse;
	}

	public String getDisbursementResponse() {
		return disbursementResponse;
	}

	public void setDisbursementResponse(String disbursementResponse) {
		this.disbursementResponse = disbursementResponse;
	}

	public String getCrmModificationRequest() {
		return crmModificationRequest;
	}

	public void setCrmModificationRequest(String crmModificationRequest) {
		this.crmModificationRequest = crmModificationRequest;
	}

	public String getLoanCreationRequest() {
		return loanCreationRequest;
	}

	public void setLoanCreationRequest(String loanCreationRequest) {
		this.loanCreationRequest = loanCreationRequest;
	}

	public String getCollateralDetailsRequest() {
		return collateralDetailsRequest;
	}

	public void setCollateralDetailsRequest(String collateralDetailsRequest) {
		this.collateralDetailsRequest = collateralDetailsRequest;
	}

	public String getDisbursementRequest() {
		return disbursementRequest;
	}

	public void setDisbursementRequest(String disbursementRequest) {
		this.disbursementRequest = disbursementRequest;
	}

	public String getListType() {
		return listType;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
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

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getSanctionedLoanAmount() {
		return sanctionedLoanAmount;
	}

	public void setSanctionedLoanAmount(String sanctionedLoanAmount) {
		this.sanctionedLoanAmount = sanctionedLoanAmount;
	}

	public String getDisbursalAmount() {
		return disbursalAmount;
	}

	public void setDisbursalAmount(String disbursalAmount) {
		this.disbursalAmount = disbursalAmount;
	}

	public boolean isCifCreation() {
		return cifCreation;
	}

	public void setCifCreation(boolean cifCreation) {
		this.cifCreation = cifCreation;
	}

	public boolean isAccountOpening() {
		return accountOpening;
	}

	public void setAccountOpening(boolean accountOpening) {
		this.accountOpening = accountOpening;
	}

	public boolean isLoanCreation() {
		return loanCreation;
	}

	public void setLoanCreation(boolean loanCreation) {
		this.loanCreation = loanCreation;
	}

	public boolean isCollateralDetails() {
		return collateralDetails;
	}

	public void setCollateralDetails(boolean collateralDetails) {
		this.collateralDetails = collateralDetails;
	}

	public boolean isDisbusrment() {
		return disbusrment;
	}

	public void setDisbusrment(boolean disbusrment) {
		this.disbusrment = disbusrment;
	}

	public boolean isIMPS() {
		return IMPS;
	}

	public void setIMPS(boolean iMPS) {
		IMPS = iMPS;
	}

	public boolean isCRM() {
		return CRM;
	}

	public void setCRM(boolean cRM) {
		CRM = cRM;
	}

	public String getLoanAccoutNumber() {
		return loanAccoutNumber;
	}

	public void setLoanAccoutNumber(String loanAccoutNumber) {
		this.loanAccoutNumber = loanAccoutNumber;
	}

	public String getTargetAccount() {
		return targetAccount;
	}

	public void setTargetAccount(String targetAccount) {
		this.targetAccount = targetAccount;
	}

	public String getCollateralNo() {
		return collateralNo;
	}

	public void setCollateralNo(String collateralNo) {
		this.collateralNo = collateralNo;
	}

	public String getDisbursedTransId() {
		return disbursedTransId;
	}

	public void setDisbursedTransId(String disbursedTransId) {
		this.disbursedTransId = disbursedTransId;
	}

	public String getErrorResponse() {
		return errorResponse;
	}

	public void setErrorResponse(String errorResponse) {
		this.errorResponse = errorResponse;
	}

}
