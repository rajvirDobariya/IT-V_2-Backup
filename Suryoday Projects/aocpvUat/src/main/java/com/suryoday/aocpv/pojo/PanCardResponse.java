package com.suryoday.aocpv.pojo;

public class PanCardResponse {
	private String LastUpdatedDate;
	private String FirstName;
	private String NameOnCard;
	private String Middlename;
	private String LastName;
	private String PanTitle;
	private String PanID;
	private String Pan_Status;

	public String getLastUpdatedDate() {
		return LastUpdatedDate;
	}

	public void setLastUpdatedDate(String lastUpdatedDate) {
		LastUpdatedDate = lastUpdatedDate;
	}

	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public String getNameOnCard() {
		return NameOnCard;
	}

	public void setNameOnCard(String nameOnCard) {
		NameOnCard = nameOnCard;
	}

	public String getMiddlename() {
		return Middlename;
	}

	public void setMiddlename(String middlename) {
		Middlename = middlename;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public String getPanTitle() {
		return PanTitle;
	}

	public void setPanTitle(String panTitle) {
		PanTitle = panTitle;
	}

	public String getPanID() {
		return PanID;
	}

	public void setPanID(String panID) {
		PanID = panID;
	}

	public String getPan_Status() {
		return Pan_Status;
	}

	public void setPan_Status(String pan_Status) {
		Pan_Status = pan_Status;
	}

	public PanCardResponse(String lastUpdatedDate, String firstName, String nameOnCard, String middlename,
			String lastName, String panTitle, String panID, String pan_Status) {
		super();
		LastUpdatedDate = lastUpdatedDate;
		FirstName = firstName;
		NameOnCard = nameOnCard;
		Middlename = middlename;
		LastName = lastName;
		PanTitle = panTitle;
		PanID = panID;
		Pan_Status = pan_Status;
	}

	public PanCardResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

}
