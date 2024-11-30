package com.suryoday.roaocpv.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tbl_br_masters")
public class BRNetMasters {
	@Column(name="ID")
	private String ID;
	@Id
	@Column(name="SubCodeID")
	private String SubCodeID;
	@Column(name="Description")
	private String Description;
	@Column(name="DisplayOrder")
	private int DisplayOrder;
	@Column(name="IsDefault")
	private String IsDefault;
	@Column(name="IsActive")
	private String IsActive;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getSubCodeID() {
		return SubCodeID;
	}
	public void setSubCodeID(String subCodeID) {
		SubCodeID = subCodeID;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public int getDisplayOrder() {
		return DisplayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		DisplayOrder = displayOrder;
	}
	public String getIsDefault() {
		return IsDefault;
	}
	public void setIsDefault(String isDefault) {
		IsDefault = isDefault;
	}
	public String getIsActive() {
		return IsActive;
	}
	public void setIsActive(String isActive) {
		IsActive = isActive;
	}
	@Override
	public String toString() {
		return "BRNetMasters [ID=" + ID + ", SubCodeID=" + SubCodeID + ", Description=" + Description
				+ ", DisplayOrder=" + DisplayOrder + ", IsDefault=" + IsDefault + ", IsActive=" + IsActive + "]";
	}
	
	
}
