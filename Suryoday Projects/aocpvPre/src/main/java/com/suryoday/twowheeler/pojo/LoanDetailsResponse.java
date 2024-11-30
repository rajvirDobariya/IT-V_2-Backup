package com.suryoday.twowheeler.pojo;

import java.util.List;

public class LoanDetailsResponse {
	private String applicationNo;
	private String appNoWithProductCode;
	private String scheme;
	private String marginMoney;
	private String amount;
	private String preApprovalAmount;
	private String rateOfInterest;
	private String tenure;
	private String emi;
	private String insuranceEmi;
	private String totalOnRoadPrice;
	private String status;
	private String flowStatus;
	private String stage;
	private String tenureMin;
	private String tenureMax;
	private String requiredAmount;
	private List<LoanCharges> loanCharges;
	
	public String getRequiredAmount() {
		return requiredAmount;
	}
	public void setRequiredAmount(String requiredAmount) {
		this.requiredAmount = requiredAmount;
	}
	public List<LoanCharges> getLoanCharges() {
		return loanCharges;
	}
	public void setLoanCharges(List<LoanCharges> loanCharges) {
		this.loanCharges = loanCharges;
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
	public String getPreApprovalAmount() {
		return preApprovalAmount;
	}
	public void setPreApprovalAmount(String preApprovalAmount) {
		this.preApprovalAmount = preApprovalAmount;
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
	public String getScheme() {
		return scheme;
	}
	public void setScheme(String scheme) {
		this.scheme = scheme;
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
	public String getTotalOnRoadPrice() {
		return totalOnRoadPrice;
	}
	public void setTotalOnRoadPrice(String totalOnRoadPrice) {
		this.totalOnRoadPrice = totalOnRoadPrice;
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
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	
	
}
