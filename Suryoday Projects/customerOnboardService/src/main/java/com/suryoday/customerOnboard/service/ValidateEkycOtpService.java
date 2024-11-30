package com.suryoday.customerOnboard.service;

public interface ValidateEkycOtpService {

	String getXmlRequest(String otp,String uid, String stan) throws Exception;

	String sendEkyc(String xmlRequest);

	String decryptString(String hsmData);

}
