package com.suryoday.dsaOnboard.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="dsa_payout_scemeMaster")
public class PayoutSchemeMaster {
	@Id
	@Column(name="Id")
	private String id;
	
	@Column(name="Product")
	private String product;
	
	@Column(name="Agency_type")
	private String agencyType;
	
	@Column(name="Payout_frequency")
	private String payoutFrequency;
	
	@Column(name="Year")
	private String year;
	
	@Column(name="Scheme_code")
	private String schemeCode;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getAgencyType() {
		return agencyType;
	}

	public void setAgencyType(String agencyType) {
		this.agencyType = agencyType;
	}

	public String getPayoutFrequency() {
		return payoutFrequency;
	}

	public void setPayoutFrequency(String payoutFrequency) {
		this.payoutFrequency = payoutFrequency;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getSchemeCode() {
		return schemeCode;
	}

	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}

	@Override
	public String toString() {
		return "PayoutSchemeMaster [id=" + id + ", product=" + product + ", agencyType=" + agencyType
				+ ", payoutFrequency=" + payoutFrequency + ", year=" + year + ", schemeCode=" + schemeCode + "]";
	}
	
	
}
