package com.suryoday.twowheeler.pojo;

public class BsrDetailsResponse {

	private String state;
	private String district;
	private String city;
	private String occupation;
	private String loanPurpose;
	private String bussinessSegment;
	private String classificationAdvance;
	private String governmentSponsoredScheme;
	private String channelCode;
	private String populationCode;
	private String sanctionDepartment;

	public String getLoanPurpose() {
		return loanPurpose;
	}

	public void setLoanPurpose(String loanPurpose) {
		this.loanPurpose = loanPurpose;
	}

	public String getBussinessSegment() {
		return bussinessSegment;
	}

	public void setBussinessSegment(String bussinessSegment) {
		this.bussinessSegment = bussinessSegment;
	}

	public String getClassificationAdvance() {
		return classificationAdvance;
	}

	public void setClassificationAdvance(String classificationAdvance) {
		this.classificationAdvance = classificationAdvance;
	}

	public String getGovernmentSponsoredScheme() {
		return governmentSponsoredScheme;
	}

	public void setGovernmentSponsoredScheme(String governmentSponsoredScheme) {
		this.governmentSponsoredScheme = governmentSponsoredScheme;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getPopulationCode() {
		return populationCode;
	}

	public void setPopulationCode(String populationCode) {
		this.populationCode = populationCode;
	}

	public String getSanctionDepartment() {
		return sanctionDepartment;
	}

	public void setSanctionDepartment(String sanctionDepartment) {
		this.sanctionDepartment = sanctionDepartment;
	}

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
