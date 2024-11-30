package com.suryoday.aocpv.service;


import org.json.JSONArray;
import org.json.JSONObject;


public interface RetrieveReportService {

	JSONObject writeExcel(JSONArray json, String userId);
}
