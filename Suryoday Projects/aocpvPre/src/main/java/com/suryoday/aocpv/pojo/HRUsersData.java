package com.suryoday.aocpv.pojo;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tbl_hr_userData")
public class HRUsersData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Id")
	private long id;
	@Column(name="UserId")
	private String userId;
	@Column(name="UserName")
	private String userName;
	@Column(name="Password")
	private String password;
	@Column(name="UserEmail")
	private String userEmail;
	@Column(name="Designation")
	private String designation;
	@Column(name="UserRole")
	private String userRole;
	@Column(name="Gender")
	private String gender;
	@Column(name="Mobile_Number")
	private String mobileNumber;
	@Column(name="Area")
	private String area;
	@Column(name="Region")
	private String region;
	@Column(name="Location")
	private String location;
	@Column(name="State")
	private String state;
	@Column(name="City")
	private String city;
	@Column(name="BranchId")
	private String branchId;
	@Column(name="LoginFlag")
	private Integer loginFlag;
	@Column(name="LoginDate")
	private LocalDateTime loginDate;
	@Column(name="ReportingManagerId")
	private String reportingManagerId;
	@Column(name="ReportingManagerName")
	private String reportingManagerName;
	@Column(name="SkipLevelManagerId")
	private String skipLevelManagerId;
	@Column(name="SkipLevelManagerName")
	private String skipLevelManagerName;
	@Column(name="BusinessUnit")
	private String businessUnit;
	@Column(name="DateOfJoining")
	private String dateOfJoining;
	@Column(name="DateOfResignation")
	private String dateOfResignation;
	@Column(name="LastWorkingDate")
	private String lastWorkingDate;
	@Column(name="ActiveStatus")
	private String activeStatus;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getLoginFlag() {
		return loginFlag;
	}
	public void setLoginFlag(int loginFlag) {
		this.loginFlag = loginFlag;
	}
	public LocalDateTime getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(LocalDateTime loginDate) {
		this.loginDate = loginDate;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public void setLoginFlag(Integer loginFlag) {
		this.loginFlag = loginFlag;
	}
	
	public String getReportingManagerId() {
		return reportingManagerId;
	}
	public void setReportingManagerId(String reportingManagerId) {
		this.reportingManagerId = reportingManagerId;
	}
	public String getReportingManagerName() {
		return reportingManagerName;
	}
	public void setReportingManagerName(String reportingManagerName) {
		this.reportingManagerName = reportingManagerName;
	}
	public String getSkipLevelManagerId() {
		return skipLevelManagerId;
	}
	public void setSkipLevelManagerId(String skipLevelManagerId) {
		this.skipLevelManagerId = skipLevelManagerId;
	}
	public String getSkipLevelManagerName() {
		return skipLevelManagerName;
	}
	public void setSkipLevelManagerName(String skipLevelManagerName) {
		this.skipLevelManagerName = skipLevelManagerName;
	}
	
	public String getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}
	public String getDateOfJoining() {
		return dateOfJoining;
	}
	public void setDateOfJoining(String dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}
	public String getDateOfResignation() {
		return dateOfResignation;
	}
	public void setDateOfResignation(String dateOfResignation) {
		this.dateOfResignation = dateOfResignation;
	}
	public String getLastWorkingDate() {
		return lastWorkingDate;
	}
	public void setLastWorkingDate(String lastWorkingDate) {
		this.lastWorkingDate = lastWorkingDate;
	}
	public String getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}
	@Override
	public String toString() {
		return "HRUsersData [id=" + id + ", userId=" + userId + ", userName=" + userName + ", password=" + password
				+ ", userEmail=" + userEmail + ", designation=" + designation + ", userRole=" + userRole + ", gender="
				+ gender + ", mobileNumber=" + mobileNumber + ", area=" + area + ", region=" + region + ", location="
				+ location + ", state=" + state + ", city=" + city + ", branchId=" + branchId + ", loginFlag="
				+ loginFlag + ", loginDate=" + loginDate + ", reportingManagerId=" + reportingManagerId
				+ ", reportingManagerName=" + reportingManagerName + ", skipLevelManagerId=" + skipLevelManagerId
				+ ", skipLevelManagerName=" + skipLevelManagerName + ", businessUnit=" + businessUnit
				+ ", dateOfJoining=" + dateOfJoining + ", dateOfResignation=" + dateOfResignation + ", lastWorkingDate="
				+ lastWorkingDate + ", activeStatus=" + activeStatus + "]";
	}
	
	
}
