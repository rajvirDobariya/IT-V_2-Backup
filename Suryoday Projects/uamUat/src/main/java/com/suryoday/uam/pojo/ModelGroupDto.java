package com.suryoday.uam.pojo;

public class ModelGroupDto {
	
private String modelGroupName;
	
	
	private Long isActive;


	public String getModelGroupName() {
		return modelGroupName;
	}


	public void setModelGroupName(String modelGroupName) {
		this.modelGroupName = modelGroupName;
	}


	public Long getIsActive() {
		return isActive;
	}


	public void setIsActive(Long isActive) {
		this.isActive = isActive;
	}


	@Override
	public String toString() {
		return "ModelGroupDto [modelGroupName=" + modelGroupName + ", isActive=" + isActive + "]";
	}


	



}
