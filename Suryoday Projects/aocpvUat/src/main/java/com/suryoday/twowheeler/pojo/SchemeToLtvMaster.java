package com.suryoday.twowheeler.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SchemeToLtvMaster {
	@Id
	private String id;
	private String ltvPolicyCode;
	private String ltvPolicyDescription;
	private String creditStatusCode;
	private String creditStatusDescription;
	private String ltvPolicy;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLtvPolicyCode() {
		return ltvPolicyCode;
	}

	public void setLtvPolicyCode(String ltvPolicyCode) {
		this.ltvPolicyCode = ltvPolicyCode;
	}

	public String getLtvPolicyDescription() {
		return ltvPolicyDescription;
	}

	public void setLtvPolicyDescription(String ltvPolicyDescription) {
		this.ltvPolicyDescription = ltvPolicyDescription;
	}

	public String getCreditStatusCode() {
		return creditStatusCode;
	}

	public void setCreditStatusCode(String creditStatusCode) {
		this.creditStatusCode = creditStatusCode;
	}

	public String getCreditStatusDescription() {
		return creditStatusDescription;
	}

	public void setCreditStatusDescription(String creditStatusDescription) {
		this.creditStatusDescription = creditStatusDescription;
	}

	public String getLtvPolicy() {
		return ltvPolicy;
	}

	public void setLtvPolicy(String ltvPolicy) {
		this.ltvPolicy = ltvPolicy;
	}

}
