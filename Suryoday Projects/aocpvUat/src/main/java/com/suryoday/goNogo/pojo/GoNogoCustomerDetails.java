package com.suryoday.goNogo.pojo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.springframework.beans.factory.annotation.Autowired;

@Entity
public class GoNogoCustomerDetails {
	@Id
	private Long applicationNo;
	private String ekycVerify;
	@Lob
	private String ekycResponse;
	private String aadharNo;
	private String pancard;
	private String pancardVerify;
	private String pancardResponse;
	private String voterId;
	private String voterIdVerify;
	private String voterIdResponse;
	private String passport;
	private String passportVerify;
	private String passportResponse;
	private String drivingLicense;
	private String drivingLicenseVerify;
	private String drivingLicenseResponse;
	private String name;
	private String dateOfBirth;
	private String mobileNo;
	private String mobileVerify;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	private String ekycDoneBy;
	@Autowired
	private String address;
	private String status;
	private String flowStatus;
	private String branchId;

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(Long applicationNo) {
		this.applicationNo = applicationNo;
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

	public String getAadharNo() {
		return aadharNo;
	}

	public void setAadharNo(String aadharNo) {
		this.aadharNo = aadharNo;
	}

	public String getPancard() {
		return pancard;
	}

	public void setPancard(String pancard) {
		this.pancard = pancard;
	}

	public String getPancardVerify() {
		return pancardVerify;
	}

	public void setPancardVerify(String pancardVerify) {
		this.pancardVerify = pancardVerify;
	}

	public String getPancardResponse() {
		return pancardResponse;
	}

	public void setPancardResponse(String pancardResponse) {
		this.pancardResponse = pancardResponse;
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

	public String getVoterIdResponse() {
		return voterIdResponse;
	}

	public void setVoterIdResponse(String voterIdResponse) {
		this.voterIdResponse = voterIdResponse;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public String getPassportVerify() {
		return passportVerify;
	}

	public void setPassportVerify(String passportVerify) {
		this.passportVerify = passportVerify;
	}

	public String getPassportResponse() {
		return passportResponse;
	}

	public void setPassportResponse(String passportResponse) {
		this.passportResponse = passportResponse;
	}

	public String getDrivingLicense() {
		return drivingLicense;
	}

	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}

	public String getDrivingLicenseVerify() {
		return drivingLicenseVerify;
	}

	public void setDrivingLicenseVerify(String drivingLicenseVerify) {
		this.drivingLicenseVerify = drivingLicenseVerify;
	}

	public String getDrivingLicenseResponse() {
		return drivingLicenseResponse;
	}

	public void setDrivingLicenseResponse(String drivingLicenseResponse) {
		this.drivingLicenseResponse = drivingLicenseResponse;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getMobileVerify() {
		return mobileVerify;
	}

	public void setMobileVerify(String mobileVerify) {
		this.mobileVerify = mobileVerify;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getEkycDoneBy() {
		return ekycDoneBy;
	}

	public void setEkycDoneBy(String ekycDoneBy) {
		this.ekycDoneBy = ekycDoneBy;
	}

	@Override
	public String toString() {
		return "GoNogoCustomerDetails [applicationNo=" + applicationNo + ", ekycVerify=" + ekycVerify
				+ ", ekycResponse=" + ekycResponse + ", aadharNo=" + aadharNo + ", pancard=" + pancard
				+ ", pancardVerify=" + pancardVerify + ", pancardResponse=" + pancardResponse + ", voterId=" + voterId
				+ ", voterIdVerify=" + voterIdVerify + ", voterIdResponse=" + voterIdResponse + ", passport=" + passport
				+ ", passportVerify=" + passportVerify + ", passportResponse=" + passportResponse + ", drivingLicense="
				+ drivingLicense + ", drivingLicenseVerify=" + drivingLicenseVerify + ", drivingLicenseResponse="
				+ drivingLicenseResponse + ", name=" + name + ", dateOfBirth=" + dateOfBirth + ", mobileNo=" + mobileNo
				+ ", mobileVerify=" + mobileVerify + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate
				+ ", ekycDoneBy=" + ekycDoneBy + ", address=" + address + ", status=" + status + ", flowStatus="
				+ flowStatus + ", branchId=" + branchId + "]";
	}

}
