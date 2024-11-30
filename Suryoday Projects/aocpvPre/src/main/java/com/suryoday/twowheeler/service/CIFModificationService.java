package com.suryoday.twowheeler.service;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public interface CIFModificationService {

	JSONObject crmModification(JSONObject jsonObject, JSONObject header);

}
