package com.suryoday.CustomerIntraction.Pojo;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="MOM_summary_details_audit")
public class MOMSummaryDetailsAudit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long meetingId;
	private String topicOfDiscussion;
	private String branchActionable;
	private String feedback;
	@Column(nullable = true)
	private LocalDate ETA;
	private String meetingNumber;
	private long createdBy;
	private long updatedBy;
	private String role;
	private LocalDate createdDate;
	private LocalDate updatedDate;
	
	public long getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(long meetingId) {
		this.meetingId = meetingId;
	}
	public String getTopicOfDiscussion() {
		return topicOfDiscussion;
	}
	public void setTopicOfDiscussion(String topicOfDiscussion) {
		this.topicOfDiscussion = topicOfDiscussion;
	}
	public String getBranchActionable() {
		return branchActionable;
	}
	public void setBranchActionable(String branchActionable) {
		this.branchActionable = branchActionable;
	}
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	public LocalDate getETA() {
		return ETA;
	}
	public void setETA(LocalDate eTA) {
		ETA = eTA;
	}
	public String getMeetingNumber() {
		return meetingNumber;
	}
	public void setMeetingNumber(String meetingNumber) {
		this.meetingNumber = meetingNumber;
	}
	public long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}
	public long getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(long updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public LocalDate getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}
	public LocalDate getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(LocalDate updatedDate) {
		this.updatedDate = updatedDate;
	}
	@Override
	public String toString() {
		return "MOMSummaryDetailsAudit [meetingId=" + meetingId + ", topicOfDiscussion=" + topicOfDiscussion
				+ ", branchActionable=" + branchActionable + ", feedback=" + feedback + ", ETA=" + ETA
				+ ", meetingNumber=" + meetingNumber + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy
				+ ", role=" + role + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + "]";
	}
	
	
	
	
	
	
}