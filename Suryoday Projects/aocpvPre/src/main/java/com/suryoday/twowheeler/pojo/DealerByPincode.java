package com.suryoday.twowheeler.pojo;

public class DealerByPincode {

	private String dealerCode;
	private String dealerName;
	private String dealerAddress;
	private String branchIdArray;
	
	public String getBranchIdArray() {
		return branchIdArray;
	}

	public void setBranchIdArray(String branchIdArray) {
		this.branchIdArray = branchIdArray;
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

	public DealerByPincode(String dealerCode, String dealerName, String dealerAddress,String branchIdArray) {
		super();
		this.dealerCode = dealerCode;
		this.dealerName = dealerName;
		this.dealerAddress = dealerAddress;
		this.branchIdArray = branchIdArray;
	}

	public DealerByPincode() {
		super();
		// TODO Auto-generated constructor stub
	}

}
