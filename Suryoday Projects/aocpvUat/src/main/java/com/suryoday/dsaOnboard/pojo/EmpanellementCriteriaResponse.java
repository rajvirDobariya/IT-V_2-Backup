package com.suryoday.dsaOnboard.pojo;

import java.util.List;

public class EmpanellementCriteriaResponse {

	private String applicationNo;
	private String noOfYearsInBusiness;
	private String noOfYearsInFinancialBusiness;
	private String empanelledFiType;
	private String empanelledFiName;
	private String blacklisted;
	private List<CfrReportResponse> cfrReport;
	private String ageOfProprietor;
	private String status;
	private String flowStatus;
	private String otherServices;
	private AmlReportResponse amlReport;

	public AmlReportResponse getAmlReport() {
		return amlReport;
	}

	public void setAmlReport(AmlReportResponse amlReport) {
		this.amlReport = amlReport;
	}

	public String getOtherServices() {
		return otherServices;
	}

	public void setOtherServices(String otherServices) {
		this.otherServices = otherServices;
	}

	public List<CfrReportResponse> getCfrReport() {
		return cfrReport;
	}

	public void setCfrReport(List<CfrReportResponse> cfrReport) {
		this.cfrReport = cfrReport;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getNoOfYearsInBusiness() {
		return noOfYearsInBusiness;
	}

	public void setNoOfYearsInBusiness(String noOfYearsInBusiness) {
		this.noOfYearsInBusiness = noOfYearsInBusiness;
	}

	public String getNoOfYearsInFinancialBusiness() {
		return noOfYearsInFinancialBusiness;
	}

	public void setNoOfYearsInFinancialBusiness(String noOfYearsInFinancialBusiness) {
		this.noOfYearsInFinancialBusiness = noOfYearsInFinancialBusiness;
	}

	public String getEmpanelledFiType() {
		return empanelledFiType;
	}

	public void setEmpanelledFiType(String empanelledFiType) {
		this.empanelledFiType = empanelledFiType;
	}

	public String getEmpanelledFiName() {
		return empanelledFiName;
	}

	public void setEmpanelledFiName(String empanelledFiName) {
		this.empanelledFiName = empanelledFiName;
	}

	public String getBlacklisted() {
		return blacklisted;
	}

	public void setBlacklisted(String blacklisted) {
		this.blacklisted = blacklisted;
	}

	public String getAgeOfProprietor() {
		return ageOfProprietor;
	}

	public void setAgeOfProprietor(String ageOfProprietor) {
		this.ageOfProprietor = ageOfProprietor;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}

}
