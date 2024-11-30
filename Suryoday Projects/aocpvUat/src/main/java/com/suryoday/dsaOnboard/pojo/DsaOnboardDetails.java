package com.suryoday.dsaOnboard.pojo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class DsaOnboardDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
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
	private String ifscCode;
	private String bankAccountNo;
	private String bankName;
	private String branchName;
	private String accountholderName;
	private String customerID;
	private String noOfYearsInBusiness;
	private String noOfYearsInFinancialBusiness;
	private String empanelledFiType;
	private String empanelledFiName;
	private String blacklisted;
	@Lob
	private String blacklistInfo;
	private String ageOfProprietor;
	private String otherServices;
	private String ageMoreThan70;
	@Lob
	private String businessReference;
	@Lob
	private String relationToSsfbStaff;
	private String dsaCode;
	private String cibilScore;
	private String branchId;
	private String leadId;
	private String leadName;
	private String password;
	private String typeOfRelationship;
	private String productType;
	private String noOfPartner;
	private String constitutionType;
	@Lob
	private String creditFeedback;
	@Lob
	private String salesManagerFeedback;
	@Lob
	private String referenceCheck;
	@Lob
	private String bankDetailsResponse;
	private String payoutSchemeCode;
	private String tdsRate;
	@Lob
	private String creditOpsFeedback;
	@Lob
	private String nationalSalesManager;
	@Lob
	private String cfrReport;
	@Lob
	private String amlReport;
	private String regiCode;
	private String transactionId;
	private String callId;
	private String nsmRemarkVerify;
	private String payoutDetailsVerify;
	private String sendLeegalityVerify;
	private String checkLeegalityVerify;
	private LocalDate leegalityDate;
	@Lob
	private String dsaCodeCreationRequest;
	@Lob
	private String dsaCodeCreationResponse;
	@Lob
	private String finacalDsaMasterRequest;
	@Lob
	private String finacalDsaMasterResponse;
	private String dmsUploadVerify;
	private String bankDetailsVerify;

	public String getBankDetailsVerify() {
		return bankDetailsVerify;
	}

	public void setBankDetailsVerify(String bankDetailsVerify) {
		this.bankDetailsVerify = bankDetailsVerify;
	}

	public String getDmsUploadVerify() {
		return dmsUploadVerify;
	}

	public void setDmsUploadVerify(String dmsUploadVerify) {
		this.dmsUploadVerify = dmsUploadVerify;
	}

	public String getDsaCodeCreationRequest() {
		return dsaCodeCreationRequest;
	}

	public void setDsaCodeCreationRequest(String dsaCodeCreationRequest) {
		this.dsaCodeCreationRequest = dsaCodeCreationRequest;
	}

	public String getDsaCodeCreationResponse() {
		return dsaCodeCreationResponse;
	}

	public void setDsaCodeCreationResponse(String dsaCodeCreationResponse) {
		this.dsaCodeCreationResponse = dsaCodeCreationResponse;
	}

	public String getFinacalDsaMasterRequest() {
		return finacalDsaMasterRequest;
	}

	public void setFinacalDsaMasterRequest(String finacalDsaMasterRequest) {
		this.finacalDsaMasterRequest = finacalDsaMasterRequest;
	}

	public String getFinacalDsaMasterResponse() {
		return finacalDsaMasterResponse;
	}

	public void setFinacalDsaMasterResponse(String finacalDsaMasterResponse) {
		this.finacalDsaMasterResponse = finacalDsaMasterResponse;
	}

	public LocalDate getLeegalityDate() {
		return leegalityDate;
	}

	public void setLeegalityDate(LocalDate leegalityDate) {
		this.leegalityDate = leegalityDate;
	}

	public String getSendLeegalityVerify() {
		return sendLeegalityVerify;
	}

	public void setSendLeegalityVerify(String sendLeegalityVerify) {
		this.sendLeegalityVerify = sendLeegalityVerify;
	}

	public String getCheckLeegalityVerify() {
		return checkLeegalityVerify;
	}

	public void setCheckLeegalityVerify(String checkLeegalityVerify) {
		this.checkLeegalityVerify = checkLeegalityVerify;
	}

	public String getNsmRemarkVerify() {
		return nsmRemarkVerify;
	}

	public void setNsmRemarkVerify(String nsmRemarkVerify) {
		this.nsmRemarkVerify = nsmRemarkVerify;
	}

	public String getPayoutDetailsVerify() {
		return payoutDetailsVerify;
	}

	public void setPayoutDetailsVerify(String payoutDetailsVerify) {
		this.payoutDetailsVerify = payoutDetailsVerify;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getCallId() {
		return callId;
	}

	public void setCallId(String callId) {
		this.callId = callId;
	}

	public String getRegiCode() {
		return regiCode;
	}

	public void setRegiCode(String regiCode) {
		this.regiCode = regiCode;
	}

	public String getCfrReport() {
		return cfrReport;
	}

	public void setCfrReport(String cfrReport) {
		this.cfrReport = cfrReport;
	}

	public String getAmlReport() {
		return amlReport;
	}

	public void setAmlReport(String amlReport) {
		this.amlReport = amlReport;
	}

	public String getNationalSalesManager() {
		return nationalSalesManager;
	}

	public void setNationalSalesManager(String nationalSalesManager) {
		this.nationalSalesManager = nationalSalesManager;
	}

	public String getCreditOpsFeedback() {
		return creditOpsFeedback;
	}

	public void setCreditOpsFeedback(String creditOpsFeedback) {
		this.creditOpsFeedback = creditOpsFeedback;
	}

	public String getBlacklistInfo() {
		return blacklistInfo;
	}

	public void setBlacklistInfo(String blacklistInfo) {
		this.blacklistInfo = blacklistInfo;
	}

	public String getPayoutSchemeCode() {
		return payoutSchemeCode;
	}

	public void setPayoutSchemeCode(String payoutSchemeCode) {
		this.payoutSchemeCode = payoutSchemeCode;
	}

	public String getTdsRate() {
		return tdsRate;
	}

	public void setTdsRate(String tdsRate) {
		this.tdsRate = tdsRate;
	}

	public String getBankDetailsResponse() {
		return bankDetailsResponse;
	}

	public void setBankDetailsResponse(String bankDetailsResponse) {
		this.bankDetailsResponse = bankDetailsResponse;
	}

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

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getNoOfYearsInBusiness() {
		return noOfYearsInBusiness;
	}

	public void setNoOfYearsInBusiness(String noOfYearsInBusiness) {
		this.noOfYearsInBusiness = noOfYearsInBusiness;
	}

	public String getNoOfYearsInFinancialBusiness() {
		return noOfYearsInFinancialBusiness;
	}

	public void setNoOfYearsInFinancialBusiness(String noOfYearsInFinancialBusiness) {
		this.noOfYearsInFinancialBusiness = noOfYearsInFinancialBusiness;
	}

	public String getEmpanelledFiType() {
		return empanelledFiType;
	}

	public void setEmpanelledFiType(String empanelledFiType) {
		this.empanelledFiType = empanelledFiType;
	}

	public String getEmpanelledFiName() {
		return empanelledFiName;
	}

	public void setEmpanelledFiName(String empanelledFiName) {
		this.empanelledFiName = empanelledFiName;
	}

	public String getBlacklisted() {
		return blacklisted;
	}

	public void setBlacklisted(String blacklisted) {
		this.blacklisted = blacklisted;
	}

	public String getAgeOfProprietor() {
		return ageOfProprietor;
	}

	public void setAgeOfProprietor(String ageOfProprietor) {
		this.ageOfProprietor = ageOfProprietor;
	}

	public String getOtherServices() {
		return otherServices;
	}

	public void setOtherServices(String otherServices) {
		this.otherServices = otherServices;
	}

	public String getAgeMoreThan70() {
		return ageMoreThan70;
	}

	public void setAgeMoreThan70(String ageMoreThan70) {
		this.ageMoreThan70 = ageMoreThan70;
	}

	public String getBusinessReference() {
		return businessReference;
	}

	public void setBusinessReference(String businessReference) {
		this.businessReference = businessReference;
	}

	public String getRelationToSsfbStaff() {
		return relationToSsfbStaff;
	}

	public void setRelationToSsfbStaff(String relationToSsfbStaff) {
		this.relationToSsfbStaff = relationToSsfbStaff;
	}

	public String getDsaCode() {
		return dsaCode;
	}

	public void setDsaCode(String dsaCode) {
		this.dsaCode = dsaCode;
	}

	public String getCibilScore() {
		return cibilScore;
	}

	public void setCibilScore(String cibilScore) {
		this.cibilScore = cibilScore;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTypeOfRelationship() {
		return typeOfRelationship;
	}

	public void setTypeOfRelationship(String typeOfRelationship) {
		this.typeOfRelationship = typeOfRelationship;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
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

	public String getCreditFeedback() {
		return creditFeedback;
	}

	public void setCreditFeedback(String creditFeedback) {
		this.creditFeedback = creditFeedback;
	}

	public String getSalesManagerFeedback() {
		return salesManagerFeedback;
	}

	public void setSalesManagerFeedback(String salesManagerFeedback) {
		this.salesManagerFeedback = salesManagerFeedback;
	}

	public String getReferenceCheck() {
		return referenceCheck;
	}

	public void setReferenceCheck(String referenceCheck) {
		this.referenceCheck = referenceCheck;
	}

}
