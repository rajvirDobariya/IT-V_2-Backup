package com.suryoday.roaocpv.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SURYODAY_MASTER")
public class ROAOCPVProductType {
	@Id
	@Column(name="id")
	private int id;
	private String productType;
	private String keys;
	private String value;
	private String createdBy;
	@Override
	public String toString() {
		return "ROAOCPVProductType [createdBy=" + createdBy + ", id=" + id + ", keys=" + keys + ", productType="
				+ productType + ", value=" + value + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getKeys() {
		return keys;
	}
	public void setKey(String keys) {
		this.keys = keys;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
}
