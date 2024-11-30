package com.suryoday.roaocpv.pojo;

public class Address {

	private String AddressType;
	private String address_Line1;
	private String address_Line2;
	private String pincode;
	private String city;
	private String district;
	private String state;
	private String landmark;
	private String address_Line3;
	
	
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
	public String getAddressType() {
		return AddressType;
	}
	public void setAddressType(String addressType) {
		AddressType = addressType;
	}
	
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
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
	public String getLandmark() {
		return landmark;
	}
	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}
	public Address(String addressType, String address_Line1, String address_Line2, String pincode, String city, String district,
			String state, String landmark, String address_Line3) {
		super();
		AddressType = addressType;
		this.address_Line1 = address_Line1;
		this.address_Line2 = address_Line2;
		this.pincode = pincode;
		this.city = city;
		this.district = district;
		this.state = state;
		this.landmark = landmark;
		this.address_Line3 = address_Line3;
	}
	public Address() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
