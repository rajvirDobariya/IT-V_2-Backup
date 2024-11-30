package com.suryoday.dsaOnboard.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DsaonboardSchemeMaster {
	@Id
	private Long id;
	private String schemeCode;
	private String schemeType;
	private String productGroup;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSchemeCode() {
		return schemeCode;
	}

	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}

	public String getSchemeType() {
		return schemeType;
	}

	public void setSchemeType(String schemeType) {
		this.schemeType = schemeType;
	}

	public String getProductGroup() {
		return productGroup;
	}

	public void setProductGroup(String productGroup) {
		this.productGroup = productGroup;
	}

}
