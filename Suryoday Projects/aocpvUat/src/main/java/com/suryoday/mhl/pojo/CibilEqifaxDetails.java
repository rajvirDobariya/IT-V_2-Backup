package com.suryoday.mhl.pojo;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_CIBIL_Eqifax_Details")
public class CibilEqifaxDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "CIBID ")
	private long cibId;

	@Column(name = "Application_no")
	private String applicationNo;

	@Column(name = "Member_id")
	private String memberId;

	@Column(name = "SCORE")
	private String score;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATEDATE")
	private LocalDateTime updateDate;

	@Lob
	@Column(name = "bureau_report_response")
	private String bureauReportResponse;

	public long getCibId() {
		return cibId;
	}

	public void setCibId(long cibId) {
		this.cibId = cibId;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

	public String getBureauReportResponse() {
		return bureauReportResponse;
	}

	public void setBureauReportResponse(String bureauReportResponse) {
		this.bureauReportResponse = bureauReportResponse;
	}

}
