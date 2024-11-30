package com.digitisation.branchreports.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "branch_master_V2")
public class BranchMaster2 {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sr_no")
	private int srNo;
	@Column(name = "branch_id")
	private Long branchid;
	private String applicablefor;
	private String branchcode;
	private String address;
	@Column(name = "branch_name")
	private String branchname;
	private String city;
	private String state;
	@Column(name = "created_time")
	private LocalDateTime createdDate;

	public String getBranchname() {
		return branchname;
	}

	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getApplicablefor() {
		return applicablefor;
	}

	public void setApplicablefor(String applicablefor) {
		this.applicablefor = applicablefor;
	}

	public Long getBranchid() {
		return branchid;
	}

	public void setBranchid(Long branchid) {
		this.branchid = branchid;
	}

	public String getBranchcode() {
		return branchcode;
	}

	public void setBranchcode(String branchcode) {
		this.branchcode = branchcode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}
}
