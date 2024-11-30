package com.suryoday.dsaOnboard.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DsaOnboardCPV {

	@Id
	private int id;
	private String creditManager;
	private String state;
	private String district;
	private String ssfbBranchLocation;
	private String fIAgencyName;
	private String emailId;
	private String contactPersonName;
	private String mobileNo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreditManager() {
		return creditManager;
	}

	public void setCreditManager(String creditManager) {
		this.creditManager = creditManager;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getSsfbBranchLocation() {
		return ssfbBranchLocation;
	}

	public void setSsfbBranchLocation(String ssfbBranchLocation) {
		this.ssfbBranchLocation = ssfbBranchLocation;
	}

	public String getfIAgencyName() {
		return fIAgencyName;
	}

	public void setfIAgencyName(String fIAgencyName) {
		this.fIAgencyName = fIAgencyName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getContactPersonName() {
		return contactPersonName;
	}

	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public DsaOnboardCPV(int id, String creditManager, String state, String district, String ssfbBranchLocation,
			String fIAgencyName, String emailId, String contactPersonName, String mobileNo) {
		super();
		this.id = id;
		this.creditManager = creditManager;
		this.state = state;
		this.district = district;
		this.ssfbBranchLocation = ssfbBranchLocation;
		this.fIAgencyName = fIAgencyName;
		this.emailId = emailId;
		this.contactPersonName = contactPersonName;
		this.mobileNo = mobileNo;
	}

	public DsaOnboardCPV() {
		super();
		// TODO Auto-generated constructor stub
	}

}
