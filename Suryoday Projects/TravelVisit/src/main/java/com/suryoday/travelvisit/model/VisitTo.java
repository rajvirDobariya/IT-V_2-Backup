package com.suryoday.travelvisit.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "travel_visit_visit_to")
public class VisitTo extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String cifNo;
	private String name;
	private String address;
	private String phoneNumber;
	private String alternativePhoneNumber;
	private String product;
	private String centerReport;
	private String remarks;
	@Lob
	private String image;
	private String mapUrlLink;

	private Long roId;

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

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getCenterReport() {
		return centerReport;
	}

	public void setCenterReport(String centerReport) {
		this.centerReport = centerReport;
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
	
	public Long getRoId() {
		return roId;
	}

	public void setRoId(Long roId) {
		this.roId = roId;
	}

	@Override
	public String toString() {
		return "VisitTo [id=" + id + ", cifNo=" + cifNo + ", name=" + name + ", address=" + address + ", phoneNumber="
				+ phoneNumber + ", alternativePhoneNumber=" + alternativePhoneNumber + ", product=" + product
				+ ", centerReport=" + centerReport + ", remarks=" + remarks + ", image=" + image + ", mapUrlLink="
				+ mapUrlLink + "]";
	}

}
