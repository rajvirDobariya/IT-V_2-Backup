package com.suryoday.dsaOnboard.pojo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DsaUserLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long applicationNo;
	private String stages;
	private String userId;
	private LocalDateTime createdDate;
	private String userRole;
	private String remarks;
	
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
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
		this.stages = stages;
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
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public DsaUserLog(long applicationNo, String stages, String userId, LocalDateTime createdDate) {
		super();
		this.applicationNo = applicationNo;
		this.stages = stages;
		this.userId = userId;
		this.createdDate = createdDate;
	}
	public DsaUserLog() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
