package com.suryoday.twowheeler.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
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
import com.suryoday.twowheeler.pojo.LeegalityInfo;
import com.suryoday.twowheeler.pojo.TwowheelerDetailesTable;
import com.suryoday.twowheeler.repository.LeegalityInfoRepository;
import com.suryoday.twowheeler.service.TwowheelerDetailsService;
import com.suryoday.twowheeler.service.TwowheelerLeegalityService;

@Service
public class TwowheelerLeegalityServiceImpl implements TwowheelerLeegalityService {

	private static Logger logger = LoggerFactory.getLogger(TwowheelerLeegalityServiceImpl.class);

	@Autowired
	LeegalityInfoRepository leegalityInfoRepository;

	@Autowired
	TwowheelerDetailsService twowheelerDetailsService;

	@Override
	public JSONObject sendLeegality(String applicationNo, String base64pdf, String documentType) {

		JSONObject sendResponse = new JSONObject();
//		String applicationNo =jsonObject.getJSONObject("Data").getString("ApplicationNo");
//		String base64pdf=jsonObject.getJSONObject("Data").getString("Base64Pdf");
		JSONObject request = getRequest(applicationNo, base64pdf, documentType);
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

	private JSONObject getRequest(String applicationNo, String base64pdf, String documentType) {
		TwowheelerDetailesTable byApplication = twowheelerDetailsService.getByApplication(applicationNo);

		JSONObject parent = new JSONObject();
		JSONObject data = new JSONObject();
		if (documentType.equals("loanAgreement")) {
			data.put("ProfileId", "CJLS5A2");
			data.put("StampValue", "20");
		} else if (documentType.equals("sanctionLetter")) {
			data.put("ProfileId", "MLQMqY9");
		}

		JSONObject file = new JSONObject();
		file.put("Name", "Sound box");
		file.put("File", base64pdf);
		data.put("File", file);

		JSONArray invitees = new JSONArray();
		JSONObject invitees1 = new JSONObject();
		invitees1.put("Name", byApplication.getName());
		invitees1.put("Email", "");
		invitees1.put("Phone", byApplication.getMobileNo());
		invitees.put(invitees1);
		JSONObject invitees2 = new JSONObject();
		invitees2.put("Name", "");
		invitees2.put("Email", "");
		invitees2.put("Phone", "");
		invitees.put(invitees2);
		data.put("Invitees", invitees);
		JSONArray cc = new JSONArray();
		JSONObject cc1 = new JSONObject();
		cc1.put("Name", "Chintan");
		cc1.put("Email", "chintan.takwani1@suryodaybank.com");
		cc.put(cc1);
		data.put("CC", cc);
		data.put("Irn", "Chintan_220994");
		parent.put("Data", data);
		byApplication.setSanctiondate(LocalDate.now());
		twowheelerDetailsService.saveData(byApplication);
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

	@Override
	public void updateDocumentId(JSONObject data, String applicationNo, String documentType) {
		String documentId = data.getJSONObject("Data").getString("DocumentId");
		JSONArray invitees = data.getJSONObject("Data").getJSONArray("Invitees");

		List<LeegalityInfo> listDB = leegalityInfoRepository.getByApplicationNoAndDocument(applicationNo, documentType);
		if (listDB.size() == 0) {
			List<LeegalityInfo> list = new ArrayList<>();
			for (int n = 0; n < invitees.length(); n++) {
				JSONObject jsonObject = invitees.getJSONObject(n);

				if (!jsonObject.get("SignUrl").equals(null)) {

					String Email = "";
					String Name = "";
					if (!jsonObject.get("Email").equals(null)) {
						Email = jsonObject.getString("Email");
					}
					if (!jsonObject.get("Name").equals(null)) {
						Name = jsonObject.getString("Name");
					}
					String Phone = "";
					String SignUrl = jsonObject.getString("SignUrl");
					String Active = jsonObject.getString("Active");
					String ExpiryDate = jsonObject.getString("ExpiryDate");
					LeegalityInfo info = new LeegalityInfo(applicationNo, Active, Email, ExpiryDate, Name, Phone,
							SignUrl, documentId, "false", documentType);

					list.add(info);
				}
			}
			leegalityInfoRepository.saveAll(list);
		}
	}

	@Override
	public JSONObject checkLeegality(String applicationNo, JSONObject header, String documentType) {
		List<LeegalityInfo> listDB = leegalityInfoRepository.getByApplicationNoAndDocument(applicationNo, documentType);
		if (listDB.size() == 0) {
			throw new NoSuchElementException("No recoed Present");
		}
		String documentId = listDB.get(0).getDocumentId();
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
	public List<LeegalityInfo> updateLeegalityVerify(String applicationNo, JSONObject data, String documentType) {
		List<LeegalityInfo> listDB = leegalityInfoRepository.getByApplicationNoAndDocument(applicationNo, documentType);
		if (listDB.size() == 0) {

		}
		for (LeegalityInfo info : listDB) {
			String name = data.getJSONObject("Data").getJSONArray("Requests").getJSONObject(0).getString("Name");
			String name1 = data.getJSONObject("Data").getJSONArray("Requests").getJSONObject(1).getString("Name");
			if (info.getName().equals(name)) {
				String signed1 = data.getJSONObject("Data").getJSONArray("Requests").getJSONObject(0)
						.getString("Signed");
				info.setSigned(signed1);
			} else if (info.getName().equals(name1)) {
				String signed2 = data.getJSONObject("Data").getJSONArray("Requests").getJSONObject(1)
						.getString("Signed");
				info.setSigned(signed2);
			}
			leegalityInfoRepository.save(info);
		}
		return listDB;
	}

	@Override
	public List<LeegalityInfo> getByApplicationNoAndDocument(String applicationNo, String documentType) {
		List<LeegalityInfo> list = leegalityInfoRepository.getByApplicationNoAndDocument(applicationNo, documentType);
		if (list.size() == 0) {
			throw new NoSuchElementException("No Record Found");
		}
		return list;
	}

}
