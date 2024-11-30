package com.suryoday.roaocpv.service;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public interface ROAOCPVPassportService {

//	JSONObject verifyPassport(JSONObject jsonObject, JSONObject header);

	JSONObject verifyPassport(String passportNo, String doi, JSONObject header, String applicationNo);

}
