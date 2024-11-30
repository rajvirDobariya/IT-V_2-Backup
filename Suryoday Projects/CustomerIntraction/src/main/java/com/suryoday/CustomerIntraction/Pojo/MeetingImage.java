package com.suryoday.CustomerIntraction.Pojo;

import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "meeting_image")
public class MeetingImage {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;
	@Lob
	private byte[] image;
	private String type;
	private String meetingNumber;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMeetingNumber() {
		return meetingNumber;
	}

	public void setMeetingNumber(String meetingNumber) {
		this.meetingNumber = meetingNumber;
	}

	@Override
	public String toString() {
		return "MeetingImage [id=" + id + ", image=" + Arrays.toString(image) + ", type=" + type + ", meetingNumber="
				+ meetingNumber + "]";
	}

}
