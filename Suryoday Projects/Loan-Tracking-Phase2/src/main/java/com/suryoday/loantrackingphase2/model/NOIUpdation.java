package com.suryoday.loantrackingphase2.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "l_t_p2_noi_updation_activity")
public class NOIUpdation extends Auditable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 

	@Column(name = "loan_id", nullable = false)
	private String loanId;

	@Column(name = "disbursement_date")
	private LocalDate disbursementDate;

	@Column(name = "mortgage_type")
	private String mortgageType;

	@Column(name = "sanction_letter")
	private Boolean sanctionLetter;

	@Column(name = "index_2")
	private Boolean index2;

	@Column(name = "selfie_photo")
	private Boolean selfiePhoto;

	@Column(name = "kyc")
	private Boolean KYC;
	
	@Column(name = "noi_comformation_from_vendor")
	private Boolean noiComformationFromVendor;

	@Column(name = "moe_challan")
	private Boolean moeChallan;

	@Column(name = "noi_initiated_date")
	private LocalDate noiInitiatedDate;

	@Column(name = "noi_pending_with")
	private String noiPendingWith;

	@Column(name = "token_number")
	private String tokenNumber;

	@Column(name = "noi_receive_date")
	private LocalDate noiReceiveDate;

	@Column(name = "final_status")
	private String finalStatus;

	@Column(name = "remarks")
	private String remarks;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public LocalDate getDisbursementDate() {
		return disbursementDate;
	}

	public void setDisbursementDate(LocalDate disbursementDate) {
		this.disbursementDate = disbursementDate;
	}

	public String getMortgageType() {
		return mortgageType;
	}

	public void setMortgageType(String mortgageType) {
		this.mortgageType = mortgageType;
	}

	public Boolean getSanctionLetter() {
		return sanctionLetter;
	}

	public void setSanctionLetter(Boolean sanctionLetter) {
		this.sanctionLetter = sanctionLetter;
	}

	public Boolean getIndex2() {
		return index2;
	}

	public void setIndex2(Boolean index2) {
		this.index2 = index2;
	}

	public Boolean getSelfiePhoto() {
		return selfiePhoto;
	}

	public void setSelfiePhoto(Boolean selfiePhoto) {
		this.selfiePhoto = selfiePhoto;
	}

	public Boolean getKYC() {
		return KYC;
	}

	public void setKYC(Boolean kYC) {
		KYC = kYC;
	}

	public Boolean getMoeChallan() {
		return moeChallan;
	}

	public void setMoeChallan(Boolean moeChallan) {
		this.moeChallan = moeChallan;
	}

	public LocalDate getNoiInitiatedDate() {
		return noiInitiatedDate;
	}

	public void setNoiInitiatedDate(LocalDate noiInitiatedDate) {
		this.noiInitiatedDate = noiInitiatedDate;
	}

	public String getNoiPendingWith() {
		return noiPendingWith;
	}

	public void setNoiPendingWith(String noiPendingWith) {
		this.noiPendingWith = noiPendingWith;
	}

	public String getTokenNumber() {
		return tokenNumber;
	}

	public void setTokenNumber(String tokenNumber) {
		this.tokenNumber = tokenNumber;
	}

	public LocalDate getNoiReceiveDate() {
		return noiReceiveDate;
	}

	public void setNoiReceiveDate(LocalDate noiReceiveDate) {
		this.noiReceiveDate = noiReceiveDate;
	}

	public String getFinalStatus() {
		return finalStatus;
	}

	public void setFinalStatus(String finalStatus) {
		this.finalStatus = finalStatus;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Boolean getNoiComformationFromVendor() {
		return noiComformationFromVendor;
	}

	public void setNoiComformationFromVendor(Boolean noiComformationFromVendor) {
		this.noiComformationFromVendor = noiComformationFromVendor;
	}


}
