package com.suryoday.roaocpv.service;

import org.json.JSONObject;

import com.suryoday.aocpv.pojo.AocpvLoanCreation;

public interface ROAOCPVAccountCreationService {

	public JSONObject accountCreation(JSONObject jsonObject, JSONObject header, AocpvLoanCreation findByApplicationNo);

}
