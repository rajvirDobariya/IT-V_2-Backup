package com.suryoday.twowheeler.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_twoWheelerMandateDetails")
public class MandateDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String applicationNo;
	private String referenceNo;
	private String utilityCode;
	private String categoryCode;
	private String schemeName;
	private String consumerReferenceNo;
	private String mandateType;
	private String frequency;
	private String firstCollectionDate;
	private String finalCollectionDate;
	private String amountType;
	private String collectionAmount;
	private String customerName;
	private String emailId;
	private String mobileNo;
	private String pan;
	private String bankId;
	private String accountType;
	private String accountNo;
	private String reference;
	private String requestStatus;

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getUtilityCode() {
		return utilityCode;
	}

	public void setUtilityCode(String utilityCode) {
		this.utilityCode = utilityCode;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	public String getConsumerReferenceNo() {
		return consumerReferenceNo;
	}

	public void setConsumerReferenceNo(String consumerReferenceNo) {
		this.consumerReferenceNo = consumerReferenceNo;
	}

	public String getMandateType() {
		return mandateType;
	}

	public void setMandateType(String mandateType) {
		this.mandateType = mandateType;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getFirstCollectionDate() {
		return firstCollectionDate;
	}

	public void setFirstCollectionDate(String firstCollectionDate) {
		this.firstCollectionDate = firstCollectionDate;
	}

	public String getFinalCollectionDate() {
		return finalCollectionDate;
	}

	public void setFinalCollectionDate(String finalCollectionDate) {
		this.finalCollectionDate = finalCollectionDate;
	}

	public String getAmountType() {
		return amountType;
	}

	public void setAmountType(String amountType) {
		this.amountType = amountType;
	}

	public String getCollectionAmount() {
		return collectionAmount;
	}

	public void setCollectionAmount(String collectionAmount) {
		this.collectionAmount = collectionAmount;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	@Override
	public String toString() {
		return "MandateDetails [id=" + id + ", applicationNo=" + applicationNo + ", referenceNo=" + referenceNo
				+ ", utilityCode=" + utilityCode + ", categoryCode=" + categoryCode + ", schemeName=" + schemeName
				+ ", consumerReferenceNo=" + consumerReferenceNo + ", mandateType=" + mandateType + ", frequency="
				+ frequency + ", firstCollectionDate=" + firstCollectionDate + ", finalCollectionDate="
				+ finalCollectionDate + ", amountType=" + amountType + ", collectionAmount=" + collectionAmount
				+ ", customerName=" + customerName + ", emailId=" + emailId + ", mobileNo=" + mobileNo + ", pan=" + pan
				+ ", bankId=" + bankId + ", accountType=" + accountType + ", accountNo=" + accountNo + "]";
	}

}
