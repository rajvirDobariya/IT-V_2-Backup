package com.suryoday.connector.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_model_group")
public class ModelGroup implements Serializable{

	private static final long serialVersionUID = -1634369769740201125L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column(name="model_group_name")
	private String modelGroupName;
	
	@Column(name="is_active")
	private Long isActive;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "ModelGroup [id=" + id + ", modelGroupName=" + modelGroupName + ", isActive=" + isActive + "]";
	}

	

	
}
