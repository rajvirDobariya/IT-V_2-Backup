package com.suryoday.roaocpv.service;

public interface ROAOCPVEkycService {

	public String sendEkyc(String request);

	public String getJsonRequest(String pidxml, String uid) throws Exception;

	public String getJsonFaceRequest(String pidxml, String uid) throws Exception;

	public String decryptString(String request);
}
