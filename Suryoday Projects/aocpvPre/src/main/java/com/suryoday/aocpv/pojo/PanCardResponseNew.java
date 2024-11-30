package com.suryoday.aocpv.pojo;

public class PanCardResponseNew {
	private String DateOfBirth;
	private String SeedingStatus;
	private String PanStatus;
	private String Pan;
	private String Name;
	private String FatherName;
	public String getDateOfBirth() {
		return DateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		DateOfBirth = dateOfBirth;
	}
	public String getSeedingStatus() {
		return SeedingStatus;
	}
	public void setSeedingStatus(String seedingStatus) {
		SeedingStatus = seedingStatus;
	}
	public String getPanStatus() {
		return PanStatus;
	}
	public void setPanStatus(String panStatus) {
		PanStatus = panStatus;
	}
	public String getPan() {
		return Pan;
	}
	public void setPan(String pan) {
		Pan = pan;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getFatherName() {
		return FatherName;
	}
	public void setFatherName(String fatherName) {
		FatherName = fatherName;
	}
	public PanCardResponseNew(String dateOfBirth, String seedingStatus, String panStatus, String pan, String name,
			String fatherName) {
		super();
		DateOfBirth = dateOfBirth;
		SeedingStatus = seedingStatus;
		PanStatus = panStatus;
		Pan = pan;
		Name = name;
		FatherName = fatherName;
	}
	public PanCardResponseNew() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	



}
