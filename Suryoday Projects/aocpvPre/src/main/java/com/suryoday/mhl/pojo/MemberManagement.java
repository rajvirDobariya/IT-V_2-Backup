package com.suryoday.mhl.pojo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_MEMBER_MANAGEMENT")
public class MemberManagement {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int mmId;
	private String applicationNo;
	private String applicationRole;
	private long memberId;
	private String mobileNo;
	private String kycDetails;
	private String kycNo;
	private String addressProof;
	private String otherProof;
	private String basicDetails;
	private String title;
	private String firstName;
	private String middleName;
	private String lastName;
	private String dateOfBirth;
	private int age;
	private String gender;
	private String fatherName;
	private String pan60;
	private String panNo;
	private String aadherKycRequest;
	private String aadharKycResponse;
	private String panRequest;
	private String panResponse;
	private String voterIdRequest;
	private String voterIdResponse;
	private String branchId;
	private String emailId;
	private String isBankCustomer;
	private String customerId;
	private String customerCategory;
	private String occupation;
	private String educationQualification;
	private String noDependency;
	private String noFamilyMember;
	private String caste;
	private String religion;
	private String residentType;
	private String indivisualConstitution;
	private String remarks;
	private String motherName;
	private String spouseName;
	private String marriedStatus;
	private LocalDateTime creationDate;
	private LocalDateTime updatedate;
	public int getMmId() {
		return mmId;
	}
	public void setMmId(int mmId) {
		this.mmId = mmId;
	}
	public String getApplicationNo() {
		return applicationNo;
	}
	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}
	public String getApplicationRole() {
		return applicationRole;
	}
	public void setApplicationRole(String applicationRole) {
		this.applicationRole = applicationRole.toUpperCase();
	}
	public long getMemberId() {
		return memberId;
	}
	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getKycDetails() {
		return kycDetails;
	}
	public void setKycDetails(String kycDetails) {
		this.kycDetails = kycDetails.toUpperCase();
	}
	public String getKycNo() {
		return kycNo;
	}
	public void setKycNo(String kycNo) {
		this.kycNo = kycNo;
	}
	public String getAddressProof() {
		return addressProof;
	}
	public void setAddressProof(String addressProof) {
		this.addressProof = addressProof.toUpperCase();
	}
	public String getOtherProof() {
		return otherProof;
	}
	public void setOtherProof(String otherProof) {
		this.otherProof = otherProof.toUpperCase();
	}
	public String getBasicDetails() {
		return basicDetails;
	}
	public void setBasicDetails(String basicDetails) {
		this.basicDetails = basicDetails.toUpperCase();
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title.toUpperCase();
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName.toUpperCase();
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName.toUpperCase();
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName.toUpperCase();
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender.toUpperCase();
	}
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName.toUpperCase();
	}
	public String getPan60() {
		return pan60;
	}
	public void setPan60(String pan60) {
		this.pan60 = pan60.toUpperCase();
	}
	public String getPanNo() {
		return panNo;
	}
	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}
	public String getAadherKycRequest() {
		return aadherKycRequest;
	}
	public void setAadherKycRequest(String aadherKycRequest) {
		this.aadherKycRequest = aadherKycRequest;
	}
	public String getAadharKycResponse() {
		return aadharKycResponse;
	}
	public void setAadharKycResponse(String aadharKycResponse) {
		this.aadharKycResponse = aadharKycResponse;
	}
	public String getPanRequest() {
		return panRequest;
	}
	public void setPanRequest(String panRequest) {
		this.panRequest = panRequest;
	}
	public String getPanResponse() {
		return panResponse;
	}
	public void setPanResponse(String panResponse) {
		this.panResponse = panResponse;
	}
	public String getVoterIdRequest() {
		return voterIdRequest;
	}
	public void setVoterIdRequest(String voterIdRequest) {
		this.voterIdRequest = voterIdRequest;
	}
	public String getVoterIdResponse() {
		return voterIdResponse;
	}
	public void setVoterIdResponse(String voterIdResponse) {
		this.voterIdResponse = voterIdResponse;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getIsBankCustomer() {
		return isBankCustomer;
	}
	public void setIsBankCustomer(String isBankCustomer) {
		this.isBankCustomer = isBankCustomer.toUpperCase();
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerCategory() {
		return customerCategory;
	}
	public void setCustomerCategory(String customerCategory) {
		this.customerCategory = customerCategory.toUpperCase();
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation.toUpperCase();
	}
	public String getEducationQualification() {
		return educationQualification;
	}
	public void setEducationQualification(String educationQualification) {
		this.educationQualification = educationQualification.toUpperCase();
	}
	public String getNoDependency() {
		return noDependency;
	}
	public void setNoDependency(String noDependency) {
		this.noDependency = noDependency.toUpperCase();
	}
	public String getNoFamilyMember() {
		return noFamilyMember;
	}
	public void setNoFamilyMember(String noFamilyMember) {
		this.noFamilyMember = noFamilyMember.toUpperCase();
	}
	public String getCaste() {
		return caste;
	}
	public void setCaste(String caste) {
		this.caste = caste.toUpperCase();
	}
	public String getReligion() {
		return religion;
	}
	public void setReligion(String religion) {
		this.religion = religion.toUpperCase();
	}
	public String getResidentType() {
		return residentType;
	}
	public void setResidentType(String residentType) {
		this.residentType = residentType.toUpperCase();
	}
	public String getIndivisualConstitution() {
		return indivisualConstitution;
	}
	public void setIndivisualConstitution(String indivisualConstitution) {
		this.indivisualConstitution = indivisualConstitution.toUpperCase();
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks.toUpperCase();
	}
	public String getMotherName() {
		return motherName;
	}
	public void setMotherName(String motherName) {
		this.motherName = motherName.toUpperCase();
	}
	public String getSpouseName() {
		return spouseName;
	}
	public void setSpouseName(String spouseName) {
		this.spouseName = spouseName.toUpperCase();
	}
	public String getMarriedStatus() {
		return marriedStatus;
	}
	public void setMarriedStatus(String marriedStatus) {
		this.marriedStatus = marriedStatus.toUpperCase();
	}
	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
	public LocalDateTime getUpdatedate() {
		return updatedate;
	}
	public void setUpdatedate(LocalDateTime updatedate) {
		this.updatedate = updatedate;
	}
	@Override
	public String toString() {
		return "MemberManagement [mmId=" + mmId + ", applicationNo=" + applicationNo + ", applicationRole="
				+ applicationRole + ", memberId=" + memberId + ", mobileNo=" + mobileNo + ", kycDetails=" + kycDetails
				+ ", kycNo=" + kycNo + ", addressProof=" + addressProof + ", otherProof=" + otherProof
				+ ", basicDetails=" + basicDetails + ", title=" + title + ", firstName=" + firstName + ", middleName="
				+ middleName + ", lastName=" + lastName + ", dateOfBirth=" + dateOfBirth + ", age=" + age + ", gender="
				+ gender + ", fatherName=" + fatherName + ", pan60=" + pan60 + ", panNo=" + panNo
				+ ", aadherKycRequest=" + aadherKycRequest + ", aadharKycResponse=" + aadharKycResponse
				+ ", panRequest=" + panRequest + ", panResponse=" + panResponse + ", voterIdRequest=" + voterIdRequest
				+ ", voterIdResponse=" + voterIdResponse + ", branchId=" + branchId + ", emailId=" + emailId
				+ ", isBankCustomer=" + isBankCustomer + ", customerId=" + customerId + ", customerCategory="
				+ customerCategory + ", occupation=" + occupation + ", educationQualification=" + educationQualification
				+ ", noDependency=" + noDependency + ", noFamilyMember=" + noFamilyMember + ", caste=" + caste
				+ ", religion=" + religion + ", residentType=" + residentType + ", indivisualConstitution="
				+ indivisualConstitution + ", remarks=" + remarks + ", motherName=" + motherName + ", spouseName="
				+ spouseName + ", marriedStatus=" + marriedStatus + ", creationDate=" + creationDate + ", updatedate="
				+ updatedate + "]";
	}
	
	
	
}