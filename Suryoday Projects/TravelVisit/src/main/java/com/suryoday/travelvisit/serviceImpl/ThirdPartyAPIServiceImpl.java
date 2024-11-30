package com.suryoday.travelvisit.serviceImpl;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.travelvisit.exception.TravelVisitException;
import com.suryoday.travelvisit.service.ThirdPartyAPIService;
import com.suryoday.travelvisit.utils.EncryptDecryptHelper;

@Service
public class ThirdPartyAPIServiceImpl implements ThirdPartyAPIService {
	Logger LOG = LoggerFactory.getLogger(ThirdPartyAPIServiceImpl.class);

	@Autowired
	private EncryptDecryptHelper encryptDecryptHelper;

	@Override
	public JSONObject getCustDetailsByCustId(String request, String channel, String X_Session_ID, String x_User_ID,
			boolean isEncrypt) {
		JSONObject response = new JSONObject();
		JSONObject responseData = new JSONObject();
		JSONObject requestJson = new JSONObject();
		// 1 VALIDATE & DECRYPT REQUEST
		if (isEncrypt) {
			requestJson = encryptDecryptHelper.validateHeadersAndSessionId(request, channel, X_Session_ID);
		} else {
			requestJson = new JSONObject(request);
		}

		// 4. PROCESS
		LOG.debug("DecryptRequestString response :: {}", requestJson.toString());

		// 4. PROCESS
		// 4.1 Get & Validate Data
		JSONObject data = requestJson.optJSONObject("Data");
		if (data == null) {
			LOG.debug("Data is null:: {}", data);
			throw new TravelVisitException("Data is null :" + data);
		}

		// Extracting all values from data
		String cifNo = data.optString("cifNo");
//		String customerDeatilsStr = getCustomerDeatilsByCustomerNo(cifNo);
		String customerDeatilsStr = "";
		JSONObject customerDetails = getCustomerDetailsFromResponse(customerDeatilsStr);
		customerDetails.put("cifNo", cifNo);
		responseData.put("customerDetails", customerDetails);
		response.put("Data", responseData);
		return response;
	}

	public String getCustomerDeatilsByCustomerNo(String customerNo) {
		String responseString = "";
		try {
			// Create a custom SSL context to disable SSL validation
			SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial((chain, authType) -> true).build();

			// Create HttpClient with custom SSLContext
			CloseableHttpClient client = HttpClientBuilder.create().setSSLContext(sslContext)
					.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE) // Disable hostname verification
					.build();

			// Define the URL
			String url = "https://intramashery.suryodaybank.co.in/ssfbuat/customers/details/v2?api_key=kyqak5muymxcrjhc5q57vz9v";

			// Create the HttpPost request
			HttpPost post = new HttpPost(url);

			// Set headers
			post.setHeader("X-Request-ID", "IBR");
			post.setHeader("X-User-ID", "30639");
			post.setHeader("X-From-ID", "TAB");
			post.setHeader("Content-Type", "application/json");

			// Create JSON body
			String jsonBody = "{ \"Data\": { \"MobileNo\": \"\", \"AadhaarNo\": \"\", \"ProductGroup\": \"CASA\", \"PanNo\": \"\", \"AadhaarReferenceNo\": \"\", \"CustomerNo\": \""
					+ customerNo + "\", \"BranchCode\": \"\" } }";

			// Set the request body
			StringEntity entity = new StringEntity(jsonBody);
			post.setEntity(entity);

			// Execute the request and get the response
			CloseableHttpResponse response = client.execute(post);

			// Get the response body
			HttpEntity responseEntity = response.getEntity();
			responseString = EntityUtils.toString(responseEntity);

			// Close the resources
			response.close();
			client.close();
		} catch (Exception e) {
			throw new TravelVisitException(
					"Exception occur during fetch customer details by customer id :" + e.getMessage());
		}
		return responseString;
	}

	public JSONObject getCustomerDetailsFromResponse(String response) {
//		JSONObject responseJSON = new JSONObject(response);
//		JSONObject data = responseJSON.getJSONObject("Data");
//		JSONArray jsonArray = data.getJSONArray("CustomerDetails");
//		JSONObject customerDetails = new JSONObject(jsonArray.get(0).toString());
//
//		// Get the FirstName and LastName
//		String firstName = customerDetails.getString("FirstName");
//		String lastName = customerDetails.getString("LastName");
//
//		// Get the address details from AddressDet (0)
//		JSONObject addressDetails = customerDetails.getJSONObject("AddressDetails").getJSONArray("AddressDet")
//				.getJSONObject(0);
//		String address1 = addressDetails.getString("ADDRESS1");
//		String address2 = addressDetails.getString("ADDRESS2");
//		String address3 = addressDetails.getString("ADDRESS3");
//		String address4 = addressDetails.getString("ADDRESS4");
//		String state = addressDetails.getString("STATE");
//		String pinCode = addressDetails.getString("PINCODE");
//		String mobile = addressDetails.getString("MOBILE");

		// RESPONSE
		JSONObject customerDetailsJson = new JSONObject();
//		customerDetailsJson.put("name", firstName+" "+lastName);
//		customerDetailsJson.put("address",
//				address1 + "," + address2 + ", " + address3 + ", " + address4 + ", " + state + ", " + pinCode + ".");
//		customerDetailsJson.put("mobile", mobile);
		customerDetailsJson.put("name", "Ravi");
		customerDetailsJson.put("address", "Pendhar");
		customerDetailsJson.put("mobile", "1111111111");

		return customerDetailsJson;
	}
}
