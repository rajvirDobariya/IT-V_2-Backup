package com.suryodaybank.enduse.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "end_use_activity")
public class EndUseActivity extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long accountNo;
	private Long custId;
	private String accountName;
	private String pslCategory;
	private Long purcd;
	private String purposeDesc;
	private String type;
	private String productDesc;
	private LocalDate sanctionDate;
	private Double sanctionAmt;
	private LocalDate disbursedDate;
	private Double disbursedAmt;
	private Integer dpd;
	private Double pos;
	private String activeClosed;
	private Long branchCode;
	private Long mobilizeBranch;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(Long accountNo) {
		this.accountNo = accountNo;
	}

	public Long getCustId() {
		return custId;
	}

	public void setCustId(Long custId) {
		this.custId = custId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getPslCategory() {
		return pslCategory;
	}

	public void setPslCategory(String pslCategory) {
		this.pslCategory = pslCategory;
	}

	public Long getPurcd() {
		return purcd;
	}

	public void setPurcd(Long purcd) {
		this.purcd = purcd;
	}

	public String getPurposeDesc() {
		return purposeDesc;
	}

	public void setPurposeDesc(String purposeDesc) {
		this.purposeDesc = purposeDesc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public LocalDate getSanctionDate() {
		return sanctionDate;
	}

	public void setSanctionDate(LocalDate sanctionDate) {
		this.sanctionDate = sanctionDate;
	}

	public Double getSanctionAmt() {
		return sanctionAmt;
	}

	public void setSanctionAmt(Double sanctionAmt) {
		this.sanctionAmt = sanctionAmt;
	}

	public LocalDate getDisbursedDate() {
		return disbursedDate;
	}

	public void setDisbursedDate(LocalDate disbursedDate) {
		this.disbursedDate = disbursedDate;
	}

	public Double getDisbursedAmt() {
		return disbursedAmt;
	}

	public void setDisbursedAmt(Double disbursedAmt) {
		this.disbursedAmt = disbursedAmt;
	}

	public Integer getDpd() {
		return dpd;
	}

	public void setDpd(Integer dpd) {
		this.dpd = dpd;
	}

	public Double getPos() {
		return pos;
	}

	public void setPos(Double pos) {
		this.pos = pos;
	}

	public String getActiveClosed() {
		return activeClosed;
	}

	public void setActiveClosed(String activeClosed) {
		this.activeClosed = activeClosed;
	}

	public Long getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(Long branchCode) {
		this.branchCode = branchCode;
	}

	public Long getMobilizeBranch() {
		return mobilizeBranch;
	}

	public void setMobilizeBranch(Long mobilizeBranch) {
		this.mobilizeBranch = mobilizeBranch;
	}
}
