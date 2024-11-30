package com.suryoday.aocpv.pojo;

public class GeoLcation {

	private String image;
	private String lat;
	private String geoLong;
	private String address;
	private String timestamp;
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	
	public String getGeoLong() {
		return geoLong;
	}
	public void setGeoLong(String geoLong) {
		this.geoLong = geoLong;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public GeoLcation(String image, String lat, String geoLong, String address, String timestamp) {
		super();
		this.image = image;
		this.lat = lat;
		this.geoLong = geoLong;
		this.address = address;
		this.timestamp = timestamp;
	}
	public GeoLcation() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "GeoLcation [image=" + image + ", lat=" + lat + ", geoLong=" + geoLong + ", address=" + address
				+ ", timestamp=" + timestamp + "]";
	}
	
	
}
