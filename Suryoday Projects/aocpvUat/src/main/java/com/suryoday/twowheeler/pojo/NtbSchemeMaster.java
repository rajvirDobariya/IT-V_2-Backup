package com.suryoday.twowheeler.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class NtbSchemeMaster {

	@Id
	private String Id;
	private String SchemeCode;
	private String SchemeDescription;
	private String startDate;
	private String endDate;
	private String laMin;
	private String laMax;
	private String tenureMin;
	private String tenureMax;
	private String roiMin;
	private String roiMax;
	private String ProcessingFee;
	private String DocumentationCharges;
	private String InsuranceCharge;
	private String effectiveIrr;

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getSchemeCode() {
		return SchemeCode;
	}

	public void setSchemeCode(String schemeCode) {
		SchemeCode = schemeCode;
	}

	public String getSchemeDescription() {
		return SchemeDescription;
	}

	public void setSchemeDescription(String schemeDescription) {
		SchemeDescription = schemeDescription;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getLaMin() {
		return laMin;
	}

	public void setLaMin(String laMin) {
		this.laMin = laMin;
	}

	public String getLaMax() {
		return laMax;
	}

	public void setLaMax(String laMax) {
		this.laMax = laMax;
	}

	public String getTenureMin() {
		return tenureMin;
	}

	public void setTenureMin(String tenureMin) {
		this.tenureMin = tenureMin;
	}

	public String getTenureMax() {
		return tenureMax;
	}

	public void setTenureMax(String tenureMax) {
		this.tenureMax = tenureMax;
	}

	public String getRoiMin() {
		return roiMin;
	}

	public void setRoiMin(String roiMin) {
		this.roiMin = roiMin;
	}

	public String getRoiMax() {
		return roiMax;
	}

	public void setRoiMax(String roiMax) {
		this.roiMax = roiMax;
	}

	public String getProcessingFee() {
		return ProcessingFee;
	}

	public void setProcessingFee(String processingFee) {
		ProcessingFee = processingFee;
	}

	public String getDocumentationCharges() {
		return DocumentationCharges;
	}

	public void setDocumentationCharges(String documentationCharges) {
		DocumentationCharges = documentationCharges;
	}

	public String getInsuranceCharge() {
		return InsuranceCharge;
	}

	public void setInsuranceCharge(String insuranceCharge) {
		InsuranceCharge = insuranceCharge;
	}

	public String getEffectiveIrr() {
		return effectiveIrr;
	}

	public void setEffectiveIrr(String effectiveIrr) {
		this.effectiveIrr = effectiveIrr;
	}

}
