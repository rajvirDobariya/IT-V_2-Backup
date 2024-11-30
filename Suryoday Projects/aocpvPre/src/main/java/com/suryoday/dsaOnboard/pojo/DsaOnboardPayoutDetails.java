package com.suryoday.dsaOnboard.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DsaOnboardPayoutDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long applicationNo;
	private String agencyType;
	private String productType;
	private String payoutFrequency;
	private String year;
	private String schemeCode;
	private String assignedToBranchCode;
	private String remarks;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getApplicationNo() {
		return applicationNo;
	}
	public void setApplicationNo(long applicationNo) {
		this.applicationNo = applicationNo;
	}
	public String getAgencyType() {
		return agencyType;
	}
	public void setAgencyType(String agencyType) {
		this.agencyType = agencyType;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getPayoutFrequency() {
		return payoutFrequency;
	}
	public void setPayoutFrequency(String payoutFrequency) {
		this.payoutFrequency = payoutFrequency;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getSchemeCode() {
		return schemeCode;
	}
	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}
	public String getAssignedToBranchCode() {
		return assignedToBranchCode;
	}
	public void setAssignedToBranchCode(String assignedToBranchCode) {
		this.assignedToBranchCode = assignedToBranchCode;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
	
	
}
