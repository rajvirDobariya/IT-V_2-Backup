package com.suryoday.roaocpv.service;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public interface ClientCreationService {

	JSONObject clientCreation(String applicationNo, JSONObject header);

}
