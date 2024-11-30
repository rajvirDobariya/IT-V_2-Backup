package com.suryoday.roaocpv.service;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public interface ROAOCPVRepaymentService {

	JSONObject repayment(String AccountNumber, JSONObject header);

}
