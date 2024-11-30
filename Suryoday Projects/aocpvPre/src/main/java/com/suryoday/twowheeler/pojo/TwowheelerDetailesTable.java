package com.suryoday.twowheeler.pojo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "twoWheelerDetails_table")
public class TwowheelerDetailesTable {
	@Id
	private String applicationNo;
	private String appNoWithProductCode;
	private LocalDateTime createdTimestamp;
	private LocalDateTime updatedTimestamp;
	private String salesCreatedBy;
	private String salesBranchId;
	private String status;
	private String psdBranchId;
	private String psdCreatedBy;
	private String dealerName;
	private String customerId;
	private String name;
	private String mobileNo;
	private String preApprovalAmount;
	private String scheme;
	private String manufacture;
	private String model;
	private String variant;
	private String variantCode;
	private String dealerLocation;
	private String exShowroomPrice;
	private String roadTax;
	private String RegistrationCharges;
	private String accessories;
	private String addonsCharges;
	private String totalOnRoadPrice;
	private String flowStatus;
	private String mobileNoVerify;
	private String dateOfBirth;
	@Lob
	private String address;
	private String stage;
	private String aadharNo;
	private String aadharNoVerify;
	private String voterId;
	private String voterIdVerify;
	private String pancard;
	private String pancardVerify;
	private String marginMoney;
	private String amount;
	private String rateOfInterest;
	private String tenure;
	private String tenureMin;
	private String tenureMax;
	private String emi;
	private String insuranceEmi;
	private String engineNumber;
	private String chasisNumber;
	private String ekycVerify;
	@Lob
	private String ekycResponse;
	@Lob
	private String panResponse;
	private String ekycDoneBy;
	private String beneficiaryName;
	private String beneficiaryAccountNo;
	private String beneficiaryBankName;
	private String beneficiaryIFSC;
	private String beneficiaryBranchName;
	private String dealerCode;
	private String accountName;
	private String accountNumber;
	private String accountBranchId;
	private String accountIfsc;
	private String accountBankName;
	private String repaymentType;
	private String accountType;
	private String beneficiaryAccountType;
	private String finaldisbustmentAmount;
	private String totaldeductionAmount;
	private String listType;
	@Lob
	private String remark;
	@Lob
	private String checkList;
	@Lob
	private String loanCharges;
	private String form60;
	private String requiredAmount;
	private String selectedIdentityProof;
	private String identityProof;
	private String identityProofVerify;
	@Lob
	private String dudupeResponse;
	@Lob
	private String referenceInfo;
	@Lob
	private String tvrForm;
	private String yearOfManufacturer;
	private LocalDate sanctiondate;
	private String totalOnRoadPriceAllocated;
	private String personalDetailsVerify;
	private String assetDetailsVerify;
	private String loanChargeVerify;
	private String addNomineeVerify;
	private String documentUploadVerify;
	private String sanctionLetterVerify;
	private String loanAgreementVerify;
	private String disbursementDetailsVerify;
	private String loanPurpose;
	private String bussinessSegment;
	private String classificationAdvance;
	private String governmentSponsoredScheme;
	private String channelCode;
	private String populationCode;
	private String sanctionDepartment;
	private String utilityBillIsVerify;
	@Lob
	private String utilityBillResponse;
	private String serviceProvider;
	private String utilityBillNo;
	private String serviceProviderCode;
	private String utilityBill;
	private String effectiveIrr;
	
	public String getEffectiveIrr() {
		return effectiveIrr;
	}

	public void setEffectiveIrr(String effectiveIrr) {
		this.effectiveIrr = effectiveIrr;
	}

	public String getVariantCode() {
		return variantCode;
	}

	public void setVariantCode(String variantCode) {
		this.variantCode = variantCode;
	}

	public String getLoanPurpose() {
		return loanPurpose;
	}

	public void setLoanPurpose(String loanPurpose) {
		this.loanPurpose = loanPurpose;
	}

	public String getBussinessSegment() {
		return bussinessSegment;
	}

	public void setBussinessSegment(String bussinessSegment) {
		this.bussinessSegment = bussinessSegment;
	}

	public String getClassificationAdvance() {
		return classificationAdvance;
	}

	public void setClassificationAdvance(String classificationAdvance) {
		this.classificationAdvance = classificationAdvance;
	}

	public String getGovernmentSponsoredScheme() {
		return governmentSponsoredScheme;
	}

	public void setGovernmentSponsoredScheme(String governmentSponsoredScheme) {
		this.governmentSponsoredScheme = governmentSponsoredScheme;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getPopulationCode() {
		return populationCode;
	}

	public void setPopulationCode(String populationCode) {
		this.populationCode = populationCode;
	}

	public String getSanctionDepartment() {
		return sanctionDepartment;
	}

	public void setSanctionDepartment(String sanctionDepartment) {
		this.sanctionDepartment = sanctionDepartment;
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

	public String getServiceProviderCode() {
		return serviceProviderCode;
	}

	public void setServiceProviderCode(String serviceProviderCode) {
		this.serviceProviderCode = serviceProviderCode;
	}

	public String getUtilityBill() {
		return utilityBill;
	}

	public void setUtilityBill(String utilityBill) {
		this.utilityBill = utilityBill;
	}

	public String getPersonalDetailsVerify() {
		return personalDetailsVerify;
	}

	public void setPersonalDetailsVerify(String personalDetailsVerify) {
		this.personalDetailsVerify = personalDetailsVerify;
	}

	public String getAssetDetailsVerify() {
		return assetDetailsVerify;
	}

	public void setAssetDetailsVerify(String assetDetailsVerify) {
		this.assetDetailsVerify = assetDetailsVerify;
	}

	public String getLoanChargeVerify() {
		return loanChargeVerify;
	}

	public void setLoanChargeVerify(String loanChargeVerify) {
		this.loanChargeVerify = loanChargeVerify;
	}

	public String getAddNomineeVerify() {
		return addNomineeVerify;
	}

	public void setAddNomineeVerify(String addNomineeVerify) {
		this.addNomineeVerify = addNomineeVerify;
	}

	public String getDocumentUploadVerify() {
		return documentUploadVerify;
	}

	public void setDocumentUploadVerify(String documentUploadVerify) {
		this.documentUploadVerify = documentUploadVerify;
	}

	public String getSanctionLetterVerify() {
		return sanctionLetterVerify;
	}

	public void setSanctionLetterVerify(String sanctionLetterVerify) {
		this.sanctionLetterVerify = sanctionLetterVerify;
	}

	public String getLoanAgreementVerify() {
		return loanAgreementVerify;
	}

	public void setLoanAgreementVerify(String loanAgreementVerify) {
		this.loanAgreementVerify = loanAgreementVerify;
	}

	public String getDisbursementDetailsVerify() {
		return disbursementDetailsVerify;
	}

	public void setDisbursementDetailsVerify(String disbursementDetailsVerify) {
		this.disbursementDetailsVerify = disbursementDetailsVerify;
	}

	public String getTotalOnRoadPriceAllocated() {
		return totalOnRoadPriceAllocated;
	}

	public void setTotalOnRoadPriceAllocated(String totalOnRoadPriceAllocated) {
		this.totalOnRoadPriceAllocated = totalOnRoadPriceAllocated;
	}

	public LocalDate getSanctiondate() {
		return sanctiondate;
	}

	public void setSanctiondate(LocalDate sanctiondate) {
		this.sanctiondate = sanctiondate;
	}

	public String getYearOfManufacturer() {
		return yearOfManufacturer;
	}

	public void setYearOfManufacturer(String yearOfManufacturer) {
		this.yearOfManufacturer = yearOfManufacturer;
	}

	public String getTvrForm() {
		return tvrForm;
	}

	public void setTvrForm(String tvrForm) {
		this.tvrForm = tvrForm;
	}

	public String getReferenceInfo() {
		return referenceInfo;
	}

	public void setReferenceInfo(String referenceInfo) {
		this.referenceInfo = referenceInfo;
	}

	public String getDudupeResponse() {
		return dudupeResponse;
	}

	public void setDudupeResponse(String dudupeResponse) {
		this.dudupeResponse = dudupeResponse;
	}

	public String getAccountIfsc() {
		return accountIfsc;
	}

	public void setAccountIfsc(String accountIfsc) {
		this.accountIfsc = accountIfsc;
	}

	public String getAccountBankName() {
		return accountBankName;
	}

	public void setAccountBankName(String accountBankName) {
		this.accountBankName = accountBankName;
	}

	public String getRepaymentType() {
		return repaymentType;
	}

	public void setRepaymentType(String repaymentType) {
		this.repaymentType = repaymentType;
	}
	public String getForm60() {
		return form60;
	}

	public void setForm60(String form60) {
		this.form60 = form60;
	}

	public String getRequiredAmount() {
		return requiredAmount;
	}

	public void setRequiredAmount(String requiredAmount) {
		this.requiredAmount = requiredAmount;
	}

	public String getSelectedIdentityProof() {
		return selectedIdentityProof;
	}

	public void setSelectedIdentityProof(String selectedIdentityProof) {
		this.selectedIdentityProof = selectedIdentityProof;
	}

	public String getIdentityProof() {
		return identityProof;
	}

	public void setIdentityProof(String identityProof) {
		this.identityProof = identityProof;
	}

	public String getIdentityProofVerify() {
		return identityProofVerify;
	}

	public void setIdentityProofVerify(String identityProofVerify) {
		this.identityProofVerify = identityProofVerify;
	}

	public String getLoanCharges() {
		return loanCharges;
	}

	public void setLoanCharges(String loanCharges) {
		this.loanCharges = loanCharges;
	}

	public String getCheckList() {
		return checkList;
	}

	public void setCheckList(String checkList) {
		this.checkList = checkList;
	}

	public String getBeneficiaryBankName() {
		return beneficiaryBankName;
	}

	public void setBeneficiaryBankName(String beneficiaryBankName) {
		this.beneficiaryBankName = beneficiaryBankName;
	}

	public String getBeneficiaryIFSC() {
		return beneficiaryIFSC;
	}

	public void setBeneficiaryIFSC(String beneficiaryIFSC) {
		this.beneficiaryIFSC = beneficiaryIFSC;
	}

	public String getBeneficiaryBranchName() {
		return beneficiaryBranchName;
	}

	public void setBeneficiaryBranchName(String beneficiaryBranchName) {
		this.beneficiaryBranchName = beneficiaryBranchName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getListType() {
		return listType;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}

	public String getTenureMin() {
		return tenureMin;
	}

	public void setTenureMin(String tenureMin) {
		this.tenureMin = tenureMin;
	}

	public String getTenureMax() {
		return tenureMax;
	}

	public void setTenureMax(String tenureMax) {
		this.tenureMax = tenureMax;
	}

	public String getFinaldisbustmentAmount() {
		return finaldisbustmentAmount;
	}

	public void setFinaldisbustmentAmount(String finaldisbustmentAmount) {
		this.finaldisbustmentAmount = finaldisbustmentAmount;
	}

	public String getTotaldeductionAmount() {
		return totaldeductionAmount;
	}

	public void setTotaldeductionAmount(String totaldeductionAmount) {
		this.totaldeductionAmount = totaldeductionAmount;
	}

	public String getAccountBranchId() {
		return accountBranchId;
	}

	public void setAccountBranchId(String accountBranchId) {
		this.accountBranchId = accountBranchId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getDealerCode() {
		return dealerCode;
	}

	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public String getBeneficiaryAccountNo() {
		return beneficiaryAccountNo;
	}

	public void setBeneficiaryAccountNo(String beneficiaryAccountNo) {
		this.beneficiaryAccountNo = beneficiaryAccountNo;
	}

	public String getEkycDoneBy() {
		return ekycDoneBy;
	}

	public void setEkycDoneBy(String ekycDoneBy) {
		this.ekycDoneBy = ekycDoneBy;
	}

	public String getPanResponse() {
		return panResponse;
	}

	public void setPanResponse(String panResponse) {
		this.panResponse = panResponse;
	}

	public String getEkycVerify() {
		return ekycVerify;
	}

	public void setEkycVerify(String ekycVerify) {
		this.ekycVerify = ekycVerify;
	}

	public String getEkycResponse() {
		return ekycResponse;
	}

	public void setEkycResponse(String ekycResponse) {
		this.ekycResponse = ekycResponse;
	}

	public String getEngineNumber() {
		return engineNumber;
	}

	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	}

	public String getChasisNumber() {
		return chasisNumber;
	}

	public void setChasisNumber(String chasisNumber) {
		this.chasisNumber = chasisNumber;
	}

	public String getMarginMoney() {
		return marginMoney;
	}

	public void setMarginMoney(String marginMoney) {
		this.marginMoney = marginMoney;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
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

	public String getEmi() {
		return emi;
	}

	public void setEmi(String emi) {
		this.emi = emi;
	}

	public String getInsuranceEmi() {
		return insuranceEmi;
	}

	public void setInsuranceEmi(String insuranceEmi) {
		this.insuranceEmi = insuranceEmi;
	}

	public String getAadharNo() {
		return aadharNo;
	}

	public void setAadharNo(String aadharNo) {
		this.aadharNo = aadharNo;
	}

	public String getAadharNoVerify() {
		return aadharNoVerify;
	}

	public void setAadharNoVerify(String aadharNoVerify) {
		this.aadharNoVerify = aadharNoVerify;
	}

	public String getVoterId() {
		return voterId;
	}

	public void setVoterId(String voterId) {
		this.voterId = voterId;
	}

	public String getVoterIdVerify() {
		return voterIdVerify;
	}

	public void setVoterIdVerify(String voterIdVerify) {
		this.voterIdVerify = voterIdVerify;
	}

	public String getPancard() {
		return pancard;
	}

	public void setPancard(String pancard) {
		this.pancard = pancard;
	}

	public String getPancardVerify() {
		return pancardVerify;
	}

	public void setPancardVerify(String pancardVerify) {
		this.pancardVerify = pancardVerify;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getAppNoWithProductCode() {
		return appNoWithProductCode;
	}

	public void setAppNoWithProductCode(String appNoWithProductCode) {
		this.appNoWithProductCode = appNoWithProductCode;
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

	public String getSalesCreatedBy() {
		return salesCreatedBy;
	}

	public void setSalesCreatedBy(String salesCreatedBy) {
		this.salesCreatedBy = salesCreatedBy;
	}

	public String getSalesBranchId() {
		return salesBranchId;
	}

	public void setSalesBranchId(String salesBranchId) {
		this.salesBranchId = salesBranchId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPsdBranchId() {
		return psdBranchId;
	}

	public void setPsdBranchId(String psdBranchId) {
		this.psdBranchId = psdBranchId;
	}

	public String getPsdCreatedBy() {
		return psdCreatedBy;
	}

	public void setPsdCreatedBy(String psdCreatedBy) {
		this.psdCreatedBy = psdCreatedBy;
	}

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getPreApprovalAmount() {
		return preApprovalAmount;
	}

	public void setPreApprovalAmount(String preApprovalAmount) {
		this.preApprovalAmount = preApprovalAmount;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getManufacture() {
		return manufacture;
	}

	public void setManufacture(String manufacture) {
		this.manufacture = manufacture;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getVariant() {
		return variant;
	}

	public void setVariant(String variant) {
		this.variant = variant;
	}

	public String getDealerLocation() {
		return dealerLocation;
	}

	public void setDealerLocation(String dealerLocation) {
		this.dealerLocation = dealerLocation;
	}

	public String getExShowroomPrice() {
		return exShowroomPrice;
	}

	public void setExShowroomPrice(String exShowroomPrice) {
		this.exShowroomPrice = exShowroomPrice;
	}

	public String getRoadTax() {
		return roadTax;
	}

	public void setRoadTax(String roadTax) {
		this.roadTax = roadTax;
	}

	public String getRegistrationCharges() {
		return RegistrationCharges;
	}

	public void setRegistrationCharges(String registrationCharges) {
		RegistrationCharges = registrationCharges;
	}

	public String getAccessories() {
		return accessories;
	}

	public void setAccessories(String accessories) {
		this.accessories = accessories;
	}

	public String getAddonsCharges() {
		return addonsCharges;
	}

	public void setAddonsCharges(String addonsCharges) {
		this.addonsCharges = addonsCharges;
	}

	public String getTotalOnRoadPrice() {
		return totalOnRoadPrice;
	}

	public void setTotalOnRoadPrice(String totalOnRoadPrice) {
		this.totalOnRoadPrice = totalOnRoadPrice;
	}

	public String getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}

	public String getMobileNoVerify() {
		return mobileNoVerify;
	}

	public void setMobileNoVerify(String mobileNoVerify) {
		this.mobileNoVerify = mobileNoVerify;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getBeneficiaryAccountType() {
		return beneficiaryAccountType;
	}

	public void setBeneficiaryAccountType(String beneficiaryAccountType) {
		this.beneficiaryAccountType = beneficiaryAccountType;
	}

	@Override
	public String toString() {
		return "TwowheelerDetailesTable [applicationNo=" + applicationNo + ", appNoWithProductCode="
				+ appNoWithProductCode + ", createdTimestamp=" + createdTimestamp + ", updatedTimestamp="
				+ updatedTimestamp + ", salesCreatedBy=" + salesCreatedBy + ", salesBranchId=" + salesBranchId
				+ ", status=" + status + ", psdBranchId=" + psdBranchId + ", psdCreatedBy=" + psdCreatedBy
				+ ", dealerName=" + dealerName + ", customerId=" + customerId + ", name=" + name + ", mobileNo="
				+ mobileNo + ", preApprovalAmount=" + preApprovalAmount + ", scheme=" + scheme + ", manufacture="
				+ manufacture + ", model=" + model + ", variant=" + variant + ", dealerLocation=" + dealerLocation
				+ ", exShowroomPrice=" + exShowroomPrice + ", roadTax=" + roadTax + ", RegistrationCharges="
				+ RegistrationCharges + ", accessories=" + accessories + ", addonsCharges=" + addonsCharges
				+ ", totalOnRoadPrice=" + totalOnRoadPrice + ", flowStatus=" + flowStatus + ", mobileNoVerify="
				+ mobileNoVerify + ", dateOfBirth=" + dateOfBirth + ", address=" + address + ", stage=" + stage
				+ ", aadharNo=" + aadharNo + ", aadharNoVerify=" + aadharNoVerify + ", voterId=" + voterId
				+ ", voterIdVerify=" + voterIdVerify + ", pancard=" + pancard + ", pancardVerify=" + pancardVerify
				+ ", marginMoney=" + marginMoney + ", amount=" + amount + ", rateOfInterest=" + rateOfInterest
				+ ", tenure=" + tenure + ", tenureMin=" + tenureMin + ", tenureMax=" + tenureMax + ", emi=" + emi
				+ ", insuranceEmi=" + insuranceEmi + ", engineNumber=" + engineNumber + ", chasisNumber=" + chasisNumber
				+ ", ekycVerify=" + ekycVerify + ", ekycResponse=" + ekycResponse + ", panResponse=" + panResponse
				+ ", ekycDoneBy=" + ekycDoneBy + ", beneficiaryName=" + beneficiaryName + ", beneficiaryAccountNo="
				+ beneficiaryAccountNo + ", beneficiaryBankName=" + beneficiaryBankName + ", beneficiaryIFSC="
				+ beneficiaryIFSC + ", beneficiaryBranchName=" + beneficiaryBranchName + ", dealerCode=" + dealerCode
				+ ", accountName=" + accountName + ", accountNumber=" + accountNumber + ", accountBranchId="
				+ accountBranchId + ", accountIfsc=" + accountIfsc + ", accountBankName=" + accountBankName
				+ ", repaymentType=" + repaymentType + ", accountType=" + accountType + ", beneficiaryAccountType="
				+ beneficiaryAccountType + ", finaldisbustmentAmount=" + finaldisbustmentAmount
				+ ", totaldeductionAmount=" + totaldeductionAmount + ", listType=" + listType + ", remark=" + remark
				+ ", checkList=" + checkList + ", loanCharges=" + loanCharges + ", form60=" + form60
				+ ", requiredAmount=" + requiredAmount + ", selectedIdentityProof=" + selectedIdentityProof
				+ ", identityProof=" + identityProof + ", identityProofVerify=" + identityProofVerify
				+ ", dudupeResponse=" + dudupeResponse + ", referenceInfo=" + referenceInfo + "]";
	}

	
}
