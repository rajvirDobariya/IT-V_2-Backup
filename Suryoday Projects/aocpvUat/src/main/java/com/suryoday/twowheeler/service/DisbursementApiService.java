package com.suryoday.twowheeler.service;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.suryoday.twowheeler.pojo.TwowheelerLoanCreation;

@Component
public interface DisbursementApiService {

	JSONObject disbursement(String applicationNo, JSONObject header);

	JSONObject collateralDetails(String applicationNo, JSONObject header);

	JSONObject loanCreation(String applicationNo, JSONObject header);

	JSONObject crmModification(String applicationNo, JSONObject header);

	JSONObject cifCreation(String applicationNo, JSONObject header);

	JSONObject spdc(TwowheelerLoanCreation twloanCreation, JSONObject header);

}
