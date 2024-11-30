package com.digitisation.branchreports.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "branch_d_branch_user_maker")
public class BranchUserMakerModel implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private Long reportId;

	private String createdby;
	private String updatedby;
	private Date createdDate;
	private Date updatedDate;

	private String branchname;
	private String branchcode;
	private String status;
	private String action;
	private Long roDocument;
	private Long auditorDocument;
	private LocalDate reportdate;
//	private LocalDate reportEnddate;
	private LocalDate submissiondate;

	private String roRemarks;
	private String bmRemarks;
	private String hoRemarks;
	private String auditorRemarks;
	private Long roId;
	private Long bmId;
	private Long hoId;
	private Long auditorId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	public String getUpdatedby() {
		return updatedby;
	}

	public void setUpdatedby(String updatedby) {
		this.updatedby = updatedby;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getBranchname() {
		return branchname;
	}

	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}

	public String getBranchcode() {
		return branchcode;
	}

	public void setBranchcode(String branchcode) {
		this.branchcode = branchcode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Long getRoDocument() {
		return roDocument;
	}

	public void setRoDocument(Long roDocument) {
		this.roDocument = roDocument;
	}

	public Long getAuditorDocument() {
		return auditorDocument;
	}

	public void setAuditorDocument(Long auditorDocument) {
		this.auditorDocument = auditorDocument;
	}

	public LocalDate getReportdate() {
		return reportdate;
	}

	public void setReportdate(LocalDate reportdate) {
		this.reportdate = reportdate;
	}

	public LocalDate getSubmissiondate() {
		return submissiondate;
	}

	public void setSubmissiondate(LocalDate submissiondate) {
		this.submissiondate = submissiondate;
	}

	public String getRoRemarks() {
		return roRemarks;
	}

	public void setRoRemarks(String roRemarks) {
		this.roRemarks = roRemarks;
	}

	public String getBmRemarks() {
		return bmRemarks;
	}

	public void setBmRemarks(String bmRemarks) {
		this.bmRemarks = bmRemarks;
	}

	public String getHoRemarks() {
		return hoRemarks;
	}

	public void setHoRemarks(String hoRemarks) {
		this.hoRemarks = hoRemarks;
	}

	public String getAuditorRemarks() {
		return auditorRemarks;
	}

	public void setAuditorRemarks(String auditorRemarks) {
		this.auditorRemarks = auditorRemarks;
	}

	public Long getRoId() {
		return roId;
	}

	public void setRoId(Long roId) {
		this.roId = roId;
	}

	public Long getBmId() {
		return bmId;
	}

	public void setBmId(Long bmId) {
		this.bmId = bmId;
	}

	public Long getHoId() {
		return hoId;
	}

	public void setHoId(Long hoId) {
		this.hoId = hoId;
	}

	public Long getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(Long auditorId) {
		this.auditorId = auditorId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
