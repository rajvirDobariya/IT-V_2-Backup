package com.suryoday.aocpv.serviceImp;

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

import com.suryoday.aocpv.service.CustomerService;
import com.suryoday.connector.serviceImpl.GenerateProperty;



@Service
public class CustomerServiceImpl implements CustomerService{

	
	private static Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
	
	@Override
	public JSONObject getData(JSONObject jsonObject, JSONObject header) {
		
					JSONObject sendAuthenticateResponse = new JSONObject();
					
					URL obj=null;
					try {
						
						GenerateProperty x = GenerateProperty.getInstance();
						x.getappprop();
						 x.bypassssl();
						// Create all-trusting host name verifier
							HostnameVerifier allHostsValid = new HostnameVerifier() {
								public boolean verify(String hostname, SSLSession session) {
									return true;
								}
							};
							
						HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	
						
						obj = new URL(x.BASEURL+"customers/getData?api_key="+x.api_key);
					
					HttpURLConnection con = (HttpURLConnection) obj.openConnection();
					con.setRequestMethod("POST");
					con.setRequestProperty("X-Request-ID","IBR");
					con.setRequestProperty("X-Transaction-ID", "ABC");
					con.setRequestProperty("X-To-ID","Others");
					con.setRequestProperty("X-From-ID","TAB");
					con.setRequestProperty("Content-Type", "application/json");
		
							getHeadersRequestInfo(con);
							
							sendAuthenticateResponse = GetResponseData(jsonObject, sendAuthenticateResponse, con,"POST");
							System.out.println(sendAuthenticateResponse);
					
					} 
					
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		return sendAuthenticateResponse;
	}
	
	private static JSONObject GetResponseData(JSONObject parent, JSONObject sendAuthenticateResponse,
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

	
	public void getHeadersRequestInfo(HttpURLConnection con) {
		
	}

	@Override
	public JSONObject externaldedupe(String customerNo) {
		JSONObject request = new JSONObject();
		JSONObject Data = new JSONObject();
		Data.put("CustomerNo", customerNo);
		Data.put("ProductGroup", "Loan");
		request.put("Data", Data);
		JSONObject sendAuthenticateResponse = new JSONObject();
		URL obj=null;
		try {
			
			GenerateProperty x = GenerateProperty.getInstance();
			x.getappprop();
			 x.bypassssl();
			// Create all-trusting host name verifier
				HostnameVerifier allHostsValid = new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				};
				
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			
			obj = new URL(x.BASEURL+"customers/details?api_key="+x.api_key);
			logger.debug(x.BASEURL+"customers/details?api_key="+x.api_key);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("X-Request-ID","NOV");
		con.setRequestProperty("Content-Type", "application/json");
		
				getHeadersRequestInfo(con);
				
				sendAuthenticateResponse = GetResponseData(request, sendAuthenticateResponse, con,"POST");
				System.out.println(sendAuthenticateResponse);
		
		} 
		
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
return sendAuthenticateResponse;
	}
}
