package com.suryoday.aocpv.serviceImp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suryoday.aocpv.pojo.NotificationUser;
import com.suryoday.aocpv.repository.PushNotificationRepo;
import com.suryoday.aocpv.service.PushNotificationService;
import com.suryoday.connector.serviceImpl.GenerateProperty;

@Component
public class PushNotificationServImpl implements PushNotificationService{
	
	private static Logger logger = LoggerFactory.getLogger(PushNotificationServImpl.class);
	@Autowired
	PushNotificationRepo pushnotificationrepo;
	@Override
	public JSONObject sendNotification(String tokenId, JSONObject header) {
		JSONObject sendResponse = new JSONObject();
		JSONObject request=getRequest(tokenId);
		URL obj = null;
		try {
			
			
			GenerateProperty x = GenerateProperty.getInstance();
			x.getappprop();
			//GenerateProperty x = GenerateProperty.getInstance();
			 x.bypassssl();
			// Create all-trusting host name verifier
				HostnameVerifier allHostsValid = new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				};
				
			
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid); 
			logger.debug("https://fcm.googleapis.com/fcm/send");
	
			 obj =new URL("https://fcm.googleapis.com/fcm/send");
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Authorization", "key="+header.getString("Authorization-key"));
				con.setRequestProperty("Content-Type","application/json");
				
				sendResponse = getResponseData(request, sendResponse, con,"POST");

			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return sendResponse;
	}

	
	private JSONObject getRequest(String tokenId) {
		JSONObject data=new JSONObject();
		JSONObject notification=new JSONObject();
		notification.put("title","Alankit");
		notification.put("body","Welcome alankit Push Service.");
		data.put("notification",notification);
		data.put("to",tokenId);
		return data;
	}


	private static JSONObject getResponseData(JSONObject parent, JSONObject sendAuthenticateResponse,
			HttpURLConnection con,String MethodType) throws IOException {
		
		con.setDoOutput(true);
		OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
		os.write(parent.toString());
		os.flush();
		os.close();
		
		int responseCode = con.getResponseCode();
		logger.debug("POST Response Code :: " + responseCode);
		
		
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			
			JSONObject 	sendauthenticateResponse1  = new JSONObject();
			   sendauthenticateResponse1.put("data", response.toString());
			   sendAuthenticateResponse = sendauthenticateResponse1;
		}
		else
		{
					logger.debug("POST request not worked");
			
			JSONObject 	sendauthenticateResponse1  = new JSONObject();
			 
			 JSONObject errr = new JSONObject();
			 errr.put("Description","Server Error "+responseCode);
			 
			 JSONObject j = new JSONObject();
			 j.put("Error",errr);
			 
			sendauthenticateResponse1.put("data", ""+j);
			sendAuthenticateResponse = sendauthenticateResponse1;
		}
		
				return sendAuthenticateResponse;
		
	}


	@Override
	public JSONObject sendNotificationAll(JSONArray regIds, JSONObject header) {
JSONObject sendResponse = new JSONObject();
JSONObject request=getRequest2(regIds);
		
		URL obj = null;
		try {
			
			
			GenerateProperty x = GenerateProperty.getInstance();
			x.getappprop();
			//GenerateProperty x = GenerateProperty.getInstance();
			 x.bypassssl();
			// Create all-trusting host name verifier
				HostnameVerifier allHostsValid = new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				};
				
			
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid); 
			logger.debug("https://fcm.googleapis.com/fcm/send");
	
			 obj =new URL("https://fcm.googleapis.com/fcm/send");
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Authorization", "key="+header.getString("Authorization-key"));
				con.setRequestProperty("Content-Type","application/json");
				
				sendResponse = getResponseData2(request, sendResponse, con,"POST");

			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return sendResponse;
	}
	

	private JSONObject getRequest2(JSONArray regIds) {
		JSONObject Data=new JSONObject();
		JSONObject notification =new JSONObject();
		notification.put("text","Welcome alankit Push Service.");
		notification.put("title","Alankit Universe");
		Data.put("notification",notification);
		JSONObject data=new JSONObject();
		data.put("message","Welcome alankit Push Service.");
		Data.put("data",data);
		Data.put("registration_ids",regIds);
		return Data;
	}


	private static JSONObject getResponseData2(JSONObject parent, JSONObject sendAuthenticateResponse,
			HttpURLConnection con,String MethodType) throws IOException {
		
		con.setDoOutput(true);
		OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
		os.write(parent.toString());
		os.flush();
		os.close();
		
		int responseCode = con.getResponseCode();
		logger.debug("POST Response Code :: " + responseCode);
		
		
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			
			JSONObject 	sendauthenticateResponse1  = new JSONObject();
			   sendauthenticateResponse1.put("data", response.toString());
			   sendAuthenticateResponse = sendauthenticateResponse1;
		}
		else
		{
					logger.debug("POST request not worked");
			
			JSONObject 	sendauthenticateResponse1  = new JSONObject();
			 
			 JSONObject errr = new JSONObject();
			 errr.put("Description","Server Error "+responseCode);
			 
			 JSONObject j = new JSONObject();
			 j.put("Error",errr);
			 
			sendauthenticateResponse1.put("data", ""+j);
			sendAuthenticateResponse = sendauthenticateResponse1;
		}
		
				return sendAuthenticateResponse;
		
	}


	@Override
	public List<NotificationUser> fetchByUserId(String empId) {
		List<NotificationUser> list=pushnotificationrepo.fetchByUserId(empId);
		if(list.isEmpty())
		{
			throw new NoSuchElementException("List is empty");
		}
		return list;
				
	}


	@Override
	public List<NotificationUser> fetchByBranchId(String branchId) {
		List<NotificationUser> list=pushnotificationrepo.fetchByBranchId(branchId);
		if(list.isEmpty())
		{
			throw new NoSuchElementException("List is empty");
		}
		return list;
	}
	
	
}
