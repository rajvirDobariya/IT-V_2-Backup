package com.suryoday.mhl.pojo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_MHL_USER_LOG")
public class MhlUserLog {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int userId;
	private String applicationNo;
	private String task;
	private String createdBy;
	private LocalDateTime CreatedDate;
	private LocalDateTime updatedDate;
	private String role;
	private String remark;
	private String status;
	private String branchId;
	private String branchName;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task.toUpperCase();
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy.toUpperCase();
	}

	public LocalDateTime getCreatedDate() {
		return CreatedDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		CreatedDate = createdDate;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role.toUpperCase();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark.toUpperCase();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status.toUpperCase();
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName.toUpperCase();
	}

	@Override
	public String toString() {
		return "MhlUserLog [userId=" + userId + ", applicationNo=" + applicationNo + ", task=" + task + ", createdBy="
				+ createdBy + ", CreatedDate=" + CreatedDate + ", updatedDate=" + updatedDate + ", role=" + role
				+ ", remark=" + remark + ", status=" + status + ", branchId=" + branchId + ", branchName=" + branchName
				+ "]";
	}

}
