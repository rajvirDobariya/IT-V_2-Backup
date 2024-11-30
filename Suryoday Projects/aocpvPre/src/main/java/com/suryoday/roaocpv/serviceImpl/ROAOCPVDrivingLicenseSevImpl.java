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
import com.suryoday.roaocpv.service.ROAOCPVDrivingLicenseService;

@Component
public class ROAOCPVDrivingLicenseSevImpl implements ROAOCPVDrivingLicenseService{
	private static Logger logger = LoggerFactory.getLogger(ROAOCPVDrivingLicenseSevImpl.class);
	@Override
	public JSONObject authenticateDrivingLicense(String drivingLicenseNo, JSONObject header) {
JSONObject sendResponse = new JSONObject();
JSONObject request=getRequest(drivingLicenseNo);
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
			logger.debug(x.BASEURL+"karza/authenticate/driverLicense?api_key="+x.api_key);
			 obj =new URL(x.BASEURL+"karza/authenticate/driverLicense?api_key="+x.api_key);
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
	
	
	private JSONObject getRequest(String drivingLicenseNo) {
		JSONObject parent=new JSONObject();
		parent.put("dlNo",drivingLicenseNo);
		parent.put("dob","05-10-1994");
		parent.put("additionalDetails",true);
		parent.put("consent","Y");
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
	public JSONObject getDrivingLicenseDetails(String drivingLicenseNo,String dob) {
		JSONObject data =new JSONObject();
		String resp="{\r\n"
				+ "    \"requestId\": \"b475dfd8-76c0-4cf5-8c9c-570152aa3602\",\r\n"
				+ "    \"result\": {\r\n"
				+ "        \"status\": \"Active\",\r\n"
				+ "        \"father/husband\": \"HARIBHAI\",\r\n"
				+ "        \"validity\": {\r\n"
				+ "            \"nonTransport\": \"11-02-2013 to 10-02-2033\",\r\n"
				+ "            \"transport\": \"\"\r\n"
				+ "        },\r\n"
				+ "        \"address\": [\r\n"
				+ "            {\r\n"
				+ "                \"district\": \"KHEDA\",\r\n"
				+ "                \"pin\": 387320,\r\n"
				+ "                \"completeAddress\": \"IN GATE, AT-DABHAN, TA-NADIAD, DIST-KHEDA. 387320\",\r\n"
				+ "                \"country\": \"\",\r\n"
				+ "                \"state\": \"GUJARAT\",\r\n"
				+ "                \"addressLine1\": \"\",\r\n"
				+ "                \"type\": \"NA\"\r\n"
				+ "            }\r\n"
				+ "        ],\r\n"
				+ "        \"covDetails\": [\r\n"
				+ "            {\r\n"
				+ "                \"issueDate\": \"11-02-2013\",\r\n"
				+ "                \"cov\": \"MCWG\"\r\n"
				+ "            },\r\n"
				+ "            {\r\n"
				+ "                \"issueDate\": \"11-02-2013\",\r\n"
				+ "                \"cov\": \"LMV\"\r\n"
				+ "            }\r\n"
				+ "        ],\r\n"
				+ "        \"bloodGroup\": \"\",\r\n"
				+ "        \"dlNumber\": \"GJ0720130001614\",\r\n"
				+ "        \"name\": \"KRUNAL Kumar\",\r\n"
				+ "        \"img\": \""
				+ "        \"dob\": \"15-08-1993\",\r\n"
				+ "        \"issueDate\": \"11-02-2013\",\r\n"
				+ "        \"statusDetails\": {\r\n"
				+ "            \"remarks\": \"\",\r\n"
				+ "            \"to\": \"\",\r\n"
				+ "            \"from\": \"\"\r\n"
				+ "        }\r\n"
				+ "    },\r\n"
				+ "    \"statusCode\": 101\r\n"
				+ "}";
		JSONObject resp2=new JSONObject(resp);
		data.put("data",resp2.toString());
		return data;
	}
	
}
