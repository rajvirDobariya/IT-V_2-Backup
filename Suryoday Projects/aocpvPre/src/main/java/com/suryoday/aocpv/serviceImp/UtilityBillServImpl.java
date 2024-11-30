package com.suryoday.aocpv.serviceImp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suryoday.aocpv.pojo.BillServiceProviders;
import com.suryoday.aocpv.repository.UtilityBillRepo;
import com.suryoday.aocpv.service.UtilityBillService;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;

@Component
public class UtilityBillServImpl implements UtilityBillService{
	
	private static Logger logger = LoggerFactory.getLogger(UtilityBillServImpl.class);
			
	@Autowired
	UtilityBillRepo billrepo;
	@Override
	public List<BillServiceProviders> fetchServiceProviders() {
		List<BillServiceProviders> list=billrepo.fetchServiceProviders();
		if(list.isEmpty())
		{
			throw new NoSuchElementException("No Record Found");
		}
		return list;
	}
	@Override
	public JSONObject authenticateUtilityBill(JSONObject request, JSONObject header) {
		
		JSONObject sendResponse = new JSONObject();
		
		URL obj = null;
		try {

			ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
			x.getappprop();
			x.bypassssl();
			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			 obj = new URL(x.BASEURL+"electricity/bill?api_key="+x.api_key);
			logger.debug(x.BASEURL+"electricity/bill?api_key="+x.api_key);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("x-karza-key", "UhpgyU8YwWNZK8k"); 
			con.setRequestProperty("X-Request-ID", "TAB");
			
			sendResponse = getResponseData(request, sendResponse, con,"POST");
//				
//				getHeadersRequestInfo(con);

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

}
