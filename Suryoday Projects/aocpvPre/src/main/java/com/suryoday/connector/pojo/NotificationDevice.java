package com.suryoday.connector.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_notificatiion_devices")
public class NotificationDevice {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Long id;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "device_id")
	private String deviceId;

	@Column(name = "server_token")
	private String serverToken;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getServerToken() {
		return serverToken;
	}

	public void setServerToken(String serverToken) {
		this.serverToken = serverToken;
	}

	@Override
	public String toString() {
		return "NotificationDevice [id=" + id + ", userId=" + userId + ", deviceId=" + deviceId + ", serverToken="
				+ serverToken + "]";
	}

	

}
