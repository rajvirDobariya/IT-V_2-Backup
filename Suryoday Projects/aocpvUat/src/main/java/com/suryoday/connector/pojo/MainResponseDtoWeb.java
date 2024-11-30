package com.suryoday.connector.pojo;

import java.util.List;

public class MainResponseDtoWeb {

	private String userId;

	private String userName;

	private Long branchId;

	private boolean isAdmin;

	private Long isActive;

	private String mobileNo;

	private Long empId;

	private String userRoleName;

	private List<PermissionDto> permissions;

	private String SERVER_TOKEN;

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

	private String serverKey;

	private List<BranchInfo> listofBranches;

	private List<String> userAccess;

	private String productGroup;

	private String department;

	private String emailId;

	private String level;

	private String allowCreditAccess;

	public String getAllowCreditAccess() {
		return allowCreditAccess;
	}

	public void setAllowCreditAccess(String allowCreditAccess) {
		this.allowCreditAccess = allowCreditAccess;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
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

	public String getProductGroup() {
		return productGroup;
	}

	public void setProductGroup(String productGroup) {
		this.productGroup = productGroup;
	}

	public List<String> getUserAccess() {
		return userAccess;
	}

	public void setUserAccess(List<String> userAccess) {
		this.userAccess = userAccess;
	}

	public List<BranchInfo> getListofBranches() {
		return listofBranches;
	}

	public void setListofBranches(List<BranchInfo> listofBranches) {
		this.listofBranches = listofBranches;
	}

	public String getServerKey() {
		return serverKey;
	}

	public void setServerKey(String serverKey) {
		this.serverKey = serverKey;
	}

	public String getSERVER_TOKEN() {
		return SERVER_TOKEN;
	}

	public void setSERVER_TOKEN(String sERVER_TOKEN) {
		SERVER_TOKEN = sERVER_TOKEN;
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

	public String getUserRoleName() {
		return userRoleName;
	}

	public void setUserRoleName(String userRoleName) {
		this.userRoleName = userRoleName;
	}

	public List<PermissionDto> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<PermissionDto> permissions) {
		this.permissions = permissions;
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
		return "MainResponseDtoWeb [userId=" + userId + ", userName=" + userName + ", branchId=" + branchId
				+ ", isAdmin=" + isAdmin + ", isActive=" + isActive + ", mobileNo=" + mobileNo + ", empId=" + empId
				+ ", userRoleName=" + userRoleName + ", permissions=" + permissions + ", SERVER_TOKEN=" + SERVER_TOKEN
				+ ", branchName=" + branchName + ", designation=" + designation + ", city=" + city + ", state=" + state
				+ ", area=" + area + ", lastAccessTime=" + lastAccessTime + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + ", updatedBy=" + updatedBy
				+ ", serverKey=" + serverKey + "]";
	}

}
