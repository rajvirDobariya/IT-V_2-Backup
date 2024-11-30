package com.suryoday.roaocpv.service;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public interface IndividualClientDetailsService {

	JSONObject indivialClientDetails(String branchId, String clientId, JSONObject header);

}
