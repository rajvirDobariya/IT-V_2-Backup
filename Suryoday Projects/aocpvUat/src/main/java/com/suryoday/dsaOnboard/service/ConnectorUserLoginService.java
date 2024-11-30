package com.suryoday.dsaOnboard.service;

import org.springframework.stereotype.Component;

import com.suryoday.dsaOnboard.pojo.ConnectorUserLogin;

@Component
public interface ConnectorUserLoginService {
	String checkEmailId(String emailId);

	String checkMobileNo(String mobileNo);

	void save(ConnectorUserLogin user);

	ConnectorUserLogin validateUser(String emailId, String encryptPassword);

}
