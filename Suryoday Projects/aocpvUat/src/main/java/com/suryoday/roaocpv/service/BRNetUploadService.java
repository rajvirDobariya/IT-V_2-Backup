package com.suryoday.roaocpv.service;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public interface BRNetUploadService {

//	JSONObject clientCreation(JSONObject jsonObject, JSONObject header);

	JSONObject clientCreation(String applicationNo, JSONObject header);

	JSONObject uploadImage(String branchId, String clientId, JSONObject header);

}
