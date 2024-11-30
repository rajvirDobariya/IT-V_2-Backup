package com.suryoday.aocpv.pojo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="tbl_notification_userhistory")
public class NotificatonUserHistory {
	@Id
	private String empId;
	private long id;
	private String tokenId;
	private String senderId;
	private String notificationtemplateId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	@Lob
	private String contentTemplate;
	private String branchId;
	private String role;
	private int readHistory;
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
	public String getContentTemplate() {
		return contentTemplate;
	}
	public void setContentTemplate(String contentTemplate) {
		this.contentTemplate = contentTemplate;
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
	public int getReadHistory() {
		return readHistory;
	}
	public void setReadHistory(int readHistory) {
		this.readHistory = readHistory;
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
				+ ", updatedAt=" + updatedAt + ", contentTemplate=" + contentTemplate + ", branchId=" + branchId
				+ ", role=" + role + ", readHistory=" + readHistory + ", trash=" + trash + "]";
	}
	
	
	
	
}
