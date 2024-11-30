package com.suryoday.CustomerIntraction.Util;

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
			System.out.println("Error while decrypting: " + e.toString());
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

	public static void main(String a[]) throws Exception {
		/*
		 * Secret Key must be in the form of 16 byte like,
		 *
		 * private static final byte[] secretKey = new byte[] { ‘m’, ‘u’, ‘s’, ‘t’, ‘b’,
		 * ‘e’, ‘1’, ‘6’, ‘b’, ‘y’, ‘t’,’e’, ‘s’, ‘k’, ‘e’, ‘y’};
		 *
		 * below is the direct 16byte string we can use
		 */
		String secretKey = "JEFSQVRISUA=";
		String encodedBase64Key = encodeKey(secretKey);
		System.out.println("EncodedBase64Key = " + encodedBase64Key); // This need to be share between client and server

// To check actual key from encoded base 64 secretKey
// String toDecodeBase64Key = decodeKey(encodedBase64Key);
// System.out.println(“toDecodeBase64Key = “+toDecodeBase64Key);

		String toEncrypt = "{\"Data\":{\"meetingDate\":\"2024-06-17\",\"branchCode\":\"SSFBLJ\",\"createdBy\":\"24095\",\"meetingStartTime\":\"11:00 AM\",\"meetingEndTime\":\"02:00 PM\",\"topicOfDiscussion\":\"KT\",\"branchActionable\":\"KT\",\"feedback\":\"test\",\"ETA\":\"2024-06-17\",\"memberList\":[{\"name\":\"Karishma Kumar\",\"CIFNo\":\"240000695\",\"memberRole\":\"Customer\"},{\"name\":\"Tara Kumar\",\"CIFNo\":\"240000695\",\"memberRole\":\"Staff\"},{\"name\":\"Navin Kumar\",\"CIFNo\":\"240000695\",\"memberRole\":\"Staff\"}],\"image\":[{\"base64Image\":\"RmFzZTY0IGVuY29kaW5nIHN0cmluZw==\",\"documentType\":\"jpg\"},{\"base64Image\":\"RmFzNNY0IGVuY29kaW5nIHN0cmluZw==\",\"documentType\":\"jpg\"},{\"base64Image\":\"RmFzTNY0IGVuY29kaW5nIHN0cmluZw==\",\"documentType\":\"pdf\"},{\"base64Image\":\"RmTzTNY0IGVuY29kaW5nIHN0cmluZw==\",\"documentType\":\"pdf\"}]}}";
		System.out.println("Plain text = " + toEncrypt);

// AES Encryption based on above secretKey
		String encrStr = Crypt.encrypt(toEncrypt, encodedBase64Key);
		System.out.println("Cipher Text: Encryption of str = " + encrStr);

// AES Decryption based on above secretKey
//        String decrStr = Crypt.decrypt(encrStr, encodedBase64Key);
//        System.out.println("Decryption of str = " + decrStr);
	}
}