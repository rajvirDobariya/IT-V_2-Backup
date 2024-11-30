package com.suryoday.aocpv.pojo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_notificatin_userhistory")
public class NotificatonUserHistory {
	@Id
	private long id;
	private String empId;
	private String tokenId;
	private String senderId;
	private String notificationtemplateId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	@Lob
	private String content;
	private String branchId;
	private String role;
	private int read;
	private int trash;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getNotificationtemplateId() {
		return notificationtemplateId;
	}

	public void setNotificationtemplateId(String notificationtemplateId) {
		this.notificationtemplateId = notificationtemplateId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public int getRead() {
		return read;
	}

	public void setRead(int read) {
		this.read = read;
	}

	public int getTrash() {
		return trash;
	}

	public void setTrash(int trash) {
		this.trash = trash;
	}

	@Override
	public String toString() {
		return "NotificatonUserHistory [id=" + id + ", empId=" + empId + ", tokenId=" + tokenId + ", senderId="
				+ senderId + ", notificationtemplateId=" + notificationtemplateId + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + ", content=" + content + ", branchId=" + branchId + ", role=" + role
				+ ", read=" + read + ", trash=" + trash + "]";
	}

}
