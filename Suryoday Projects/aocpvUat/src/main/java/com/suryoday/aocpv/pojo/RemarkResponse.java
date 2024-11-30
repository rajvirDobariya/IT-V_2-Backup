package com.suryoday.aocpv.pojo;

public class RemarkResponse {

	private String decision;
	private String rejectReason;
	private String remarks;
	private String date;
	private String updatedBy;

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public RemarkResponse(String decision, String rejectReason, String remarks, String date, String updatedBy) {
		super();
		this.decision = decision;
		this.rejectReason = rejectReason;
		this.remarks = remarks;
		this.date = date;
		this.updatedBy = updatedBy;
	}

	public RemarkResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

}
