package com.digitisation.branchreports.utils;

import org.json.JSONObject;

public class EncryptDecryptHelper {

	public static JSONObject decryptRequestString(String encryptResponseString, String channel, String X_Session_ID) {
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

	public static JSONObject encryptResponseString(JSONObject responseJson, String channel, String X_Session_ID,
			String X_encode_ID) {
		// 3 RESPONSE
		String data = responseJson.toString();
		String encryptString2 = "";
		if (channel.equalsIgnoreCase("WEB")) {
			try {
				encryptString2 = Crypt.encrypt(data, X_encode_ID);
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
}