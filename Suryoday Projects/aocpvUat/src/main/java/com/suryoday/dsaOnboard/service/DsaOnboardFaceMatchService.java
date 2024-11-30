package com.suryoday.dsaOnboard.service;

import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public interface DsaOnboardFaceMatchService {

	JSONObject faceMatch(List<String> list, JSONObject header);

}
