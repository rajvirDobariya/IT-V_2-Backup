package com.suryoday.twowheeler.pojo;

public class BankDetailsResponse {
	private String member;
	private String accountIfsc;
	private String accountBankName;
	private String accountBranchId;
	private String accountNumber;
	private String accountName;
	private String repaymentType;
	private String accountType;
	private String beneficiaryAccountType;

	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
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

	public String getAccountBranchId() {
		return accountBranchId;
	}

	public void setAccountBranchId(String accountBranchId) {
		this.accountBranchId = accountBranchId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getRepaymentType() {
		return repaymentType;
	}

	public void setRepaymentType(String repaymentType) {
		this.repaymentType = repaymentType;
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

}
