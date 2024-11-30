package com.suryoday.aocpv.pojo;



public class Address {

	
		private String address_Line1;
		private String address_Line2;
		private String address_Line3;
		private String city;
		private String pincode;
		private String district;
		private String state;
		private String country;
		private String addressType;
		private String landmark;
		private String latitude;
		private String longitude;
		
		
		public String getLatitude() {
			return latitude;
		}
		public void setLatitude(String latitude) {
			this.latitude = latitude;
		}
		public String getLongitude() {
			return longitude;
		}
		public void setLongitude(String longitude) {
			this.longitude = longitude;
		}
		public String getAddressType() {
			return addressType;
		}
		public void setAddressType(String addressType) {
			this.addressType = addressType;
		}
		public String getLandmark() {
			return landmark;
		}
		public void setLandmark(String landmark) {
			this.landmark = landmark;
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
		public Address(String address_Line1, String address_Line2, String address_Line3, String city, String pincode,
				String district, String state, String country,String addressType,String landmark,String latitude,String longitude) {
			super();
			this.address_Line1 = address_Line1;
			this.address_Line2 = address_Line2;
			this.address_Line3 = address_Line3;
			this.city = city;
			this.pincode = pincode;
			this.district = district;
			this.state = state;
			this.country = country;
			this.addressType = addressType;
			this.landmark = landmark;
			this.latitude = latitude;
			this.longitude = longitude;
		}
		public Address() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		
}
