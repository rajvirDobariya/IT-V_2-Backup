package com.suryoday.uam.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_user")
public class User implements Serializable {

	private static final long serialVersionUID = 4137993558605652473L;

	@Id

	@Column(name = "userId")
	private String userId;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "branch_id")
	private Long branchId;

	@Column(name = "is_admin")
	private boolean isAdmin;

	@Column(name = "is_active")
	private Long isActive;

	@Column(name = "empId")
	private Long empId;

	@Column(name = "mobileNo")
	private String mobileNo;

	@ManyToOne
	@JoinColumn(name = "user_permission_id")
	private UserRole userRole;

	@Column(name = "permission")
	private String permission;

	@Column(name = "branch_name")
	private String branchName;

	@Column(name = "designation")
	private String designation;

	@Column(name = "city")
	private String city;

	@Column(name = "state")
	private String state;

	@Column(name = "area")
	private String area;

	@Column(name = "last_access_time")
	private String lastAccessTime;

	@Lob
	@Column(name = "geo_location")
	private String geoLocation;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_date")
	private String createdDate;

	@Column(name = "updated_date")
	private String updatedDate;

	@Column(name = "status")
	private String status;

	@Column(name = "approved_by")
	private String approvedBy;

	@Column(name = "approved_date")
	private LocalDateTime approvedDate;

	private String updatedBy;

	@Lob
	private String branchIdArray;
	
	private String userAccess;
	private String department;
	private String emailId;
	private String level;
	private Integer smscallId;
	private LocalDateTime lockingStartTime;
	private LocalDateTime lockingEndTime;
	private Integer sendSmscallId;
	private LocalDateTime lockingEndTimeSendSms;
	private String lastBrowserName;
	@Lob
	private String otherRole;
	
	
	public String getOtherRole() {
		return otherRole;
	}

	public void setOtherRole(String otherRole) {
		this.otherRole = otherRole;
	}

	public String getLastBrowserName() {
		return lastBrowserName;
	}

	public void setLastBrowserName(String lastBrowserName) {
		this.lastBrowserName = lastBrowserName;
	}

	public LocalDateTime getLockingEndTimeSendSms() {
		return lockingEndTimeSendSms;
	}

	public void setLockingEndTimeSendSms(LocalDateTime lockingEndTimeSendSms) {
		this.lockingEndTimeSendSms = lockingEndTimeSendSms;
	}

	public Integer getSendSmscallId() {
		return sendSmscallId;
	}

	public void setSendSmscallId(Integer sendSmscallId) {
		this.sendSmscallId = sendSmscallId;
	}

	public Integer getSmscallId() {
		return smscallId;
	}

	public void setSmscallId(Integer smscallId) {
		this.smscallId = smscallId;
	}

	public LocalDateTime getLockingStartTime() {
		return lockingStartTime;
	}

	public void setLockingStartTime(LocalDateTime lockingStartTime) {
		this.lockingStartTime = lockingStartTime;
	}

	public LocalDateTime getLockingEndTime() {
		return lockingEndTime;
	}

	public void setLockingEndTime(LocalDateTime lockingEndTime) {
		this.lockingEndTime = lockingEndTime;
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

	public String getUserAccess() {
		return userAccess;
	}

	public void setUserAccess(String userAccess) {
		this.userAccess = userAccess;
	}

	public String getBranchIdArray() {
		return branchIdArray;
	}

	public void setBranchIdArray(String branchIdArray) {
		this.branchIdArray = branchIdArray;
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

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public String getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(String geoLocation) {
		this.geoLocation = geoLocation;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public LocalDateTime getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(LocalDateTime approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", branchId=" + branchId + ", isAdmin=" + isAdmin
				+ ", isActive=" + isActive + ", empId=" + empId + ", mobileNo=" + mobileNo + ", userRole=" + userRole
				+ ", permission=" + permission + ", branchName=" + branchName + ", designation=" + designation
				+ ", city=" + city + ", state=" + state + ", area=" + area + ", lastAccessTime=" + lastAccessTime
				+ ", geoLocation=" + geoLocation + ", createdBy=" + createdBy + ", createdDate=" + createdDate
				+ ", updatedDate=" + updatedDate + ", status=" + status + ", approvedBy=" + approvedBy
				+ ", approvedDate=" + approvedDate + ", updatedBy=" + updatedBy + ", branchIdArray=" + branchIdArray
				+ ", userAccess=" + userAccess + ", department=" + department + ", emailId=" + emailId + "]";
	}

	

}
