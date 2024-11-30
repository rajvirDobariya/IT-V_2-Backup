package com.digitisation.branchreports.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "new_branch_master")
public class NewBranchMaster {

	@Id
	private String id;
	private String branchCode;
	private String branchName;
	private String cbmEmpId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getCbmEmpId() {
		return cbmEmpId;
	}

	public void setCbmEmpId(String cbmEmpId) {
		this.cbmEmpId = cbmEmpId;
	}

}
