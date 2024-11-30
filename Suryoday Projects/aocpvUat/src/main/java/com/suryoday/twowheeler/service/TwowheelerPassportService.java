package com.suryoday.twowheeler.service;

import org.json.JSONObject;

public interface TwowheelerPassportService {

	JSONObject verifyPassport(String passportNo, String doi, JSONObject header, String applicationNo, String member);
}
