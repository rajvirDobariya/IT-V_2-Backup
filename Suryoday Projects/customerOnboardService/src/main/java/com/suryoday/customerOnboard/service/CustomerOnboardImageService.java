package com.suryoday.customerOnboard.service;

import java.util.Map;

import org.json.JSONArray;

public interface CustomerOnboardImageService {

	void saveAadharPhoto(Map<String, String> map, String applicationNo, JSONArray jsonArray);

}
