package com.suryoday.hastakshar.service;

import org.json.JSONObject;

public interface EmailService {

	public void sendEmailToManagerOrOpsTeamAfter48hours();

	public void sendLimitEnhancementEmailToApprover(String approver1, String accountNo, String transactionDesc,
			String message);

	public void sendEmailToOpsTeam(String accountNo, String transactionDesc, String message);

	public JSONObject sendEmail(String toMail, String subject, String templateID, String apiKey, String fromName,
			String fromMail, String type, String var1, String var2);

}
