package com.digitisation.branchreports.utils;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Crypt {

	private static final Logger LOG = LoggerFactory.getLogger(Crypt.class);
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

	public static String decrypt(String jsonRequest, String sessionID) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			String originalInput = "$ARATHI@" + sdf.format(new Date());
			String secret = Base64.getEncoder().encodeToString(originalInput.getBytes());
			Key key = generateKey(secret);
			Cipher cipher = Cipher.getInstance(ALGO);
			cipher.init(Cipher.DECRYPT_MODE, key);
			org.json.JSONObject encryptJSONObject = new org.json.JSONObject(jsonRequest);
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			return new String(cipher.doFinal(Base64.getDecoder().decode(encryptString)));
		} catch (Exception e) {
			LOG.debug("Something went wrong in ecrypt request :" + e.getMessage());
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