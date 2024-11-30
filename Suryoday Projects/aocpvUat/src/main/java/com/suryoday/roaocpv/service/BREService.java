package com.suryoday.roaocpv.service;

import org.json.JSONObject;

public interface BREService {

	JSONObject createLead(String applicationNo, JSONObject header);

	JSONObject fetchBre(String applicationNo, JSONObject header, String leadId);

	JSONObject equifaxReport(String applicationNo, JSONObject header, String leadId);

	JSONObject equifaxReport(String applicationNo);

	JSONObject brerules();

}
