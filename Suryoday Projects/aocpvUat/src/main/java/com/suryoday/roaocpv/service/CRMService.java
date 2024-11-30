package com.suryoday.roaocpv.service;

import java.io.IOException;

import org.json.JSONObject;

public interface CRMService {

	public JSONObject crmData(String customerID, String x_User_ID) throws IOException;

	public JSONObject getBranchNameCode(JSONObject header) throws IOException;

	public JSONObject getRepayment();

	public JSONObject fetchAllBranch();
}
