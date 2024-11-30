package com.suryoday.twowheeler.service;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public interface SPDCApiService {

	JSONObject spdcManagement(JSONObject jsonObject, JSONObject header);

}
