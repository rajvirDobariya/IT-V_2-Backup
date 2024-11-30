package com.suryoday.loantrackingphase2.service;

import org.json.JSONObject;

public interface HealthCheckActivityService {

	JSONObject addHealthCheckActivity(String request, String channel, String X_Session_ID, String X_User_ID,
			boolean isEncrypt);

	JSONObject getAllHealthCheckActivity(String request, String channel, String X_Session_ID, String X_User_ID,
			boolean isEncrypt);

	JSONObject getHealthCheckActivityById(String request, String channel, String X_Session_ID, String X_User_ID,
			boolean isEncrypt);

	JSONObject updateProductChecksAndQueries(String request, String channel, String X_Session_ID, String X_User_ID,
			boolean isEncrypt);

	JSONObject getCustomerDataByCustomerNo(String request, String channel, String X_Session_ID, String X_User_ID,
			boolean isEncrypt);

	JSONObject getEmployeeNameByEmployeeId(String request, String channel, String X_Session_ID, String X_User_ID,
			boolean isEncrypt);

	JSONObject getExcelHealthCheckActivities(String request, String channel, String X_Session_ID, String X_User_ID,
			boolean isEncrypt);
}
