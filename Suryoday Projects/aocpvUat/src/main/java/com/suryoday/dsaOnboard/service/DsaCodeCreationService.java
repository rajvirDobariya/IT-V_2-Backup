package com.suryoday.dsaOnboard.service;

import org.json.JSONObject;

import com.suryoday.dsaOnboard.pojo.DsaOnboardDetails;

public interface DsaCodeCreationService {

	JSONObject codeCreation(DsaOnboardDetails dsaOnboardDetails);

	JSONObject addDsaId(DsaOnboardDetails dsaOnboardDetails);

}
