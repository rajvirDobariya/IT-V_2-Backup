package com.suryoday.aocpv.pojo;

public class IncomeDetail {

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
	private String dob;
	private long mobile;
	private String married;
	private String aadharCard;
	private String panCard;
	private String aadharNoVerify;
	private String pancardNoVerify;
	private String mobileVerify;
	private String voterId;
	private String voterIdVerify;
	// private List<Image> image;
	private String gaurantor;
	private String form60;
	private String drivingLicense;
	private String drivingLicenseIsVerify;
	private String passport;
	private String passportIsVerify;
	private String authVerify;

	public String getAuthVerify() {
		return authVerify;
	}

	public void setAuthVerify(String authVerify) {
		this.authVerify = authVerify;
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
		this.earning = earning;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getOccCode() {
		return occCode;
	}

	public void setOccCode(String occCode) {
		this.occCode = occCode;
	}

	public String getPrimarySourceOfIncome() {
		return primarySourceOfIncome;
	}

	public void setPrimarySourceOfIncome(String primarySourceOfIncome) {
		this.primarySourceOfIncome = primarySourceOfIncome;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
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
		this.married = married;
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

	public String getAadharNoVerify() {
		return aadharNoVerify;
	}

	public void setAadharNoVerify(String aadharNoVerify) {
		this.aadharNoVerify = aadharNoVerify;
	}

	public String getPancardNoVerify() {
		return pancardNoVerify;
	}

	public void setPancardNoVerify(String pancardNoVerify) {
		this.pancardNoVerify = pancardNoVerify;
	}

	public String getMobileVerify() {
		return mobileVerify;
	}

	public void setMobileVerify(String mobileVerify) {
		this.mobileVerify = mobileVerify;
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

	@Override
	public String toString() {
		return "IncomeDetail [member=" + member + ", earning=" + earning + ", occupation=" + occupation + ", occCode="
				+ occCode + ", primarySourceOfIncome=" + primarySourceOfIncome + ", securedLoan=" + securedLoan
				+ ", unsecuredLoan=" + unsecuredLoan + ", monthlyIncome=" + monthlyIncome + ", monthlyLoanEmi="
				+ monthlyLoanEmi + ", title=" + title + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", gender=" + gender + ", age=" + age + ", dob=" + dob + ", mobile=" + mobile + ", married=" + married
				+ ", aadharCard=" + aadharCard + ", panCard=" + panCard + ", aadharNoVerify=" + aadharNoVerify
				+ ", pancardNoVerify=" + pancardNoVerify + ", mobileVerify=" + mobileVerify + ", voterId=" + voterId
				+ ", voterIdVerify=" + voterIdVerify + "]";
	}

}
