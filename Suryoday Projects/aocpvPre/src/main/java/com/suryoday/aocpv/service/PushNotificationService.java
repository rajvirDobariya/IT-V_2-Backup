package com.suryoday.aocpv.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.suryoday.aocpv.pojo.NotificationUser;

@Component
public interface PushNotificationService {


	JSONObject sendNotification(String tokenId, JSONObject header);

	List<NotificationUser> fetchByUserId(String empId);

	List<NotificationUser> fetchByBranchId(String branchId);

	JSONObject sendNotificationAll(JSONArray regIds, JSONObject header);

}
