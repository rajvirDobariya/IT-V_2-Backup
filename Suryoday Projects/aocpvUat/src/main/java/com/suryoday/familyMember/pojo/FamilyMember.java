package com.suryoday.familyMember.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "family_member")
public class FamilyMember {
	@Id
	private int id;
	private String customerId;
	private String member;
	private String earning;
	private String occupation;
	private String occCode;
	private String primarySourceOfIncome;
	private String monthlyIncome;
	private String monthlyLoanEmi;
	private String title;
	private String firstName;
	private String lastName;
	private String gender;
	private String age;
	private String dob;
	private String mobile;
	private String mobileVerify;
	private String married;
	private String aadharCard;
	private String aadharNoVerify;
	private String panCard;
	private String pancardNoVerify;
	private String voterId;
	private String voterIdVerify;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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

	public String getMonthlyIncome() {
		return monthlyIncome;
	}

	public void setMonthlyIncome(String monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}

	public String getMonthlyLoanEmi() {
		return monthlyLoanEmi;
	}

	public void setMonthlyLoanEmi(String monthlyLoanEmi) {
		this.monthlyLoanEmi = monthlyLoanEmi;
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

	@Override
	public String toString() {
		return "FamilyMember [id=" + id + ", customerId=" + customerId + ", member=" + member + ", earning=" + earning
				+ ", occupation=" + occupation + ", occCode=" + occCode + ", primarySourceOfIncome="
				+ primarySourceOfIncome + ", monthlyIncome=" + monthlyIncome + ", monthlyLoanEmi=" + monthlyLoanEmi
				+ ", title=" + title + ", firstName=" + firstName + ", lastName=" + lastName + ", gender=" + gender
				+ ", age=" + age + ", dob=" + dob + ", mobile=" + mobile + ", mobileVerify=" + mobileVerify
				+ ", married=" + married + ", aadharCard=" + aadharCard + ", aadharNoVerify=" + aadharNoVerify
				+ ", panCard=" + panCard + ", pancardNoVerify=" + pancardNoVerify + ", voterId=" + voterId
				+ ", voterIdVerify=" + voterIdVerify + "]";
	}

}
