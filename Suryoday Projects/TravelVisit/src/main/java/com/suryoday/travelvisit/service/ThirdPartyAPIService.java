package com.suryoday.travelvisit.service;

import org.json.JSONObject;

public interface ThirdPartyAPIService {

	public JSONObject getCustDetailsByCustId(String request, String channel, String x_Session_ID, String x_User_ID,
			boolean b);

}
