package com.suryoday.payment.payment;

import com.suryoday.connector.rd.util.AppzillonAESUtils;

public class DecryptionDemoMB {

	public static void main(String[] args) {

		String key = "b806fb69-1bc1-4e11-9169-5dcd8719ee92";
		String data = "xL7oUVEcGNRAvcS8QeDFErouetXk/ft6hoRqQYzivfs\u003d";
		String encryptString2 = AppzillonAESUtils.decryptContainerString(data, key);

		System.out.println(encryptString2);
	}

}
