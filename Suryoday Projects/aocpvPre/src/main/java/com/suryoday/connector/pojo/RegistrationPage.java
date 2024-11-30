package com.suryoday.connector.pojo;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="tbl_connector_onboard")
public class RegistrationPage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column(name="application_no")
	private Long  applicationNo;
	
	@Column(name="status")
	private String status;
	
	@Column(name="flow_status")
	private String flowStatus;
	
	@Column(name="lead_number")
	private String leadNumber;
	
	@Column(name="mobile")
	private String mobile;
	
	@Column(name="mobile_verify")
	private String mobileVerify;
	
	@Column(name="email")
	private String email;
	
	@Column(name="email_verify")
	private String emailVerify;
	
	@Column(name="pancard")
	private String pancard;
	
	@Column(name="pancard_verify")
	private String pancardVerify;
	
	@Lob
	@Column(name="pancard_response")
	private String pancardResponse;
	
	@Column(name="gstno")
	private String gstno;
	
	@Column(name="gst_verify")
	private String gstVerify;
	
	@Lob
	@Column(name="gst_response")
	private String gstResponse;
	
	@Column(name="aadhaar")
	private String aadhaar;
	
	@Column(name="aadhaar_verify")
	private String aadhaarVerify;
	
	@Column(name="present_occupation")
	private String presentOccupation;
	
	@Column(name="mobile_no2")
	private String mobileNo2;
	
	@Column(name="mobilelinkwithaadhar")
	private String mobilelinkwithaadhar;
	
	@Lob
	@Column(name="aadhaar_response")
	private String aadhaarResponse;

	@Column(name="company_name")
	private String companyName;
	
	@Column(name="entity")
	private String entity;
	
	@Column(name="custname")
	private String custname;
	
	@Column(name="date_of_birth")
	private LocalDate dob;	
	
	@Lob
	@Column(name="address_details")
	private String addressDetails;
	
	@Lob
	@Column(name="bank_details")
	private String bankDetails;
	
	@Lob
	@Column(name="others_details")
	private String othersDetails;
	
	@Lob
	@Column(name="basic_details")
	private String basicDetails;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(Long applicationNo) {
		this.applicationNo = applicationNo;
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

	public String getLeadNumber() {
		return leadNumber;
	}

	public void setLeadNumber(String leadNumber) {
		this.leadNumber = leadNumber;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMobileVerify() {
		return mobileVerify;
	}

	public void setMobileVerify(String mobileVerify) {
		this.mobileVerify = mobileVerify;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailVerify() {
		return emailVerify;
	}

	public void setEmailVerify(String emailVerify) {
		this.emailVerify = emailVerify;
	}

	public String getPancard() {
		return pancard;
	}

	public void setPancard(String pancard) {
		this.pancard = pancard;
	}

	public String getPancardVerify() {
		return pancardVerify;
	}

	public void setPancardVerify(String pancardVerify) {
		this.pancardVerify = pancardVerify;
	}

	public String getPancardResponse() {
		return pancardResponse;
	}

	public void setPancardResponse(String pancardResponse) {
		this.pancardResponse = pancardResponse;
	}

	public String getGstno() {
		return gstno;
	}

	public void setGstno(String gstno) {
		this.gstno = gstno;
	}

	public String getGstVerify() {
		return gstVerify;
	}

	public void setGstVerify(String gstVerify) {
		this.gstVerify = gstVerify;
	}

	public String getGstResponse() {
		return gstResponse;
	}

	public void setGstResponse(String gstResponse) {
		this.gstResponse = gstResponse;
	}

	public String getAadhaar() {
		return aadhaar;
	}

	public void setAadhaar(String aadhaar) {
		this.aadhaar = aadhaar;
	}

	public String getAadhaarVerify() {
		return aadhaarVerify;
	}

	public void setAadhaarVerify(String aadhaarVerify) {
		this.aadhaarVerify = aadhaarVerify;
	}

	public String getPresentOccupation() {
		return presentOccupation;
	}

	public void setPresentOccupation(String presentOccupation) {
		this.presentOccupation = presentOccupation;
	}

	public String getMobileNo2() {
		return mobileNo2;
	}

	public void setMobileNo2(String mobileNo2) {
		this.mobileNo2 = mobileNo2;
	}

	public String getMobilelinkwithaadhar() {
		return mobilelinkwithaadhar;
	}

	public void setMobilelinkwithaadhar(String mobilelinkwithaadhar) {
		this.mobilelinkwithaadhar = mobilelinkwithaadhar;
	}

	public String getAadhaarResponse() {
		return aadhaarResponse;
	}

	public void setAadhaarResponse(String aadhaarResponse) {
		this.aadhaarResponse = aadhaarResponse;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getCustname() {
		return custname;
	}

	public void setCustname(String custname) {
		this.custname = custname;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getAddressDetails() {
		return addressDetails;
	}

	public void setAddressDetails(String addressDetails) {
		this.addressDetails = addressDetails;
	}

	public String getBankDetails() {
		return bankDetails;
	}

	public void setBankDetails(String bankDetails) {
		this.bankDetails = bankDetails;
	}

	public String getOthersDetails() {
		return othersDetails;
	}

	public void setOthersDetails(String othersDetails) {
		this.othersDetails = othersDetails;
	}

	public String getBasicDetails() {
		return basicDetails;
	}

	public void setBasicDetails(String basicDetails) {
		this.basicDetails = basicDetails;
	}

	@Override
	public String toString() {
		return "RegistrationPage [id=" + id + ", applicationNo=" + applicationNo + ", status=" + status
				+ ", flowStatus=" + flowStatus + ", leadNumber=" + leadNumber + ", mobile=" + mobile + ", mobileVerify="
				+ mobileVerify + ", email=" + email + ", emailVerify=" + emailVerify + ", pancard=" + pancard
				+ ", pancardVerify=" + pancardVerify + ", pancardResponse=" + pancardResponse + ", gstno=" + gstno
				+ ", gstVerify=" + gstVerify + ", gstResponse=" + gstResponse + ", aadhaar=" + aadhaar
				+ ", aadhaarVerify=" + aadhaarVerify + ", presentOccupation=" + presentOccupation + ", mobileNo2="
				+ mobileNo2 + ", mobilelinkwithaadhar=" + mobilelinkwithaadhar + ", aadhaarResponse=" + aadhaarResponse
				+ ", companyName=" + companyName + ", entity=" + entity + ", custname=" + custname + ", dob=" + dob
				+ ", addressDetails=" + addressDetails + ", bankDetails=" + bankDetails + ", othersDetails="
				+ othersDetails + ", basicDetails=" + basicDetails + "]";
	}
	
}
