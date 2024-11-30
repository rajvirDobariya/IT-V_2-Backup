package com.suryoday.aocpv.pojo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "aocp_table")
public class AocpCustomer {

	@Id
	private long applicationNo;
	private long customerId;
	private LocalDate creationDate;
	private LocalDate updatedate;
	private String createdby;
	private String title;
	private String name;
	private long mobileNo;
	private String mobileNoVerify;
	private LocalDate dateOfBirth;
	private String address;
	private String houseOwnership;
	private String roofType;
	private String residenceStability;
	private String utilityBill;
	private String relationshipWithOwner;
	private String ownerAddress;
	private String otherAssets;
	private String vintage;
	//@OneToMany(cascade = CascadeType.ALL,targetEntity = AocpvIncomeDetails.class)
	//@JoinColumn(name = "application_no" , referencedColumnName = "applicationNo")
	//private List<AocpvIncomeDetails> income;
	private double totalMonthlyIncome;
	private double totalMonthlyEmi;
	private double foodAndUtility;
	private double rent;
	private double transportation;
	private double medical;
	private double education;
	private double other;
	private double total;
	private double monthlyBalance;
	private String purposeOfLoan;
	private String loanCode;
	private String existingLoanPurpose;
	private String mobileLinkToAadhar;
	private long mobile2;
	private double maxEmieligibility;
	private String customerClassification;
	private Double requestLoan;
	private String addressVerify;
	private String flowStatus;
	private String branchid;
	@Column(length = 500)
	private String remarks;
	private String rejectreason;
	private String Status;
	private String buisness;
	@Lob
	private String ResponseEmi;
	@Lob
	private String finalSanction;
	@Lob
	private String finalsanctionAmount;
	@Column(length = 500)
	private String aoRemarks;
	private String roRemarks;
	private String eligibleAmount;
	private String purposeId;
	private String subCategoryPurposeId;
	private String subCategory;
	private String sameAs;
	private String listType;
	private String pendingWith;
	private String isNomineeDetails;
	private String nomineeAddressSameAs;
	@Lob
	private String accountData;
	private String appNoWithProductCode;
	private LocalDateTime timestamp;
	private String isLive;
	private String AoSelfieeIsLive;
	private String RoSelfieeIsLive;
	private String productCode;
	private String ekycVerify;
	private String form60;
	private String bestOfferEmi;
	private String bestOfferTenuer;
	private String bestOfferROI;
	private String bestOfferAmount;
	@Lob
	private String brNetAddress;
	private String utilityBillIsVerify;
	@Lob
	private String utilityBillResponse;
	private String serviceProvider;
	private String utilityBillNo;
	private String serviceProviderCode;
	@Lob
	private String upaResponse;
	private String upiVerify;
	private String rejectedBy;
	private String isActive;
	private String obligation;
	@Column(length = 500)
	private String guarantorRemarks;
	private String emiProductCode;
	private String dmsUploadVerify;
	private String dmsUploadId;
	
	public String getDmsUploadVerify() {
		return dmsUploadVerify;
	}
	public void setDmsUploadVerify(String dmsUploadVerify) {
		this.dmsUploadVerify = dmsUploadVerify;
	}
	public String getDmsUploadId() {
		return dmsUploadId;
	}
	public void setDmsUploadId(String dmsUploadId) {
		this.dmsUploadId = dmsUploadId;
	}
	public String getEmiProductCode() {
		return emiProductCode;
	}
	public void setEmiProductCode(String emiProductCode) {
		this.emiProductCode = emiProductCode;
	}
	public String getGuarantorRemarks() {
		return guarantorRemarks;
	}
	public void setGuarantorRemarks(String guarantorRemarks) {
		this.guarantorRemarks = guarantorRemarks;
	}
	public String getObligation() {
		return obligation;
	}
	public void setObligation(String obligation) {
		this.obligation = obligation;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getUpiVerify() {
		return upiVerify;
	}
	public void setUpiVerify(String upiVerify) {
		this.upiVerify = upiVerify;
	}
	public String getRejectedBy() {
		return rejectedBy;
	}
	public void setRejectedBy(String rejectedBy) {
		this.rejectedBy = rejectedBy;
	}
	public String getUpaResponse() {
		return upaResponse;
	}
	public void setUpaResponse(String upaResponse) {
		this.upaResponse = upaResponse;
	}
	public String getServiceProviderCode() {
		return serviceProviderCode;
	}
	public void setServiceProviderCode(String serviceProviderCode) {
		this.serviceProviderCode = serviceProviderCode;
	}
	public String getServiceProvider() {
		return serviceProvider;
	}
	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}
	public String getUtilityBillNo() {
		return utilityBillNo;
	}
	public void setUtilityBillNo(String utilityBillNo) {
		this.utilityBillNo = utilityBillNo;
	}
	public String getUtilityBillIsVerify() {
		return utilityBillIsVerify;
	}
	public void setUtilityBillIsVerify(String utilityBillIsVerify) {
		this.utilityBillIsVerify = utilityBillIsVerify;
	}
	public String getUtilityBillResponse() {
		return utilityBillResponse;
	}
	public void setUtilityBillResponse(String utilityBillResponse) {
		this.utilityBillResponse = utilityBillResponse;
	}
	public String getBestOfferEmi() {
		return bestOfferEmi;
	}
	public void setBestOfferEmi(String bestOfferEmi) {
		this.bestOfferEmi = bestOfferEmi;
	}
	public String getBestOfferTenuer() {
		return bestOfferTenuer;
	}
	public void setBestOfferTenuer(String bestOfferTenuer) {
		this.bestOfferTenuer = bestOfferTenuer;
	}
	public String getBestOfferROI() {
		return bestOfferROI;
	}
	public void setBestOfferROI(String bestOfferROI) {
		this.bestOfferROI = bestOfferROI;
	}
	public String getBestOfferAmount() {
		return bestOfferAmount;
	}
	public void setBestOfferAmount(String bestOfferAmount) {
		this.bestOfferAmount = bestOfferAmount;
	}
	public String getForm60() {
		return form60;
	}
	public void setForm60(String form60) {
		this.form60 = form60;
	}
	public String getRoRemarks() {
		return roRemarks;
	}
	public void setRoRemarks(String roRemarks) {
		this.roRemarks = roRemarks;
	}
	public String getEkycVerify() {
		return ekycVerify;
	}
	public void setEkycVerify(String ekycVerify) {
		this.ekycVerify = ekycVerify;
	}
	public String getAoSelfieeIsLive() {
		return AoSelfieeIsLive;
	}
	public void setAoSelfieeIsLive(String aoSelfieeIsLive) {
		AoSelfieeIsLive = aoSelfieeIsLive;
	}
	public String getRoSelfieeIsLive() {
		return RoSelfieeIsLive;
	}
	public void setRoSelfieeIsLive(String roSelfieeIsLive) {
		RoSelfieeIsLive = roSelfieeIsLive;
	}
	public String getIsLive() {
		return isLive;
	}
	public void setIsLive(String isLive) {
		this.isLive = isLive;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public String getAppNoWithProductCode() {
		return appNoWithProductCode;
	}
	public void setAppNoWithProductCode(String appNoWithProductCode) {
		this.appNoWithProductCode = appNoWithProductCode;
	}
	public String getAccountData() {
		return accountData;
	}
	public void setAccountData(String accountData) {
		this.accountData = accountData;
	}
	public String getIsNomineeDetails() {
		return isNomineeDetails;
	}
	public void setIsNomineeDetails(String isNomineeDetails) {
		this.isNomineeDetails = isNomineeDetails;
	}
	public String getNomineeAddressSameAs() {
		return nomineeAddressSameAs;
	}
	public void setNomineeAddressSameAs(String nomineeAddressSameAs) {
		this.nomineeAddressSameAs = nomineeAddressSameAs;
	}
	public String getPendingWith() {
		return pendingWith;
	}
	public void setPendingWith(String pendingWith) {
		this.pendingWith = pendingWith;
	}
	public String getListType() {
		return listType;
	}
	public void setListType(String listType) {
		this.listType = listType;
	}
	@Column(nullable = true)
	private int versioncode;
	@Lob
	private String nomineeDetails;
	
	public String getNomineeDetails() {
		return nomineeDetails;
	}
	public void setNomineeDetails(String nomineeDetails) {
		this.nomineeDetails = nomineeDetails;
	}
	public int getVersioncode() {
		return versioncode;
	}
	public void setVersioncode(int versioncode) {
		this.versioncode = versioncode;
	}
	public String getSameAs() {
		return sameAs;
	}
	public void setSameAs(String sameAs) {
		this.sameAs = sameAs;
	}
	public String getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	public String getPurposeId() {
		return purposeId;
	}
	public void setPurposeId(String purposeId) {
		this.purposeId = purposeId;
	}
	public String getSubCategoryPurposeId() {
		return subCategoryPurposeId;
	}
	public void setSubCategoryPurposeId(String subCategoryPurposeId) {
		this.subCategoryPurposeId = subCategoryPurposeId;
	}
	public String getEligibleAmount() {
		return eligibleAmount;
	}
	public void setEligibleAmount(String eligibleAmount) {
		this.eligibleAmount = eligibleAmount;
	}
	public String getFinalSanction() {
		return finalSanction;
	}
	public void setFinalSanction(String finalSanction) {
		this.finalSanction = finalSanction;
	}
	public Double getRequestLoan() {
		return requestLoan;
	}
	public void setRequestLoan(Double requestLoan) {
		this.requestLoan = requestLoan;
	}
	
	public long getApplicationNo() {
		return applicationNo;
	}
	public void setApplicationNo(long applicationNo) {
		this.applicationNo = applicationNo;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public LocalDate getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}
	public LocalDate getUpdatedate() {
		return updatedate;
	}
	public void setUpdatedate(LocalDate updatedate) {
		this.updatedate = updatedate;
	}
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby.toUpperCase();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name.toUpperCase();
	}
	public long getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(long mobileNo) {
		this.mobileNo = mobileNo;
	}
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address.toUpperCase();
	}
	public String getHouseOwnership() {
		return houseOwnership;
	}
	public void setHouseOwnership(String houseOwnership) {
		this.houseOwnership = houseOwnership.toUpperCase();
	}
	public String getRoofType() {
		return roofType;
	}
	public void setRoofType(String roofType) {
		this.roofType = roofType.toUpperCase();
	}
	public String getResidenceStability() {
		return residenceStability;
	}
	public void setResidenceStability(String residenceStability) {
		this.residenceStability = residenceStability.toUpperCase();
	}
	public String getUtilityBill() {
		return utilityBill;
	}
	public void setUtilityBill(String utilityBill) {
		this.utilityBill = utilityBill.toUpperCase();
	}
	public String getRelationshipWithOwner() {
		return relationshipWithOwner;
	}
	public void setRelationshipWithOwner(String relationshipWithOwner) {
		this.relationshipWithOwner = relationshipWithOwner.toUpperCase();
	}
	public String getOwnerAddress() {
		return ownerAddress;
	}
	public void setOwnerAddress(String ownerAddress) {
		this.ownerAddress = ownerAddress.toUpperCase();
	}
	public String getOtherAssets() {
		return otherAssets;
	}
	public void setOtherAssets(String otherAssets) {
		this.otherAssets = otherAssets.toUpperCase();
	}
	public String getVintage() {
		return vintage;
	}
	public void setVintage(String vintage) {
		this.vintage = vintage.toUpperCase();
	}
	
	public double getTotalMonthlyIncome() {
		return totalMonthlyIncome;
	}
	public void setTotalMonthlyIncome(double totalMonthlyIncome) {
		this.totalMonthlyIncome = totalMonthlyIncome;
	}
	public double getTotalMonthlyEmi() {
		return totalMonthlyEmi;
	}
	public void setTotalMonthlyEmi(double totalMonthlyEmi) {
		this.totalMonthlyEmi = totalMonthlyEmi;
	}
	public double getFoodAndUtility() {
		return foodAndUtility;
	}
	public void setFoodAndUtility(double foodAndUtility) {
		this.foodAndUtility = foodAndUtility;
	}
	public double getRent() {
		return rent;
	}
	public void setRent(double rent) {
		this.rent = rent;
	}
	public double getTransportation() {
		return transportation;
	}
	public void setTransportation(double transportation) {
		this.transportation = transportation;
	}
	public double getMedical() {
		return medical;
	}
	public void setMedical(double medical) {
		this.medical = medical;
	}
	public double getEducation() {
		return education;
	}
	public void setEducation(double education) {
		this.education = education;
	}
	public double getOther() {
		return other;
	}
	public void setOther(double other) {
		this.other = other;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public double getMonthlyBalance() {
		return monthlyBalance;
	}
	public void setMonthlyBalance(double monthlyBalance) {
		this.monthlyBalance = monthlyBalance;
	}
	public String getPurposeOfLoan() {
		return purposeOfLoan;
	}
	public void setPurposeOfLoan(String purposeOfLoan) {
		this.purposeOfLoan = purposeOfLoan.toUpperCase();
	}
	public String getLoanCode() {
		return loanCode;
	}
	public void setLoanCode(String loanCode) {
		this.loanCode = loanCode.toUpperCase();
	}
	public String getExistingLoanPurpose() {
		return existingLoanPurpose;
	}
	public void setExistingLoanPurpose(String existingLoanPurpose) {
		this.existingLoanPurpose = existingLoanPurpose.toUpperCase();
	}
	public String getMobileLinkToAadhar() {
		return mobileLinkToAadhar;
	}
	public void setMobileLinkToAadhar(String mobileLinkToAadhar) {
		this.mobileLinkToAadhar = mobileLinkToAadhar.toUpperCase();
	}
	public long getMobile2() {
		return mobile2;
	}
	public void setMobile2(long mobile2) {
		this.mobile2 = mobile2;
	}
	public double getMaxEmieligibility() {
		return maxEmieligibility;
	}
	public void setMaxEmieligibility(double maxEmieligibility) {
		this.maxEmieligibility = maxEmieligibility;
	}
	public String getCustomerClassification() {
		return customerClassification;
	}
	public void setCustomerClassification(String customerClassification) {
		this.customerClassification = customerClassification.toUpperCase();
	}
	public String getFlowStatus() {
		return flowStatus;
	}
	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus.toUpperCase();
	}
	public String getBranchid() {
		return branchid;
	}
	public void setBranchid(String branchid) {
		this.branchid = branchid;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks.toUpperCase();
	}
	public String getRejectreason() {
		return rejectreason;
	}
	public void setRejectreason(String rejectreason) {
		this.rejectreason = rejectreason.toUpperCase();
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status.toUpperCase();
	}
	public String getBuisness() {
		return buisness;
	}
	public void setBuisness(String buisness) {
		this.buisness = buisness.toUpperCase();
	}
	
	
	
	public String getResponseEmi() {
		return ResponseEmi;
	}
	
	public void setResponseEmi(String responseEmi) {
		ResponseEmi = responseEmi.toUpperCase();
	}
	public String getMobileNoVerify() {
		return mobileNoVerify;
	}
	public void setMobileNoVerify(String mobileNoVerify) {
		this.mobileNoVerify = mobileNoVerify.toUpperCase();
	}
	public String getFinalsanctionAmount() {
		return finalsanctionAmount;
	}
	public void setFinalsanctionAmount(String finalsanctionAmount) {
		this.finalsanctionAmount = finalsanctionAmount;
	}
	public String getAoRemarks() {
		return aoRemarks;
	}
	public void setAoRemarks(String aoRemarks) {
		this.aoRemarks = aoRemarks.toUpperCase();
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title.toUpperCase();
	}
	public String getAddressVerify() {
		return addressVerify;
	}
	public void setAddressVerify(String addressVerify) {
		this.addressVerify = addressVerify;
	}
	
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	public String getBrNetAddress() {
		return brNetAddress;
	}
	public void setBrNetAddress(String brNetAddress) {
		this.brNetAddress = brNetAddress;
	}
	@Override
	public String toString() {
		return "AocpCustomer [applicationNo=" + applicationNo + ", customerId=" + customerId + ", creationDate="
				+ creationDate + ", updatedate=" + updatedate + ", createdby=" + createdby + ", title=" + title
				+ ", name=" + name + ", mobileNo=" + mobileNo + ", mobileNoVerify=" + mobileNoVerify + ", dateOfBirth="
				+ dateOfBirth + ", address=" + address + ", houseOwnership=" + houseOwnership + ", roofType=" + roofType
				+ ", residenceStability=" + residenceStability + ", utilityBill=" + utilityBill
				+ ", relationshipWithOwner=" + relationshipWithOwner + ", ownerAddress=" + ownerAddress
				+ ", otherAssets=" + otherAssets + ", vintage=" + vintage + ", totalMonthlyIncome=" + totalMonthlyIncome
				+ ", totalMonthlyEmi=" + totalMonthlyEmi + ", foodAndUtility=" + foodAndUtility + ", rent=" + rent
				+ ", transportation=" + transportation + ", medical=" + medical + ", education=" + education
				+ ", other=" + other + ", total=" + total + ", monthlyBalance=" + monthlyBalance + ", purposeOfLoan="
				+ purposeOfLoan + ", loanCode=" + loanCode + ", existingLoanPurpose=" + existingLoanPurpose
				+ ", mobileLinkToAadhar=" + mobileLinkToAadhar + ", mobile2=" + mobile2 + ", maxEmieligibility="
				+ maxEmieligibility + ", customerClassification=" + customerClassification + ", requestLoan="
				+ requestLoan + ", addressVerify=" + addressVerify + ", flowStatus=" + flowStatus + ", branchid="
				+ branchid + ", remarks=" + remarks + ", rejectreason=" + rejectreason + ", Status=" + Status
				+ ", buisness=" + buisness + ", ResponseEmi=" + ResponseEmi + ", finalSanction=" + finalSanction
				+ ", finalsanctionAmount=" + finalsanctionAmount + ", aoRemarks=" + aoRemarks + ", eligibleAmount="
				+ eligibleAmount + "]";
	}
	
	
	
}
