package com.suryoday.aocpv.pojo;

public class Image {

	private String documenttype;
	private String imageName;
	private String type;
	private long size;
	private String image;
	private GeoLcation geoLocation;
	private String member;

	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
	}

	public String getDocumenttype() {
		return documenttype;
	}

	public void setDocumenttype(String documenttype) {
		this.documenttype = documenttype;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public GeoLcation getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(GeoLcation geoLocation) {
		this.geoLocation = geoLocation;
	}

	public Image(String documenttype, String imageName, String type, long size, String image, String member,
			GeoLcation geoLocation) {
		super();
		this.documenttype = documenttype;
		this.imageName = imageName;
		this.type = type;
		this.size = size;
		this.image = image;
		this.geoLocation = geoLocation;
		this.member = member;
	}

	public Image() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Image [documenttype=" + documenttype + ", imageName=" + imageName + ", type=" + type + ", size=" + size
				+ ", image=" + image + ", geoLocation=" + geoLocation + ", member=" + member + "]";
	}

}