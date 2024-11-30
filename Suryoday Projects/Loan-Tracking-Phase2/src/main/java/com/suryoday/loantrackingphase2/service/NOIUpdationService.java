package com.suryoday.loantrackingphase2.service;

import org.json.JSONObject;

public interface NOIUpdationService {

	JSONObject addNOIUpdationActivity(String request, String channel, String X_Session_ID, String X_User_ID,
			Boolean isEncy);

	JSONObject getNOIUpdationActivities(String request, String channel, String X_Session_ID, String X_User_ID,
			Boolean isEncy);

	JSONObject getExcelNOIUpdationActivities(String request, String channel, String X_Session_ID, String X_User_ID,
			Boolean isEncy);

	JSONObject getLoanAccountDetailsByAccNo(String request, String channel, String X_Session_ID, String X_User_ID,
			Boolean isEncy);

	JSONObject getNOIUpdateActivityById(String request, String channel, String X_Session_ID, String X_User_ID,
			Boolean isEncy);

}
