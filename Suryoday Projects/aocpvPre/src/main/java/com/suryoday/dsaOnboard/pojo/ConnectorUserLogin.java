package com.suryoday.dsaOnboard.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name="tbl_connector_userLogin")
public class ConnectorUserLogin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String entity;
	private String typeOfRelationship;
	private String productType;
	private String noOfPartner;
	private String mobileNo;
	private String emailId;
	private String password;
	private String companyName;
	private String gstNumber;
	private String leadId;
	private String branchId;
	
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getLeadId() {
		return leadId;
	}
	public void setLeadId(String leadId) {
		this.leadId = leadId;
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
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getGstNumber() {
		return gstNumber;
	}
	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}
	@Override
	public String toString() {
		return "ConnectorUserLogin [id=" + id + ", entity=" + entity + ", typeOfRelationship=" + typeOfRelationship
				+ ", productType=" + productType + ", noOfPartner=" + noOfPartner + ", mobileNo=" + mobileNo
				+ ", emailId=" + emailId + ", password=" + password + ", companyName=" + companyName + ", gstNumber="
				+ gstNumber + "]";
	}
	
}
