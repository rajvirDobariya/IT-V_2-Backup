package com.suryoday.roaocpv.serviceImpl;

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
import org.springframework.stereotype.Component;

import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.roaocpv.service.ROAOCPVPassportService;
@Component
public class ROAOCPVPassportServImpl implements ROAOCPVPassportService{

	private static Logger logger = LoggerFactory.getLogger(ROAOCPVPassportServImpl.class);
	@Override
	public JSONObject verifyPassport(String passportNo, String doi,JSONObject header) {
		JSONObject sendResponse = new JSONObject();
		JSONObject request=getRequest(passportNo,doi);
		
		URL obj = null;
		try {
			
			ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
			x.getappprop();
			 x.bypassssl();
				HostnameVerifier allHostsValid = new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				};
				
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid); 
			logger.debug(x.BASEURL+"karza/verify/passport?api_key="+x.api_key);
			 obj =new URL(x.BASEURL+"karza/verify/passport?api_key="+x.api_key);
			// obj =new URL("https://intramashery.suryodaybank.co.in/aadhar/reference?api_key=kyqak5muymxcrjhc5q57vz9v");
			
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/json");
				con.setRequestProperty("X-Request-ID", header.getString("X-Request-ID"));
				

				sendResponse = getResponseData(request, sendResponse, con,"POST");
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return sendResponse;
	}
	
	
	private JSONObject getRequest(String passportNo, String doi) {
		JSONObject parent=new JSONObject();
		parent.put("consent","y");
		parent.put("fileNo","BO3072344560818");
		parent.put("dob","17/08/1987");
		parent.put("passportNo",passportNo);
		parent.put("doi",doi);
		parent.put("name","OMKAR MILIND SHIRHATTI");
		return parent;
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
	public JSONObject verifyPassport2(String passportNo, String doi) {
		JSONObject data=new JSONObject();
		JSONObject resp=new JSONObject();
		resp.put("passportNumberFromSource","S3733862");
		resp.put("applicationDate","14/05/2018");
		resp.put("typeOfApplication","Tatkaal");
		resp.put("statusCode","101");
		resp.put("dispatchedOnFromSource","14/05/2018");
		resp.put("surnameFromPassport","SHIRHATTI");
		resp.put("nameFromPassport","OMKAR MILIND");
		data.put("data",resp.toString());
		return data;
	}

}
