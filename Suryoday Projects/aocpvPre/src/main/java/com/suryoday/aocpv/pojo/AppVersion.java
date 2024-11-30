package com.suryoday.aocpv.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AppVersion {
	@Id
	private String versionCode;
	private String appId;
	private String appVersion;
	
	
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	@Override
	public String toString() {
		return "AppVersion [versionCode=" + versionCode + ", appId=" + appId + ", appVersion=" + appVersion + "]";
	}
	
	
}
