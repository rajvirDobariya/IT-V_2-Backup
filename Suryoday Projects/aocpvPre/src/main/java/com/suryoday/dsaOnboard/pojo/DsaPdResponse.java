package com.suryoday.dsaOnboard.pojo;

import java.time.LocalDateTime;

public class DsaPdResponse {
	
	private String applicationNo;
	private String member;
	private String panCardNo;
	private String name;
	private String gender;
	private String presentOccupation;
	private String alternateMobileNo;
	private String aadharNo;
	private String mobLinkwithAadhar;
	private String dateOfBirth;
	private String entityName;
	private String flowStatus;
	private LocalDateTime updatedDate;
	
	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
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
	public String getPanCardNo() {
		return panCardNo;
	}
	public void setPanCardNo(String panCardNo) {
		this.panCardNo = panCardNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPresentOccupation() {
		return presentOccupation;
	}
	public void setPresentOccupation(String presentOccupation) {
		this.presentOccupation = presentOccupation;
	}
	public String getAlternateMobileNo() {
		return alternateMobileNo;
	}
	public void setAlternateMobileNo(String alternateMobileNo) {
		this.alternateMobileNo = alternateMobileNo;
	}
	public String getAadharNo() {
		return aadharNo;
	}
	public void setAadharNo(String aadharNo) {
		this.aadharNo = aadharNo;
	}
	public String getMobLinkwithAadhar() {
		return mobLinkwithAadhar;
	}
	public void setMobLinkwithAadhar(String mobLinkwithAadhar) {
		this.mobLinkwithAadhar = mobLinkwithAadhar;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getFlowStatus() {
		return flowStatus;
	}
	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}
	public DsaPdResponse(String applicationNo, String member, String panCardNo, String name, String gender,
			String presentOccupation, String alternateMobileNo, String aadharNo, String mobLinkwithAadhar,
			String dateOfBirth, String entityName, String flowStatus, LocalDateTime updatedDate) {
		super();
		this.applicationNo = applicationNo;
		this.member = member;
		this.panCardNo = panCardNo;
		this.name = name;
		this.gender = gender;
		this.presentOccupation = presentOccupation;
		this.alternateMobileNo = alternateMobileNo;
		this.aadharNo = aadharNo;
		this.mobLinkwithAadhar = mobLinkwithAadhar;
		this.dateOfBirth = dateOfBirth;
		this.entityName = entityName;
		this.flowStatus = flowStatus;
		this.updatedDate = updatedDate;
	}
	public DsaPdResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
