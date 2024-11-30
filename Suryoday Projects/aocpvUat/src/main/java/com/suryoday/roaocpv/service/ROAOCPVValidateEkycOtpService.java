package com.suryoday.roaocpv.service;

public interface ROAOCPVValidateEkycOtpService {

	String getXmlRequest(String otp, String uid, String stan) throws Exception;

	String sendEkyc(String xmlRequest);

	String decryptString(String hsmData);

}
