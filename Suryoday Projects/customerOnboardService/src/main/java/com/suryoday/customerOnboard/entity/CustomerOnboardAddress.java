package com.suryoday.customerOnboard.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class CustomerOnboardAddress {

	@Id()
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String addressType;
	private String address_Line1;
	private String address_Line2;
	private String address_Line3;
	private String city;
	private String pincode;
	private String district;
	private String state;
	private String country;
	private String landmark;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAddressType() {
		return addressType;
	}
	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}
	public String getAddress_Line1() {
		return address_Line1;
	}
	public void setAddress_Line1(String address_Line1) {
		this.address_Line1 = address_Line1;
	}
	public String getAddress_Line2() {
		return address_Line2;
	}
	public void setAddress_Line2(String address_Line2) {
		this.address_Line2 = address_Line2;
	}
	public String getAddress_Line3() {
		return address_Line3;
	}
	public void setAddress_Line3(String address_Line3) {
		this.address_Line3 = address_Line3;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getLandmark() {
		return landmark;
	}
	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}
	@Override
	public String toString() {
		return "CustomerOnboardAddress [id=" + id + ", addressType=" + addressType + ", address_Line1=" + address_Line1
				+ ", address_Line2=" + address_Line2 + ", address_Line3=" + address_Line3 + ", city=" + city
				+ ", pincode=" + pincode + ", district=" + district + ", state=" + state + ", country=" + country
				+ ", landmark=" + landmark + "]";
	}
	
	
}
