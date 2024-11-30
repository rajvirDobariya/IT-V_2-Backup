package com.digitisation.branchreports.enums;

public enum UserRole {
	BRANCH_MAKER("BRANCH-MAKER"), BRANCH_CHECKER("BRANCH-CHECKER"), BRANCH_HO("BRANCH-HO"),
	BRANCH_AUDITOR("BRANCH-AUDITOR");

	private final String role;

	UserRole(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}
}