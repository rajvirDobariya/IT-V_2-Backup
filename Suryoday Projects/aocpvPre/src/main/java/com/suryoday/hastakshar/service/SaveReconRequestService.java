package com.suryoday.hastakshar.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public interface SaveReconRequestService {

	public JSONObject saveReconRequestData(JSONObject jsonRequest);

	public JSONArray fetchReconList(JSONObject jsonRequest);

}
