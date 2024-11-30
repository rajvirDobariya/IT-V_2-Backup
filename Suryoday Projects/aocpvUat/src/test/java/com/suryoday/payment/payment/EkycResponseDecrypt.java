package com.suryoday.payment.payment;

import java.util.Base64;

import org.json.JSONObject;
import org.json.XML;

import com.suryoday.connector.rd.util.EncryptDecryptHelper;

public class EkycResponseDecrypt {
	public static void main(String[] args) {
		String sendEkyc = "";
		org.json.JSONObject jsonConverted1 = XML.toJSONObject(sendEkyc);
		String response = jsonConverted1.toString();
		JSONObject jsonAPIResponse = new JSONObject(response);
		if (jsonAPIResponse.has("Response")) {
			org.json.JSONObject jsonObject = jsonAPIResponse.getJSONObject("Response");
			String hsmData = jsonAPIResponse.getJSONObject("Response").getString("HsmData");
			System.out.println(hsmData);
			try {
				String KEY = "B9049FAF3B35762087749A0F3C553226";
				byte[] bytesToDecrypt = Base64.getDecoder().decode(hsmData);

				String decrypt = EncryptDecryptHelper.decrypt(KEY.getBytes(), bytesToDecrypt);
				System.out.println(decrypt);
				org.json.JSONObject jsonConverted2 = XML.toJSONObject(decrypt);
				System.out.println(jsonConverted2);
				System.out.println(decrypt);
			} catch (Exception e) {

				e.printStackTrace();
			}

		}
	}
}
