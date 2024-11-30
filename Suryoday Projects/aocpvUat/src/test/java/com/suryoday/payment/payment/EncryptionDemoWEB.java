package com.suryoday.payment.payment;

import com.suryoday.connector.rd.util.Crypt;

public class EncryptionDemoWEB {

	public static void main(String[] args) {
		String encryptString = "{\r\n" + "    \"Data\": {\r\n" + "        \"userId\": \"31788\"\r\n" + "    }\r\n"
				+ "}";
		String X_encode_ID = "==";
		String decryptContainerString = "";
		try {
			decryptContainerString = Crypt.encrypt(encryptString, X_encode_ID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(decryptContainerString);
	}
}
