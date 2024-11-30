package com.suryoday.twowheeler.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.serviceImp.NoSuchElementException;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.twowheeler.pojo.MandateDetails;
import com.suryoday.twowheeler.repository.MandateDetailsRepo;
import com.suryoday.twowheeler.service.NachMandateService;

@Service
public class NachMandateServiceImpl implements NachMandateService {
	@Autowired
	MandateDetailsRepo mandateDtlsRepo;
	private static Logger logger = LoggerFactory.getLogger(NachMandateServiceImpl.class);

	@Override
	public JSONObject mandateCreation(JSONObject nachRequest) {

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
			obj = new URL(x.BASEURL + "mandate/creation?api_key=" + x.api_key);
			logger.debug(x.BASEURL + "mandate/creation?api_key=" + x.api_key);

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Request-ID", "COR");
			sendResponse = getResponse(nachRequest, sendResponse, con, "POST");

		} catch (Exception e) {

			e.getMessage();
		}

		return sendResponse;
	}

//	private JSONObject getMandateCreationRequest(JSONObject jsonObject) {
//		JSONObject Data = jsonObject.getJSONObject("Data");
//		JSONObject parent=new JSONObject();
//		JSONObject data=new JSONObject();
//		data.put("ReferenceNumber",Data.getString("referenceNumber"));
//		data.put("UtilityCode",Data.getString("utilityCode"));
//		data.put("CategoryCode",Data.getString("categoryCode"));
//		data.put("SchemeName","TWO-WHEELER-LOAN");
//		data.put("ConsumerReferenceNumber",Data.getString("consumerReferenceNumbe"));
//		data.put("MandateType","RCUR");
//		data.put("Frequency","MNTH");
//		data.put("FirstCollectionDate",Data.getString("consumerReferenceNumbe"));
//		data.put("FinalCollectionDate",Data.getString("consumerReferenceNumbe"));
//		data.put("AmountType","MAXA");
//		data.put("CollectionAmount","10");
//		data.put("CustomerName","Abhinav");
//		data.put("Landline","");
//		data.put("MobileNumber","9723481316");
//		data.put("EmailId","ypandit930@gmail.com");
//		data.put("Pan","ABCDE0011A");
//		data.put("BankId","AGCX");
//		data.put("AccountType","SAVINGS");
//		data.put("AccountNumber","3456789878733");
//		parent.put("Data",data);
//		System.out.println("request :"+parent);
//		return parent;
//	}

	private static JSONObject getResponse(JSONObject parent, JSONObject sendAuthenticateResponse, HttpURLConnection con,
			String MethodType) throws IOException {

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
	public JSONObject fetchMandate(JSONObject jsonObject) {
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
			obj = new URL(x.BASEURLV2 + "emandate/status?api_key=" + x.api_key);
			logger.debug(x.BASEURLV2 + "emandate/status?api_key=" + x.api_key);

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("X-Request-ID", "PCP");
			con.setRequestProperty("x-api-key", "8026c547-e15e-4baa-a1e6-f390bf393101");
			sendResponse = getResponse(jsonObject, sendResponse, con, "POST");

		} catch (Exception e) {

			e.getMessage();
		}

		return sendResponse;
	}

	@Override
	public List<MandateDetails> fetchMandateDetails(String applicationno) {
		List<MandateDetails> list = mandateDtlsRepo.fetchMandateDetails(applicationno);
		if (list.isEmpty()) {
			throw new NoSuchElementException("No record found");
		}
		return list;

	}

	@Override
	public void savemandate(JSONObject jsonObject, String applicationNo, String reference) {
		String referenceNo = jsonObject.getJSONObject("Data").getString("ReferenceNumber");
		Optional<MandateDetails> optional = mandateDtlsRepo.getByApplicationNoAndReferenceNo(applicationNo,
				referenceNo);
		if (optional.isPresent()) {

		} else {
			MandateDetails mandateDetails = new MandateDetails();
			mandateDetails.setApplicationNo(applicationNo);
			mandateDetails.setReferenceNo(referenceNo);
			mandateDetails.setUtilityCode(jsonObject.getJSONObject("Data").getString("UtilityCode"));
			mandateDetails.setCategoryCode(jsonObject.getJSONObject("Data").getString("CategoryCode"));
			mandateDetails.setSchemeName(jsonObject.getJSONObject("Data").getString("SchemeName"));
			mandateDetails
					.setConsumerReferenceNo(jsonObject.getJSONObject("Data").getString("ConsumerReferenceNumber"));
			mandateDetails.setMandateType(jsonObject.getJSONObject("Data").getString("MandateType"));
			mandateDetails.setFrequency(jsonObject.getJSONObject("Data").getString("Frequency"));
			mandateDetails.setFirstCollectionDate(jsonObject.getJSONObject("Data").getString("FirstCollectionDate"));
			mandateDetails.setFinalCollectionDate(jsonObject.getJSONObject("Data").getString("FinalCollectionDate"));
			mandateDetails.setAmountType(jsonObject.getJSONObject("Data").getString("AmountType"));
			mandateDetails.setCollectionAmount(jsonObject.getJSONObject("Data").getString("CollectionAmount"));
			mandateDetails.setCustomerName(jsonObject.getJSONObject("Data").getString("CustomerName"));
			mandateDetails.setEmailId(jsonObject.getJSONObject("Data").getString("EmailId"));
			mandateDetails.setMobileNo(jsonObject.getJSONObject("Data").getString("MobileNumber"));
			mandateDetails.setBankId(jsonObject.getJSONObject("Data").getString("BankId"));
			mandateDetails.setAccountNo(jsonObject.getJSONObject("Data").getString("AccountNumber"));
			mandateDetails.setAccountType(jsonObject.getJSONObject("Data").getString("AccountType"));
			mandateDetails.setPan(jsonObject.getJSONObject("Data").getString("Pan"));
			mandateDetails.setReference(reference);
			mandateDetails.setRequestStatus("Initiated");
			mandateDtlsRepo.save(mandateDetails);
		}
	}

	@Override
	public MandateDetails getByApplicationNoAndReference(String applicationNo, String referenceNo) {
		Optional<MandateDetails> optional = mandateDtlsRepo.getByApplicationNoAndReferenceNo(applicationNo,
				referenceNo);
		if (optional.isPresent()) {
			return optional.get();
		}
		throw new NoSuchElementException("No record found");
	}

	@Override
	public void save(MandateDetails mandateDetails) {
		mandateDtlsRepo.save(mandateDetails);

	}

	@Override
	public List<MandateDetails> getMandateDetails(String applicationNo) {
		List<MandateDetails> list = mandateDtlsRepo.fetchMandateDetails(applicationNo);
		return list;
	}
}
