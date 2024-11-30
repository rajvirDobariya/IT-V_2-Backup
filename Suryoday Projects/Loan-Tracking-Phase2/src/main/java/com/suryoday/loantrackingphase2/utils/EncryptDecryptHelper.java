package com.suryoday.loantrackingphase2.utils;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.suryoday.loantrackingphase2.exception.LoanException;
import com.suryoday.loantrackingphase2.repository.ProductMasterRepository;

@Service
public class EncryptDecryptHelper {

	private static final Logger LOG = LoggerFactory.getLogger(EncryptDecryptHelper.class.getName());

	@Autowired
	ProductMasterRepository productMasterRepository;

	public JSONObject decryptRequestString(String encryptResponseString, String channel, String X_Session_ID) {
		// 1 REQUEST
		JSONObject encryptJSONObject = new JSONObject(encryptResponseString);
		String decryptContainerString = "";
		String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
		if (channel.equalsIgnoreCase("WEB")) {
			decryptContainerString = Crypt.decrypt(encryptJSONObject.toString(), X_Session_ID);
		} else if (channel.equalsIgnoreCase("MB")) {

			decryptContainerString = AppzillonAESUtils.decryptContainerString(encryptString, X_Session_ID);
		}
		return new JSONObject(decryptContainerString);
	}

	public JSONObject encryptResponseString(JSONObject responseJson, String channel, String X_Session_ID) {
		// 3 RESPONSE
		String data = responseJson.toString();
		String encryptString2 = "";
		if (channel.equalsIgnoreCase("WEB")) {
			try {
				encryptString2 = Crypt.encrypt(data, X_Session_ID);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (channel.equalsIgnoreCase("MB")) {
			encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, X_Session_ID);
		}
		JSONObject data2 = new JSONObject();
		data2.put("value", encryptString2);
		JSONObject data3 = new JSONObject();
		data3.put("Data", data2);
		return data3;
	}

	public JSONObject validateHeadersAndSessionId(String encryptRequestString, String channel, String X_Session_ID) {
		// Prepare detailed message with field values
		String message = String.format(
				"One or more required fields are null or empty in validateHeadersAndSessionId. Values - "
						+ "encryptRequestString: '%s', channel: '%s', X_Session_ID: '%s'",
				encryptRequestString, channel, X_Session_ID);
		LOG.debug("Validate Headers :" + message);

		// 1 Check fields null and empty
		if (encryptRequestString == null || encryptRequestString.isEmpty() || channel == null || channel.isEmpty()
				|| X_Session_ID == null || X_Session_ID.isEmpty()) {

			LOG.debug("Validate Headers :" + message);
			throw new LoanException("Validate Headers :" + message);
		}

		// 2 Check DB session ID
		String dbSessionId = productMasterRepository.checkSessingId(X_Session_ID);
		if (dbSessionId == null || dbSessionId.isEmpty()) {
			LOG.debug("SessionId is expired or Invalid sessionId :: {}", HttpStatus.UNAUTHORIZED);
			throw new LoanException("SessionId is expired or Invalid sessionId!");
		}
		// 3 DecryptRequestString
		return decryptRequestString(encryptRequestString, channel, X_Session_ID);
	}

	public void validateHeadersAndSessionId(String channel, String X_Session_ID) {
		// Prepare detailed message with field values
		String message = String
				.format("One or more required fields are null or empty in validateHeadersAndSessionId. Values - "
						+ "channel: '%s', X_Session_ID: '%s'", channel, X_Session_ID);
		LOG.debug("Validate Headers :" + message);

		// 1 Check fields null and empty
		if (channel == null || channel.isEmpty() || X_Session_ID == null || X_Session_ID.isEmpty()) {
			LOG.debug("Validate Headers :" + message);
			throw new LoanException("Validate Headers :" + message);
		}

		// 2 Check DB session IDproductMasterRepository
		String dbSessionId = productMasterRepository.checkSessingId(X_Session_ID);
		if (dbSessionId == null || dbSessionId.isEmpty()) {
			LOG.debug("SessionId is expired or Invalid sessionId :: {}", HttpStatus.UNAUTHORIZED);
			throw new LoanException("SessionId is expired or Invalid sessionId!");
		}
	}

}