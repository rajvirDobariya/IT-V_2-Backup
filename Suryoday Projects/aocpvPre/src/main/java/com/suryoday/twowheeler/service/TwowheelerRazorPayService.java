package com.suryoday.twowheeler.service;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.suryoday.twowheeler.pojo.TwowheelerPaymentDetails;

@Component
public interface TwowheelerRazorPayService {

	JSONObject sendPaymentLink(String applicationNo, JSONObject header);

	JSONObject fetchPaymentLink(String orderId, JSONObject header);

	void save(TwowheelerPaymentDetails paymentDetails);

}
