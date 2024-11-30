package com.suryoday.customerOnboard.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;


@Entity
public class CustomerOnboardDetails {

	@Id
	private Long applicationNo;
	private String mobileNo;
	private String mobileNoVerify;
	private String aadharNo;
	private String aadharNoVerify;
	private String pancardNo;
	private String pancardNoVerify;
	private String dateOfBirth;
	private String name;
	private String status;
	private String flowStatus;
	private LocalDateTime createdTime;
	private LocalDateTime updatedTime;
	private String createdBy;
	private String nameOnPanCard;
	private String dobOnPanCard;
	@Lob
	private String pancardResponse;
	private String ekycDoneBy;
	private String ekycVerify;
	@Lob
	private String ekycResponse;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "applicatioNo")
	private List<CustomerOnboardAddress> addresses;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "applicatioNo")
	private List<CustomerOnboardImage> images;
	
	public List<CustomerOnboardAddress> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<CustomerOnboardAddress> addresses) {
		this.addresses = addresses;
	}
	public List<CustomerOnboardImage> getImages() {
		return images;
	}
	public void setImages(List<CustomerOnboardImage> images) {
		this.images = images;
	}
	public String getEkycDoneBy() {
		return ekycDoneBy;
	}
	public void setEkycDoneBy(String ekycDoneBy) {
		this.ekycDoneBy = ekycDoneBy;
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
	public String getNameOnPanCard() {
		return nameOnPanCard;
	}
	public void setNameOnPanCard(String nameOnPanCard) {
		this.nameOnPanCard = nameOnPanCard;
	}
	public String getDobOnPanCard() {
		return dobOnPanCard;
	}
	public void setDobOnPanCard(String dobOnPanCard) {
		this.dobOnPanCard = dobOnPanCard;
	}
	public String getPancardResponse() {
		return pancardResponse;
	}
	public void setPancardResponse(String pancardResponse) {
		this.pancardResponse = pancardResponse;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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
	
	public LocalDateTime getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}
	public LocalDateTime getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(LocalDateTime updatedTime) {
		this.updatedTime = updatedTime;
	}
	public Long getApplicationNo() {
		return applicationNo;
	}
	public void setApplicationNo(Long applicationNo) {
		this.applicationNo = applicationNo;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getMobileNoVerify() {
		return mobileNoVerify;
	}
	public void setMobileNoVerify(String mobileNoVerify) {
		this.mobileNoVerify = mobileNoVerify;
	}
	public String getAadharNo() {
		return aadharNo;
	}
	public void setAadharNo(String aadharNo) {
		this.aadharNo = aadharNo;
	}
	public String getAadharNoVerify() {
		return aadharNoVerify;
	}
	public void setAadharNoVerify(String aadharNoVerify) {
		this.aadharNoVerify = aadharNoVerify;
	}
	public String getPancardNo() {
		return pancardNo;
	}
	public void setPancardNo(String pancardNo) {
		this.pancardNo = pancardNo;
	}
	public String getPancardNoVerify() {
		return pancardNoVerify;
	}
	public void setPancardNoVerify(String pancardNoVerify) {
		this.pancardNoVerify = pancardNoVerify;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "CustomerOnboardDetails [applicationNo=" + applicationNo + ", mobileNo=" + mobileNo + ", mobileNoVerify="
				+ mobileNoVerify + ", aadharNo=" + aadharNo + ", aadharNoVerify=" + aadharNoVerify + ", pancardNo="
				+ pancardNo + ", pancardNoVerify=" + pancardNoVerify + ", dateOfBirth=" + dateOfBirth + ", name=" + name
				+ ", status=" + status + ", flowStatus=" + flowStatus + ", createdTime=" + createdTime
				+ ", updatedTime=" + updatedTime + ", createdBy=" + createdBy + ", nameOnPanCard=" + nameOnPanCard
				+ ", dobOnPanCard=" + dobOnPanCard + ", pancardResponse=" + pancardResponse + ", ekycDoneBy="
				+ ekycDoneBy + ", ekycVerify=" + ekycVerify + ", ekycResponse=" + ekycResponse + ", addresses="
				+ addresses + ", images=" + images + "]";
	}
	
	
	
	
	
	
	
	
}
