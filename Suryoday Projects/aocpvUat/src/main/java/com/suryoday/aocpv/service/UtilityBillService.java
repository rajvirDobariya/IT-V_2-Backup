package com.suryoday.aocpv.service;

import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.suryoday.aocpv.pojo.BillServiceProviders;

@Component
public interface UtilityBillService {

	List<BillServiceProviders> fetchServiceProviders();

	JSONObject authenticateUtilityBill(JSONObject request, JSONObject header);

}
