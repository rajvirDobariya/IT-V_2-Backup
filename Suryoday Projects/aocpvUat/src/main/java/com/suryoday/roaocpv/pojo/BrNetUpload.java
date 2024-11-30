package com.suryoday.roaocpv.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name = "tbl_brnet_upload")
public class BrNetUpload {
	@Id
	private String clientId;
	private String fileName;
	private String isUploaded;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getIsUploaded() {
		return isUploaded;
	}

	public void setIsUploaded(String isUploaded) {
		this.isUploaded = isUploaded;
	}

	@Override
	public String toString() {
		return "BrNetUpload [clientId=" + clientId + ", fileName=" + fileName + ", isUploaded=" + isUploaded + "]";
	}

}
