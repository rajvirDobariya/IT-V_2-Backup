package com.suryoday.connector.service;

import org.json.JSONObject;

public interface GetLoanDetailsService {

	public JSONObject getLoanDetails(String accountNo, JSONObject header);

}
