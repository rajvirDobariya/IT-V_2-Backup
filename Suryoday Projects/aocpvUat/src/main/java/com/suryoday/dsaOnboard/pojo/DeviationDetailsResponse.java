package com.suryoday.dsaOnboard.pojo;

public class DeviationDetailsResponse {

	private String name;
	private String member;
	private String nameMatch;
	private String faceMatch;
	private String nameMatchPercent;
	private String faceMatchPercent;
	private String ekycVerify;
	private String ekycDoneBy;
	private String gstNo;
	private String gstNoVerify;
	private String aadharNo;
	private boolean aadharNoVerify;
	private String panCardNo;
	private boolean panCardVerify;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
	}

	public String getNameMatch() {
		return nameMatch;
	}

	public void setNameMatch(String nameMatch) {
		this.nameMatch = nameMatch;
	}

	public String getFaceMatch() {
		return faceMatch;
	}

	public void setFaceMatch(String faceMatch) {
		this.faceMatch = faceMatch;
	}

	public String getNameMatchPercent() {
		return nameMatchPercent;
	}

	public void setNameMatchPercent(String nameMatchPercent) {
		this.nameMatchPercent = nameMatchPercent;
	}

	public String getFaceMatchPercent() {
		return faceMatchPercent;
	}

	public void setFaceMatchPercent(String faceMatchPercent) {
		this.faceMatchPercent = faceMatchPercent;
	}

	public String getEkycVerify() {
		return ekycVerify;
	}

	public void setEkycVerify(String ekycVerify) {
		this.ekycVerify = ekycVerify;
	}

	public String getEkycDoneBy() {
		return ekycDoneBy;
	}

	public void setEkycDoneBy(String ekycDoneBy) {
		this.ekycDoneBy = ekycDoneBy;
	}

	public String getGstNo() {
		return gstNo;
	}

	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}

	public String getGstNoVerify() {
		return gstNoVerify;
	}

	public void setGstNoVerify(String gstNoVerify) {
		this.gstNoVerify = gstNoVerify;
	}

	public String getAadharNo() {
		return aadharNo;
	}

	public void setAadharNo(String aadharNo) {
		this.aadharNo = aadharNo;
	}

	public boolean isAadharNoVerify() {
		return aadharNoVerify;
	}

	public void setAadharNoVerify(boolean aadharNoVerify) {
		this.aadharNoVerify = aadharNoVerify;
	}

	public String getPanCardNo() {
		return panCardNo;
	}

	public void setPanCardNo(String panCardNo) {
		this.panCardNo = panCardNo;
	}

	public boolean isPanCardVerify() {
		return panCardVerify;
	}

	public void setPanCardVerify(boolean panCardVerify) {
		this.panCardVerify = panCardVerify;
	}

	public DeviationDetailsResponse(String name, String member, String nameMatch, String faceMatch,
			String nameMatchPercent, String faceMatchPercent, String ekycVerify, String ekycDoneBy, String gstNo,
			String gstNoVerify, String aadharNo, boolean aadharNoVerify, String panCardNo, boolean panCardVerify) {
		super();
		this.name = name;
		this.member = member;
		this.nameMatch = nameMatch;
		this.faceMatch = faceMatch;
		this.nameMatchPercent = nameMatchPercent;
		this.faceMatchPercent = faceMatchPercent;
		this.ekycVerify = ekycVerify;
		this.ekycDoneBy = ekycDoneBy;
		this.gstNo = gstNo;
		this.gstNoVerify = gstNoVerify;
		this.aadharNo = aadharNo;
		this.aadharNoVerify = aadharNoVerify;
		this.panCardNo = panCardNo;
		this.panCardVerify = panCardVerify;
	}

}
