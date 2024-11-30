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
	private String emailId;
//	private String faceMatch;
//	private String faceMatchScore;
	private String nameMatch;
	private String nameMatchScore;
	private String employmentType;
	private String workStability;
	private String negativeLocality;
	private String localityName;
	@Lob
	private String addressArray;
	private String distanceFromBranch;
	private String nameOnPan;
	private String dobOnPancard;
	private String nameOnAAdhar;
	private String penny_drop_name_match_score;
	private String spouseName;
	private String relationWithApplicant;
	private String otherCompanyName;
	private String cibilScore;
	private String amlScore;
	private String customerId;
	@Lob
	private String dedupeResponse;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getDedupeResponse() {
		return dedupeResponse;
	}

	public void setDedupeResponse(String dedupeResponse) {
		this.dedupeResponse = dedupeResponse;
	}

	public String getAmlScore() {
		return amlScore;
	}

	public void setAmlScore(String amlScore) {
		this.amlScore = amlScore;
	}

	public String getDobOnPancard() {
		return dobOnPancard;
	}

	public void setDobOnPancard(String dobOnPancard) {
		this.dobOnPancard = dobOnPancard;
	}

	public String getCibilScore() {
		return cibilScore;
	}

	public void setCibilScore(String cibilScore) {
		this.cibilScore = cibilScore;
	}

	public String getOtherCompanyName() {
		return otherCompanyName;
	}

	public void setOtherCompanyName(String otherCompanyName) {
		this.otherCompanyName = otherCompanyName;
	}

	public String getSpouseName() {
		return spouseName;
	}

	public void setSpouseName(String spouseName) {
		this.spouseName = spouseName;
	}

	public String getRelationWithApplicant() {
		return relationWithApplicant;
	}

	public void setRelationWithApplicant(String relationWithApplicant) {
		this.relationWithApplicant = relationWithApplicant;
	}

	public String getPenny_drop_name_match_score() {
		return penny_drop_name_match_score;
	}

	public void setPenny_drop_name_match_score(String penny_drop_name_match_score) {
		this.penny_drop_name_match_score = penny_drop_name_match_score;
	}

	public String getNameOnPan() {
		return nameOnPan;
	}

	public void setNameOnPan(String nameOnPan) {
		this.nameOnPan = nameOnPan;
	}

	public String getNameOnAAdhar() {
		return nameOnAAdhar;
	}

	public void setNameOnAAdhar(String nameOnAAdhar) {
		this.nameOnAAdhar = nameOnAAdhar;
	}

	public String getNameMatch() {
		return nameMatch;
	}

	public void setNameMatch(String nameMatch) {
		this.nameMatch = nameMatch;
	}

	public String getNameMatchScore() {
		return nameMatchScore;
	}

	public void setNameMatchScore(String nameMatchScore) {
		this.nameMatchScore = nameMatchScore;
	}

	public String getDistanceFromBranch() {
		return distanceFromBranch;
	}

	public void setDistanceFromBranch(String distanceFromBranch) {
		this.distanceFromBranch = distanceFromBranch;
	}

	public String getAddressArray() {
		return addressArray;
	}

	public void setAddressArray(String addressArray) {
		this.addressArray = addressArray;
	}

	public String getNegativeLocality() {
		return negativeLocality;
	}

	public void setNegativeLocality(String negativeLocality) {
		this.negativeLocality = negativeLocality;
	}

	public String getLocalityName() {
		return localityName;
	}

	public void setLocalityName(String localityName) {
		this.localityName = localityName;
	}

	public String getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}

	public String getWorkStability() {
		return workStability;
	}

	public void setWorkStability(String workStability) {
		this.workStability = workStability;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
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

	public String getAnnualIncome() {
		return annualIncome;
	}

	public void setAnnualIncome(String annualIncome) {
		this.annualIncome = annualIncome;
	}

	@Override
	public String toString() {
		return "TwoWheelerFamilyMember [id=" + id + ", applicationNo=" + applicationNo + ", firstName=" + firstName
				+ ", title=" + title + ", middleName=" + middleName + ", lastName=" + lastName + ", occupation="
				+ occupation + ", gender=" + gender + ", age=" + age + ", dob=" + dob + ", appNoWithProductCode="
				+ appNoWithProductCode + ", createdTimestamp=" + createdTimestamp + ", mobile=" + mobile
				+ ", mobileVerify=" + mobileVerify + ", married=" + married + ", aadharCard=" + aadharCard
				+ ", aadharNoVerify=" + aadharNoVerify + ", panCard=" + panCard + ", pancardNoVerify=" + pancardNoVerify
				+ ", voterId=" + voterId + ", voterIdVerify=" + voterIdVerify + ", earning=" + earning + ", member="
				+ member + ", category=" + category + ", panCardResponse=" + panCardResponse + ", voterIdResponse="
				+ voterIdResponse + ", ekycVerify=" + ekycVerify + ", ekycResponse=" + ekycResponse + ", ekycDoneBy="
				+ ekycDoneBy + ", updatedTimestamp=" + updatedTimestamp + ", address=" + address
				+ ", maxEmiEligibility=" + maxEmiEligibility + ", guardian=" + guardian + ", nomineeRelationship="
				+ nomineeRelationship + ", fatherName=" + fatherName + ", motherName=" + motherName + ", caste=" + caste
				+ ", religion=" + religion + ", education=" + education + ", noOfDependent=" + noOfDependent
				+ ", noOfFamilyMember=" + noOfFamilyMember + ", annualIncome=" + annualIncome + ", companyName="
				+ companyName + ", form60=" + form60 + ", selectedIdentityProof=" + selectedIdentityProof
				+ ", identityProof=" + identityProof + ", identityProofVerify=" + identityProofVerify + ", occCode="
				+ occCode + ", religionCode=" + religionCode + ", casteCode=" + casteCode + ", educationCode="
				+ educationCode + ", marriedCode=" + marriedCode + ", genderCode=" + genderCode + ", titleCode="
				+ titleCode + ", categoryCode=" + categoryCode + ", emailId=" + emailId + ", nameMatch=" + nameMatch
				+ ", nameMatchScore=" + nameMatchScore + ", employmentType=" + employmentType + ", workStability="
				+ workStability + ", negativeLocality=" + negativeLocality + ", localityName=" + localityName
				+ ", addressArray=" + addressArray + ", distanceFromBranch=" + distanceFromBranch + ", nameOnPan="
				+ nameOnPan + ", nameOnAAdhar=" + nameOnAAdhar + ", penny_drop_name_match_score="
				+ penny_drop_name_match_score + ", spouseName=" + spouseName + ", relationWithApplicant="
				+ relationWithApplicant + "]";
	}

}
