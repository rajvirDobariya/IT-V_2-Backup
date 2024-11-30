package com.suryoday.roaocpv.service;

import org.json.JSONObject;

public interface WhatsappMediaSendService {

	public JSONObject sendMedia(JSONObject jsonObject, JSONObject header);

	public JSONObject sendMessage(JSONObject jsonObject, JSONObject header);

}
