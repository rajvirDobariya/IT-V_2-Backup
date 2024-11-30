package com.suryoday.twowheeler.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_tw_asset")
public class AssetMaster {
	@Id
	private String id;
	private String code;
	private String collateralType;
	private String manufactureCode;
	private String manufacturer;
	private String modelCode;
	private String model;
	private String variantCode;
	private String variant;
	private String fuelTypeCode;
	private String exShowroomPrice;
	private String roadTax;
	private String registrationCharges;
	private String fuelTypeDescription;
	private String assetProfile;
	private String totalOnRoadPriceAllocated;

	public String getTotalOnRoadPriceAllocated() {
		return totalOnRoadPriceAllocated;
	}

	public void setTotalOnRoadPriceAllocated(String totalOnRoadPriceAllocated) {
		this.totalOnRoadPriceAllocated = totalOnRoadPriceAllocated;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCollateralType() {
		return collateralType;
	}

	public void setCollateralType(String collateralType) {
		this.collateralType = collateralType;
	}

	public String getManufactureCode() {
		return manufactureCode;
	}

	public void setManufactureCode(String manufactureCode) {
		this.manufactureCode = manufactureCode;
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

	public String getVariantCode() {
		return variantCode;
	}

	public void setVariantCode(String variantCode) {
		this.variantCode = variantCode;
	}

	public String getVariant() {
		return variant;
	}

	public void setVariant(String variant) {
		this.variant = variant;
	}

	public String getFuelTypeCode() {
		return fuelTypeCode;
	}

	public void setFuelTypeCode(String fuelTypeCode) {
		this.fuelTypeCode = fuelTypeCode;
	}

	public String getExShowroomPrice() {
		return exShowroomPrice;
	}

	public void setExShowroomPrice(String exShowroomPrice) {
		this.exShowroomPrice = exShowroomPrice;
	}

	public String getRoadTax() {
		return roadTax;
	}

	public void setRoadTax(String roadTax) {
		this.roadTax = roadTax;
	}

	public String getRegistrationCharges() {
		return registrationCharges;
	}

	public void setRegistrationCharges(String registrationCharges) {
		this.registrationCharges = registrationCharges;
	}

	public String getFuelTypeDescription() {
		return fuelTypeDescription;
	}

	public void setFuelTypeDescription(String fuelTypeDescription) {
		this.fuelTypeDescription = fuelTypeDescription;
	}

	public String getAssetProfile() {
		return assetProfile;
	}

	public void setAssetProfile(String assetProfile) {
		this.assetProfile = assetProfile;
	}

	@Override
	public String toString() {
		return "AssetMaster [id=" + id + ", code=" + code + ", collateralType=" + collateralType + ", manufactureCode="
				+ manufactureCode + ", manufacturer=" + manufacturer + ", modelCode=" + modelCode + ", model=" + model
				+ ", variantCode=" + variantCode + ", variant=" + variant + ", fuelTypeCode=" + fuelTypeCode
				+ ", exShowroomPrice=" + exShowroomPrice + ", roadTax=" + roadTax + ", registrationCharges="
				+ registrationCharges + ", fuelTypeDescription=" + fuelTypeDescription + ", assetProfile="
				+ assetProfile + "]";
	}

}
