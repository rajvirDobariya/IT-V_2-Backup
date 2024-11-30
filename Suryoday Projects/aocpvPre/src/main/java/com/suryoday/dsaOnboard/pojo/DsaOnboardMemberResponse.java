package com.suryoday.dsaOnboard.pojo;

import java.util.List;

public class DsaOnboardMemberResponse {

	private long applicationNo;
	private String panCardNo;
	private boolean panCardVerify;
	private String name;
	private String gender;
	private String presentOccupation;
	private String alternateMobileNo;
	private String aadharNo;
	private boolean aadharNoVerify;
	private String mobLinkwithAadhar;
	private String dateOfBirth;
	private List<Address> address;
	private String member;
	private String mobile;
	private boolean mobileNoVerify;
	private boolean alternateMobileNoVerify;
	private String entityName;
	private String addressForCommunication;
	private String gstNo;
	private String gstNoVerify;
	private String isMsmeRegister;
	private List<GstResponse> secondaryGst;
	private String companyName;
	private String isPrimaryMember;
	private String emailId;
	private String nameOnPanCard;
	private String dobOnPanCard;
	private String aadharPanLinkStatus;
	private String fatherName;
	
	public String getNameOnPanCard() {
		return nameOnPanCard;
	}
	public void setNameOnPanCard(String nameOnPanCard) {
		this.nameOnPanCard = nameOnPanCard;
	}
	public String getDobOnPanCard() {
		return dobOnPanCard;
	}
	public void setDobOnPanCard(String dobOnPanCard) {
		this.dobOnPanCard = dobOnPanCard;
	}
	public String getAadharPanLinkStatus() {
		return aadharPanLinkStatus;
	}
	public void setAadharPanLinkStatus(String aadharPanLinkStatus) {
		this.aadharPanLinkStatus = aadharPanLinkStatus;
	}
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getIsPrimaryMember() {
		return isPrimaryMember;
	}
	public void setIsPrimaryMember(String isPrimaryMember) {
		this.isPrimaryMember = isPrimaryMember;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getIsMsmeRegister() {
		return isMsmeRegister;
	}
	public void setIsMsmeRegister(String isMsmeRegister) {
		this.isMsmeRegister = isMsmeRegister;
	}
	public List<GstResponse> getSecondaryGst() {
		return secondaryGst;
	}
	public void setSecondaryGst(List<GstResponse> secondaryGst) {
		this.secondaryGst = secondaryGst;
	}
	public String getGstNo() {
		return gstNo;
	}
	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}
	public String getGstNoVerify() {
		return gstNoVerify;
	}
	public void setGstNoVerify(String gstNoVerify) {
		this.gstNoVerify = gstNoVerify;
	}
	public long getApplicationNo() {
		return applicationNo;
	}
	public void setApplicationNo(long applicationNo) {
		this.applicationNo = applicationNo;
	}
	public String getPanCardNo() {
		return panCardNo;
	}
	public void setPanCardNo(String panCardNo) {
		this.panCardNo = panCardNo;
	}
	public boolean isPanCardVerify() {
		return panCardVerify;
	}
	public void setPanCardVerify(boolean panCardVerify) {
		this.panCardVerify = panCardVerify;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPresentOccupation() {
		return presentOccupation;
	}
	public void setPresentOccupation(String presentOccupation) {
		this.presentOccupation = presentOccupation;
	}
	public String getAlternateMobileNo() {
		return alternateMobileNo;
	}
	public void setAlternateMobileNo(String alternateMobileNo) {
		this.alternateMobileNo = alternateMobileNo;
	}
	public String getAadharNo() {
		return aadharNo;
	}
	public void setAadharNo(String aadharNo) {
		this.aadharNo = aadharNo;
	}
	public boolean isAadharNoVerify() {
		return aadharNoVerify;
	}
	public void setAadharNoVerify(boolean aadharNoVerify) {
		this.aadharNoVerify = aadharNoVerify;
	}
	public String getMobLinkwithAadhar() {
		return mobLinkwithAadhar;
	}
	public void setMobLinkwithAadhar(String mobLinkwithAadhar) {
		this.mobLinkwithAadhar = mobLinkwithAadhar;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public List<Address> getAddress() {
		return address;
	}
	public void setAddress(List<Address> address) {
		this.address = address;
	}
	public String getMember() {
		return member;
	}
	public void setMember(String member) {
		this.member = member;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public boolean isMobileNoVerify() {
		return mobileNoVerify;
	}
	public void setMobileNoVerify(boolean mobileNoVerify) {
		this.mobileNoVerify = mobileNoVerify;
	}
	public boolean isAlternateMobileNoVerify() {
		return alternateMobileNoVerify;
	}
	public void setAlternateMobileNoVerify(boolean alternateMobileNoVerify) {
		this.alternateMobileNoVerify = alternateMobileNoVerify;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getAddressForCommunication() {
		return addressForCommunication;
	}
	public void setAddressForCommunication(String addressForCommunication) {
		this.addressForCommunication = addressForCommunication;
	}
	
	
	
}
