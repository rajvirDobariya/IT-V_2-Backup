package com.suryoday.twowheeler.service;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
@Component
public interface TwowheelerBREService {

	JSONObject fetchBre(JSONObject header, String leadId);

	JSONObject createLead(String applicationNo,String member, JSONObject header);

	JSONObject breRules(String applicationNo);

	JSONObject equifaxReport(String applicationNo);

}
