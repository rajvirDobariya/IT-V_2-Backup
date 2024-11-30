package com.suryoday.aocpv.pojo;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Lob;



public class ResponseAocpCustomerDataWeb {
	private long applicationNo;
	private long customerId;
	private String name;
	private long mobileNo;
	private String dateOfBirth;
	private List<Address> address;
	private String houseOwnership;
	private String roofType;
	private String residenceStability;
	private String utilityBill;
	private String relationshipWithOwner;
	private List<OwnerAddress> ownerAddress;
	private List<String> otherAssets;
	private String vintage;
	@Lob
	private List<IncomeDetailWeb> incomeDetails;
	private List<FinalSaction> finalsanction;
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
	private String existingLoanPurpose;
	private String mobileLinkToAadhar;
	private long mobile2;
	private double maxEmieligibility;
	private String customerClassification;
	private String flowStatus;
	private String branchId;
	private String Status;
	private EmiResponse emiResponse;
	private String mobileNoVerify;
	private AocpvLoanCreation aocpvLoanCreation;
	@Column(length = 500)
	private String aoRemarks;
	private String roRemarks;
	private String address_verify;
	private String createdBy;
	private List<RemarkResponse> remark1;
	private String purposeId;
	private String subCategoryPurposeId;
	private String subCategory;
	private String sameAs ;
	private int versioncode;
	private String listType;
	private String appNoWithProductCode;
	private String ekycVerify;
	private LocalDateTime timestamp;
	private String AgentName;
	private String agentMobileNo;
	private String branchName;
	private String form60;
	private String bestOfferEmi;
	private String bestOfferTenuer;
	private String bestOfferROI;
	private String bestOfferAmount;
	private String utilityBillIsVerify;
	private String utilityBillResponse;
	private String serviceProvider;
	private String upaResponse;
	private String upiVerify;
	private String guarantorRemarks;
	
	public String getGuarantorRemarks() {
		return guarantorRemarks;
	}
	public void setGuarantorRemarks(String guarantorRemarks) {
		this.guarantorRemarks = guarantorRemarks;
	}
	public String getUpaResponse() {
		return upaResponse;
	}
	public void setUpaResponse(String upaResponse) {
		this.upaResponse = upaResponse;
	}
	public String getUpiVerify() {
		return upiVerify;
	}
	public void setUpiVerify(String upiVerify) {
		this.upiVerify = upiVerify;
	}
	public String getAgentMobileNo() {
		return agentMobileNo;
	}
	public void setAgentMobileNo(String agentMobileNo) {
		this.agentMobileNo = agentMobileNo;
	}
	public String getServiceProvider() {
		return serviceProvider;
	}
	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}
	public String getUtilityBillResponse() {
		return utilityBillResponse;
	}
	public void setUtilityBillResponse(String utilityBillResponse) {
		this.utilityBillResponse = utilityBillResponse;
	}
	public String getUtilityBillIsVerify() {
		return utilityBillIsVerify;
	}
	public void setUtilityBillIsVerify(String utilityBillIsVerify) {
		this.utilityBillIsVerify = utilityBillIsVerify;
	}
	public String getForm60() {
		return form60;
	}
	public void setForm60(String form60) {
		this.form60 = form60;
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
	public String getAgentName() {
		return AgentName;
	}
	public void setAgentName(String agentName) {
		AgentName = agentName;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getRoRemarks() {
		return roRemarks;
	}
	public void setRoRemarks(String roRemarks) {
		this.roRemarks = roRemarks;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public String getEkycVerify() {
		return ekycVerify;
	}
	public void setEkycVerify(String ekycVerify) {
		this.ekycVerify = ekycVerify;
	}
	public String getAppNoWithProductCode() {
		return appNoWithProductCode;
	}
	public void setAppNoWithProductCode(String appNoWithProductCode) {
		this.appNoWithProductCode = appNoWithProductCode;
	}
	public String getListType() {
		return listType;
	}
	public void setListType(String listType) {
		this.listType = listType;
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
	public String getAddress_verify() {
		return address_verify;
	}
	public void setAddress_verify(String address_verify) {
		this.address_verify = address_verify;
	}
	//	@Lob
//	private String buisness_photo;
//	private String bLat;
//	private String bLong;
//	private String bAddress;
//	@Lob
//	private String utilityBillPhoto;
//	private String uLat;
//	private String uLong;
//	private String uAddress;
//	@Lob
//	private String customerPhoto;
//	private String pLat;
//	private String pLong;
//	private String pAddress;
	private Double requestLoan;
	//private String requestedLoanResponse;
	private String creationDate;
	private String updateDate;
	private String remarks;
	private String buisness;
	private String eligibleAmount;
//	@Lob
//	private String houseImagePhoto1;
//	@Lob
//	private String houseImagePhoto2;
//	private String houseImageLat1;
//	private String houseImageLong1;
//	private String houseImageAddress1;
//	private String houseImageLat2;
//	private String houseImageLong2;
//	private String houseImageAddress2;
	
	private List<Image> images;
	
	public String getEligibleAmount() {
		return eligibleAmount;
	}
	public void setEligibleAmount(String eligibleAmount) {
		this.eligibleAmount = eligibleAmount;
	}
	public List<Image> getImages() {
		return images;
	}
	public void setImages(List<Image> images) {
		this.images = images;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(long mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public List<Address> getAddress() {
		return address;
	}
	public void setAddress(List<Address> address) {
		this.address = address;
	}
	public String getHouseOwnership() {
		return houseOwnership;
	}
	public void setHouseOwnership(String houseOwnership) {
		this.houseOwnership = houseOwnership;
	}
	public String getRoofType() {
		return roofType;
	}
	public void setRoofType(String roofType) {
		this.roofType = roofType;
	}
	public String getResidenceStability() {
		return residenceStability;
	}
	public void setResidenceStability(String residenceStability) {
		this.residenceStability = residenceStability;
	}
	public String getUtilityBill() {
		return utilityBill;
	}
	public void setUtilityBill(String utilityBill) {
		this.utilityBill = utilityBill;
	}
	public String getRelationshipWithOwner() {
		return relationshipWithOwner;
	}
	public void setRelationshipWithOwner(String relationshipWithOwner) {
		this.relationshipWithOwner = relationshipWithOwner;
	}
	public List<OwnerAddress> getOwnerAddress() {
		return ownerAddress;
	}
	public void setOwnerAddress(List<OwnerAddress> ownerAddress) {
		this.ownerAddress = ownerAddress;
	}
	public List<String> getOtherAssets() {
		return otherAssets;
	}
	public void setOtherAssets(List<String> otherAssets) {
		this.otherAssets = otherAssets;
	}
	public String getVintage() {
		return vintage;
	}
	public void setVintage(String vintage) {
		this.vintage = vintage;
	}
	
	public List<IncomeDetailWeb> getIncomeDetails() {
		return incomeDetails;
	}
	public void setIncomeDetails(List<IncomeDetailWeb> incomeDetails) {
		this.incomeDetails = incomeDetails;
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
		this.purposeOfLoan = purposeOfLoan;
	}
	public String getExistingLoanPurpose() {
		return existingLoanPurpose;
	}
	public void setExistingLoanPurpose(String existingLoanPurpose) {
		this.existingLoanPurpose = existingLoanPurpose;
	}
	public String getMobileLinkToAadhar() {
		return mobileLinkToAadhar;
	}
	public void setMobileLinkToAadhar(String mobileLinkToAadhar) {
		this.mobileLinkToAadhar = mobileLinkToAadhar;
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
		this.customerClassification = customerClassification;
	}
	public String getFlowStatus() {
		return flowStatus;
	}
	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getBuisness() {
		return buisness;
	}
	public void setBuisness(String buisness) {
		this.buisness = buisness;
	}
	public Double getRequestLoan() {
		return requestLoan;
	}
	public void setRequestLoan(Double requestLoan) {
		this.requestLoan = requestLoan;
	}
	
	public EmiResponse getEmiResponse() {
		return emiResponse;
	}
	public void setEmiResponse(EmiResponse emiResponse) {
		this.emiResponse = emiResponse;
	}
	public List<FinalSaction> getFinalsanction() {
		return finalsanction;
	}
	public void setFinalsanction(List<FinalSaction> finalsanction) {
		this.finalsanction = finalsanction;
	}
	public String getMobileNoVerify() {
		return mobileNoVerify;
	}
	public void setMobileNoVerify(String mobileNoVerify) {
		this.mobileNoVerify = mobileNoVerify;
	}
	public AocpvLoanCreation getAocpvLoanCreation() {
		return aocpvLoanCreation;
	}
	public void setAocpvLoanCreation(AocpvLoanCreation aocpvLoanCreation) {
		this.aocpvLoanCreation = aocpvLoanCreation;
	}
	public String getAoRemarks() {
		return aoRemarks;
	}
	public void setAoRemarks(String aoRemarks) {
		this.aoRemarks = aoRemarks;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	
	public List<RemarkResponse> getRemark1() {
		return remark1;
	}
	public void setRemark1(List<RemarkResponse> remark1) {
		this.remark1 = remark1;
	}
	@Override
	public String toString() {
		return "ResponseAocpCustomerData [applicationNo=" + applicationNo + ", customerId=" + customerId + ", name="
				+ name + ", mobileNo=" + mobileNo + ", dateOfBirth=" + dateOfBirth + ", address=" + address
				+ ", houseOwnership=" + houseOwnership + ", roofType=" + roofType + ", residenceStability="
				+ residenceStability + ", utilityBill=" + utilityBill + ", relationshipWithOwner="
				+ relationshipWithOwner + ", ownerAddress=" + ownerAddress + ", otherAssets=" + otherAssets
				+ ", vintage=" + vintage + ", incomeDetails=" + incomeDetails + ", finalsanction=" + finalsanction
				+ ", totalMonthlyIncome=" + totalMonthlyIncome + ", totalMonthlyEmi=" + totalMonthlyEmi
				+ ", foodAndUtility=" + foodAndUtility + ", rent=" + rent + ", transportation=" + transportation
				+ ", medical=" + medical + ", education=" + education + ", other=" + other + ", total=" + total
				+ ", monthlyBalance=" + monthlyBalance + ", purposeOfLoan=" + purposeOfLoan + ", existingLoanPurpose="
				+ existingLoanPurpose + ", mobileLinkToAadhar=" + mobileLinkToAadhar + ", mobile2=" + mobile2
				+ ", maxEmieligibility=" + maxEmieligibility + ", customerClassification=" + customerClassification
				+ ", flowStatus=" + flowStatus + ", branchId=" + branchId + ", Status=" + Status + ", emiResponse="
				+ emiResponse + ", mobileNoVerify=" + mobileNoVerify + ", aocpvLoanCreation=" + aocpvLoanCreation
				+ ", aoRemarks=" + aoRemarks + ", address_verify=" + address_verify + ", createdBy=" + createdBy
				+ ", requestLoan=" + requestLoan + ", creationDate=" + creationDate + ", updateDate=" + updateDate
				+ ", remarks=" + remarks + ", buisness=" + buisness + ", eligibleAmount=" + eligibleAmount + ", images="
				+ images + "]";
	}
	
	
}
