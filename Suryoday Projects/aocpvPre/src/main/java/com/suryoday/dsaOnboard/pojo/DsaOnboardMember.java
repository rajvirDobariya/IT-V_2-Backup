package com.suryoday.dsaOnboard.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class DsaOnboardMember {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
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
	@Lob
	private String address;
	private String member;
	private String mobile;
	private boolean mobileNoVerify;
	private boolean alternateMobileNoVerify;
	private String entityName;
	private String constitutionType;
	private String addressForCommunication;
	@Lob
	private String pancardResponse;
	private String nameMatch;
	private String faceMatch;
	private String nameMatchPercent;
	private String faceMatchPercent;
	private String ekycVerify;
	private String ekycDoneBy;
	@Lob
	private String ekycResponse;
	private String gstNo;
	@Lob
	private String gstResponse;
	private String gstNoVerify;
	private String cibilScore;
	@Lob
	private String secondaryGst;
	private String isMsmeRegister;
	private String companyName;
	private String isPrimaryMember;
	private String nameOnPanCard;
	private String dobOnPanCard;
	private String aadharPanLinkStatus;
	private String emailId;
	private String nameOnAadhar;
	private String fatherName;
	
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public String getDobOnPanCard() {
		return dobOnPanCard;
	}
	public void setDobOnPanCard(String dobOnPanCard) {
		this.dobOnPanCard = dobOnPanCard;
	}
	public String getNameOnAadhar() {
		return nameOnAadhar;
	}
	public void setNameOnAadhar(String nameOnAadhar) {
		this.nameOnAadhar = nameOnAadhar;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getNameOnPanCard() {
		return nameOnPanCard;
	}
	public void setNameOnPanCard(String nameOnPanCard) {
		this.nameOnPanCard = nameOnPanCard;
	}
	public String getAadharPanLinkStatus() {
		return aadharPanLinkStatus;
	}
	public void setAadharPanLinkStatus(String aadharPanLinkStatus) {
		this.aadharPanLinkStatus = aadharPanLinkStatus;
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
	public String getSecondaryGst() {
		return secondaryGst;
	}
	public void setSecondaryGst(String secondaryGst) {
		this.secondaryGst = secondaryGst;
	}
	public String getEkycDoneBy() {
		return ekycDoneBy;
	}
	public void setEkycDoneBy(String ekycDoneBy) {
		this.ekycDoneBy = ekycDoneBy;
	}
	public String getGstNoVerify() {
		return gstNoVerify;
	}
	public void setGstNoVerify(String gstNoVerify) {
		this.gstNoVerify = gstNoVerify;
	}
	public String getCibilScore() {
		return cibilScore;
	}
	public void setCibilScore(String cibilScore) {
		this.cibilScore = cibilScore;
	}
	public String getNameMatch() {
		return nameMatch;
	}
	public void setNameMatch(String nameMatch) {
		this.nameMatch = nameMatch;
	}
	public String getFaceMatch() {
		return faceMatch;
	}
	public void setFaceMatch(String faceMatch) {
		this.faceMatch = faceMatch;
	}
	public String getNameMatchPercent() {
		return nameMatchPercent;
	}
	public void setNameMatchPercent(String nameMatchPercent) {
		this.nameMatchPercent = nameMatchPercent;
	}
	public String getFaceMatchPercent() {
		return faceMatchPercent;
	}
	public void setFaceMatchPercent(String faceMatchPercent) {
		this.faceMatchPercent = faceMatchPercent;
	}
	public String getEkycVerify() {
		return ekycVerify;
	}
	public void setEkycVerify(String ekycVerify) {
		this.ekycVerify = ekycVerify;
	}
	public String getEkycResponse() {
		return ekycResponse;
	}
	public void setEkycResponse(String ekycResponse) {
		this.ekycResponse = ekycResponse;
	}
	public String getGstNo() {
		return gstNo;
	}
	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}
	public String getGstResponse() {
		return gstResponse;
	}
	public void setGstResponse(String gstResponse) {
		this.gstResponse = gstResponse;
	}
	public String getPancardResponse() {
		return pancardResponse;
	}
	public void setPancardResponse(String pancardResponse) {
		this.pancardResponse = pancardResponse;
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
	public String getAddressForCommunication() {
		return addressForCommunication;
	}
	public void setAddressForCommunication(String addressForCommunication) {
		this.addressForCommunication = addressForCommunication;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getConstitutionType() {
		return constitutionType;
	}
	public void setConstitutionType(String constitutionType) {
		this.constitutionType = constitutionType;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMember() {
		return member;
	}
	public void setMember(String member) {
		this.member = member;
	}
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "DsaOnboardMember [id=" + id + ", applicationNo=" + applicationNo + ", panCardNo=" + panCardNo
				+ ", panCardVerify=" + panCardVerify + ", name=" + name + ", gender=" + gender + ", presentOccupation="
				+ presentOccupation + ", alternateMobileNo=" + alternateMobileNo + ", aadharNo=" + aadharNo
				+ ", aadharNoVerify=" + aadharNoVerify + ", mobLinkwithAadhar=" + mobLinkwithAadhar + ", dateOfBirth="
				+ dateOfBirth + ", address=" + address + "]";
	}
	
}
