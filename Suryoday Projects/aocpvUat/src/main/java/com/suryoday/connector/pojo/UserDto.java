package com.suryoday.connector.pojo;

import java.util.List;

public class UserDto {
	private String userName;

	private Long branchId;

	private boolean isAdmin;

	private Long isActive;

	private String mobileNo;

	private Long empId;

	private String userRoleName;

	private String permission;

	private String branchName;

	private String designation;

	private String city;

	private String state;

	private String area;

	private String lastAccessTime;

	private String createdBy;

	private String createdDate;

	private String updatedDate;

	private String updatedBy;

	private String branchIdArray;

	private List<String> userAccess;

	private String department;

	private String emailId;

	private String otherRole;

	private String allowCreditAccess;

	public String getAllowCreditAccess() {
		return allowCreditAccess;
	}

	public void setAllowCreditAccess(String allowCreditAccess) {
		this.allowCreditAccess = allowCreditAccess;
	}

	public String getOtherRole() {
		return otherRole;
	}

	public void setOtherRole(String otherRole) {
		this.otherRole = otherRole;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public List<String> getUserAccess() {
		return userAccess;
	}

	public void setUserAccess(List<String> userAccess) {
		this.userAccess = userAccess;
	}

	public String getBranchIdArray() {
		return branchIdArray;
	}

	public void setBranchIdArray(String branchIdArray) {
		this.branchIdArray = branchIdArray;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserRoleName() {
		return userRoleName;
	}

	public void setUserRoleName(String userRoleName) {
		this.userRoleName = userRoleName;
	}

	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Long getIsActive() {
		return isActive;
	}

	public void setIsActive(Long isActive) {
		this.isActive = isActive;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(String lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Override
	public String toString() {
		return "UserDto [userName=" + userName + ", branchId=" + branchId + ", isAdmin=" + isAdmin + ", isActive="
				+ isActive + ", mobileNo=" + mobileNo + ", empId=" + empId + ", userRoleName=" + userRoleName
				+ ", permission=" + permission + ", branchName=" + branchName + ", designation=" + designation
				+ ", city=" + city + ", state=" + state + ", area=" + area + ", lastAccessTime=" + lastAccessTime
				+ ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate
				+ ", updatedBy=" + updatedBy + "]";
	}

}
