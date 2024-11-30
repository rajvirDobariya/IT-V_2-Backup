package com.suryoday.dsaOnboard.pojo;

import java.time.LocalDateTime;
import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tbl_mortgage_details")
public class MortgageDetails {
	@Id
	private long applicationId;
	private String empId;
	private String empName;
	private String purposeToVisit;
	private String toVisit;
	private String visitName;
	private String dateOfActivity;
	private String contactNo;
	private String relationshipType;
	private String remarks;
	@Lob
	@JsonIgnore
	private byte[] image;
	private String geoLatLong;
	private LocalDateTime createdDate = LocalDateTime.now();

	/**
	 * @return the applicationId
	 */
	public long getApplicationId() {
		return applicationId;
	}

	/**
	 * @param applicationId the applicationId to set
	 */
	public void setApplicationId(long applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * @return the empId
	 */
	public String getEmpId() {
		return empId;
	}

	/**
	 * @param empId the empId to set
	 */
	public void setEmpId(String empId) {
		this.empId = empId;
	}

	/**
	 * @return the empName
	 */
	public String getEmpName() {
		return empName;
	}

	/**
	 * @param empName the empName to set
	 */
	public void setEmpName(String empName) {
		this.empName = empName;
	}

	/**
	 * @return the purposeToVisit
	 */
	public String getPurposeToVisit() {
		return purposeToVisit;
	}

	/**
	 * @param purposeToVisit the purposeToVisit to set
	 */
	public void setPurposeToVisit(String purposeToVisit) {
		this.purposeToVisit = purposeToVisit;
	}

	/**
	 * @return the visitName
	 */
	public String getVisitName() {
		return visitName;
	}

	/**
	 * @param visitName the visitName to set
	 */
	public void setVisitName(String visitName) {
		this.visitName = visitName;
	}

	/**
	 * @return the dateOfActivity
	 */
	public String getDateOfActivity() {
		return dateOfActivity;
	}

	/**
	 * @param dateOfActivity the dateOfActivity to set
	 */
	public void setDateOfActivity(String dateOfActivity) {
		this.dateOfActivity = dateOfActivity;
	}

	/**
	 * @return the contactNo
	 */
	public String getContactNo() {
		return contactNo;
	}

	/**
	 * @param contactNo the contactNo to set
	 */
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	/**
	 * @return the relationshipType
	 */
	public String getRelationshipType() {
		return relationshipType;
	}

	/**
	 * @param relationshipType the relationshipType to set
	 */
	public void setRelationshipType(String relationshipType) {
		this.relationshipType = relationshipType;
	}

	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * @return the image
	 */
	public byte[] getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(byte[] image) {
		this.image = image;
	}

	/**
	 * @return the createdDate
	 */
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the geoLatLong
	 */
	public String getGeoLatLong() {
		return geoLatLong;
	}

	/**
	 * @param geoLatLong the geoLatLong to set
	 */
	public void setGeoLatLong(String geoLatLong) {
		this.geoLatLong = geoLatLong;
	}

	/**
	 * @return the toVisit
	 */
	public String getToVisit() {
		return toVisit;
	}

	/**
	 * @param toVisit the toVisit to set
	 */
	public void setToVisit(String toVisit) {
		this.toVisit = toVisit;
	}

	@Override
	public String toString() {
		return "MortgageDetails [applicationId=" + applicationId + ", empId=" + empId + ", empName=" + empName
				+ ", purposeToVisit=" + purposeToVisit + ", toVisit=" + toVisit + ", visitName=" + visitName
				+ ", dateOfActivity=" + dateOfActivity + ", contactNo=" + contactNo + ", relationshipType="
				+ relationshipType + ", remarks=" + remarks + ", image=" + Arrays.toString(image) + ", geoLatLong="
				+ geoLatLong + ", createdDate=" + createdDate + "]";
	}

}
