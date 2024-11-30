package com.suryoday.twowheeler.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tbl_tw_model")
public class TWModelMaster {
	@Id
	private String id;
	private String variantId;
	private String manufacturerId;
	private String manufacturerName;
	private String modelId;
	private String modelName;
	private String variantName;
	private String fuelType;
	private String cc;
	private String vehicleCategory;
	private String cityId;
	private String cityName;
	private String onRoadPrice;
	private String variantPriceId;
	private String accessories;
	private String exShowroomPrice;
	private String insurancePrice;
	private String rtoCharges;
	private String extendedWarranty;
	private String handlingAndDocumentationCharges;
	private String tpInsurance;
	private String isActive;
	public String getVariantId() {
		return variantId;
	}
	public void setVariantId(String variantId) {
		this.variantId = variantId;
	}
	public String getManufacturerId() {
		return manufacturerId;
	}
	public void setManufacturerId(String manufacturerId) {
		this.manufacturerId = manufacturerId;
	}
	public String getManufacturerName() {
		return manufacturerName;
	}
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	public String getModelId() {
		return modelId;
	}
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getVariantName() {
		return variantName;
	}
	public void setVariantName(String variantName) {
		this.variantName = variantName;
	}
	public String getFuelType() {
		return fuelType;
	}
	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public String getVehicleCategory() {
		return vehicleCategory;
	}
	public void setVehicleCategory(String vehicleCategory) {
		this.vehicleCategory = vehicleCategory;
	}
	public String getOnRoadPrice() {
		return onRoadPrice;
	}
	public void setOnRoadPrice(String onRoadPrice) {
		this.onRoadPrice = onRoadPrice;
	}
	public String getVariantPriceId() {
		return variantPriceId;
	}
	public void setVariantPriceId(String variantPriceId) {
		this.variantPriceId = variantPriceId;
	}
	public String getAccessories() {
		return accessories;
	}
	public void setAccessories(String accessories) {
		this.accessories = accessories;
	}
	public String getExShowroomPrice() {
		return exShowroomPrice;
	}
	public void setExShowroomPrice(String exShowroomPrice) {
		this.exShowroomPrice = exShowroomPrice;
	}
	public String getInsurancePrice() {
		return insurancePrice;
	}
	public void setInsurancePrice(String insurancePrice) {
		this.insurancePrice = insurancePrice;
	}
	public String getRtoCharges() {
		return rtoCharges;
	}
	public void setRtoCharges(String rtoCharges) {
		this.rtoCharges = rtoCharges;
	}
	public String getExtendedWarranty() {
		return extendedWarranty;
	}
	public void setExtendedWarranty(String extendedWarranty) {
		this.extendedWarranty = extendedWarranty;
	}
	public String getHandlingAndDocumentationCharges() {
		return handlingAndDocumentationCharges;
	}
	public void setHandlingAndDocumentationCharges(String handlingAndDocumentationCharges) {
		this.handlingAndDocumentationCharges = handlingAndDocumentationCharges;
	}
	public String getTpInsurance() {
		return tpInsurance;
	}
	public void setTpInsurance(String tpInsurance) {
		this.tpInsurance = tpInsurance;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	@Override
	public String toString() {
		return "TWModelMaster [variantId=" + variantId + ", manufacturerId=" + manufacturerId + ", manufacturerName="
				+ manufacturerName + ", modelId=" + modelId + ", modelName=" + modelName + ", variantName="
				+ variantName + ", fuelType=" + fuelType + ", cc=" + cc + ", vehicleCategory=" + vehicleCategory
				+ ", onRoadPrice=" + onRoadPrice + ", variantPriceId=" + variantPriceId + ", accessories=" + accessories
				+ ", exShowroomPrice=" + exShowroomPrice + ", insurancePrice=" + insurancePrice + ", rtoCharges="
				+ rtoCharges + ", extendedWarranty=" + extendedWarranty + ", handlingAndDocumentationCharges="
				+ handlingAndDocumentationCharges + ", tpInsurance=" + tpInsurance + ", isActive=" + isActive + "]";
	}
	
	
	
}
