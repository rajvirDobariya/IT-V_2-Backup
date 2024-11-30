package com.suryoday.Counterfeit.Pojo;

import java.time.YearMonth;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "counterfeit")
public class Counterfeit extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	private String isDetect;

	private String branchCode;

	private String branchName;

	private String status;

	private Long roId;

	private String roRole;

	private String roRemarks;

	private Long bmId;

	private String bmRole;

	private String bmRemarks;

	private String dailyMonthly;

	private YearMonth month;

	private Boolean theNotesHaveBeenImpounded;
	private Boolean theRegisterUpdatedWithDetails;
	private Boolean anAcknowledgmentReceiptPrepared;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIsDetect() {
		return isDetect;
	}

	public void setIsDetect(String isDetect) {
		this.isDetect = isDetect;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getRoId() {
		return roId;
	}

	public void setRoId(Long roId) {
		this.roId = roId;
	}

	public String getRoRole() {
		return roRole;
	}

	public void setRoRole(String roRole) {
		this.roRole = roRole;
	}

	public String getRoRemarks() {
		return roRemarks;
	}

	public void setRoRemarks(String roRemarks) {
		this.roRemarks = roRemarks;
	}

	public Long getBmId() {
		return bmId;
	}

	public void setBmId(Long bmId) {
		this.bmId = bmId;
	}

	public String getBmRole() {
		return bmRole;
	}

	public void setBmRole(String bmRole) {
		this.bmRole = bmRole;
	}

	public String getBmRemarks() {
		return bmRemarks;
	}

	public void setBmRemarks(String bmRemarks) {
		this.bmRemarks = bmRemarks;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getDailyMonthly() {
		return dailyMonthly;
	}

	public void setDailyMonthly(String dailyMonthly) {
		this.dailyMonthly = dailyMonthly;
	}

	public YearMonth getMonth() {
		return month;
	}

	public void setMonth(YearMonth month) {
		this.month = month;
	}

	public Boolean getTheNotesHaveBeenImpounded() {
		return theNotesHaveBeenImpounded;
	}

	public void setTheNotesHaveBeenImpounded(Boolean theNotesHaveBeenImpounded) {
		this.theNotesHaveBeenImpounded = theNotesHaveBeenImpounded;
	}

	public Boolean getTheRegisterUpdatedWithDetails() {
		return theRegisterUpdatedWithDetails;
	}

	public void setTheRegisterUpdatedWithDetails(Boolean theRegisterUpdatedWithDetails) {
		this.theRegisterUpdatedWithDetails = theRegisterUpdatedWithDetails;
	}

	public Boolean getAnAcknowledgmentReceiptPrepared() {
		return anAcknowledgmentReceiptPrepared;
	}

	public void setAnAcknowledgmentReceiptPrepared(Boolean anAcknowledgmentReceiptPrepared) {
		this.anAcknowledgmentReceiptPrepared = anAcknowledgmentReceiptPrepared;
	}

}
