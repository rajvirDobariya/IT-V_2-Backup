package com.suryoday.twowheeler.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "twoWheelerImage_table")
public class TwowheelerImage {

	@Id
	private int id;
	private String imageName;
	@Lob
	private byte[] images;
	private String flowStatus;
	private String applicationNo;
    private String type;
	private long Size;
	private String documenttype;
	@Column(length = 500)
	private String GeoLocation;
	private String member;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public byte[] getImages() {
		return images;
	}
	public void setImages(byte[] images) {
		this.images = images;
	}
	public String getFlowStatus() {
		return flowStatus;
	}
	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}
	public String getApplicationNo() {
		return applicationNo;
	}
	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getSize() {
		return Size;
	}
	public void setSize(long size) {
		Size = size;
	}
	public String getDocumenttype() {
		return documenttype;
	}
	public void setDocumenttype(String documenttype) {
		this.documenttype = documenttype;
	}
	public String getGeoLocation() {
		return GeoLocation;
	}
	public void setGeoLocation(String geoLocation) {
		GeoLocation = geoLocation;
	}
	public String getMember() {
		return member;
	}
	public void setMember(String member) {
		this.member = member;
	}
	
	
	
}
