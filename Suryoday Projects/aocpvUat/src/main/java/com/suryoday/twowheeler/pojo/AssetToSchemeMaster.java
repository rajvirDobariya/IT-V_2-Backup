package com.suryoday.twowheeler.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AssetToSchemeMaster {

	@Id
	private String id;
	private String schemeCode;
	private String schemeDescription;
	private String creditStatusCode;
	private String creditStatusDescription;
	private String collateralTypeCode;
	private String collateralType;
	private String manufacturerCode;
	private String manufacturer;
	private String modelCode;
	private String model;
	private String fuelTypeCode;
	private String fuelTypeDescription;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		id = id;
	}

	public String getSchemeCode() {
		return schemeCode;
	}

	public void setSchemeCode(String schemeCode) {
		schemeCode = schemeCode;
	}

	public String getSchemeDescription() {
		return schemeDescription;
	}

	public void setSchemeDescription(String schemeDescription) {
		schemeDescription = schemeDescription;
	}

	public String getCreditStatusCode() {
		return creditStatusCode;
	}

	public void setCreditStatusCode(String creditStatusCode) {
		this.creditStatusCode = creditStatusCode;
	}

	public String getCreditStatusDescription() {
		return creditStatusDescription;
	}

	public void setCreditStatusDescription(String creditStatusDescription) {
		this.creditStatusDescription = creditStatusDescription;
	}

	public String getCollateralTypeCode() {
		return collateralTypeCode;
	}

	public void setCollateralTypeCode(String collateralTypeCode) {
		this.collateralTypeCode = collateralTypeCode;
	}

	public String getCollateralType() {
		return collateralType;
	}

	public void setCollateralType(String collateralType) {
		this.collateralType = collateralType;
	}

	public String getManufacturerCode() {
		return manufacturerCode;
	}

	public void setManufacturerCode(String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getFuelTypeCode() {
		return fuelTypeCode;
	}

	public void setFuelTypeCode(String fuelTypeCode) {
		this.fuelTypeCode = fuelTypeCode;
	}

	public String getFuelTypeDescription() {
		return fuelTypeDescription;
	}

	public void setFuelTypeDescription(String fuelTypeDescription) {
		this.fuelTypeDescription = fuelTypeDescription;
	}

}
