package com.suryoday.connector.pojo;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="connector_images")
public class ConnectorImages {
	
	@Id
	@Column(name="id")
	private int id;
	
	@Column(name="application_no")
	private Long  applicationNo;
	
	@Lob
	@Column(name="images")
	private byte[] images;
	
	@Column(name="flow_status")
	private String flowStatus;
	
	@Column(name="type")
    private String type;
	
	@Column(name="imageSize")
	private long Size;
	
	@Column(name="document_type")
	private String documenttype;
	
	
	@Column(name="geo_location",length = 500)
	private String GeoLocation;
	
	@Column(name="member")
	private String member;
	
	@Column(name="version_code",nullable = true)
	private int versioncode;
	
	@Column(name="image_name")
	private String imageName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Long getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(Long applicationNo) {
		this.applicationNo = applicationNo;
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

	public int getVersioncode() {
		return versioncode;
	}

	public void setVersioncode(int versioncode) {
		this.versioncode = versioncode;
	}
	
	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	@Override
	public String toString() {
		return "ConnectorImages [id=" + id + ", applicationNo=" + applicationNo + ", images=" + Arrays.toString(images)
				+ ", flowStatus=" + flowStatus + ", type=" + type + ", Size=" + Size + ", documenttype=" + documenttype
				+ ", GeoLocation=" + GeoLocation + ", member=" + member + ", versioncode=" + versioncode + ", imageName=" +imageName+ "]";
	}
}
