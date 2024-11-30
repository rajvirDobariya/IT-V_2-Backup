package com.suryoday.aocpv.pojo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_notification_usertable")
public class NotificationUser {
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long pid;
	private String userId;
	private String tokenId;
	private String deviceId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String branchId;
	private String role;

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdateedAt() {
		return updatedAt;
	}

	public void setUpdateedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "NotificationUser [pid=" + pid + ", userId=" + userId + ", tokenId=" + tokenId + ", deviceId=" + deviceId
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", branchId=" + branchId + ", role=" + role
				+ "]";
	}

}
