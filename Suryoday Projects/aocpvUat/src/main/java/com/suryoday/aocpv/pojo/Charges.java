package com.suryoday.aocpv.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_charges")
public class Charges {
	private String product;
	@Id
	private String productCode;
	private String productName;
	private String loanAmount;
	private String tenure;
	private String flatRate;
	private String effectiveAnnualizedInterestRate;
	private String rateOfInterest;
	private String repaymentStrucyture1;
	private String repaymentStrucyture2;
	private String pchargeTotal;
	private String pchargeFees;
	private String pchargesGst;
	private String insuranceTerm;
	private String insurancePremium;
	private String firePerilsContentInsurancePremium;
	private String netDisbursedAmount;
	private String isActive;

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}

	public String getTenure() {
		return tenure;
	}

	public void setTenure(String tenure) {
		this.tenure = tenure;
	}

	public String getFlatRate() {
		return flatRate;
	}

	public void setFlatRate(String flatRate) {
		this.flatRate = flatRate;
	}

	public String getEffectiveAnnualizedInterestRate() {
		return effectiveAnnualizedInterestRate;
	}

	public void setEffectiveAnnualizedInterestRate(String effectiveAnnualizedInterestRate) {
		this.effectiveAnnualizedInterestRate = effectiveAnnualizedInterestRate;
	}

	public String getRateOfInterest() {
		return rateOfInterest;
	}

	public void setRateOfInterest(String rateOfInterest) {
		this.rateOfInterest = rateOfInterest;
	}

	public String getRepaymentStrucyture1() {
		return repaymentStrucyture1;
	}

	public void setRepaymentStrucyture1(String repaymentStrucyture1) {
		this.repaymentStrucyture1 = repaymentStrucyture1;
	}

	public String getRepaymentStrucyture2() {
		return repaymentStrucyture2;
	}

	public void setRepaymentStrucyture2(String repaymentStrucyture2) {
		this.repaymentStrucyture2 = repaymentStrucyture2;
	}

	public String getPchargeTotal() {
		return pchargeTotal;
	}

	public void setPchargeTotal(String pchargeTotal) {
		this.pchargeTotal = pchargeTotal;
	}

	public String getPchargeFees() {
		return pchargeFees;
	}

	public void setPchargeFees(String pchargeFees) {
		this.pchargeFees = pchargeFees;
	}

	public String getPchargesGst() {
		return pchargesGst;
	}

	public void setPchargesGst(String pchargesGst) {
		this.pchargesGst = pchargesGst;
	}

	public String getInsuranceTerm() {
		return insuranceTerm;
	}

	public void setInsuranceTerm(String insuranceTerm) {
		this.insuranceTerm = insuranceTerm;
	}

	public String getInsurancePremium() {
		return insurancePremium;
	}

	public void setInsurancePremium(String insurancePremium) {
		this.insurancePremium = insurancePremium;
	}

	public String getFirePerilsContentInsurancePremium() {
		return firePerilsContentInsurancePremium;
	}

	public void setFirePerilsContentInsurancePremium(String firePerilsContentInsurancePremium) {
		this.firePerilsContentInsurancePremium = firePerilsContentInsurancePremium;
	}

	public String getNetDisbursedAmount() {
		return netDisbursedAmount;
	}

	public void setNetDisbursedAmount(String netDisbursedAmount) {
		this.netDisbursedAmount = netDisbursedAmount;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "Charges [product=" + product + ", productCode=" + productCode + ", productName=" + productName
				+ ", loanAmount=" + loanAmount + ", tenure=" + tenure + ", flatRate=" + flatRate
				+ ", effectiveAnnualizedInterestRate=" + effectiveAnnualizedInterestRate + ", rateOfInterest="
				+ rateOfInterest + ", repaymentStrucyture1=" + repaymentStrucyture1 + ", repaymentStrucyture2="
				+ repaymentStrucyture2 + ", pchargeTotal=" + pchargeTotal + ", pchargeFees=" + pchargeFees
				+ ", pchargesGst=" + pchargesGst + ", insuranceTerm=" + insuranceTerm + ", insurancePremium="
				+ insurancePremium + ", firePerilsContentInsurancePremium=" + firePerilsContentInsurancePremium
				+ ", netDisbursedAmount=" + netDisbursedAmount + ", isActive=" + isActive + "]";
	}

}
