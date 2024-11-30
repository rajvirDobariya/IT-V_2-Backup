package com.suryoday.collections.pojo;

public class ReceiptImageResponse {
	
	private String images;
	private String receiptNo;
	private String customerID;
	private String customerImage;
	private String latitude;
	private String longitude;
	private String address;
	private String timestamp;
	public String getImages() {
		return images;
	}
	public void setImages(String images) {
		this.images = images;
	}
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	public String getCustomerImage() {
		return customerImage;
	}
	public void setCustomerImage(String customerImage) {
		this.customerImage = customerImage;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public ReceiptImageResponse(String images, String receiptNo, String customerID, String customerImage,
			String latitude, String longitude, String address, String timestamp) {
		super();
		this.images = images;
		this.receiptNo = receiptNo;
		this.customerID = customerID;
		this.customerImage = customerImage;
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
		this.timestamp = timestamp;
	}
	public ReceiptImageResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "ReceiptImageResponse [images=" + images + ", receiptNo=" + receiptNo + ", customerID=" + customerID
				+ ", customerImage=" + customerImage + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", address=" + address + ", timestamp=" + timestamp + "]";
	}
	
	
}
	