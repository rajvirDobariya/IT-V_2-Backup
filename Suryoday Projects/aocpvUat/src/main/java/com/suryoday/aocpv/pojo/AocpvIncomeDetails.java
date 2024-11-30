package com.suryoday.aocpv.pojo;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class AocpvIncomeDetails {

	// @GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private int id;
	private long applicationNo;
	private String member;
	private String earning;
	private String occupation;
	private String occCode;
	private String primarySourceOfIncome;
	private double securedLoan;
	private double unsecuredLoan;
	private double monthlyIncome;
	private double monthlyLoanEmi;
	private String title;
	private String firstName;
	private String lastName;
	private String gender;
	private int age;
	private LocalDate dob;
	private long mobile;
	private String mobileVerify;
	private String married;
	private String aadharCard;
	private String aadharNoVerify;
	private String panCard;
	private String pancardNoVerify;
	private String voterId;
	private String voterIdVerify;
	@Column(nullable = true)
	private int versionCode;
	private String gaurantor;
	private String form60;
	private String drivingLicense;
	private String drivingLicenseIsVerify;
	private String passport;
	private String passportIsVerify;
	@Lob
	private String panCardResponse;
	@Lob
	private String voterIdResponse;
	@Lob
	private String drivingLicenseResponse;
	@Lob
	private String passportResponse;
	private String isActive;
	private String nameOnnameOnPancard;
	private String dobONPancard;
	private String authVerify;

	public String getAuthVerify() {
		return authVerify;
	}

	public void setAuthVerify(String authVerify) {
		this.authVerify = authVerify;
	}

	public String getNameOnnameOnPancard() {
		return nameOnnameOnPancard;
	}

	public void setNameOnnameOnPancard(String nameOnnameOnPancard) {
		this.nameOnnameOnPancard = nameOnnameOnPancard;
	}

	public String getDobONPancard() {
		return dobONPancard;
	}

	public void setDobONPancard(String dobONPancard) {
		this.dobONPancard = dobONPancard;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getDrivingLicenseResponse() {
		return drivingLicenseResponse;
	}

	public void setDrivingLicenseResponse(String drivingLicenseResponse) {
		this.drivingLicenseResponse = drivingLicenseResponse;
	}

	public String getPassportResponse() {
		return passportResponse;
	}

	public void setPassportResponse(String passportResponse) {
		this.passportResponse = passportResponse;
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

	public String getForm60() {
		return form60;
	}

	public void setForm60(String form60) {
		this.form60 = form60;
	}

	public String getDrivingLicense() {
		return drivingLicense;
	}

	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}

	public String getDrivingLicenseIsVerify() {
		return drivingLicenseIsVerify;
	}

	public void setDrivingLicenseIsVerify(String drivingLicenseIsVerify) {
		this.drivingLicenseIsVerify = drivingLicenseIsVerify;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public String getPassportIsVerify() {
		return passportIsVerify;
	}

	public void setPassportIsVerify(String passportIsVerify) {
		this.passportIsVerify = passportIsVerify;
	}

	public String getGaurantor() {
		return gaurantor;
	}

	public void setGaurantor(String gaurantor) {
		this.gaurantor = gaurantor;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
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

	public String getAadharCard() {
		return aadharCard;
	}

	public void setAadharCard(String aadharCard) {
		this.aadharCard = aadharCard;
	}

	public String getPanCard() {
		return panCard;
	}

	public void setPanCard(String panCard) {
		this.panCard = panCard;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
	}

	public String getEarning() {
		return earning;
	}

	public void setEarning(String earning) {
		this.earning = earning.toUpperCase();
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation.toUpperCase();
	}

	public String getOccCode() {
		return occCode;
	}

	public void setOccCode(String occCode) {
		this.occCode = occCode.toUpperCase();
	}

	public String getPrimarySourceOfIncome() {
		return primarySourceOfIncome;
	}

	public void setPrimarySourceOfIncome(String primarySourceOfIncome) {
		this.primarySourceOfIncome = primarySourceOfIncome.toUpperCase();
	}

	public double getSecuredLoan() {
		return securedLoan;
	}

	public void setSecuredLoan(double securedLoan) {
		this.securedLoan = securedLoan;
	}

	public double getUnsecuredLoan() {
		return unsecuredLoan;
	}

	public void setUnsecuredLoan(double unsecuredLoan) {
		this.unsecuredLoan = unsecuredLoan;
	}

	public double getMonthlyIncome() {
		return monthlyIncome;
	}

	public void setMonthlyIncome(double monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}

	public double getMonthlyLoanEmi() {
		return monthlyLoanEmi;
	}

	public void setMonthlyLoanEmi(double monthlyLoanEmi) {
		this.monthlyLoanEmi = monthlyLoanEmi;
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

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName.toUpperCase();
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender.toUpperCase();
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public long getMobile() {
		return mobile;
	}

	public void setMobile(long mobile) {
		this.mobile = mobile;
	}

	public String getMarried() {
		return married;
	}

	public void setMarried(String married) {
		this.married = married.toUpperCase();
	}

	public long getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(long applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getMobileVerify() {
		return mobileVerify;
	}

	public void setMobileVerify(String mobileVerify) {
		this.mobileVerify = mobileVerify.toUpperCase();
	}

	public String getAadharNoVerify() {
		return aadharNoVerify;
	}

	public void setAadharNoVerify(String aadharNoVerify) {
		this.aadharNoVerify = aadharNoVerify.toUpperCase();
	}

	public String getPancardNoVerify() {
		return pancardNoVerify;
	}

	public void setPancardNoVerify(String pancardNoVerify) {
		this.pancardNoVerify = pancardNoVerify.toUpperCase();
	}

	@Override
	public String toString() {
		return "AocpvIncomeDetails [id=" + id + ", applicationNo=" + applicationNo + ", member=" + member + ", earning="
				+ earning + ", occupation=" + occupation + ", occCode=" + occCode + ", primarySourceOfIncome="
				+ primarySourceOfIncome + ", securedLoan=" + securedLoan + ", unsecuredLoan=" + unsecuredLoan
				+ ", monthlyIncome=" + monthlyIncome + ", monthlyLoanEmi=" + monthlyLoanEmi + ", title=" + title
				+ ", firstName=" + firstName + ", lastName=" + lastName + ", gender=" + gender + ", age=" + age
				+ ", dob=" + dob + ", mobile=" + mobile + ", mobileVerify=" + mobileVerify + ", married=" + married
				+ ", aadharCard=" + aadharCard + ", aadharNoVerify=" + aadharNoVerify + ", panCard=" + panCard
				+ ", pancardNoVerify=" + pancardNoVerify + ", voterId=" + voterId + ", voterIdVerify=" + voterIdVerify
				+ "]";
	}

}
