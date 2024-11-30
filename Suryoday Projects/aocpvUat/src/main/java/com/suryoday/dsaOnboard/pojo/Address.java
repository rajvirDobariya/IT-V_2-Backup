package com.suryoday.dsaOnboard.pojo;

public class Address {
	private String addressType;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String addressLat;
	private String addressLong;
	private String pincode;
	private String city;
	private String district;
	private String state;

	public String getAddressLat() {
		return addressLat;
	}

	public void setAddressLat(String addressLat) {
		this.addressLat = addressLat;
	}

	public String getAddressLong() {
		return addressLong;
	}

	public void setAddressLong(String addressLong) {
		this.addressLong = addressLong;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
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

	@Override
	public String toString() {
		return "Address [addressType=" + addressType + ", addressLine1=" + addressLine1 + ", addressLine2="
				+ addressLine2 + ", addressLine3=" + addressLine3 + ", pincode=" + pincode + ", city=" + city
				+ ", district=" + district + ", state=" + state + "]";
	}

}
