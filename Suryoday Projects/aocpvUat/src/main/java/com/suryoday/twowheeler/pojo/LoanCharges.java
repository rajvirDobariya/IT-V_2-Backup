package com.suryoday.twowheeler.pojo;

public class LoanCharges {

	private String chargeName;
	private String chargeAmount;
	private String taxAmount;
	private String totalAmount;
	private String type;

	public String getChargeName() {
		return chargeName;
	}

	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}

	public String getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(String chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	public String getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(String taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public LoanCharges(String chargeName, String chargeAmount, String taxAmount, String totalAmount, String type) {
		super();
		this.chargeName = chargeName;
		this.chargeAmount = chargeAmount;
		this.taxAmount = taxAmount;
		this.totalAmount = totalAmount;
		this.type = type;
	}

	public LoanCharges() {
		super();
		// TODO Auto-generated constructor stub
	}

}
