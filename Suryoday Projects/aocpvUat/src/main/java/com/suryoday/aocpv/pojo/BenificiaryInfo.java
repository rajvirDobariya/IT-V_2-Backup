package com.suryoday.aocpv.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BenificiaryInfo {

	String BeneficiaryId;

	String NickName;

	String BeneficiaryName;

	Number Sequence;

	String BeneficiaryBank;

	String BeneficiaryBankCity;

	String BeneficiaryBranch;

	String BeneficiaryBankIfsc;

	String Status;

	String BeneficiaryMaxLimit;

	String BeneficiaryEmailId;

	String BeneficiaryMobileNumber;

	public BenificiaryInfo() {

	}

	@JsonProperty("BeneficiaryId")
	public String getBeneficiaryId() {
		return BeneficiaryId;
	}

	public void setBeneficiaryId(String beneficiaryId) {
		BeneficiaryId = beneficiaryId;
	}

	@JsonProperty("NickName")
	public String getNickName() {
		return NickName;
	}

	public void setNickName(String nickName) {
		NickName = nickName;
	}

	@JsonProperty("BeneficiaryName")
	public String getBeneficiaryName() {
		return BeneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		BeneficiaryName = beneficiaryName;
	}

	@JsonProperty("Sequence")
	public Number getSequence() {
		return Sequence;
	}

	public void setSequence(Number sequence) {
		Sequence = sequence;
	}

	@JsonProperty("BeneficiaryBank")
	public String getBeneficiaryBank() {
		if (BeneficiaryBank == null) {
			BeneficiaryBank = "";
		}
		return BeneficiaryBank;
	}

	public void setBeneficiaryBank(String beneficiaryBank) {
		BeneficiaryBank = beneficiaryBank;
	}

	@JsonProperty("BeneficiaryBankCity")
	public String getBeneficiaryBankCity() {
		return BeneficiaryBankCity;
	}

	public void setBeneficiaryBankCity(String beneficiaryBankCity) {
		BeneficiaryBankCity = beneficiaryBankCity;
	}

	@JsonProperty("BeneficiaryBranch")
	public String getBeneficiaryBranch() {
		if (BeneficiaryBranch == null) {
			BeneficiaryBranch = "";
		}
		return BeneficiaryBranch;
	}

	public void setBeneficiaryBranch(String beneficiaryBranch) {
		BeneficiaryBranch = beneficiaryBranch;
	}

	@JsonProperty("BeneficiaryBankIfsc")
	public String getBeneficiaryBankIfsc() {
		if (BeneficiaryBankIfsc == null) {
			BeneficiaryBankIfsc = "";
		}
		return BeneficiaryBankIfsc;
	}

	public void setBeneficiaryBankIfsc(String beneficiaryBankIfsc) {
		BeneficiaryBankIfsc = beneficiaryBankIfsc;
	}

	@JsonProperty("Status")
	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	@JsonProperty("BeneficiaryMaxLimit")
	public String getBeneficiaryMaxLimit() {
		return BeneficiaryMaxLimit;
	}

	public void setBeneficiaryMaxLimit(String beneficiaryMaxLimit) {
		BeneficiaryMaxLimit = beneficiaryMaxLimit;
	}

	public void setBeneficiaryEmailId(String beneficiaryEmailId) {
		BeneficiaryEmailId = beneficiaryEmailId;
	}

	public void setBeneficiaryMobileNumber(String beneficiaryMobileNumber) {
		BeneficiaryMobileNumber = beneficiaryMobileNumber;
	}

	@Override
	public String toString() {
		return "BenificiaryInfo [BeneficiaryId=" + BeneficiaryId + ", NickName=" + NickName + ", BeneficiaryName="
				+ BeneficiaryName + ", Sequence=" + Sequence + ", BeneficiaryBank=" + BeneficiaryBank
				+ ", BeneficiaryBankCity=" + BeneficiaryBankCity + ", BeneficiaryBranch=" + BeneficiaryBranch
				+ ", BeneficiaryBankIfsc=" + BeneficiaryBankIfsc + ", Status=" + Status + ", BeneficiaryMaxLimit="
				+ BeneficiaryMaxLimit + ", BeneficiaryEmailId=" + BeneficiaryEmailId + ", BeneficiaryMobileNumber="
				+ BeneficiaryMobileNumber + "]";
	}

}
