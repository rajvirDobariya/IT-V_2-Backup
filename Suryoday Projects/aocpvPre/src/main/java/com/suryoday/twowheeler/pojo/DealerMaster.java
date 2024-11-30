package com.suryoday.twowheeler.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_tw_dealer")
public class DealerMaster {
	@Id
	private String id;
	private String dealerCode;
	private String dealerName;
	private String dealerAddress;
	private String dealerLatitude;
	private String dealerLongitude;
	private String pinCode;
	private String ssfbBranchCode;
	private String ssfbBranchName;
	private String manufacturerDescription;
	private String dealerTypeDescription;
	private String accountNumber;
	private String ifscCode;
	private String bankName;
	private String branchCode;
	private String branchDescription;
	private String email;
	private String phoneNumber;
	private String beneficiaryAccNum;
	private String pan;
	private String gst;
	private String cheque;
	private String otherDocs;
	private String accNumberValidated;
	@Lob
	private String branchIdArray;
	
	public String getBranchIdArray() {
		return branchIdArray;
	}

	public void setBranchIdArray(String branchIdArray) {
		this.branchIdArray = branchIdArray;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDealerCode() {
		return dealerCode;
	}

	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public String getDealerAddress() {
		return dealerAddress;
	}

	public void setDealerAddress(String dealerAddress) {
		this.dealerAddress = dealerAddress;
	}

	public String getDealerLatitude() {
		return dealerLatitude;
	}

	public void setDealerLatitude(String dealerLatitude) {
		this.dealerLatitude = dealerLatitude;
	}

	public String getDealerLongitude() {
		return dealerLongitude;
	}

	public void setDealerLongitude(String dealerLongitude) {
		this.dealerLongitude = dealerLongitude;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getSsfbBranchCode() {
		return ssfbBranchCode;
	}

	public void setSsfbBranchCode(String ssfbBranchCode) {
		this.ssfbBranchCode = ssfbBranchCode;
	}

	public String getSsfbBranchName() {
		return ssfbBranchName;
	}

	public void setSsfbBranchName(String ssfbBranchName) {
		this.ssfbBranchName = ssfbBranchName;
	}

	public String getManufacturerDescription() {
		return manufacturerDescription;
	}

	public void setManufacturerDescription(String manufacturerDescription) {
		this.manufacturerDescription = manufacturerDescription;
	}

	public String getDealerTypeDescription() {
		return dealerTypeDescription;
	}

	public void setDealerTypeDescription(String dealerTypeDescription) {
		this.dealerTypeDescription = dealerTypeDescription;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBranchDescription() {
		return branchDescription;
	}

	public void setBranchDescription(String branchDescription) {
		this.branchDescription = branchDescription;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getBeneficiaryAccNum() {
		return beneficiaryAccNum;
	}

	public void setBeneficiaryAccNum(String beneficiaryAccNum) {
		this.beneficiaryAccNum = beneficiaryAccNum;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getGst() {
		return gst;
	}

	public void setGst(String gst) {
		this.gst = gst;
	}

	public String getCheque() {
		return cheque;
	}

	public void setCheque(String cheque) {
		this.cheque = cheque;
	}

	public String getOtherDocs() {
		return otherDocs;
	}

	public void setOtherDocs(String otherDocs) {
		this.otherDocs = otherDocs;
	}

	public String getAccNumberValidated() {
		return accNumberValidated;
	}

	public void setAccNumberValidated(String accNumberValidated) {
		this.accNumberValidated = accNumberValidated;
	}

	@Override
	public String toString() {
		return "DealerMaster [id=" + id + ", dealerCode=" + dealerCode + ", dealerName=" + dealerName
				+ ", dealerAddress=" + dealerAddress + ", dealerLatitude=" + dealerLatitude + ", dealerLongitude="
				+ dealerLongitude + ", pinCode=" + pinCode + ", ssfbBranchCode=" + ssfbBranchCode + ", ssfbBranchName="
				+ ssfbBranchName + ", manufacturerDescription=" + manufacturerDescription + ", dealerTypeDescription="
				+ dealerTypeDescription + ", accountNumber=" + accountNumber + ", ifscCode=" + ifscCode + ", bankName="
				+ bankName + ", branchCode=" + branchCode + ", branchDescription=" + branchDescription + ", email="
				+ email + ", phoneNumber=" + phoneNumber + ", beneficiaryAccNum=" + beneficiaryAccNum + ", pan=" + pan
				+ ", gst=" + gst + ", cheque=" + cheque + ", otherDocs=" + otherDocs + ", accNumberValidated="
				+ accNumberValidated + "]";
	}

}
