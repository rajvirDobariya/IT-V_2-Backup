package com.suryoday.twowheeler.pojo;

public class BsrDetailsResponse {

	private String state;
	private String district;
	private String city;
	private String occupation;
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	@Override
	public String toString() {
		return "BsrDetailsResponse [state=" + state + ", district=" + district + ", city=" + city + ", occupation="
				+ occupation + "]";
	}
	
	
}
