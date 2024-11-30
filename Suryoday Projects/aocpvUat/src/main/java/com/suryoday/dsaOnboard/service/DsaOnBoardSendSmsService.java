package com.suryoday.dsaOnboard.service;

import org.json.JSONObject;

public interface DsaOnBoardSendSmsService {

	JSONObject sendLink(JSONObject request, JSONObject header);

}
