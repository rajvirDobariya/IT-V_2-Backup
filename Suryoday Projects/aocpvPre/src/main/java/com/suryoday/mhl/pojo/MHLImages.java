package com.suryoday.mhl.pojo;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity(name = "mhl_images")
public class MHLImages {
	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private int id;
	private String imageName;
	@Lob
	private byte[] images;
	private String status;
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
		this.imageName = imageName.toUpperCase();
	}
	public byte[] getImages() {
		return images;
	}
	public void setImages(byte[] images) {
		this.images = images;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status.toUpperCase();
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
		this.type = type.toUpperCase();
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
		this.documenttype = documenttype.toUpperCase();
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
		this.member = member.toUpperCase();
	}
	@Override
	public String toString() {
		return "MHLImages [id=" + id + ", imageName=" + imageName + ", images=" + Arrays.toString(images) + ", status="
				+ status + ", applicationNo=" + applicationNo + ", type=" + type + ", Size=" + Size + ", documenttype="
				+ documenttype + ", GeoLocation=" + GeoLocation + ", member=" + member + "]";
	}
	
}
