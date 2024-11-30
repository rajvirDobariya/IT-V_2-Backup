package com.suryoday.payment.payment;

public class Data {

	String UserID;

	String Password;

	public Data() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the userID
	 */
	public String getUserID() {
		return UserID;
	}

	/**
	 * @param userID the userID to set
	 */
	public void setUserID(String userID) {
		UserID = userID;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return Password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		Password = password;
	}

	@Override
	public String toString() {
		return "Data [UserID=" + UserID + ", Password=" + Password + "]";
	}

}
