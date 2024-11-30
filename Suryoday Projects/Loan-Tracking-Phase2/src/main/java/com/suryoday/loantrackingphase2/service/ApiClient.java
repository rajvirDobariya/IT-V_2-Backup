package com.suryoday.loantrackingphase2.service;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.suryoday.loantrackingphase2.exception.ValidationException;

@Service
public class ApiClient {

	private static final String BASEURL = "https://intramashery.suryodaybank.co.in/";

	public JSONObject getCustomerDetails(String customerNo) {
		String responseString = "";

		try {
			// Create SSL context to bypass certificate verification
			SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial((chain, authType) -> true).build();

			// Disable hostname verification
			CloseableHttpClient httpClient = HttpClientBuilder.create().setSSLContext(sslContext)
					.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();

			// Construct the API URL
			String url = BASEURL + "ssfbuat/customers/details/v2?api_key=kyqak5muymxcrjhc5q57vz9v";
			HttpPost postRequest = new HttpPost(url);

			// Add Headers
			postRequest.setHeader("X-Request-ID", "IBR");
			postRequest.setHeader("X-User-ID", "30639");
			postRequest.setHeader("X-From-ID", "TAB");
			postRequest.setHeader("Content-Type", "application/json");

			// Create JSON body as per the curl command
			JSONObject requestBody = new JSONObject();
			JSONObject data = new JSONObject();
			data.put("ProductGroup", "CASA");
			data.put("CustomerNo", customerNo);
			requestBody.put("Data", data);

			// Set the JSON body to the request
			StringEntity entity = new StringEntity(requestBody.toString());
			postRequest.setEntity(entity);

			// Execute the POST request
			HttpResponse response = httpClient.execute(postRequest);
			responseString = EntityUtils.toString(response.getEntity());

		} catch (Exception e) {
			throw new ValidationException("Customer details not found with this customer no :" + customerNo + "!");
		}
		JSONObject response = new JSONObject(responseString);
		return response;
	}

	public JSONObject getLoanAccountDetails(String accountNo) {
		String responseString = "";

		try {
			// Create SSL context to bypass certificate verification
			SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial((chain, authType) -> true).build();

			// Disable hostname verification
			CloseableHttpClient httpClient = HttpClientBuilder.create().setSSLContext(sslContext)
					.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();

			// Construct the API URL
			String url = BASEURL + "ssfbuat/pu/loan/account/details/" + accountNo
					+ "/v2?api_key=kyqak5muymxcrjhc5q57vz9v";
			HttpPost postRequest = new HttpPost(url);

			// Add Headers
			postRequest.setHeader("Accept", "application/json");
			postRequest.setHeader("Content-Type", "application/json");
			postRequest.setHeader("X-Request-ID", "NOV");
			postRequest.setHeader("cache-control", "no-cache");

			// No JSON body needed for this request

			// Execute the POST request
			HttpResponse response = httpClient.execute(postRequest);
			responseString = EntityUtils.toString(response.getEntity());

		} catch (Exception e) {
			e.printStackTrace(); // Log the error in production
		}

		// Parse the response string into a JSONObject
		JSONObject response = new JSONObject(responseString);
		return response;
	}
}
