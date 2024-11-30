package com.suryoday.hastakshar.enums;

public enum EmailTemplate {
	HASTAKSHAR_STATUS("HastaksharStatus09012024"), HASTAKSHAR_REMINDER("HastaksharReminder09012024");

	private final String templateName;

	EmailTemplate(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateName() {
		return templateName;
	}
}
