package com.suryoday.hastakshar.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public interface CustomerDetailsService {

	public JSONObject getCustomerDetails(JSONObject jsonObject, JSONObject header);

}
