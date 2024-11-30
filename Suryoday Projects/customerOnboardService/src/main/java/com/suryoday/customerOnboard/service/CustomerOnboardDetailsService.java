package com.suryoday.customerOnboard.service;

import javax.servlet.http.HttpServletRequest;

import com.suryoday.customerOnboard.entity.CustomerOnboardDetails;

public interface CustomerOnboardDetailsService {

	String createLead(String mobileNo, String x_User_ID);

	void saveResponse(String proof, String panCardNo, String applicationNo, String response);

	boolean validateSessionId(String x_Session_ID, HttpServletRequest request);

	String getSessionId(String x_User_ID, HttpServletRequest request);

	CustomerOnboardDetails getByAapplicationNo(String applicationNo);

	void save(CustomerOnboardDetails customeDetails);

}
