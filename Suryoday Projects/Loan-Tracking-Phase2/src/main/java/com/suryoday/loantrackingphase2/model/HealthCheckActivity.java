package com.suryoday.loantrackingphase2.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "l_t_p2_health_check_activity")
public class HealthCheckActivity extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "status", nullable = false)
	private String status;

	@Column(name = "pan_no", length = 10, nullable = false)
	private String panNo;

	@Column(name = "cust_id", nullable = false)
	private String custId;

	@Column(name = "cust_name", nullable = false)
	private String custName;

	@Column(name = "file_receive_date", nullable = false)
	private LocalDate fileReceiveDate;

	@Column(name = "bdm_emp_id", nullable = false)
	private Long BDMEmpId;

	@Column(name = "sm_emp_id", nullable = false)
	private Long SMEmpId;

	@Column(name = "asm_emp_id", nullable = false)
	private Long ASMEmpId;

	@Column(name = "bdm_emp_name", nullable = false)
	private String BDMEmpName;

	@Column(name = "sm_emp_name", nullable = false)
	private String SMEmpName;

	@Column(name = "asm_emp_name", nullable = false)
	private String ASMEmpName;

	@Column(name = "cust_sourcing_branch_name", nullable = false)
	private String custSourcingBranchName;

	@Column(name = "product_id", nullable = false)
	private Long productCode;

	@Lob
	@Column(name = "product_data")
	private String productData;

	@ElementCollection
	@CollectionTable(name = "health_check_activity_data", joinColumns = @JoinColumn(name = "activity_id"))
	@Column(name = "queries")
	private List<String> queries;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "loan_amount")
	private String loanAmount;

	@Column(name = "application_id")
	private String applicationId;

	@Column(name = "first_time_right")
	private String firstTimeRight;

	@Column(name = "billed_or_dispatched")
	private String billedOrDispatched;

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public LocalDate getFileReceiveDate() {
		return fileReceiveDate;
	}

	public void setFileReceiveDate(LocalDate fileReceiveDate) {
		this.fileReceiveDate = fileReceiveDate;
	}

	public Long getBDMEmpId() {
		return BDMEmpId;
	}

	public void setBDMEmpId(Long BDMEmpId) {
		this.BDMEmpId = BDMEmpId;
	}

	public Long getSMEmpId() {
		return SMEmpId;
	}

	public void setSMEmpId(Long SMEmpId) {
		this.SMEmpId = SMEmpId;
	}

	public Long getASMEmpId() {
		return ASMEmpId;
	}

	public void setASMEmpId(Long ASMEmpId) {
		this.ASMEmpId = ASMEmpId;
	}

	public String getCustSourcingBranchName() {
		return custSourcingBranchName;
	}

	public void setCustSourcingBranchName(String custSourcingBranchName) {
		this.custSourcingBranchName = custSourcingBranchName;
	}

	public Long getProductCode() {
		return productCode;
	}

	public void setProductCode(Long productCode) {
		this.productCode = productCode;
	}

	public String getProductData() {
		return productData;
	}

	public void setProductData(String productData) {
		this.productData = productData;
	}

	public List<String> getQueries() {
		return queries;
	}

	public void setQueries(List<String> queries) {
		this.queries = queries;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getBDMEmpName() {
		return BDMEmpName;
	}

	public void setBDMEmpName(String bDMEmpName) {
		BDMEmpName = bDMEmpName;
	}

	public String getSMEmpName() {
		return SMEmpName;
	}

	public void setSMEmpName(String sMEmpName) {
		SMEmpName = sMEmpName;
	}

	public String getASMEmpName() {
		return ASMEmpName;
	}

	public void setASMEmpName(String aSMEmpName) {
		ASMEmpName = aSMEmpName;
	}

	public String getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getFirstTimeRight() {
		return firstTimeRight;
	}

	public void setFirstTimeRight(String firstTimeRight) {
		this.firstTimeRight = firstTimeRight;
	}

	public String getBilledOrDispatched() {
		return billedOrDispatched;
	}

	public void setBilledOrDispatched(String billedOrDispatched) {
		this.billedOrDispatched = billedOrDispatched;
	}
	
}
