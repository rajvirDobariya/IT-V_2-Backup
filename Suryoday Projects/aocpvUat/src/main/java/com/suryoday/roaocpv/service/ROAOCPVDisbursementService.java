package com.suryoday.roaocpv.service;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public interface ROAOCPVDisbursementService {

	JSONObject checkDisbursementStatus(String branchId, String applicationId, JSONObject header);

}
