package com.suryoday.aocpv.pojo;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AOCPV_USERLOG")
public class UserLog {

	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private long applicationNo;
	private String stages;
	private String userId;
	private LocalDateTime createdDate;
	private LocalDateTime timestamp;
	private String userRole;
	@Column(nullable = true)
	private int versionCode;
	
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getApplicationNo() {
		return applicationNo;
	}
	public void setApplicationNo(long applicationNo) {
		this.applicationNo = applicationNo;
	}
	public String getStages() {
		return stages;
	}
	public void setStages(String stages) {
		this.stages = stages.toUpperCase();
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}
	@Override
	public String toString() {
		return "userLog [id=" + id + ", applicationNo=" + applicationNo + ", stages=" + stages + ", userId=" + userId
				+ ", createdDate=" + createdDate + "]";
	}
    
	
}
