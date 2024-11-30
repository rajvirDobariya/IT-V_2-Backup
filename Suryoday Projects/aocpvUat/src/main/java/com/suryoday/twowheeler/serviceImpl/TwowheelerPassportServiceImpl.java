package com.suryoday.twowheeler.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.twowheeler.pojo.TwoWheelerFamilyMember;
import com.suryoday.twowheeler.service.TwowheelerFamilyMemberService;
import com.suryoday.twowheeler.service.TwowheelerPassportService;

@Service
public class TwowheelerPassportServiceImpl implements TwowheelerPassportService {

	@Autowired
	TwowheelerFamilyMemberService familyMemberService;

	private static Logger logger = LoggerFactory.getLogger(TwowheelerPassportServiceImpl.class);

	@Override
	public JSONObject verifyPassport(String passportNo, String doi, JSONObject header, String applicationNo,
			String member) {
		JSONObject sendResponse = new JSONObject();
		JSONObject request = getRequest(passportNo, doi, applicationNo, member);

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
			logger.debug(x.BASEURLV2 + "karza/verify/passport?api_key=" + x.api_key);
			obj = new URL(x.BASEURLV2 + "karza/verify/passport?api_key=" + x.api_key);

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Request-ID", header.getString("X-Request-ID"));

			sendResponse = getResponseData(request, sendResponse, con, "POST");
		} catch (Exception e) {

			e.printStackTrace();
		}

		return sendResponse;
	}

	private JSONObject getRequest(String passportNo, String doi, String applicationNo, String member) {
		JSONObject parent = new JSONObject();

		TwoWheelerFamilyMember familyMember = familyMemberService.fetchByApplicationNoAndMember(applicationNo, member);

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String dateOfBirth = familyMember.getDob();
		LocalDate dob = LocalDate.parse(dateOfBirth, dateTimeFormatter);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String dobString = dob.format(formatter);

		parent.put("consent", "Y");
		parent.put("fileNo", "");
		parent.put("dob", dobString);
		parent.put("passportNo", passportNo);
		parent.put("doi", doi);
		parent.put("name", familyMember.getFirstName());
		logger.debug("request" + parent);
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

}
