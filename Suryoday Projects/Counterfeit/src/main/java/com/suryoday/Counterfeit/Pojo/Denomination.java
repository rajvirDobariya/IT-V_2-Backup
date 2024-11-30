package com.suryoday.Counterfeit.Pojo;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "counterfeit_denomination")
public class Denomination extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	private LocalDate detectDate;

	private Long counterfeitId;

	private Integer denominationNote;

	private String tendererAccountNumber;

	private String tendererCustomerName;

	private String serialNumber;

	private String securityFeatureBreached;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCounterfeitId() {
		return counterfeitId;
	}

	public void setCounterfeitId(Long counterfeitId) {
		this.counterfeitId = counterfeitId;
	}

	public String getTendererCustomerName() {
		return tendererCustomerName;
	}

	public void setTendererCustomerName(String tendererCustomerName) {
		this.tendererCustomerName = tendererCustomerName;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getSecurityFeatureBreached() {
		return securityFeatureBreached;
	}

	public void setSecurityFeatureBreached(String securityFeatureBreached) {
		this.securityFeatureBreached = securityFeatureBreached;
	}

	public Integer getDenominationNote() {
		return denominationNote;
	}

	public void setDenominationNote(Integer denominationNote) {
		this.denominationNote = denominationNote;
	}

	public LocalDate getDetectDate() {
		return detectDate;
	}

	public void setDetectDate(LocalDate detectDate) {
		this.detectDate = detectDate;
	}

	public String getTendererAccountNumber() {
		return tendererAccountNumber;
	}

	public void setTendererAccountNumber(String tendererAccountNumber) {
		this.tendererAccountNumber = tendererAccountNumber;
	}

}
