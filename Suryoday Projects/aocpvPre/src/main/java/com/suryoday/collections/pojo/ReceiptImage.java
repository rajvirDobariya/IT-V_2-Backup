package com.suryoday.collections.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "IMAGE_RECEIPT")
public class ReceiptImage {

	 @Id
	 @GeneratedValue
	 @Column(name = "id")
	private int id;
	 @Lob
	private byte[] images;
	private String receiptNo;
	private String customerID;
	private String customerImage;
	private String latitude;
	private String longitude;
	private String address;
	private String timestamp;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}	
	public byte[] getImages() {
		return images;
	}
	public void setImages(byte[] images) {
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
	
	@Override
	public String toString() {
		return "ReceiptImage [id=" + id + ", receiptNo=" + receiptNo + ", customerID=" + customerID + ", customerImage="
				+ customerImage + ", latitude=" + latitude + ", longitude=" + longitude + ", address=" + address
				+ ", timestamp=" + timestamp + ", images=" +images+ "]";
	}
	
}
