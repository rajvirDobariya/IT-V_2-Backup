package com.suryoday.twowheeler.pojo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TwowheelerUserLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String applicationNo;
	private String stages;
	private String userId;
	private LocalDateTime createdDate;
	private String userRole;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
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

	public TwowheelerUserLog(String applicationNo, String stages, String userId, LocalDateTime createdDate,
			String userRole) {
		super();
		this.applicationNo = applicationNo;
		this.stages = stages;
		this.userId = userId;
		this.createdDate = createdDate;
		this.userRole = userRole;
	}

	public TwowheelerUserLog() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "TwowheelerUserLog [id=" + id + ", applicationNo=" + applicationNo + ", stages=" + stages + ", userId="
				+ userId + ", createdDate=" + createdDate + ", userRole=" + userRole + "]";
	}

}
