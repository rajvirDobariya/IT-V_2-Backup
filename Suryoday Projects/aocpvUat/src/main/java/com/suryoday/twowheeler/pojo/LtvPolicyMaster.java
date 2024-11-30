package com.suryoday.twowheeler.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class LtvPolicyMaster {

	@Id
	private String id;
	private String ltvPolicyCode;
	private String ltvPolicyDescription;
	private String schemeCode;
	private String schemeDescription;

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

	public String getSchemeCode() {
		return schemeCode;
	}

	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}

	public String getSchemeDescription() {
		return schemeDescription;
	}

	public void setSchemeDescription(String schemeDescription) {
		this.schemeDescription = schemeDescription;
	}

}
