package com.suryoday.aocpv.pojo;

public class EmiResponse {

	private String TransactionCode;
	private String AmountRequested;
	private String AnnualPercentageRate;
	private String BalanceAmount;
	private String CommitmentFee;
	private String InterestAmortization;
	private String InterestAmount;
	private String InterestRate;
	private String MaturityBalance;
	private String MtgInsurance;
	private String NoOfAddDays;
	private String OddDaysIntAmount;
	private String OriginationFees;
	private String PaymentAmount;
	private String Term;
	private String TotalAmountFinanced;
	private String OutstandingBalance;

	public String getOutstandingBalance() {
		return OutstandingBalance;
	}

	public void setOutstandingBalance(String outstandingBalance) {
		OutstandingBalance = outstandingBalance;
	}

	public String getTransactionCode() {
		return TransactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		TransactionCode = transactionCode;
	}

	public String getAmountRequested() {
		return AmountRequested;
	}

	public void setAmountRequested(String amountRequested) {
		AmountRequested = amountRequested;
	}

	public String getAnnualPercentageRate() {
		return AnnualPercentageRate;
	}

	public void setAnnualPercentageRate(String annualPercentageRate) {
		AnnualPercentageRate = annualPercentageRate;
	}

	public String getBalanceAmount() {
		return BalanceAmount;
	}

	public void setBalanceAmount(String balanceAmount) {
		BalanceAmount = balanceAmount;
	}

	public String getCommitmentFee() {
		return CommitmentFee;
	}

	public void setCommitmentFee(String commitmentFee) {
		CommitmentFee = commitmentFee;
	}

	public String getInterestAmortization() {
		return InterestAmortization;
	}

	public void setInterestAmortization(String interestAmortization) {
		InterestAmortization = interestAmortization;
	}

	public String getInterestAmount() {
		return InterestAmount;
	}

	public void setInterestAmount(String interestAmount) {
		InterestAmount = interestAmount;
	}

	public String getInterestRate() {
		return InterestRate;
	}

	public void setInterestRate(String interestRate) {
		InterestRate = interestRate;
	}

	public String getMaturityBalance() {
		return MaturityBalance;
	}

	public void setMaturityBalance(String maturityBalance) {
		MaturityBalance = maturityBalance;
	}

	public String getMtgInsurance() {
		return MtgInsurance;
	}

	public void setMtgInsurance(String mtgInsurance) {
		MtgInsurance = mtgInsurance;
	}

	public String getNoOfAddDays() {
		return NoOfAddDays;
	}

	public void setNoOfAddDays(String noOfAddDays) {
		NoOfAddDays = noOfAddDays;
	}

	public String getOddDaysIntAmount() {
		return OddDaysIntAmount;
	}

	public void setOddDaysIntAmount(String oddDaysIntAmount) {
		OddDaysIntAmount = oddDaysIntAmount;
	}

	public String getOriginationFees() {
		return OriginationFees;
	}

	public void setOriginationFees(String originationFees) {
		OriginationFees = originationFees;
	}

	public String getPaymentAmount() {
		return PaymentAmount;
	}

	public void setPaymentAmount(String paymentAmount) {
		PaymentAmount = paymentAmount;
	}

	public String getTerm() {
		return Term;
	}

	public void setTerm(String term) {
		Term = term;
	}

	public String getTotalAmountFinanced() {
		return TotalAmountFinanced;
	}

	public void setTotalAmountFinanced(String totalAmountFinanced) {
		TotalAmountFinanced = totalAmountFinanced;
	}

	public EmiResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmiResponse(String transactionCode, String amountRequested, String annualPercentageRate,
			String balanceAmount, String commitmentFee, String interestAmortization, String interestAmount,
			String interestRate, String maturityBalance, String mtgInsurance, String noOfAddDays,
			String oddDaysIntAmount, String originationFees, String paymentAmount, String term,
			String totalAmountFinanced, String outstandingBalance) {
		super();
		TransactionCode = transactionCode;
		AmountRequested = amountRequested;
		AnnualPercentageRate = annualPercentageRate;
		BalanceAmount = balanceAmount;
		CommitmentFee = commitmentFee;
		InterestAmortization = interestAmortization;
		InterestAmount = interestAmount;
		InterestRate = interestRate;
		MaturityBalance = maturityBalance;
		MtgInsurance = mtgInsurance;
		NoOfAddDays = noOfAddDays;
		OddDaysIntAmount = oddDaysIntAmount;
		OriginationFees = originationFees;
		PaymentAmount = paymentAmount;
		Term = term;
		TotalAmountFinanced = totalAmountFinanced;
		OutstandingBalance = outstandingBalance;
	}

	@Override
	public String toString() {
		return "EmiResponse [TransactionCode=" + TransactionCode + ", AmountRequested=" + AmountRequested
				+ ", AnnualPercentageRate=" + AnnualPercentageRate + ", BalanceAmount=" + BalanceAmount
				+ ", CommitmentFee=" + CommitmentFee + ", InterestAmortization=" + InterestAmortization
				+ ", InterestAmount=" + InterestAmount + ", InterestRate=" + InterestRate + ", MaturityBalance="
				+ MaturityBalance + ", MtgInsurance=" + MtgInsurance + ", NoOfAddDays=" + NoOfAddDays
				+ ", OddDaysIntAmount=" + OddDaysIntAmount + ", OriginationFees=" + OriginationFees + ", PaymentAmount="
				+ PaymentAmount + ", Term=" + Term + ", TotalAmountFinanced=" + TotalAmountFinanced
				+ ", OutstandingBalance=" + OutstandingBalance + "]";
	}

}
