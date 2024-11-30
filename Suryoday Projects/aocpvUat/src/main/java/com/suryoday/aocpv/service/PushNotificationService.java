package com.suryoday.aocpv.service;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public interface PushNotificationService {

	JSONObject sendNotification(JSONObject jsonObject, JSONObject header);

}
