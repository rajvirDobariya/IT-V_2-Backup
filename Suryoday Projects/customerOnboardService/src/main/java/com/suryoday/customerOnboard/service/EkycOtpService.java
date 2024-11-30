package com.suryoday.customerOnboard.service;

public interface EkycOtpService {

	
	String getXmlRequest(String uid, String stan) throws Exception;

	String sendEkyc(String xmlRequest);
}
