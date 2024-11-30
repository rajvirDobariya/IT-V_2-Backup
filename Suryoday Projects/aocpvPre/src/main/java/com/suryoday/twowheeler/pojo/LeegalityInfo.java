package com.suryoday.twowheeler.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class LeegalityInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String applicationNo;
	private String Active;
	private String Email;
	private String Expired;
	private String ExpiryDate;
	private String Name;
	private String Phone;
	private String Rejected;
	private String SignType;
	private String SignUrl;
	private String Signed;
	private String documentId;
	private String documentType;
	
	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getActive() {
		return Active;
	}

	public void setActive(String active) {
		Active = active;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getExpired() {
		return Expired;
	}

	public void setExpired(String expired) {
		Expired = expired;
	}

	public String getExpiryDate() {
		return ExpiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		ExpiryDate = expiryDate;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getRejected() {
		return Rejected;
	}

	public void setRejected(String rejected) {
		Rejected = rejected;
	}

	public String getSignType() {
		return SignType;
	}

	public void setSignType(String signType) {
		SignType = signType;
	}

	public String getSignUrl() {
		return SignUrl;
	}

	public void setSignUrl(String signUrl) {
		SignUrl = signUrl;
	}

	public String getSigned() {
		return Signed;
	}

	public void setSigned(String signed) {
		Signed = signed;
	}

	public LeegalityInfo(String applicationNo, String active, String email, String expiryDate, String name,
			String phone, String signUrl,String documentId,String Signed,String documentType) {
		super();
		this.applicationNo = applicationNo;
		Active = active;
		Email = email;
		ExpiryDate = expiryDate;
		Name = name;
		Phone = phone;
		SignUrl = signUrl;
		this.documentId = documentId;
		this.Signed = Signed;
		this.documentType = documentType;
	}

	public LeegalityInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}
