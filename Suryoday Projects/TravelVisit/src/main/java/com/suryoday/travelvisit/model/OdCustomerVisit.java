package com.suryoday.travelvisit.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "travel_visit_od_customer_visit")
public class OdCustomerVisit extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String cifNo;
	private String name;
	private String address;
	private String phoneNumber;
	private String alternativePhoneNumber;
	private String remarks;

	@Lob
	private String image;

	private String mapUrlLink;

	private String business;
	private String isCustomerAvailable;
	private String reason;
	private LocalDate ptpDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCifNo() {
		return cifNo;
	}

	public void setCifNo(String cifNo) {
		this.cifNo = cifNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAlternativePhoneNumber() {
		return alternativePhoneNumber;
	}

	public void setAlternativePhoneNumber(String alternativePhoneNumber) {
		this.alternativePhoneNumber = alternativePhoneNumber;
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

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public String getIsCustomerAvailable() {
		return isCustomerAvailable;
	}

	public void setIsCustomerAvailable(String isCustomerAvailable) {
		this.isCustomerAvailable = isCustomerAvailable;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getMapUrlLink() {
		return mapUrlLink;
	}

	public void setMapUrlLink(String mapUrlLink) {
		this.mapUrlLink = mapUrlLink;
	}

	public LocalDate getPtpDate() {
		return ptpDate;
	}

	public void setPtpDate(LocalDate ptpDate) {
		this.ptpDate = ptpDate;
	}

	@Override
	public String toString() {
		return "OdCustomerVisit [id=" + id + ", cifNo=" + cifNo + ", name=" + name + ", address=" + address
				+ ", phoneNumber=" + phoneNumber + ", alternativePhoneNumber=" + alternativePhoneNumber + ", remarks="
				+ remarks + ", image=" + image + ", mapUrlLink=" + mapUrlLink + ", business=" + business
				+ ", isCustomerAvailable=" + isCustomerAvailable + ", reason=" + reason + ", ptpDate=" + ptpDate + "]";
	}


}
