package com.suryoday.roaocpv.service;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public interface ROAOCPVLoanCreationService {

	JSONObject loanCreation(String applicationNo, JSONObject header);

//	JSONObject statuscheck(JSONObject jsonObject, JSONObject header);

	JSONObject statuscheck(String branchId, String applicationId, JSONObject header);

}
