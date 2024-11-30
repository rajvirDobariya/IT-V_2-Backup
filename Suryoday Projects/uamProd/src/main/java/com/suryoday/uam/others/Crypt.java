package com.suryoday.uam.others;

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

	public static String decrypt(String strToDecrypt, String secret1) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			String originalInput = "$ARATHI@" + sdf.format(new Date());
			String secret = Base64.getEncoder().encodeToString(originalInput.getBytes());

			Key key = generateKey(secret);
			Cipher cipher = Cipher.getInstance(ALGO);
			cipher.init(Cipher.DECRYPT_MODE, key);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
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
		String secretKey = "Viju@12345567767";
		String encodedBase64Key = encodeKey(secretKey);
		System.out.println("EncodedBase64Key = " + encodedBase64Key); // This need to be share between client and server

// To check actual key from encoded base 64 secretKey
// String toDecodeBase64Key = decodeKey(encodedBase64Key);
// System.out.println(“toDecodeBase64Key = “+toDecodeBase64Key);

		String toEncrypt = "Hi Virendra!";
		System.out.println("Plain text = " + toEncrypt);

// AES Encryption based on above secretKey
		String encrStr = Crypt.encrypt(toEncrypt, encodedBase64Key);
		System.out.println("Cipher Text: Encryption of str = " + encrStr);

// AES Decryption based on above secretKey
		encrStr="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";
		String decrStr = Crypt.decrypt(encrStr, encodedBase64Key);
		System.out.println("Decryption of str = " + decrStr);

		// 01-10-2024
	}
}
