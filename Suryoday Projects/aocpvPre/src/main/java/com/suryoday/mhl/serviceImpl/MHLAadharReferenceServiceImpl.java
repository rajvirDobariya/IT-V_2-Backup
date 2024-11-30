package com.suryoday.mhl.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.suryoday.mhl.others.MHLGenerateProperty;
import com.suryoday.mhl.service.MHLAadharReferenceService;

@Service
public class MHLAadharReferenceServiceImpl implements MHLAadharReferenceService {

	private static Logger logger = LoggerFactory.getLogger(MHLAadharReferenceServiceImpl.class);
	
	public JSONObject createAadharReference(JSONObject jSONObject, JSONObject header) {
		
			JSONObject sendResponse = new JSONObject();
		
			URL obj = null;
			try {
				
				MHLGenerateProperty x = MHLGenerateProperty.getInstance();
				
				 x.bypassssl();
					HostnameVerifier allHostsValid = new HostnameVerifier() {
						public boolean verify(String hostname, SSLSession session) {
							return true;
						}
					};
					
				HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid); 
				
				 obj =new URL("https://intramashery.suryodaybank.co.in/aadhar/reference/v2?api_key=kyqak5muymxcrjhc5q57vz9v");
				 
				
					HttpURLConnection con = (HttpURLConnection) obj.openConnection();
					con.setRequestMethod("POST");
					con.setRequestProperty("Content-Type", "application/json");
					con.setRequestProperty("X-Correlation-ID",header.getString("X-Correlation-ID"));
					con.setRequestProperty("X-Request-ID", header.getString("X-Request-ID"));
					con.setRequestProperty("tenant",header.getString("tenant"));

					sendResponse = getResponseData(jSONObject, sendResponse, con,"POST");
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
			return sendResponse;
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

		public JSONObject fetchAadharByReferenceNo(String referenceNo) {
			JSONObject sendResponse = new JSONObject();
			
			URL obj = null;
			try {
				
				MHLGenerateProperty x = MHLGenerateProperty.getInstance();
				 x.bypassssl();
				// Create all-trusting host name verifier
					HostnameVerifier allHostsValid = new HostnameVerifier() {
						public boolean verify(String hostname, SSLSession session) {
							return true;
						}
					};
					
				HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid); 
				
				 obj =new URL("https://intramashery.suryodaybank.co.in/aadhar/reference/"+referenceNo+"?api_key=kyqak5muymxcrjhc5q57vz9v");
					HttpURLConnection con = (HttpURLConnection) obj.openConnection();
					con.setRequestMethod("GET");
					con.setRequestProperty("Content-Type", "application/json");
//					con.setRequestProperty("X-Correlation-ID",header.getString("X-Correlation-ID"));
//					con.setRequestProperty("X-Request-ID", header.getString("X-Request-ID"));
//					con.setRequestProperty("tenant",header.getString("tenant"));
					
					sendResponse = getResponse(referenceNo, sendResponse, con,"GET");
//					
//					getHeadersRequestInfo(con);
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
			return sendResponse;
		
			
		}
		
		private static JSONObject getResponse(String parent, JSONObject sendAuthenticateResponse,
				HttpURLConnection con,String MethodType) throws IOException {
			
			con.setDoOutput(true);
			//OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
			//os.write(parent.toString());
			//os.flush();
			//os.close();
			
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
						logger.debug("GET request not worked");
				
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

}
