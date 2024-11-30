package com.suryoday.familyMember.pojo;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class FamilyMemberImages {

	@Id
	private int id;
	private String imageName;
	@Lob
	private byte[] images;
	private String customerId;
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

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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

	@Override
	public String toString() {
		return "FamilyMemberImages [id=" + id + ", imageName=" + imageName + ", images=" + Arrays.toString(images)
				+ ", customerId=" + customerId + ", type=" + type + ", Size=" + Size + ", documenttype=" + documenttype
				+ ", GeoLocation=" + GeoLocation + ", member=" + member + "]";
	}

}
