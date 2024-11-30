package com.suryoday.dsaOnboard.pojo;

public class MortgageDetailsResp {
	private String empId;
	private String empName;
	private String purposeToVisit;
	private String visitName;
	private String dateOfActivity;
	private String contactNo;
	private String relationshipType;
	private String remarks;
	private String geoLatLong;

	public MortgageDetailsResp() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MortgageDetailsResp(String empId, String empName, String purposeToVisit, String visitName,
			String dateOfActivity, String contactNo, String relationshipType, String remarks, String geoLatLong) {
		super();
		this.empId = empId;
		this.empName = empName;
		this.purposeToVisit = purposeToVisit;
		this.visitName = visitName;
		this.dateOfActivity = dateOfActivity;
		this.contactNo = contactNo;
		this.relationshipType = relationshipType;
		this.remarks = remarks;
		this.geoLatLong = geoLatLong;
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

	@Override
	public String toString() {
		return "MortgageDetailsResp [empId=" + empId + ", empName=" + empName + ", purposeToVisit=" + purposeToVisit
				+ ", visitName=" + visitName + ", dateOfActivity=" + dateOfActivity + ", contactNo=" + contactNo
				+ ", relationshipType=" + relationshipType + ", remarks=" + remarks + ", geoLatLong=" + geoLatLong
				+ "]";
	}

}
