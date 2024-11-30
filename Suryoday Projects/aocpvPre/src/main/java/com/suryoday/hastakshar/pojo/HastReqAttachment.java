package com.suryoday.hastakshar.pojo;

import java.time.LocalDateTime;
import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "hastakshar_Attachment")
public class HastReqAttachment {

//	@Column(nullable = false, unique = true)
	@Id
	private String id;
	private String applictioNO;
	private String attachmentType;
	private String attachmentName;
	private String latitude;
	private String longitude;
	private LocalDateTime createDate;
	@Lob
	private byte[] attachment;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplictioNO() {
		return applictioNO;
	}

	public void setApplictioNO(String applictioNO) {
		this.applictioNO = applictioNO;
	}

	public String getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
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

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public byte[] getAttachment() {
		return attachment;
	}

	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}

	@Override
	public String toString() {
		return "HastReqAttachment [applictioNO=" + applictioNO + ", attachmentType=" + attachmentType
				+ ", attachmentName=" + attachmentName + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", createDate=" + createDate + ", attachment=" + Arrays.toString(attachment) + "]";
	}
}