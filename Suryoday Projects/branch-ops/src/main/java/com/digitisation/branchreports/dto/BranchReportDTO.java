package com.digitisation.branchreports.dto;

import java.time.LocalDate;

public class BranchReportDTO {

	private String branchCode;
	private String branchName;
	private LocalDate reportDate;
	private String reportFrequency;
	private Long reports;

	// Constructor
	public BranchReportDTO(String branchCode, String branchName, LocalDate reportDate, String reportFrequency,
			Long reports) {
		this.branchCode = branchCode;
		this.branchName = branchName;
		this.reportDate = reportDate;
		this.reportFrequency = reportFrequency;
		this.reports = reports;
	}

	// Getters and Setters
	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public LocalDate getReportDate() {
		return reportDate;
	}

	public void setReportDate(LocalDate reportDate) {
		this.reportDate = reportDate;
	}

	public String getReportFrequency() {
		return reportFrequency;
	}

	public void setReportFrequency(String reportFrequency) {
		this.reportFrequency = reportFrequency;
	}

	public Long getReports() {
		return reports;
	}

	public void setReports(Long reports) {
		this.reports = reports;
	}
}
