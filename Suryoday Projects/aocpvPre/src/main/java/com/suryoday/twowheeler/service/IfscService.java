package com.suryoday.twowheeler.service;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public interface IfscService {

	JSONObject searchIfsc(String ifscCode, JSONObject header);
	JSONObject pennyDrop(String accountNo,String ifsc,JSONObject header);
}
