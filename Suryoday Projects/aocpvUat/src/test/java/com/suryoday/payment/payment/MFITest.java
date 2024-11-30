package com.suryoday.payment.payment;

import java.util.Base64;

import com.suryoday.connector.rd.util.CustomProperties;
import com.suryoday.connector.rd.util.EncryptDecryptHelper;

public class MFITest {

	public static void main(String[] args) {

		String decrypt = "";
		CustomProperties prop;
		try {

			String KEY = "B9049FAF3B35762087749A0F3C553226";
			byte[] bytesToDecrypt = Base64.getDecoder().decode(decrypt);

			decrypt = EncryptDecryptHelper.decrypt(KEY.getBytes(), bytesToDecrypt);

		} catch (Exception e) {

			e.printStackTrace();
		}
		System.out.println(decrypt);

	}

}
