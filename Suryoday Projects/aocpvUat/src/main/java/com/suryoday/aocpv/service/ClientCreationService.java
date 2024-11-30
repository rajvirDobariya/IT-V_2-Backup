package com.suryoday.aocpv.service;

import org.json.JSONObject;

import com.suryoday.aocpv.pojo.AocpCustomer;

public interface ClientCreationService {

	JSONObject clientCreation(AocpCustomer applicationNo);

}
