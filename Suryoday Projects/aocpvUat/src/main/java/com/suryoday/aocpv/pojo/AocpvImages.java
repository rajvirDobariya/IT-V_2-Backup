package com.suryoday.aocpv.pojo;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class AocpvImages {

	// @GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private int id;
	private String imageName;
	@Lob
	private byte[] images;
	private String flowStatus;
	private long applicationNo;
	private String type;
	private long Size;
	private String documenttype;
	@Column(length = 500)
	private String GeoLocation;
	private String member;
	@Column(nullable = true)
	private int versioncode;

	public int getVersioncode() {
		return versioncode;
	}

	public void setVersioncode(int versioncode) {
		this.versioncode = versioncode;
	}

	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
	}

	public AocpvImages() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the imageName
	 */
	public String getImageName() {
		return imageName;
	}

	/**
	 * @param imageName the imageName to set
	 */
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	/**
	 * @return the images
	 */
	public byte[] getImages() {
		return images;
	}

	/**
	 * @param images the images to set
	 */
	public void setImages(byte[] images) {
		this.images = images;
	}

	/**
	 * @return the flowStatus
	 */
	public String getFlowStatus() {
		return flowStatus;
	}

	/**
	 * @param flowStatus the flowStatus to set
	 */
	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}

	/**
	 * @return the applicationNo
	 */
	public long getApplicationNo() {
		return applicationNo;
	}

	/**
	 * @param applicationNo the applicationNo to set
	 */
	public void setApplicationNo(long applicationNo) {
		this.applicationNo = applicationNo;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the size
	 */
	public long getSize() {
		return Size;
	}

	/**
	 * @param l the size to set
	 */
	public void setSize(long l) {
		Size = l;
	}

	/**
	 * @return the documenttype
	 */
	public String getDocumenttype() {
		return documenttype;
	}

	/**
	 * @param documenttype the documenttype to set
	 */
	public void setDocumenttype(String documenttype) {
		this.documenttype = documenttype;
	}

	/**
	 * @return the geoLocation
	 */
	public String getGeoLocation() {
		return GeoLocation;
	}

	/**
	 * @param geoLocation the geoLocation to set
	 */
	public void setGeoLocation(String geoLocation) {
		GeoLocation = geoLocation;
	}

	@Override
	public String toString() {
		return "AocpvImages [id=" + id + ", imageName=" + imageName + ", images=" + Arrays.toString(images)
				+ ", flowStatus=" + flowStatus + ", applicationNo=" + applicationNo + ", type=" + type + ", Size="
				+ Size + ", documenttype=" + documenttype + ", GeoLocation=" + GeoLocation + ", member=" + member + "]";
	}

}
