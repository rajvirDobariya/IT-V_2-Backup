package com.suryoday.audioComplaint.entity;

import java.time.LocalDateTime;

public class AudioComplaintResponse {

	private String audioName;
	private String type;
	private long size;
	private String audio;
	private GeoLcation geoLocation;
	private String complaintNo;
	private LocalDateTime creationDate;
	private LocalDateTime updatedDate;
	private String status;
	private String userId;
	private String remarks;
	private String branchId;
	
	
	public String getAudioName() {
		return audioName;
	}
	public void setAudioName(String audioName) {
		this.audioName = audioName;
	}
	public String getAudio() {
		return audio;
	}
	public void setAudio(String audio) {
		this.audio = audio;
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
	
	public GeoLcation getGeoLocation() {
		return geoLocation;
	}
	public void setGeoLocation(GeoLcation geoLocation) {
		this.geoLocation = geoLocation;
	}
	public String getComplaintNo() {
		return complaintNo;
	}
	public void setComplaintNo(String complaintNo) {
		this.complaintNo = complaintNo;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	
	
	
	
	
	
}
