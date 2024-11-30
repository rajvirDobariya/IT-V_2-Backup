package com.suryoday.connector.service;

public interface AuthService {

	public String sendAuth(String request);

	public String getJsonRequest(String parent, String uid) throws Exception;

}
