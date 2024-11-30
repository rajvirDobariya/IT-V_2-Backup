package com.suryoday.mhl.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "TBL_COLLATER_DETAIL ")
public class CollateralDetails {

	@Id
	private String applicationNo;
	private String identificationOfProperty;
	private String propertyType;
	private String AgeOfBuilding;
	private String propertyClassification;
	private String ValueOfProperty;
	private String AreaOfProperty;
	private String DetailsOfSite;
	private String typeOfLoan;
	private String nameOfPropertyHolders;
	private String yearOfPurchase;
	private String purchasedValue;
	private String currentMarketValue;
	private String builtUpArea;
	private String builtUpEstimate;
	private String loanDetails;
	public String getApplicationNo() {
		return applicationNo;
	}
	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}
	public String getIdentificationOfProperty() {
		return identificationOfProperty;
	}
	public void setIdentificationOfProperty(String identificationOfProperty) {
		this.identificationOfProperty = identificationOfProperty.toUpperCase();
	}
	public String getPropertyType() {
		return propertyType;
	}
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType.toUpperCase();
	}
	public String getAgeOfBuilding() {
		return AgeOfBuilding;
	}
	public void setAgeOfBuilding(String ageOfBuilding) {
		AgeOfBuilding = ageOfBuilding.toUpperCase();
	}
	public String getPropertyClassification() {
		return propertyClassification;
	}
	public void setPropertyClassification(String propertyClassification) {
		this.propertyClassification = propertyClassification.toUpperCase();
	}
	public String getValueOfProperty() {
		return ValueOfProperty;
	}
	public void setValueOfProperty(String valueOfProperty) {
		ValueOfProperty = valueOfProperty.toUpperCase();
	}
	public String getAreaOfProperty() {
		return AreaOfProperty;
	}
	public void setAreaOfProperty(String areaOfProperty) {
		AreaOfProperty = areaOfProperty.toUpperCase();
	}
	public String getDetailsOfSite() {
		return DetailsOfSite;
	}
	public void setDetailsOfSite(String detailsOfSite) {
		DetailsOfSite = detailsOfSite.toUpperCase();
	}
	public String getTypeOfLoan() {
		return typeOfLoan;
	}
	public void setTypeOfLoan(String typeOfLoan) {
		this.typeOfLoan = typeOfLoan.toUpperCase();
	}
	
	public String getNameOfPropertyHolders() {
		return nameOfPropertyHolders;
	}
	public void setNameOfPropertyHolders(String nameOfPropertyHolders) {
		this.nameOfPropertyHolders = nameOfPropertyHolders.toUpperCase();
	}
	public String getYearOfPurchase() {
		return yearOfPurchase;
	}
	public void setYearOfPurchase(String yearOfPurchase) {
		this.yearOfPurchase = yearOfPurchase.toUpperCase();
	}
	public String getPurchasedValue() {
		return purchasedValue;
	}
	public void setPurchasedValue(String purchasedValue) {
		this.purchasedValue = purchasedValue.toUpperCase();
	}
	public String getCurrentMarketValue() {
		return currentMarketValue;
	}
	public void setCurrentMarketValue(String currentMarketValue) {
		this.currentMarketValue = currentMarketValue.toUpperCase();
	}
	public String getBuiltUpArea() {
		return builtUpArea;
	}
	public void setBuiltUpArea(String builtUpArea) {
		this.builtUpArea = builtUpArea.toUpperCase();
	}
	public String getBuiltUpEstimate() {
		return builtUpEstimate;
	}
	public void setBuiltUpEstimate(String builtUpEstimate) {
		this.builtUpEstimate = builtUpEstimate.toUpperCase();
	}
	public String getLoanDetails() {
		return loanDetails;
	}
	public void setLoanDetails(String loanDetails) {
		this.loanDetails = loanDetails.toUpperCase();
	}
	@Override
	public String toString() {
		return "CollateralDetails [applicationNo=" + applicationNo + ", identificationOfProperty="
				+ identificationOfProperty + ", propertyType=" + propertyType + ", AgeOfBuilding=" + AgeOfBuilding
				+ ", propertyClassification=" + propertyClassification + ", ValueOfProperty=" + ValueOfProperty
				+ ", AreaOfProperty=" + AreaOfProperty + ", DetailsOfSite=" + DetailsOfSite + ", typeOfLoan="
				+ typeOfLoan + ", nameOfPropertyHolders=" + nameOfPropertyHolders + ", yearOfPurchase=" + yearOfPurchase
				+ ", purchasedValue=" + purchasedValue + ", currentMarketValue=" + currentMarketValue + ", builtUpArea="
				+ builtUpArea + ", builtUpEstimate=" + builtUpEstimate + ", loanDetails=" + loanDetails + "]";
	}
	
	
	
}
