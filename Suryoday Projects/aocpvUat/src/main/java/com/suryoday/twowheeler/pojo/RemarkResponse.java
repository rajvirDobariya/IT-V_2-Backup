package com.suryoday.twowheeler.pojo;

public class RemarkResponse {

	private String decision;
	private String level;
	private String date;
	private String updatedBy;
	private String remarks;

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public RemarkResponse(String decision, String level, String date, String updatedBy, String remarks) {
		super();
		this.decision = decision;
		this.level = level;
		this.date = date;
		this.updatedBy = updatedBy;
		this.remarks = remarks;
	}

	public RemarkResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

}
