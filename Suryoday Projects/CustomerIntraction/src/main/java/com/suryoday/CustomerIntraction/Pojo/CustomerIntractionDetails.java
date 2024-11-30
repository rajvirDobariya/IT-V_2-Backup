package com.suryoday.CustomerIntraction.Pojo;

import java.sql.Timestamp;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customer_intraction_details")
public class CustomerIntractionDetails {

	@Id
	private String meetingNumber;
	private long meetingId;
	private LocalDate meetingDate;
	private Timestamp meetingStartTime;
	private Timestamp meetingEndTime;
	private String status;
	private long createdBy;
	private long updatedBy;
	private LocalDate updatedDate;
	private String branchCode;
	private String currentMonth;
	private LocalDate createdDate;
	private String roRemark;
	private String bmRemark;
	private String creditOpsRemark;

	public long getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(long meetingId) {
		this.meetingId = meetingId;
	}

	public LocalDate getMeetingDate() {
		return meetingDate;
	}

	public void setMeetingDate(LocalDate meetingDate) {
		this.meetingDate = meetingDate;
	}

	public Timestamp getMeetingStartTime() {
		return meetingStartTime;
	}

	public void setMeetingStartTime(Timestamp meetingStartTime) {
		this.meetingStartTime = meetingStartTime;
	}

	public Timestamp getMeetingEndTime() {
		return meetingEndTime;
	}

	public void setMeetingEndTime(Timestamp meetingEndTime) {
		this.meetingEndTime = meetingEndTime;
	}

	public String getMeetingNumber() {
		return meetingNumber;
	}

	public void setMeetingNumber(String meetingNumber) {
		this.meetingNumber = meetingNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getCurrentMonth() {
		return currentMonth;
	}

	public void setCurrentMonth(String currentMonth) {
		this.currentMonth = currentMonth;
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
	public String getRoRemark() {
		return roRemark;
	}

	public void setRoRemark(String roRemark) {
		this.roRemark = roRemark;
	}

	public String getBmRemark() {
		return bmRemark;
	}

	public void setBmRemark(String bmRemark) {
		this.bmRemark = bmRemark;
	}

	public String getCreditOpsRemark() {
		return creditOpsRemark;
	}

	public void setCreditOpsRemark(String creditOpsRemark) {
		this.creditOpsRemark = creditOpsRemark;
	}

	

	 @Override
	public String toString() {
		return "CustomerIntractionDetails [meetingNumber=" + meetingNumber + ", meetingId=" + meetingId
				+ ", meetingDate=" + meetingDate + ", meetingStartTime=" + meetingStartTime + ", meetingEndTime="
				+ meetingEndTime + ", status=" + status + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy
				+ ", updatedDate=" + updatedDate + ", branchCode=" + branchCode + ", currentMonth=" + currentMonth
				+ ", createdDate=" + createdDate + ", roRemark=" + roRemark + ", bmRemark=" + bmRemark
				+ ", creditOpsRemark=" + creditOpsRemark + "]";
	}
	

}
