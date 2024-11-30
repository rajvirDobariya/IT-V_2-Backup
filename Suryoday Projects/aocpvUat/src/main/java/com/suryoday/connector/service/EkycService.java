package com.suryoday.connector.service;

public interface EkycService {

	public String sendEkyc(String request);

	public String getJsonRequest(String pidxml, String uid) throws Exception;

	public String decryptString(String request);
}
