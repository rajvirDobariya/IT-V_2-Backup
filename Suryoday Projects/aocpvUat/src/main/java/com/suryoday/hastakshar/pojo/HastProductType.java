package com.suryoday.hastakshar.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
public class HastProductType {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	@JsonProperty(access = Access.WRITE_ONLY)
	private int id;
	@Column(unique = true)
	private String productCode;
	private String productName;
	private String approval;
	private String empId;

	public HastProductType(int id, String productCode, String productName, String approval, String empId) {
		super();
		this.id = id;
		this.productCode = productCode;
		this.productName = productName;
		this.approval = approval;
		this.empId = empId;
	}

	public HastProductType() {
		super();
	}

	@JsonIgnore
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getApproval() {
		return approval;
	}

	public void setApproval(String approval) {
		this.approval = approval;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	@Override
	public String toString() {
		return "HastProductType [ productCode=" + productCode + ", productName=" + productName + ", approval="
				+ approval + ", empId=" + empId + "]";
	}

}
