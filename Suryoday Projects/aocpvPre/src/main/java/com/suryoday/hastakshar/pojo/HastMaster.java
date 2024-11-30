package com.suryoday.hastakshar.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hastakshar_Master")
public class HastMaster {

	@Id
	private String id;
	private String department;
	private String transactionTypes;
	private String transactionDescription;

	@Column(name = "transaction_description_v2")
	private String transactionDescriptionV2;
	private String nature;
	private String product;
	private String approver1;
	private String approver2;
	private String approver3;
	private String approver4;
	private String approver5;
	private String RequestFlow;
	private String State;
	private String keyword;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getTransactionTypes() {
		return transactionTypes;
	}

	public void setTransactionTypes(String transactionTypes) {
		this.transactionTypes = transactionTypes;
	}

	public String getTransactionDescription() {
		return transactionDescription;
	}

	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getApprover1() {
		return approver1;
	}

	public void setApprover1(String approver1) {
		this.approver1 = approver1;
	}

	public String getApprover2() {
		return approver2;
	}

	public void setApprover2(String approver2) {
		this.approver2 = approver2;
	}

	public String getApprover3() {
		return approver3;
	}

	public void setApprover3(String approver3) {
		this.approver3 = approver3;
	}

	public String getApprover4() {
		return approver4;
	}

	public void setApprover4(String approver4) {
		this.approver4 = approver4;
	}

	public String getApprover5() {
		return approver5;
	}

	public void setApprover5(String approver5) {
		this.approver5 = approver5;
	}

	public String getRequestFlow() {
		return RequestFlow;
	}

	public void setRequestFlow(String requestFlow) {
		RequestFlow = requestFlow;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public HastMaster(String id, String department, String transactionTypes, String transactionDescription,
			String nature, String product, String approver1, String approver2, String approver3, String approver4,
			String approver5, String requestFlow, String state, String keyword) {
		super();
		this.id = id;
		this.department = department;
		this.transactionTypes = transactionTypes;
		this.transactionDescription = transactionDescription;
		this.nature = nature;
		this.product = product;
		this.approver1 = approver1;
		this.approver2 = approver2;
		this.approver3 = approver3;
		this.approver4 = approver4;
		this.approver5 = approver5;
		RequestFlow = requestFlow;
		State = state;
		this.keyword = keyword;
	}

	public String getTransactionDescriptionV2() {
		return transactionDescriptionV2;
	}

	public void setTransactionDescriptionV2(String transactionDescriptionV2) {
		this.transactionDescriptionV2 = transactionDescriptionV2;
	}

	public HastMaster() {
		super();
	}

	@Override
	public String toString() {
		return "HastMaster [id=" + id + ", department=" + department + ", transactionTypes=" + transactionTypes
				+ ", transactionDescription=" + transactionDescription + ", nature=" + nature + ", product=" + product
				+ ", approver1=" + approver1 + ", approver2=" + approver2 + ", approver3=" + approver3 + ", approver4="
				+ approver4 + ", approver5=" + approver5 + ", RequestFlow=" + RequestFlow + ", State=" + State
				+ ", keyword=" + keyword + "]";
	}

}