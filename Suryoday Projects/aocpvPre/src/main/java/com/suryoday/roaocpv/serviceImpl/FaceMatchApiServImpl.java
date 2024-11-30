package com.suryoday.roaocpv.serviceImpl;

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

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.roaocpv.pojo.ApplicationDetails;
import com.suryoday.roaocpv.service.ApplicationDetailsService;
import com.suryoday.roaocpv.service.FaceMatchApiService;
@Component
@Service
public class FaceMatchApiServImpl implements FaceMatchApiService{
	private static Logger logger = LoggerFactory.getLogger(FaceMatchApiServImpl.class);
	
	@Autowired
	ApplicationDetailsService applicationDetailsService;	
	
	@Override
	public JSONObject faceMatch(List<String> list, JSONObject header) {
		
			String image1=list.get(0);
			String image2=list.get(1);
			JSONObject request=getRequest(image1,image2);
			JSONObject sendResponse = new JSONObject();
//			String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
//			Long applicationNo1 = Long.parseLong( applicationNo);
//			JSONObject request = getRequest(applicationNo1);

			URL obj = null;
			try {

				ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
				x.getappprop();
				// GenerateProperty x = GenerateProperty.getInstance();
				x.bypassssl();
				// Create all-trusting host name verifier
				HostnameVerifier allHostsValid = new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				};

				HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
				logger.debug(x.BASEURL+"face/similarity?api_key="+ x.api_key);

				obj = new URL(x.BASEURL+"face/similarity?api_key="+ x.api_key);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/json");
				con.setRequestProperty("X-Request-ID", header.getString("X-Request-ID"));
				con.setRequestProperty("X-User-ID", header.getString("X-User-ID"));
				con.setRequestProperty("X-From-ID", header.getString("X-From-ID"));
				con.setRequestProperty("X-To-ID", header.getString("X-To-ID"));
				con.setRequestProperty("X-Transaction-ID", header.getString("X-Transaction-ID"));
				con.setRequestProperty("X-Correlation-ID", header.getString("X-Correlation-ID"));

				sendResponse = getResponseData(request, sendResponse, con, "POST");

			} catch (Exception e) {

				e.printStackTrace();
			}

			return sendResponse;
		}

		private JSONObject getRequest(String image1,String image2) {
			
			JSONObject Data = new JSONObject();
			
			JSONObject data= new JSONObject();
		    data.put("Image1B64",image1);
		    data.put("Image2B64",image2);
		    Data.put("Data", data);
			
			return Data;
		}

		private static JSONObject getResponseData(JSONObject parent, JSONObject sendAuthenticateResponse,
				HttpURLConnection con, String MethodType) throws IOException {

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

				JSONObject sendauthenticateResponse1 = new JSONObject();
				sendauthenticateResponse1.put("data", response.toString());
				sendAuthenticateResponse = sendauthenticateResponse1;
			} else {
				logger.debug("POST request not worked");

				JSONObject sendauthenticateResponse1 = new JSONObject();

				JSONObject errr = new JSONObject();
				errr.put("Description", "Server Error " + responseCode);

				JSONObject j = new JSONObject();
				j.put("Error", errr);

				sendauthenticateResponse1.put("data", "" + j);
				sendAuthenticateResponse = sendauthenticateResponse1;
			}

			return sendAuthenticateResponse;

		}

		@Override
		public JSONObject nameMatch(JSONObject jsonObject, JSONObject header) {
			JSONObject sendResponse=new JSONObject();
			String ApplicationNo=jsonObject.getJSONObject("Data").getString("ApplicationNo");
			String nameOnPan=null;
			String ekycName=null;
			ApplicationDetails fetchByApplicationId = applicationDetailsService.fetchByApplicationId(ApplicationNo);
			if(fetchByApplicationId.getPanCardResponse() != null) {
				org.json.JSONObject pancard=new org.json.JSONObject(fetchByApplicationId.getPanCardResponse());
				 nameOnPan = pancard.getJSONObject("Data").getJSONArray("PANDetails").getJSONObject(0).getString("NameOnCard");	
			}
			if(fetchByApplicationId.getEkycResponse() != null) {
				org.json.JSONObject ekyc=new org.json.JSONObject(fetchByApplicationId.getEkycResponse());	
				 ekycName = ekyc.getString("name");
			}
			if(nameOnPan == null && ekycName == null) {
				
			}
			JSONObject request1=getRequest1(ekycName,nameOnPan);
			URL obj = null;
			try {

				ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
				x.getappprop();
				// GenerateProperty x = GenerateProperty.getInstance();
				x.bypassssl();
				// Create all-trusting host name verifier
				HostnameVerifier allHostsValid = new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				};

				HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
				logger.debug(x.BASEURLV3+"find/name/similarity?api_key="+ x.api_key);
				obj = new URL(x.BASEURLV3+"find/name/similarity?api_key="+ x.api_key);
				
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/json");
				con.setRequestProperty("X-Request-ID", header.getString("X-Request-ID"));
				con.setRequestProperty("X-User-ID", header.getString("X-User-ID"));
				con.setRequestProperty("X-From-ID", header.getString("X-From-ID"));
				con.setRequestProperty("X-To-ID", header.getString("X-To-ID"));
				con.setRequestProperty("X-Transaction-ID", header.getString("X-Transaction-ID"));
				con.setRequestProperty("X-Correlation-ID", header.getString("X-Correlation-ID"));

				sendResponse = getResponseData2(request1, sendResponse, con, "POST");

			} catch (Exception e) {

				e.printStackTrace();
			}

			return sendResponse;
		}
		
		private JSONObject getRequest1(String name1, String name2) {
			JSONObject Data=new JSONObject();
			JSONObject data=new JSONObject();
			data.put("Name1",name1);
			data.put("Name2",name2);
			data.put("Type","individual");
			data.put("Preset","L");
			Data.put("Data",data);
			return Data;
		}

		private static JSONObject getResponseData2(JSONObject parent, JSONObject sendAuthenticateResponse,
				HttpURLConnection con, String MethodType) throws IOException {

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

				JSONObject sendauthenticateResponse1 = new JSONObject();
				sendauthenticateResponse1.put("data", response.toString());
				sendAuthenticateResponse = sendauthenticateResponse1;
			} else {
				logger.debug("POST request not worked");

				JSONObject sendauthenticateResponse1 = new JSONObject();

				JSONObject errr = new JSONObject();
				errr.put("Description", "Server Error " + responseCode);

				JSONObject j = new JSONObject();
				j.put("Error", errr);

				sendauthenticateResponse1.put("data", "" + j);
				sendAuthenticateResponse = sendauthenticateResponse1;
			}

			return sendAuthenticateResponse;

		}

	}


