package com.suryoday.mhl.pojo;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_INCOME_DETAILS")
public class MHLIncomeDetails {
	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private int id;
	private String applicationNo;
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
	private String mobile;
	private String mobileVerify;
	private String married;
	private String aadharCard;
	private String aadharNoVerify;
	private String panCard;
	private String pancardNoVerify;
	private String voterId;
	private String voterIdVerify;
	private String applicantType;
	private String relation;
	private String modeOfIncome;
	private String modeOfSalary;
	private String incomeProof;
	private String employmentProof;
	private String frequencyOfSalary;
	private String natureOfBussiness;
	private String typeOfBussiness;
	private double totalIncomeBussiness;
	private double annualIncome;
	private String bussinessAddress;
	private String bussinessPremisesOwn;
	private String nameOfOrganization;
	private String totalExperience;
	private String bussinesRole;
	private String intersetedInLifeInsurance;
	private String intersetedInPropertyInsurance;
	private String nomineeInsurance;
	private String nomineeName;
	private String relationWithInsured;
	private String insuranceNo;
	private String insuranceType;
	private double insuranceAmount;
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
	public LocalDate getDob() {
		return dob;
	}
	public void setDob(LocalDate dob) {
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
	public String getApplicantType() {
		return applicantType;
	}
	public void setApplicantType(String applicantType) {
		this.applicantType = applicantType;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public String getModeOfIncome() {
		return modeOfIncome;
	}
	public void setModeOfIncome(String modeOfIncome) {
		this.modeOfIncome = modeOfIncome;
	}
	public String getModeOfSalary() {
		return modeOfSalary;
	}
	public void setModeOfSalary(String modeOfSalary) {
		this.modeOfSalary = modeOfSalary;
	}
	public String getIncomeProof() {
		return incomeProof;
	}
	public void setIncomeProof(String incomeProof) {
		this.incomeProof = incomeProof;
	}
	public String getEmploymentProof() {
		return employmentProof;
	}
	public void setEmploymentProof(String employmentProof) {
		this.employmentProof = employmentProof;
	}
	public String getFrequencyOfSalary() {
		return frequencyOfSalary;
	}
	public void setFrequencyOfSalary(String frequencyOfSalary) {
		this.frequencyOfSalary = frequencyOfSalary;
	}
	public String getNatureOfBussiness() {
		return natureOfBussiness;
	}
	public void setNatureOfBussiness(String natureOfBussiness) {
		this.natureOfBussiness = natureOfBussiness;
	}
	public String getTypeOfBussiness() {
		return typeOfBussiness;
	}
	public void setTypeOfBussiness(String typeOfBussiness) {
		this.typeOfBussiness = typeOfBussiness;
	}
	public double getTotalIncomeBussiness() {
		return totalIncomeBussiness;
	}
	public void setTotalIncomeBussiness(double totalIncomeBussiness) {
		this.totalIncomeBussiness = totalIncomeBussiness;
	}
	public double getAnnualIncome() {
		return annualIncome;
	}
	public void setAnnualIncome(double annualIncome) {
		this.annualIncome = annualIncome;
	}
	public String getBussinessAddress() {
		return bussinessAddress;
	}
	public void setBussinessAddress(String bussinessAddress) {
		this.bussinessAddress = bussinessAddress;
	}
	public String getBussinessPremisesOwn() {
		return bussinessPremisesOwn;
	}
	public void setBussinessPremisesOwn(String bussinessPremisesOwn) {
		this.bussinessPremisesOwn = bussinessPremisesOwn;
	}
	public String getNameOfOrganization() {
		return nameOfOrganization;
	}
	public void setNameOfOrganization(String nameOfOrganization) {
		this.nameOfOrganization = nameOfOrganization;
	}
	public String getTotalExperience() {
		return totalExperience;
	}
	public void setTotalExperience(String totalExperience) {
		this.totalExperience = totalExperience;
	}
	public String getBussinesRole() {
		return bussinesRole;
	}
	public void setBussinesRole(String bussinesRole) {
		this.bussinesRole = bussinesRole;
	}
	public String getIntersetedInLifeInsurance() {
		return intersetedInLifeInsurance;
	}
	public void setIntersetedInLifeInsurance(String intersetedInLifeInsurance) {
		this.intersetedInLifeInsurance = intersetedInLifeInsurance;
	}
	public String getIntersetedInPropertyInsurance() {
		return intersetedInPropertyInsurance;
	}
	public void setIntersetedInPropertyInsurance(String intersetedInPropertyInsurance) {
		this.intersetedInPropertyInsurance = intersetedInPropertyInsurance;
	}
	public String getNomineeInsurance() {
		return nomineeInsurance;
	}
	public void setNomineeInsurance(String nomineeInsurance) {
		this.nomineeInsurance = nomineeInsurance;
	}
	public String getNomineeName() {
		return nomineeName;
	}
	public void setNomineeName(String nomineeName) {
		this.nomineeName = nomineeName;
	}
	public String getRelationWithInsured() {
		return relationWithInsured;
	}
	public void setRelationWithInsured(String relationWithInsured) {
		this.relationWithInsured = relationWithInsured;
	}
	public String getInsuranceNo() {
		return insuranceNo;
	}
	public void setInsuranceNo(String insuranceNo) {
		this.insuranceNo = insuranceNo;
	}
	public String getInsuranceType() {
		return insuranceType;
	}
	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}
	public double getInsuranceAmount() {
		return insuranceAmount;
	}
	public void setInsuranceAmount(double insuranceAmount) {
		this.insuranceAmount = insuranceAmount;
	}
	
	@Override
	public String toString() {
		return "MHLIncomeDetails [id=" + id + ", applicationNo=" + applicationNo + ", member=" + member + ", earning="
				+ earning + ", occupation=" + occupation + ", occCode=" + occCode + ", primarySourceOfIncome="
				+ primarySourceOfIncome + ", securedLoan=" + securedLoan + ", unsecuredLoan=" + unsecuredLoan
				+ ", monthlyIncome=" + monthlyIncome + ", monthlyLoanEmi=" + monthlyLoanEmi + ", title=" + title
				+ ", firstName=" + firstName + ", lastName=" + lastName + ", gender=" + gender + ", age=" + age
				+ ", dob=" + dob + ", mobile=" + mobile + ", mobileVerify=" + mobileVerify + ", married=" + married
				+ ", aadharCard=" + aadharCard + ", aadharNoVerify=" + aadharNoVerify + ", panCard=" + panCard
				+ ", pancardNoVerify=" + pancardNoVerify + ", voterId=" + voterId + ", voterIdVerify=" + voterIdVerify
				+ ", applicantType=" + applicantType + ", relation=" + relation + ", modeOfIncome=" + modeOfIncome
				+ ", modeOfSalary=" + modeOfSalary + ", incomeProof=" + incomeProof + ", employmentProof="
				+ employmentProof + ", frequencyOfSalary=" + frequencyOfSalary + ", natureOfBussiness="
				+ natureOfBussiness + ", typeOfBussiness=" + typeOfBussiness + ", totalIncomeBussiness="
				+ totalIncomeBussiness + ", annualIncome=" + annualIncome + ", bussinessAddress=" + bussinessAddress
				+ ", bussinessPremisesOwn=" + bussinessPremisesOwn + ", nameOfOrganization=" + nameOfOrganization
				+ ", totalExperience=" + totalExperience + ", bussinesRole=" + bussinesRole
				+ ", intersetedInLifeInsurance=" + intersetedInLifeInsurance + ", intersetedInPropertyInsurance="
				+ intersetedInPropertyInsurance + ", nomineeInsurance=" + nomineeInsurance + ", nomineeName="
				+ nomineeName + ", relationWithInsured=" + relationWithInsured + ", insuranceNo=" + insuranceNo
				+ ", insuranceType=" + insuranceType + ", insuranceAmount=" + insuranceAmount + ", getId()=" + getId()
				+ ", getApplicationNo()=" + getApplicationNo() + ", getMember()=" + getMember() + ", getEarning()="
				+ getEarning() + ", getOccupation()=" + getOccupation() + ", getOccCode()=" + getOccCode()
				+ ", getPrimarySourceOfIncome()=" + getPrimarySourceOfIncome() + ", getSecuredLoan()="
				+ getSecuredLoan() + ", getUnsecuredLoan()=" + getUnsecuredLoan() + ", getMonthlyIncome()="
				+ getMonthlyIncome() + ", getMonthlyLoanEmi()=" + getMonthlyLoanEmi() + ", getTitle()=" + getTitle()
				+ ", getFirstName()=" + getFirstName() + ", getLastName()=" + getLastName() + ", getGender()="
				+ getGender() + ", getAge()=" + getAge() + ", getDob()=" + getDob() + ", getMobile()=" + getMobile()
				+ ", getMobileVerify()=" + getMobileVerify() + ", getMarried()=" + getMarried() + ", getAadharCard()="
				+ getAadharCard() + ", getAadharNoVerify()=" + getAadharNoVerify() + ", getPanCard()=" + getPanCard()
				+ ", getPancardNoVerify()=" + getPancardNoVerify() + ", getVoterId()=" + getVoterId()
				+ ", getVoterIdVerify()=" + getVoterIdVerify() + ", getApplicantType()=" + getApplicantType()
				+ ", getRelation()=" + getRelation() + ", getModeOfIncome()=" + getModeOfIncome()
				+ ", getModeOfSalary()=" + getModeOfSalary() + ", getIncomeProof()=" + getIncomeProof()
				+ ", getEmploymentProof()=" + getEmploymentProof() + ", getFrequencyOfSalary()="
				+ getFrequencyOfSalary() + ", getNatureOfBussiness()=" + getNatureOfBussiness()
				+ ", getTypeOfBussiness()=" + getTypeOfBussiness() + ", getTotalIncomeBussiness()="
				+ getTotalIncomeBussiness() + ", getAnnualIncome()=" + getAnnualIncome() + ", getBussinessAddress()="
				+ getBussinessAddress() + ", getBussinessPremisesOwn()=" + getBussinessPremisesOwn()
				+ ", getNameOfOrganization()=" + getNameOfOrganization() + ", getTotalExperience()="
				+ getTotalExperience() + ", getBussinesRole()=" + getBussinesRole()
				+ ", getIntersetedInLifeInsurance()=" + getIntersetedInLifeInsurance()
				+ ", getIntersetedInPropertyInsurance()=" + getIntersetedInPropertyInsurance()
				+ ", getNomineeInsurance()=" + getNomineeInsurance() + ", getNomineeName()=" + getNomineeName()
				+ ", getRelationWithInsured()=" + getRelationWithInsured() + ", getInsuranceNo()=" + getInsuranceNo()
				+ ", getInsuranceType()=" + getInsuranceType() + ", getInsuranceAmount()=" + getInsuranceAmount()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}
			
}
