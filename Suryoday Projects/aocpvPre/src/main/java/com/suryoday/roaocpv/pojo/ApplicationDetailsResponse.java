package com.suryoday.roaocpv.pojo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Lob;

import com.suryoday.aocpv.pojo.Charges;
import com.suryoday.aocpv.pojo.EmiResponse;
import com.suryoday.aocpv.pojo.Image;


public class ApplicationDetailsResponse {

	private Long Id;
	private String appId;
	private String applicationId;
	private String request;
	private String name;
	private String firstName;
	private String lastName;
	private String amount;
	private String mobileNo;
	private String channel;
	private String status;
	private String productType;
	private String aadharNumber;
	private LocalDateTime createts;
	private LocalDate updatedDate;
	private String branchName;
	private String loanNumber;
	private LocalDateTime loanNumberTs;
	private String branchCode;
	private String agentId;
	private String flowStatus;
	@Lob
	private String primaryDetailsReq;
	private LocalDateTime workItemTs;
	private LocalDateTime primaryDetailsTs;
	@Lob
	private PersonalDetailResponse personalDetailResponse;
	private LocalDateTime personalDetailsTs;
	@Lob
	private List<Address> addressDetailsReq;
	private LocalDateTime addressDetailsTs;
	@Lob
	private String accountDetailsReq;
	private LocalDateTime accountDetailsTs;
	@Lob
	private String productDetailsReq;
	private LocalDateTime productDetailsTs;
	@Lob
	private String documentDetailsReq;
	private LocalDateTime documentDetailsTs;
	@Lob
	private String blockValidationReq;
	private LocalDateTime blockValidationTs;
	@Lob
	private String decelerationDetailsReq;
	private LocalDateTime decelerationDetailsTs;
	@Lob
	private String additionalDetailsReq1;
	private LocalDateTime additionalDetailsTs1;
	@Lob
	private String additionalDetailsReq2;
	private LocalDateTime additionalDetailsTs2;
	@Lob
	private String additionalDetailsReq3;
	private LocalDateTime additionalDetailsTs3;
	private String customerId;
	private LocalDateTime customerCreationTs;
	private LocalDateTime initialFundingTs;
	private String accountNumber;
	private LocalDateTime accountCreationTs;
	private String workItemId;
	private LocalDateTime documentUploadTs;
	private String disburstmenetNumber;
	private LocalDateTime disburstmenetNumberTs;
	private String product;
	private String loanPurchase;
	private String requiredAmount;
	private String tenure;
	private String sourceType;
	private String sourceName;
	private String isMobileNoVerify;
	private String voterId;
	@Lob
	private String voterIdResponse;
	private String isAadharVerify;
	private String panCard;
	@Lob
	private String panCardResponse;
	@Lob
	private String ekycResponse;
	private String aadharReferenceNo;
	private String appNoWithProductCode;
	private String ekycVerify;
	private LocalDateTime updateDatets;
	private List<Image> images;
	private EmiResponse emiResponse;
	private String productCode;
	private Charges charges;
	private String netDisbustment;
	private String termInsurance;
	private String fireInsurance;
	private String isBreRuring;
	private String form60;
	private String createdBy;
	private String listType;
	private String drivingLicense;
	private String drivingLicenseIsVerify;
	private String passport;
	private String passportIsVerify;
	private String maxEmiEligibility;
	private List<Charges> bestOffers;
	
	public List<Charges> getBestOffers() {
		return bestOffers;
	}

	public void setBestOffers(List<Charges> bestOffers) {
		this.bestOffers = bestOffers;
	}

	public String getMaxEmiEligibility() {
		return maxEmiEligibility;
	}

	public void setMaxEmiEligibility(String maxEmiEligibility) {
		this.maxEmiEligibility = maxEmiEligibility;
	}

	public String getDrivingLicense() {
		return drivingLicense;
	}

	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}

	public String getDrivingLicenseIsVerify() {
		return drivingLicenseIsVerify;
	}

	public void setDrivingLicenseIsVerify(String drivingLicenseIsVerify) {
		this.drivingLicenseIsVerify = drivingLicenseIsVerify;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public String getPassportIsVerify() {
		return passportIsVerify;
	}

	public void setPassportIsVerify(String passportIsVerify) {
		this.passportIsVerify = passportIsVerify;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getListType() {
		return listType;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}

	public String getForm60() {
		return form60;
	}

	public void setForm60(String form60) {
		this.form60 = form60;
	}

	public String getIsBreRuring() {
		return isBreRuring;
	}

	public void setIsBreRuring(String isBreRuring) {
		this.isBreRuring = isBreRuring;
	}

	public String getNetDisbustment() {
		return netDisbustment;
	}

	public void setNetDisbustment(String netDisbustment) {
		this.netDisbustment = netDisbustment;
	}

	public String getTermInsurance() {
		return termInsurance;
	}

	public void setTermInsurance(String termInsurance) {
		this.termInsurance = termInsurance;
	}

	public String getFireInsurance() {
		return fireInsurance;
	}

	public void setFireInsurance(String fireInsurance) {
		this.fireInsurance = fireInsurance;
	}

	public Charges getCharges() {
		return charges;
	}

	public void setCharges(Charges charges) {
		this.charges = charges;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public EmiResponse getEmiResponse() {
		return emiResponse;
	}

	public void setEmiResponse(EmiResponse emiResponse) {
		this.emiResponse = emiResponse;
	}

	public String getEkycVerify() {
		return ekycVerify;
	}

	public void setEkycVerify(String ekycVerify) {
		this.ekycVerify = ekycVerify;
	}

	public LocalDateTime getUpdateDatets() {
		return updateDatets;
	}

	public void setUpdateDatets(LocalDateTime updateDatets) {
		this.updateDatets = updateDatets;
	}

	public String getAppNoWithProductCode() {
		return appNoWithProductCode;
	}

	public void setAppNoWithProductCode(String appNoWithProductCode) {
		this.appNoWithProductCode = appNoWithProductCode;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getLoanPurchase() {
		return loanPurchase;
	}

	public void setLoanPurchase(String loanPurchase) {
		this.loanPurchase = loanPurchase;
	}

	public String getRequiredAmount() {
		return requiredAmount;
	}

	public void setRequiredAmount(String requiredAmount) {
		this.requiredAmount = requiredAmount;
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
		this.sourceType = sourceType;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getIsMobileNoVerify() {
		return isMobileNoVerify;
	}

	public void setIsMobileNoVerify(String isMobileNoVerify) {
		this.isMobileNoVerify = isMobileNoVerify;
	}

	public String getVoterId() {
		return voterId;
	}

	public void setVoterId(String voterId) {
		this.voterId = voterId;
	}

	public String getVoterIdResponse() {
		return voterIdResponse;
	}

	public void setVoterIdResponse(String voterIdResponse) {
		this.voterIdResponse = voterIdResponse;
	}

	public String getIsAadharVerify() {
		return isAadharVerify;
	}

	public void setIsAadharVerify(String isAadharVerify) {
		this.isAadharVerify = isAadharVerify;
	}

	public String getPanCard() {
		return panCard;
	}

	public void setPanCard(String panCard) {
		this.panCard = panCard;
	}

	public String getPanCardResponse() {
		return panCardResponse;
	}

	public void setPanCardResponse(String panCardResponse) {
		this.panCardResponse = panCardResponse;
	}

	public String getEkycResponse() {
		return ekycResponse;
	}

	public void setEkycResponse(String ekycResponse) {
		this.ekycResponse = ekycResponse;
	}

	public String getAadharReferenceNo() {
		return aadharReferenceNo;
	}

	public void setAadharReferenceNo(String aadharReferenceNo) {
		this.aadharReferenceNo = aadharReferenceNo;
	}

	public String getDisburstmenetNumber() {
		return disburstmenetNumber;
	}

	public void setDisburstmenetNumber(String disburstmenetNumber) {
		this.disburstmenetNumber = disburstmenetNumber;
	}

	public LocalDateTime getDisburstmenetNumberTs() {
		return disburstmenetNumberTs;
	}

	public void setDisburstmenetNumberTs(LocalDateTime disburstmenetNumberTs) {
		this.disburstmenetNumberTs = disburstmenetNumberTs;
	}

	public String getLoanNumber() {
		return loanNumber;
	}

	public void setLoanNumber(String loanNumber) {
		this.loanNumber = loanNumber;
	}

	public LocalDateTime getLoanNumberTs() {
		return loanNumberTs;
	}

	public void setLoanNumberTs(LocalDateTime loanNumberTs) {
		this.loanNumberTs = loanNumberTs;
	}

	public LocalDateTime getWorkItemTs() {
		return workItemTs;
	}

	public void setWorkItemTs(LocalDateTime workItemTs) {
		this.workItemTs = workItemTs;
	}

	public LocalDate getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDate updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

	public LocalDateTime getCreatets() {
		return createts;
	}

	public void setCreatets(LocalDateTime createts) {
		this.createts = createts;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}

	public String getPrimaryDetailsReq() {
		return primaryDetailsReq;
	}

	public void setPrimaryDetailsReq(String primaryDetailsReq) {
		this.primaryDetailsReq = primaryDetailsReq;
	}

	public LocalDateTime getPrimaryDetailsTs() {
		return primaryDetailsTs;
	}

	public void setPrimaryDetailsTs(LocalDateTime primaryDetailsTs) {
		this.primaryDetailsTs = primaryDetailsTs;
	}

	

	public PersonalDetailResponse getPersonalDetailResponse() {
		return personalDetailResponse;
	}

	public void setPersonalDetailResponse(PersonalDetailResponse personalDetailResponse) {
		this.personalDetailResponse = personalDetailResponse;
	}

	public LocalDateTime getPersonalDetailsTs() {
		return personalDetailsTs;
	}

	public void setPersonalDetailsTs(LocalDateTime personalDetailsTs) {
		this.personalDetailsTs = personalDetailsTs;
	}

	
	public List<Address> getAddressDetailsReq() {
		return addressDetailsReq;
	}

	public void setAddressDetailsReq(List<Address> addressDetailsReq) {
		this.addressDetailsReq = addressDetailsReq;
	}

	public LocalDateTime getAddressDetailsTs() {
		return addressDetailsTs;
	}

	public void setAddressDetailsTs(LocalDateTime addressDetailsTs) {
		this.addressDetailsTs = addressDetailsTs;
	}

	public String getAccountDetailsReq() {
		return accountDetailsReq;
	}

	public void setAccountDetailsReq(String accountDetailsReq) {
		this.accountDetailsReq = accountDetailsReq;
	}

	public LocalDateTime getAccountDetailsTs() {
		return accountDetailsTs;
	}

	public void setAccountDetailsTs(LocalDateTime accountDetailsTs) {
		this.accountDetailsTs = accountDetailsTs;
	}

	public String getProductDetailsReq() {
		return productDetailsReq;
	}

	public void setProductDetailsReq(String productDetailsReq) {
		this.productDetailsReq = productDetailsReq;
	}

	public LocalDateTime getProductDetailsTs() {
		return productDetailsTs;
	}

	public void setProductDetailsTs(LocalDateTime productDetailsTs) {
		this.productDetailsTs = productDetailsTs;
	}

	public String getDocumentDetailsReq() {
		return documentDetailsReq;
	}

	public void setDocumentDetailsReq(String documentDetailsReq) {
		this.documentDetailsReq = documentDetailsReq;
	}

	public LocalDateTime getDocumentDetailsTs() {
		return documentDetailsTs;
	}

	public void setDocumentDetailsTs(LocalDateTime documentDetailsTs) {
		this.documentDetailsTs = documentDetailsTs;
	}

	public String getBlockValidationReq() {
		return blockValidationReq;
	}

	public void setBlockValidationReq(String blockValidationReq) {
		this.blockValidationReq = blockValidationReq;
	}

	public LocalDateTime getBlockValidationTs() {
		return blockValidationTs;
	}

	public void setBlockValidationTs(LocalDateTime blockValidationTs) {
		this.blockValidationTs = blockValidationTs;
	}

	public String getDecelerationDetailsReq() {
		return decelerationDetailsReq;
	}

	public void setDecelerationDetailsReq(String decelerationDetailsReq) {
		this.decelerationDetailsReq = decelerationDetailsReq;
	}

	public LocalDateTime getDecelerationDetailsTs() {
		return decelerationDetailsTs;
	}

	public void setDecelerationDetailsTs(LocalDateTime decelerationDetailsTs) {
		this.decelerationDetailsTs = decelerationDetailsTs;
	}

	public String getAdditionalDetailsReq1() {
		return additionalDetailsReq1;
	}

	public void setAdditionalDetailsReq1(String additionalDetailsReq1) {
		this.additionalDetailsReq1 = additionalDetailsReq1;
	}

	public LocalDateTime getAdditionalDetailsTs1() {
		return additionalDetailsTs1;
	}

	public void setAdditionalDetailsTs1(LocalDateTime additionalDetailsTs1) {
		this.additionalDetailsTs1 = additionalDetailsTs1;
	}

	public String getAdditionalDetailsReq2() {
		return additionalDetailsReq2;
	}

	public void setAdditionalDetailsReq2(String additionalDetailsReq2) {
		this.additionalDetailsReq2 = additionalDetailsReq2;
	}

	public LocalDateTime getAdditionalDetailsTs2() {
		return additionalDetailsTs2;
	}

	public void setAdditionalDetailsTs2(LocalDateTime additionalDetailsTs2) {
		this.additionalDetailsTs2 = additionalDetailsTs2;
	}

	public String getAdditionalDetailsReq3() {
		return additionalDetailsReq3;
	}

	public void setAdditionalDetailsReq3(String additionalDetailsReq3) {
		this.additionalDetailsReq3 = additionalDetailsReq3;
	}

	public LocalDateTime getAdditionalDetailsTs3() {
		return additionalDetailsTs3;
	}

	public void setAdditionalDetailsTs3(LocalDateTime additionalDetailsTs3) {
		this.additionalDetailsTs3 = additionalDetailsTs3;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public LocalDateTime getCustomerCreationTs() {
		return customerCreationTs;
	}

	public void setCustomerCreationTs(LocalDateTime customerCreationTs) {
		this.customerCreationTs = customerCreationTs;
	}

	public LocalDateTime getInitialFundingTs() {
		return initialFundingTs;
	}

	public void setInitialFundingTs(LocalDateTime initialFundingTs) {
		this.initialFundingTs = initialFundingTs;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public LocalDateTime getAccountCreationTs() {
		return accountCreationTs;
	}

	public void setAccountCreationTs(LocalDateTime accountCreationTs) {
		this.accountCreationTs = accountCreationTs;
	}

	public String getWorkItemId() {
		return workItemId;
	}

	public void setWorkItemId(String workItemId) {
		this.workItemId = workItemId;
	}

	public LocalDateTime getDocumentUploadTs() {
		return documentUploadTs;
	}

	public void setDocumentUploadTs(LocalDateTime documentUploadTs) {
		this.documentUploadTs = documentUploadTs;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	

}
