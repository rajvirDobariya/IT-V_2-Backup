package com.suryoday.Counterfeit.enums;

public enum UserRole {
	COUNTERFEIT_MAKER("COUNTERFEIT-MAKER"), COUNTERFEIT_CHECKER("COUNTERFEIT-CHECKER"),
	COUNTERFEIT_HO("COUNTERFEIT-HO");

	private final String role;

	UserRole(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}

	@Override
	public String toString() {
		return this.role;
	}
}
