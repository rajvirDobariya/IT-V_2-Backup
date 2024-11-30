package com.suryoday.Counterfeit.enums;

public enum DateType {
	DAILY("DAILY"), MONTHLY("MONTHLY");

	private final String dateType;

	DateType(String dateType) {
		this.dateType = dateType;
	}

	public String getRole() {
		return dateType;
	}

	@Override
	public String toString() {
		return this.dateType;
	}
}
