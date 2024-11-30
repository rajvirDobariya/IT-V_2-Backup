package com.suryoday.dsaOnboard.service;

import org.json.JSONObject;

public interface DsaAmlReportService {

	JSONObject getAmlReport(String applicationNo, String member, JSONObject header);

}
