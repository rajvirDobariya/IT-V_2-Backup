package com.suryoday.roaocpv.service;

import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public interface FaceMatchApiService {

	JSONObject faceMatch(List<String> list, JSONObject header);

	JSONObject nameMatch(JSONObject jsonObject, JSONObject header);

}
