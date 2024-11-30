package com.suryoday.roaocpv.pojo;

import java.time.LocalDateTime;
import java.util.List;

import com.suryoday.aocpv.pojo.Image;

public class ApplicationDetailsResponseWeb {
private String name;
private String dob;
private String mobileNo;
private String appNoWithProductCode;
private String applicationNo;
private LocalDateTime creationDate;
private LocalDateTime updatedDate;
private String createdBy;
private String address;
private String city;
private String state;
private String gender;
private String pincode;
private List<Image> images;


public List<Image> getImages() {
	return images;
}
public void setImages(List<Image> images) {
	this.images = images;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}
public String getCity() {
	return city;
}
public void setCity(String city) {
	this.city = city;
}
public String getState() {
	return state;
}
public void setState(String state) {
	this.state = state;
}
public String getGender() {
	return gender;
}
public void setGender(String gender) {
	this.gender = gender;
}
public String getPincode() {
	return pincode;
}
public void setPincode(String pincode) {
	this.pincode = pincode;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getDob() {
	return dob;
}
public void setDob(String dob) {
	this.dob = dob;
}
public String getMobileNo() {
	return mobileNo;
}
public void setMobileNo(String mobileNo) {
	this.mobileNo = mobileNo;
}
public String getAppNoWithProductCode() {
	return appNoWithProductCode;
}
public void setAppNoWithProductCode(String appNoWithProductCode) {
	this.appNoWithProductCode = appNoWithProductCode;
}
public String getApplicationNo() {
	return applicationNo;
}
public void setApplicationNo(String applicationNo) {
	this.applicationNo = applicationNo;
}
public LocalDateTime getCreationDate() {
	return creationDate;
}
public void setCreationDate(LocalDateTime creationDate) {
	this.creationDate = creationDate;
}
public LocalDateTime getUpdatedDate() {
	return updatedDate;
}
public void setUpdatedDate(LocalDateTime updatedDate) {
	this.updatedDate = updatedDate;
}
public String getCreatedBy() {
	return createdBy;
}
public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
}


	
}
