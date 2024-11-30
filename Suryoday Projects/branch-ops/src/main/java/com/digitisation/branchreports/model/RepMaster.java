package com.digitisation.branchreports.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "branch_d_rep_master")
public class RepMaster {

	@Id
	private long reportid;
	private String reportfrequency;
	private String reportname;

	public long getReportid() {
		return reportid;
	}

	public void setReportid(long reportid) {
		this.reportid = reportid;
	}

	public String getReportfrequency() {
		return reportfrequency;
	}

	public void setReportfrequency(String reportfrequency) {
		this.reportfrequency = reportfrequency;
	}

	public String getReportname() {
		return reportname;
	}

	public void setReportname(String reportname) {
		this.reportname = reportname;
	}

}
