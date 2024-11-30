package com.suryoday.travelvisit.service;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public interface OdCustomerVisitService {

	JSONObject addOdCustomerVisit(String request, String channel, String x_Session_ID, String x_User_ID, boolean b,
			MultipartFile image);

	JSONObject getOdCustomerVisits(String request, String channel, String x_Session_ID, String x_User_ID, boolean b);

	JSONObject getOdCustomerVisitExcel(String request, String channel, String x_Session_ID, String x_User_ID,
			boolean b);

}
