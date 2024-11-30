package com.suryoday.roaocpv.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.pojo.AocpCustomer;
import com.suryoday.aocpv.pojo.AocpvImages;
import com.suryoday.aocpv.service.AocpCustomerDataService;
import com.suryoday.aocpv.service.AocpvImageService;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.roaocpv.service.WhatsappMediaSendService;

@Service
public class WhatsappMediaSendServiceImpl implements WhatsappMediaSendService {

	@Autowired
	AocpvImageService aocpvImageService;

	@Autowired
	AocpCustomerDataService aocpCustomerDataService;

	private static Logger logger = LoggerFactory.getLogger(WhatsappMediaSendServiceImpl.class);

	@Override
	public JSONObject sendMedia(JSONObject jsonObject, JSONObject header) {
		JSONObject sendResponse = new JSONObject();
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");
		String documentType = jsonObject.getJSONObject("Data").getString("documentType");
		JSONObject request = getRequest(applicationNo, documentType);
		System.out.println(request.toString());
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
			logger.debug(x.BASEURLV3 + "whatsapp/media/template/push?api_key=" + x.api_key);
			obj = new URL(x.BASEURLV3 + "whatsapp/media/template/push?api_key=" + x.api_key);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Correlation-ID", header.getString("X-Correlation-ID"));
			con.setRequestProperty("X-Request-ID", header.getString("X-Request-ID"));
			sendResponse = getResponseData(request, sendResponse, con, "POST");

		} catch (Exception e) {

			e.printStackTrace();
		}

		return sendResponse;
	}

	private JSONObject getRequest(String applicationNo, String documentType) {
		long applicationNoInLong = Long.parseLong(applicationNo);
		AocpvImages aocpvImages = aocpvImageService.getByApplicationNoAndName(applicationNoInLong, documentType);
		byte[] images = aocpvImages.getImages();
		String base64 = Base64.getEncoder().encodeToString(images);
		AocpCustomer aocpCustomer = aocpCustomerDataService.getByApp(applicationNoInLong);
		org.json.JSONObject request = new org.json.JSONObject();
		org.json.simple.JSONObject Data = new org.json.simple.JSONObject();
		org.json.simple.JSONArray Messages = new org.json.simple.JSONArray();
		org.json.simple.JSONArray Parameters = new org.json.simple.JSONArray();
		org.json.simple.JSONObject Parameter1 = new org.json.simple.JSONObject();
		org.json.simple.JSONObject Parameter2 = new org.json.simple.JSONObject();
		org.json.simple.JSONObject massage = new org.json.simple.JSONObject();
		massage.put("Sender", "919990082544");
		massage.put("To", "91" + aocpCustomer.getMobileNo());
		massage.put("MessageId", "2993b6b54800sfsdffa51");
		massage.put("TransactionId", "2993b6b548000aasddfsd");
		massage.put("Channel", "wa");
		massage.put("Type", "mediaTemplate");
		Parameter1.put("Name", "1");
		Parameter1.put("Value", aocpCustomer.getName());
		Parameter2.put("Name", "2");
		Parameter2.put("Value", "225020000070");
		Parameters.add(Parameter1);
		Parameters.add(Parameter2);
		org.json.simple.JSONObject MediaTemplate = new org.json.simple.JSONObject();
		MediaTemplate.put("ContentType", "application/pdf");
		MediaTemplate.put("Template", "praprloanscnltr");
		MediaTemplate.put("LangCode", "en");
		MediaTemplate.put("Filename", "SANCTION_JAYSHREE JITENDRA PATIL_180258449_919607014005");
		MediaTemplate.put("Content", base64);
		MediaTemplate.put("Parameters", Parameters);
		massage.put("MediaTemplate", MediaTemplate);
		Messages.add(massage);

		Data.put("Messages", Messages);
		Data.put("ResponseType", "json");
		request.put("Data", Data);
		return request;
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
	public JSONObject sendMessage(JSONObject jsonObject, JSONObject header) {
		JSONObject sendResponse = new JSONObject();
		String applicationNo = jsonObject.getJSONObject("Data").getString("applicationNo");

		JSONObject request = getRequestforMessage(applicationNo);
		System.out.println(request.toString());
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
			logger.debug(x.BASEURLV2 + "whatsapp/media/template/push?api_key=" + x.api_key);
			obj = new URL(x.BASEURLV2 + "whatsapp/media/template/push?api_key=" + x.api_key);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Correlation-ID", header.getString("X-Correlation-ID"));
			con.setRequestProperty("X-Request-ID", header.getString("X-Request-ID"));
			sendResponse = getResponseData(request, sendResponse, con, "POST");

		} catch (Exception e) {

			e.printStackTrace();
		}

		return sendResponse;
	}

	private JSONObject getRequestforMessage(String applicationNo) {
		long applicationNoInLong = Long.parseLong(applicationNo);
		AocpCustomer aocpCustomer = aocpCustomerDataService.getByApp(applicationNoInLong);
		org.json.JSONObject request = new org.json.JSONObject();
		org.json.simple.JSONObject Data = new org.json.simple.JSONObject();
		org.json.simple.JSONArray Messages = new org.json.simple.JSONArray();
		org.json.simple.JSONArray body = new org.json.simple.JSONArray();
		org.json.simple.JSONObject Parameter1 = new org.json.simple.JSONObject();
		org.json.simple.JSONObject Parameter2 = new org.json.simple.JSONObject();
		org.json.simple.JSONObject massage = new org.json.simple.JSONObject();
		massage.put("Sender", "917875682343");
		massage.put("To", aocpCustomer.getMobileNo());
		massage.put("MessageId", "2993b6b54800sfsdffa51");
		massage.put("TransactionId", "2993b6b548000aasddfsd");
		massage.put("Channel", "wa");
		massage.put("Type", "template");
		Parameter1.put("type", "text");
		Parameter1.put("text", aocpCustomer.getName());
		Parameter2.put("type", "text");
		Parameter2.put("text", "https://10.20.2515:443/agent-onboarding/");
		body.add(Parameter1);
		body.add(Parameter2);
		org.json.simple.JSONObject Template = new org.json.simple.JSONObject();
		Template.put("TemplateId", "agentapprovedwasms");
		Template.put("LangCode", "en");
		Template.put("body", body);
		massage.put("Template", Template);
		Messages.add(massage);

		Data.put("Messages", Messages);
		Data.put("ResponseType", "json");
		request.put("Data", Data);

		return request;
	}

}
