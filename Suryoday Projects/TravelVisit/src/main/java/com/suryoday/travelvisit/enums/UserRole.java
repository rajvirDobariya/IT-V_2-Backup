package com.suryoday.travelvisit.enums;

public enum UserRole {
	RO("RO"), CREDIT_OPS("CREDIT_OPS");

	private final String role;

	UserRole(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}
}
