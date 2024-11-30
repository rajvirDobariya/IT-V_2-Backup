package com.suryoday.dsaOnboard.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.connector.serviceImpl.GenerateProperty;
import com.suryoday.dsaOnboard.pojo.DsaOnBoardLeegalityInfo;
import com.suryoday.dsaOnboard.pojo.DsaOnboardDetails;
import com.suryoday.dsaOnboard.repository.DsaOnBoardLeegalityRepository;
import com.suryoday.dsaOnboard.service.DsaOnBoardLeegalityService;

@Service
public class DsaOnBoardLeegalityServiceImpl implements DsaOnBoardLeegalityService{

	private static Logger logger = LoggerFactory.getLogger(DsaOnBoardLeegalityServiceImpl.class);
	
	@Autowired
	DsaOnBoardLeegalityRepository leegalityRepository;
	
	@Override
	public JSONObject sendLeegality(DsaOnboardDetails dsaOnboardDetails, String base64pdf, String documentType) {
		JSONObject sendResponse = new JSONObject();
		JSONObject request = getRequest(dsaOnboardDetails, base64pdf,documentType);
		

		
		URL obj = null;
		try {

			GenerateProperty x = GenerateProperty.getInstance();
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
			logger.debug(x.BASEURL + "leegality/create?api_key=" + x.api_key);
			obj = new URL(x.BASEURL + "leegality/create?api_key=" + x.api_key);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");

			sendResponse = getResponseData(request, sendResponse, con, "POST");
			sendResponse.put("request", request);
		} catch (Exception e) {

			e.printStackTrace();
		}

		return sendResponse;
	}

	private JSONObject getRequest(DsaOnboardDetails dsaOnboardDetails, String base64pdf, String documentType) {
		
		JSONObject parent = new JSONObject();
		JSONObject data = new JSONObject();
		if(documentType.equals("connectorServiceAgreement")) {
			data.put("ProfileId", "Aejut7P");
			data.put("StampValue", "0");
		}else if(documentType.equals("dsaAgreement")) {
			data.put("ProfileId", "MLQMqY9");
		}
	
		JSONObject file = new JSONObject();
		file.put("Name", documentType);
		file.put("File", base64pdf);
		data.put("File", file);
		
		JSONArray invitees = new JSONArray();
		JSONObject invitees1 = new JSONObject();
		invitees1.put("Name", dsaOnboardDetails.getCompanyName());
		invitees1.put("Email", dsaOnboardDetails.getEmailId());
		invitees1.put("Phone", dsaOnboardDetails.getMobileNo());
		invitees.put(invitees1);
		JSONObject invitees2 = new JSONObject();
		invitees2.put("Name", "");
		invitees2.put("Email", "");
		invitees2.put("Phone", "");
		invitees.put(invitees2);
		data.put("Invitees", invitees);
		JSONArray cc = new JSONArray();
		JSONObject cc1 = new JSONObject();
		cc1.put("Name", "Mohit Arya");
		cc1.put("Email", "mohit.arya@suryodaybank.com");
		cc.put(cc1);
		data.put("CC", cc);
		data.put("Irn", "Chintan_220994");
		parent.put("Data", data);
		System.out.println(parent);
		return parent;
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
	
//	@Override
//	public void updateDocumentId(JSONObject data, String applicationNo, String documentType) {
//		String documentId = data.getJSONObject("Data").getString("DocumentId");
//		JSONArray invitees = data.getJSONObject("Data").getJSONArray("Invitees");
//		
//			List<DsaOnBoardLeegalityInfo> listDB =leegalityRepository.getByApplicationNoAndDocument(applicationNo,documentType);
//			if(listDB.size() == 0) {
//			List<DsaOnBoardLeegalityInfo> list=new ArrayList<>();
//			for(int n=0;n<invitees.length();n++) {
//				JSONObject jsonObject = invitees.getJSONObject(n);
//				
//				if(!jsonObject.get("SignUrl").equals(null)) {
//					
//					String Email ="";
//					String Name ="";
//					if(!jsonObject.get("Email").equals(null)) {
//						 Email = jsonObject.getString("Email");
//					}
//					if(!jsonObject.get("Name").equals(null)) {
//						Name = jsonObject.getString("Name");
//					}
//					String Phone = "";
//					String SignUrl = jsonObject.getString("SignUrl");
//					String Active = jsonObject.getString("Active");
//					String ExpiryDate = jsonObject.getString("ExpiryDate");
//					DsaOnBoardLeegalityInfo info =new DsaOnBoardLeegalityInfo(applicationNo,Active,Email,ExpiryDate,Name,Phone,SignUrl,documentId,"false",documentType);
//				
//					list.add(info);
//				}
//			}
//			leegalityRepository.saveAll(list);
//			}
//
//		
//	}
	
//--------------------------------------------------------
	///Sidharam Yalasangi method add Changes
	@Override
	public void updateDocumentId(JSONObject data, String applicationNo, String documentType) {
	    String documentId = data.getJSONObject("Data").getString("DocumentId");
	    JSONArray invitees = data.getJSONObject("Data").getJSONArray("Invitees");

	    List<DsaOnBoardLeegalityInfo> existingRecords = leegalityRepository.getByApplicationNoAndDocument(applicationNo, documentType);

	    if (!existingRecords.isEmpty()) {
	        leegalityRepository.deleteAll(existingRecords);
	    }

	    List<DsaOnBoardLeegalityInfo> newRecords = new ArrayList<>();

	    for (int n = 0; n < invitees.length(); n++) {
	        JSONObject jsonObject = invitees.getJSONObject(n);

	        if (!jsonObject.isNull("SignUrl")) {
	            String email = jsonObject.optString("Email", "");
	            String name = jsonObject.optString("Name", "");
	            String phone = jsonObject.optString("Phone", ""); // Assuming phone can be included
	            String signUrl = jsonObject.getString("SignUrl");
	            String active = jsonObject.getString("Active");
	            String expiryDate = jsonObject.getString("ExpiryDate");

	            DsaOnBoardLeegalityInfo info = new DsaOnBoardLeegalityInfo(
	                applicationNo,
	                active,
	                email,
	                expiryDate,
	                name,
	                phone,
	                signUrl,
	                documentId,
	                "false",
	                documentType
	            );

	            newRecords.add(info);
	        }
	    }

	    if (!newRecords.isEmpty()) {
	        leegalityRepository.saveAll(newRecords);
	    }
	}

//--------------------------------------------------------
	
	

	@Override
	public List<DsaOnBoardLeegalityInfo> getByApplicationNoAndDocument(String applicationNo, String documentType) {
		List<DsaOnBoardLeegalityInfo> list =leegalityRepository.getByApplicationNoAndDocument(applicationNo,documentType);
		if(list.size()==0) {
			throw new NoSuchElementException("No Record Found");
		}
		return list;
	}

	@Override
	public JSONObject checkLeegality(String applicationNo, JSONObject header, String documentType, String documentId) {
		JSONObject sendResponse = new JSONObject();
		URL obj = null;
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

			// String transactionType="&TransactionType=D";
			logger.debug(x.BASEURL + "leegality/status/check/" + documentId + "?api_key=" + x.api_key);
			obj = new URL(x.BASEURL + "leegality/status/check/" + documentId + "?api_key=" + x.api_key);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept", "application/json");
			sendResponse = getResponse(documentId, sendResponse, con, "GET");

		} catch (Exception e) {

			e.printStackTrace();
		}

		return sendResponse;

	}

	private static JSONObject getResponse(String parent, JSONObject sendAuthenticateResponse, HttpURLConnection con,
			String MethodType) throws IOException {

		con.setDoOutput(true);
		// OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
		// os.write(parent.toString());
		// os.flush();
		// os.close();

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
			logger.debug("GET request not worked");

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
	public List<DsaOnBoardLeegalityInfo> updateLeegalityVerify(String applicationNo, JSONObject data, String documentType) {
		List<DsaOnBoardLeegalityInfo> listDB =leegalityRepository.getByApplicationNoAndDocument(applicationNo,documentType);
		if(listDB.size()== 0) {
			
		}
		for(DsaOnBoardLeegalityInfo info:listDB) {
			String name = data.getJSONObject("Data").getJSONArray("Requests").getJSONObject(0).getString("Name");
			String name1 = data.getJSONObject("Data").getJSONArray("Requests").getJSONObject(1).getString("Name");
			if(info.getName().equals(name)) {
				String signed1 = data.getJSONObject("Data").getJSONArray("Requests").getJSONObject(0).getString("Signed");
				info.setSigned(signed1);
			}else if(info.getName().equals(name1)) {
				String signed2 = data.getJSONObject("Data").getJSONArray("Requests").getJSONObject(1).getString("Signed");
				info.setSigned(signed2);
			}
			leegalityRepository.save(info);
		}
		return listDB;

	}

}
