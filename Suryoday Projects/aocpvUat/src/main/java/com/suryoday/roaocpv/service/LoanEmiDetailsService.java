package com.suryoday.roaocpv.service;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public interface LoanEmiDetailsService {

	JSONObject loanEmiDetails(String applicationNo, JSONObject header);

}
