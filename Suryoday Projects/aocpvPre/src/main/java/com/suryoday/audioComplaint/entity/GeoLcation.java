package com.suryoday.audioComplaint.entity;

public class GeoLcation {

	private String audio;
	private String lat;
	private String geoLong;
	private String address;
	private String timestamp;
	
	public String getAudio() {
		return audio;
	}
	public void setAudio(String audio) {
		this.audio = audio;
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
	public GeoLcation(String audio, String lat, String geoLong, String address, String timestamp) {
		super();
		this.audio = audio;
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
		return "GeoLcation [image=" + audio + ", lat=" + lat + ", geoLong=" + geoLong + ", address=" + address
				+ ", timestamp=" + timestamp + "]";
	}
	
	
}
