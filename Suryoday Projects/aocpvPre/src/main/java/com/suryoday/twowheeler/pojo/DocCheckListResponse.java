package com.suryoday.twowheeler.pojo;

import java.util.Objects;

public class DocCheckListResponse {
	private String documentType;
	private String documentCount;
	private String documentPresent;
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public String getDocumentCount() {
		return documentCount;
	}
	public void setDocumentCount(String documentCount) {
		this.documentCount = documentCount;
	}
	public String getDocumentPresent() {
		return documentPresent;
	}
	public void setDocumentPresent(String documentPresent) {
		this.documentPresent = documentPresent;
	}
	public DocCheckListResponse(String documentType, String documentCount, String documentPresent) {
		super();
		this.documentType = documentType;
		this.documentCount = documentCount;
		this.documentPresent = documentPresent;
	}
	public DocCheckListResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(documentType);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocCheckListResponse other = (DocCheckListResponse) obj;
		return Objects.equals(documentType, other.documentType);
	}
}
