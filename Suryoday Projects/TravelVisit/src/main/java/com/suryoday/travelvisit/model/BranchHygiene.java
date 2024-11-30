package com.suryoday.travelvisit.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="travel_visit_brnach_hygiene")
public class BranchHygiene extends Auditable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String branchId;
	private String branchName;
	private String receiptBookCheck;
	private String dressCodeCheck;
	private String keysCheck;
	private String cashVerification;
	private String distoApplicationDownloadedByAll;

	private String remarks;

	@Lob
	private String image;	
    private String mapUrlLink;
	
	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReceiptBookCheck() {
		return receiptBookCheck;
	}

	public void setReceiptBookCheck(String receiptBookCheck) {
		this.receiptBookCheck = receiptBookCheck;
	}

	public String getDressCodeCheck() {
		return dressCodeCheck;
	}

	public void setDressCodeCheck(String dressCodeCheck) {
		this.dressCodeCheck = dressCodeCheck;
	}

	public String getKeysCheck() {
		return keysCheck;
	}

	public void setKeysCheck(String keysCheck) {
		this.keysCheck = keysCheck;
	}

	public String getCashVerification() {
		return cashVerification;
	}

	public void setCashVerification(String cashVerification) {
		this.cashVerification = cashVerification;
	}

	public String getDistoApplicationDownloadedByAll() {
		return distoApplicationDownloadedByAll;
	}

	public void setDistoApplicationDownloadedByAll(String distoApplicationDownloadedByAll) {
		this.distoApplicationDownloadedByAll = distoApplicationDownloadedByAll;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public String getMapUrlLink() {
		return mapUrlLink;
	}

	public void setMapUrlLink(String mapUrlLink) {
		this.mapUrlLink = mapUrlLink;
	}

	@Override
	public String toString() {
		return "BranchHygiene [id=" + id + ", receiptBookCheck=" + receiptBookCheck + ", dressCodeCheck="
				+ dressCodeCheck + ", keysCheck=" + keysCheck + ", cashVerification=" + cashVerification
				+ ", distoApplicationDownloadedByAll=" + distoApplicationDownloadedByAll + ", remarks=" + remarks
				+ ", image=" + image + "]";
	}

}
