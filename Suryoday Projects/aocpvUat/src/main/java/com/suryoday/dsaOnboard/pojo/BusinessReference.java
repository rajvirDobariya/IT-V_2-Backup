package com.suryoday.dsaOnboard.pojo;

public class BusinessReference {
	private String id;
	private String ServicesprovidedtoFI;
	private String nameOfReference;
	private String companyName;
	private String address;
	private String mobile;

	public String getServicesprovidedtoFI() {
		return ServicesprovidedtoFI;
	}

	public void setServicesprovidedtoFI(String servicesprovidedtoFI) {
		ServicesprovidedtoFI = servicesprovidedtoFI;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNameOfReference() {
		return nameOfReference;
	}

	public void setNameOfReference(String nameOfReference) {
		this.nameOfReference = nameOfReference;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
