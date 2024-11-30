package com.suryoday.twowheeler.pojo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class TwoWheelerFamilyMember {
	@Id
	private int id;
	private String applicationNo;
	private String firstName;
	private String title;
	private String middleName;
	private String lastName;
	private String occupation;
	private String gender;
	private String age;
	private String dob;
	private String appNoWithProductCode;
	private LocalDateTime createdTimestamp;
	private String requiredAmount;
	private String mobile;
	private String mobileVerify;
	private String married;
	private String aadharCard;
	private String aadharNoVerify;
	private String panCard;
	private String pancardNoVerify;
	private String voterId;
	private String voterIdVerify;
	private String earning;
	private String member;
	private String category;
	@Lob
	private String panCardResponse;
	@Lob
	private String voterIdResponse;
	private String ekycVerify;
	@Lob
	private String ekycResponse;
	private String ekycDoneBy;
	private LocalDateTime updatedTimestamp;
	private String address;
	private String leadId;
	private String isBreRuring;
	private String isLeadCreated;
	private String breStatus;
	@Lob
	private String breResponse;
	private String maxEmiEligibility;
	@Lob
	private String guardian;
	private String nomineeRelationship;
	private String fatherName;
	private String motherName;
	private String caste;
	private String religion;
	private String education;
	private String noOfDependent;
	private String noOfFamilyMember;
	private String annualIncome;
	private String companyName;
	private String form60;
	private String selectedIdentityProof;
	private String identityProof;
	private String identityProofVerify;
	private String occCode;
	private String religionCode;
	private String casteCode;
	private String educationCode;
	private String marriedCode;
	private String genderCode;
	private String titleCode;
	private String categoryCode;
	private String nameOnPan;
	private String dobOnPancard;
	
	public String getNameOnPan() {
		return nameOnPan;
	}
	public void setNameOnPan(String nameOnPan) {
		this.nameOnPan = nameOnPan;
	}
	public String getDobOnPancard() {
		return dobOnPancard;
	}
	public void setDobOnPancard(String dobOnPancard) {
		this.dobOnPancard = dobOnPancard;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getOccCode() {
		return occCode;
	}
	public void setOccCode(String occCode) {
		this.occCode = occCode;
	}
	public String getReligionCode() {
		return religionCode;
	}
	public void setReligionCode(String religionCode) {
		this.religionCode = religionCode;
	}
	public String getCasteCode() {
		return casteCode;
	}
	public void setCasteCode(String casteCode) {
		this.casteCode = casteCode;
	}
	public String getEducationCode() {
		return educationCode;
	}
	public void setEducationCode(String educationCode) {
		this.educationCode = educationCode;
	}
	public String getMarriedCode() {
		return marriedCode;
	}
	public void setMarriedCode(String marriedCode) {
		this.marriedCode = marriedCode;
	}
	public String getGenderCode() {
		return genderCode;
	}
	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}
	public String getTitleCode() {
		return titleCode;
	}
	public void setTitleCode(String titleCode) {
		this.titleCode = titleCode;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getApplicationNo() {
		return applicationNo;
	}
	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getAppNoWithProductCode() {
		return appNoWithProductCode;
	}
	public void setAppNoWithProductCode(String appNoWithProductCode) {
		this.appNoWithProductCode = appNoWithProductCode;
	}
	public LocalDateTime getCreatedTimestamp() {
		return createdTimestamp;
	}
	public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	public String getRequiredAmount() {
		return requiredAmount;
	}
	public void setRequiredAmount(String requiredAmount) {
		this.requiredAmount = requiredAmount;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMobileVerify() {
		return mobileVerify;
	}
	public void setMobileVerify(String mobileVerify) {
		this.mobileVerify = mobileVerify;
	}
	public String getMarried() {
		return married;
	}
	public void setMarried(String married) {
		this.married = married;
	}
	public String getAadharCard() {
		return aadharCard;
	}
	public void setAadharCard(String aadharCard) {
		this.aadharCard = aadharCard;
	}
	public String getAadharNoVerify() {
		return aadharNoVerify;
	}
	public void setAadharNoVerify(String aadharNoVerify) {
		this.aadharNoVerify = aadharNoVerify;
	}
	public String getPanCard() {
		return panCard;
	}
	public void setPanCard(String panCard) {
		this.panCard = panCard;
	}
	public String getPancardNoVerify() {
		return pancardNoVerify;
	}
	public void setPancardNoVerify(String pancardNoVerify) {
		this.pancardNoVerify = pancardNoVerify;
	}
	public String getVoterId() {
		return voterId;
	}
	public void setVoterId(String voterId) {
		this.voterId = voterId;
	}
	public String getVoterIdVerify() {
		return voterIdVerify;
	}
	public void setVoterIdVerify(String voterIdVerify) {
		this.voterIdVerify = voterIdVerify;
	}
	public String getEarning() {
		return earning;
	}
	public void setEarning(String earning) {
		this.earning = earning;
	}
	public String getMember() {
		return member;
	}
	public void setMember(String member) {
		this.member = member;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getPanCardResponse() {
		return panCardResponse;
	}
	public void setPanCardResponse(String panCardResponse) {
		this.panCardResponse = panCardResponse;
	}
	public String getVoterIdResponse() {
		return voterIdResponse;
	}
	public void setVoterIdResponse(String voterIdResponse) {
		this.voterIdResponse = voterIdResponse;
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
	public String getEkycDoneBy() {
		return ekycDoneBy;
	}
	public void setEkycDoneBy(String ekycDoneBy) {
		this.ekycDoneBy = ekycDoneBy;
	}
	public LocalDateTime getUpdatedTimestamp() {
		return updatedTimestamp;
	}
	public void setUpdatedTimestamp(LocalDateTime updatedTimestamp) {
		this.updatedTimestamp = updatedTimestamp;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLeadId() {
		return leadId;
	}
	public void setLeadId(String leadId) {
		this.leadId = leadId;
	}
	public String getIsBreRuring() {
		return isBreRuring;
	}
	public void setIsBreRuring(String isBreRuring) {
		this.isBreRuring = isBreRuring;
	}
	public String getIsLeadCreated() {
		return isLeadCreated;
	}
	public void setIsLeadCreated(String isLeadCreated) {
		this.isLeadCreated = isLeadCreated;
	}
	public String getBreStatus() {
		return breStatus;
	}
	public void setBreStatus(String breStatus) {
		this.breStatus = breStatus;
	}
	public String getBreResponse() {
		return breResponse;
	}
	public void setBreResponse(String breResponse) {
		this.breResponse = breResponse;
	}
	public String getMaxEmiEligibility() {
		return maxEmiEligibility;
	}
	public void setMaxEmiEligibility(String maxEmiEligibility) {
		this.maxEmiEligibility = maxEmiEligibility;
	}
	public String getGuardian() {
		return guardian;
	}
	public void setGuardian(String guardian) {
		this.guardian = guardian;
	}
	public String getNomineeRelationship() {
		return nomineeRelationship;
	}
	public void setNomineeRelationship(String nomineeRelationship) {
		this.nomineeRelationship = nomineeRelationship;
	}
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public String getMotherName() {
		return motherName;
	}
	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}
	public String getCaste() {
		return caste;
	}
	public void setCaste(String caste) {
		this.caste = caste;
	}
	public String getReligion() {
		return religion;
	}
	public void setReligion(String religion) {
		this.religion = religion;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getNoOfDependent() {
		return noOfDependent;
	}
	public void setNoOfDependent(String noOfDependent) {
		this.noOfDependent = noOfDependent;
	}
	public String getNoOfFamilyMember() {
		return noOfFamilyMember;
	}
	public void setNoOfFamilyMember(String noOfFamilyMember) {
		this.noOfFamilyMember = noOfFamilyMember;
	}
	public String getAnnualIncome() {
		return annualIncome;
	}
	public void setAnnualIncome(String annualIncome) {
		this.annualIncome = annualIncome;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getForm60() {
		return form60;
	}
	public void setForm60(String form60) {
		this.form60 = form60;
	}
	public String getSelectedIdentityProof() {
		return selectedIdentityProof;
	}
	public void setSelectedIdentityProof(String selectedIdentityProof) {
		this.selectedIdentityProof = selectedIdentityProof;
	}
	public String getIdentityProof() {
		return identityProof;
	}
	public void setIdentityProof(String identityProof) {
		this.identityProof = identityProof;
	}
	public String getIdentityProofVerify() {
		return identityProofVerify;
	}
	public void setIdentityProofVerify(String identityProofVerify) {
		this.identityProofVerify = identityProofVerify;
	}
	@Override
	public String toString() {
		return "TwoWheelerFamilyMember [id=" + id + ", applicationNo=" + applicationNo + ", firstName=" + firstName
				+ ", title=" + title + ", middleName=" + middleName + ", lastName=" + lastName + ", occupation="
				+ occupation + ", gender=" + gender + ", age=" + age + ", dob=" + dob + ", appNoWithProductCode="
				+ appNoWithProductCode + ", createdTimestamp=" + createdTimestamp + ", requiredAmount=" + requiredAmount
				+ ", mobile=" + mobile + ", mobileVerify=" + mobileVerify + ", married=" + married + ", aadharCard="
				+ aadharCard + ", aadharNoVerify=" + aadharNoVerify + ", panCard=" + panCard + ", pancardNoVerify="
				+ pancardNoVerify + ", voterId=" + voterId + ", voterIdVerify=" + voterIdVerify + ", earning=" + earning
				+ ", member=" + member + ", category=" + category + ", panCardResponse=" + panCardResponse
				+ ", voterIdResponse=" + voterIdResponse + ", ekycVerify=" + ekycVerify + ", ekycResponse="
				+ ekycResponse + ", ekycDoneBy=" + ekycDoneBy + ", updatedTimestamp=" + updatedTimestamp + ", address="
				+ address + ", leadId=" + leadId + ", isBreRuring=" + isBreRuring + ", isLeadCreated=" + isLeadCreated
				+ ", breStatus=" + breStatus + ", breResponse=" + breResponse + ", maxEmiEligibility="
				+ maxEmiEligibility + ", guardian=" + guardian + ", nomineeRelationship=" + nomineeRelationship
				+ ", fatherName=" + fatherName + ", motherName=" + motherName + ", caste=" + caste + ", religion="
				+ religion + ", education=" + education + ", noOfDependent=" + noOfDependent + ", noOfFamilyMember="
				+ noOfFamilyMember + ", annualIncome=" + annualIncome + ", companyName=" + companyName + ", form60="
				+ form60 + ", selectedIdentityProof=" + selectedIdentityProof + ", identityProof=" + identityProof
				+ ", identityProofVerify=" + identityProofVerify + "]";
	}
	
	
}
