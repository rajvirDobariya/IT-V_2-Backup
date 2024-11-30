package com.suryoday.twowheeler.service;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public interface EmiCalculatorApiService {

	JSONObject emiCalculator(JSONObject jsonObject, JSONObject header);

	String calculateInsurance(double amountIndouble, String product, String bankName, String tenure);

}
