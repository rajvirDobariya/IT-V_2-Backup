package com.suryoday.twowheeler.service;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public interface TWBreService {

	JSONObject TWbreLoan(JSONObject jsonObject, JSONObject header);
}
