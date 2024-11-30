package com.suryoday.aocpv.service;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public interface UpiMapperService {

	JSONObject upiMapper(JSONObject jsonObject, JSONObject header);

}
