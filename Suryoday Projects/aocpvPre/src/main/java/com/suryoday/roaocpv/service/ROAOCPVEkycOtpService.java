package com.suryoday.roaocpv.service;

public interface ROAOCPVEkycOtpService {

	
	String getXmlRequest(String uid, String stan) throws Exception;

	String sendEkyc(String xmlRequest);
}
