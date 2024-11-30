package com.suryoday.mhl.pojo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "TBL_ADDRESS_DETAILS")
public class AddressDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String applicationNo;
	private String addressType;
	private String applicationRole;
	private String memberId;
	private String addressLine1;
	private String addressLine2;
	private String pincode;
	private String city;
	private String dist;
	private String state;
	private String landmark;
	private String distanceFromBranch;
	private LocalDateTime creationDate;
	private LocalDateTime updatedDate;
	private String owenershipStatus;
	private String stabiltyYear;

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

	public String getApplicationRole() {
		return applicationRole;
	}

	public void setApplicationRole(String applicationRole) {
		this.applicationRole = applicationRole.toUpperCase();
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId.toUpperCase();
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1.toUpperCase();
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2.toUpperCase();
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city.toUpperCase();
	}

	public String getDist() {
		return dist;
	}

	public void setDist(String dist) {
		this.dist = dist.toUpperCase();
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state.toUpperCase();
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark.toUpperCase();
	}

	public String getDistanceFromBranch() {
		return distanceFromBranch;
	}

	public void setDistanceFromBranch(String distanceFromBranch) {
		this.distanceFromBranch = distanceFromBranch.toUpperCase();
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getOwenershipStatus() {
		return owenershipStatus;
	}

	public void setOwenershipStatus(String owenershipStatus) {
		this.owenershipStatus = owenershipStatus.toUpperCase();
	}

	public String getStabiltyYear() {
		return stabiltyYear;
	}

	public void setStabiltyYear(String stabiltyYear) {
		this.stabiltyYear = stabiltyYear.toUpperCase();
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType.toUpperCase();
	}

	@Override
	public String toString() {
		return "AddressDetails [id=" + id + ", applicationNo=" + applicationNo + ", addressType=" + addressType
				+ ", applicationRole=" + applicationRole + ", memberId=" + memberId + ", addressLine1=" + addressLine1
				+ ", addressLine2=" + addressLine2 + ", pincode=" + pincode + ", city=" + city + ", dist=" + dist
				+ ", state=" + state + ", landmark=" + landmark + ", distanceFromBranch=" + distanceFromBranch
				+ ", creationDate=" + creationDate + ", updatedDate=" + updatedDate + ", owenershipStatus="
				+ owenershipStatus + ", stabiltyYear=" + stabiltyYear + "]";
	}

}
