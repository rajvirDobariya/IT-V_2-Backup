package com.suryoday.dsaOnboard.pojo;

public class DsaOnboardResponse {
	private long applicationNo;
	private String entity;
	private String typeOfRelationship;
	private String productType;
	private String noOfPartner;
	private String constitutionType;
	private String status;
	private String flowStatus;
	private String branchId;
	private String mobileNo;
	private String companyName;
	private String dsaCode;
	
	public String getDsaCode() {
		return dsaCode;
	}
	public void setDsaCode(String dsaCode) {
		this.dsaCode = dsaCode;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public long getApplicationNo() {
		return applicationNo;
	}
	public void setApplicationNo(long applicationNo) {
		this.applicationNo = applicationNo;
	}
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public String getTypeOfRelationship() {
		return typeOfRelationship;
	}
	public void setTypeOfRelationship(String typeOfRelationship) {
		this.typeOfRelationship = typeOfRelationship;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getNoOfPartner() {
		return noOfPartner;
	}
	public void setNoOfPartner(String noOfPartner) {
		this.noOfPartner = noOfPartner;
	}
	public String getConstitutionType() {
		return constitutionType;
	}
	public void setConstitutionType(String constitutionType) {
		this.constitutionType = constitutionType;
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
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public DsaOnboardResponse(long applicationNo, String entity, String typeOfRelationship, String productType,
			String noOfPartner, String constitutionType, String status, String flowStatus, String branchId,
			String mobileNo,String companyName,String dsaCode) {
		super();
		this.applicationNo = applicationNo;
		this.entity = entity;
		this.typeOfRelationship = typeOfRelationship;
		this.productType = productType;
		this.noOfPartner = noOfPartner;
		this.constitutionType = constitutionType;
		this.status = status;
		this.flowStatus = flowStatus;
		this.branchId = branchId;
		this.mobileNo = mobileNo;
		this.companyName = companyName;
		this.dsaCode = dsaCode;
	}
	public DsaOnboardResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
