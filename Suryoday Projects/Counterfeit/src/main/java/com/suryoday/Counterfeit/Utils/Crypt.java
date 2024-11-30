package com.suryoday.Counterfeit.Utils;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Crypt {
	
	private static final String ALGO = "AES"; // Default uses ECB PKCS5Padding

	public static String encrypt(String Data, String secret1) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		String originalInput = "$ARATHI@" + sdf.format(new Date());
		String secret = Base64.getEncoder().encodeToString(originalInput.getBytes());
		Key key = generateKey(secret);
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(Data.getBytes());
		String encryptedValue = Base64.getEncoder().encodeToString(encVal);
		return encryptedValue;
	}

	public static String decrypt(String jsonRequest, String secret1, String sessionID) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			String originalInput = "$ARATHI@" + sdf.format(new Date());
			String secret = Base64.getEncoder().encodeToString(originalInput.getBytes());
			Key key = generateKey(secret);
			Cipher cipher = Cipher.getInstance(ALGO);
			cipher.init(Cipher.DECRYPT_MODE, key);
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
//            boolean sessionId = nudgeDashboardService.fetchSessionId(sessionID);
//            String sessionId = nudgeDashboardService.fetchSessionId(sessionID);
			if (true) {
				String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
				return new String(cipher.doFinal(Base64.getDecoder().decode(encryptString)));
			} else {
				org.json.JSONObject data2 = new org.json.JSONObject();
				data2.put("value", "SessionId is expired or Invalid sessionId");
				org.json.JSONObject data3 = new org.json.JSONObject();
				data3.put("Error", data2);
				return data3.toString();
			}

		} catch (Exception e) {
		}
		return null;
	}

	private static Key generateKey(String secret) throws Exception {
		byte[] decoded = Base64.getDecoder().decode(secret.getBytes());
		Key key = new SecretKeySpec(decoded, ALGO);
		return key;
	}

	public static String decodeKey(String str) {
		byte[] decoded = Base64.getDecoder().decode(str.getBytes());
		return new String(decoded);
	}

	public static String encodeKey(String str) {
		byte[] encoded = Base64.getEncoder().encode(str.getBytes());
		return new String(encoded);
	}
}