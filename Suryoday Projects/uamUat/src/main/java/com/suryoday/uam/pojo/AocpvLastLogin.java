package com.suryoday.uam.pojo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AocpvLastLogin {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String userId;
	private String AppId;
	private String DeviceId;
	private String RequestKey;
	private String SesionId;
	private String lastRequestTime;
	private LocalDateTime loginTime;
	private LocalDateTime logoutTime;
	private String versionNo;
	private String sourceIp;
	private String transResponse;
	private String transStatus;
	private String latitude;
	private String longitude;
	
	
	public String getTransResponse() {
		return transResponse;
	}
	public void setTransResponse(String transResponse) {
		this.transResponse = transResponse;
	}
	public String getTransStatus() {
		return transStatus;
	}
	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
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
	public String getSourceIp() {
		return sourceIp;
	}
	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAppId() {
		return AppId;
	}
	public void setAppId(String appId) {
		AppId = appId;
	}
	public String getDeviceId() {
		return DeviceId;
	}
	public void setDeviceId(String deviceId) {
		DeviceId = deviceId;
	}
	public String getRequestKey() {
		return RequestKey;
	}
	public void setRequestKey(String requestKey) {
		RequestKey = requestKey;
	}
	public String getSesionId() {
		return SesionId;
	}
	public void setSesionId(String sesionId) {
		SesionId = sesionId;
	}
	public String getLastRequestTime() {
		return lastRequestTime;
	}
	public void setLastRequestTime(String lastRequestTime) {
		this.lastRequestTime = lastRequestTime;
	}
	public LocalDateTime getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(LocalDateTime loginTime) {
		this.loginTime = loginTime;
	}
	public LocalDateTime getLogoutTime() {
		return logoutTime;
	}
	public void setLogoutTime(LocalDateTime logoutTime) {
		this.logoutTime = logoutTime;
	}
	public String getVersionNo() {
		return versionNo;
	}
	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}
	@Override
	public String toString() {
		return "AocpvLastLogin [id=" + id + ", userId=" + userId + ", AppId=" + AppId + ", DeviceId=" + DeviceId
				+ ", RequestKey=" + RequestKey + ", SesionId=" + SesionId + ", lastRequestTime=" + lastRequestTime
				+ ", loginTime=" + loginTime + ", logoutTime=" + logoutTime + ", versionNo=" + versionNo + "]";
	}
	
}