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
import org.springframework.stereotype.Component;

import com.suryoday.aocpv.serviceImp.NoSuchElementException;
import com.suryoday.collections.pojo.Insurance;
import com.suryoday.collections.repository.InsuranceRepository;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.twowheeler.service.EmiCalculatorApiService;

@Component
public class EmiCalculatorServImpl implements EmiCalculatorApiService {

	private static Logger logger = LoggerFactory.getLogger(EmiCalculatorServImpl.class);

	@Autowired
	InsuranceRepository insuranceRepository;

	@Override
	public JSONObject emiCalculator(JSONObject jsonObject, JSONObject header) {
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
			obj = new URL(x.BASEURL + "loan/emi/calculator/v2?api_key=" + x.api_key);
			logger.debug(x.BASEURL + "loan/emi/calculator/v2?api_key=" + x.api_key);
			;
//			 obj = new URL("https://brn.suryodaybank.co.in/BRConnectClientNew/v1/BrNetconnect");
//			logger.debug("https://brn.suryodaybank.co.in/BRConnectClientNew/v1/BrNetconnect");
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("X-Request-ID", header.getString("X-Request-ID"));
			con.setRequestProperty("X-User-ID", header.getString("X-User-ID"));
			con.setRequestProperty("X-Transaction-ID", header.getString("X-Transaction-ID"));
			con.setRequestProperty("X-From-ID", header.getString("X-From-ID"));
			con.setRequestProperty("X-To-ID", header.getString("X-To-ID"));
			sendResponse = getResponse(jsonObject, sendResponse, con, "POST");
//				
//				getHeadersRequestInfo(con);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return sendResponse;
	}

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
	public String calculateInsurance(double amountIndouble, String product, String bankName, String tenure) {
		if (tenure.isEmpty()) {
			throw new NoSuchElementException("tenure cannot be blank");
		}

		int time = Integer.parseInt(tenure);
		int updatetime = time;

		if (updatetime <= 12 && updatetime >= 6) {
			updatetime = 12;
		} else if (updatetime <= 24 && updatetime >= 13) {
			updatetime = 24;
		} else if (updatetime <= 36 && updatetime >= 25) {
			updatetime = 36;
		}

		String timeperiod = Integer.toString(updatetime);
		Optional<Insurance> optional = insuranceRepository.fetchProduct(product, bankName, timeperiod);

		if (optional.isPresent()) {
			double maxAmount = optional.get().getMaxAmount();
			double rate = optional.get().getRate();

			rate = rate / updatetime * time;
			double emi = amountIndouble * rate;
			double finalEmi = Math.min(maxAmount, emi);
			String string = Double.toString(finalEmi);
			return string;
		} else {
			List<Insurance> list = insuranceRepository.fetchProductName(product);

			if (list.size() == 0) {
				throw new NoSuchElementException("Product is Not present");
			}

			List<Insurance> list2 = insuranceRepository.fetchBankName(product, bankName);

			if (list2.size() == 0) {
				throw new NoSuchElementException("Current Bank is Not present For this Product");
			} else {
				throw new NoSuchElementException("tenure not present");
			}
		}
	}

}
