package com.suryoday.travelvisit.service;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public interface BranchHygieneService {

	JSONObject addBranchHygiene(String request, String channel, String x_Session_ID, String x_User_ID, boolean b,
			MultipartFile image);

	JSONObject getBranchHygienes(String request, String channel, String x_Session_ID, String x_User_ID, boolean b);

	JSONObject getBranchHygieneExcel(String request, String channel, String x_Session_ID, String x_User_ID, boolean b);

	JSONObject getAllBranches(String request, String channel, String x_Session_ID, String x_User_ID, boolean b);

}
