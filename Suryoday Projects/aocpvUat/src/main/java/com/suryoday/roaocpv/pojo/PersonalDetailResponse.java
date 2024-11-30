package com.suryoday.roaocpv.pojo;

public class PersonalDetailResponse {

	private String title;
	private String firstName;
	private String middleName;
	private String lastName;
	private String mobile;
	private String dateOfBirth;
	private String age;
	private String gender;
	private String fathername;
	private String motherName;
	private String spouseName;
	private String numberOfDependant;
	private String educationQualification;
	private String caste;
	private String religion;
	private String marritialStatus;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMarritialStatus() {
		return marritialStatus;
	}

	public void setMarritialStatus(String marritialStatus) {
		this.marritialStatus = marritialStatus;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getFathername() {
		return fathername;
	}

	public void setFathername(String fathername) {
		this.fathername = fathername;
	}

	public String getMotherName() {
		return motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public String getSpouseName() {
		return spouseName;
	}

	public void setSpouseName(String spouseName) {
		this.spouseName = spouseName;
	}

	public String getNumberOfDependant() {
		return numberOfDependant;
	}

	public void setNumberOfDependant(String numberOfDependant) {
		this.numberOfDependant = numberOfDependant;
	}

	public String getEducationQualification() {
		return educationQualification;
	}

	public void setEducationQualification(String educationQualification) {
		this.educationQualification = educationQualification;
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

	public PersonalDetailResponse(String title, String firstName, String middleName, String lastName, String mobile,
			String dateOfBirth, String age, String gender, String fathername, String motherName, String spouseName,
			String numberOfDependant, String educationQualification, String caste, String religion,
			String marritialStatus) {
		super();
		this.title = title;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.mobile = mobile;
		this.dateOfBirth = dateOfBirth;
		this.age = age;
		this.gender = gender;
		this.fathername = fathername;
		this.motherName = motherName;
		this.spouseName = spouseName;
		this.numberOfDependant = numberOfDependant;
		this.educationQualification = educationQualification;
		this.caste = caste;
		this.religion = religion;
		this.marritialStatus = marritialStatus;
	}

	public PersonalDetailResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "PersonalDetailResponse [firstName=" + firstName + ", middleName=" + middleName + ", lastName="
				+ lastName + ", mobile=" + mobile + ", dateOfBirth=" + dateOfBirth + ", age=" + age + ", gender="
				+ gender + ", fathername=" + fathername + ", motherName=" + motherName + ", spouseName=" + spouseName
				+ ", numberOfDependant=" + numberOfDependant + ", educationQualification=" + educationQualification
				+ ", caste=" + caste + ", religion=" + religion + "]";
	}

}
