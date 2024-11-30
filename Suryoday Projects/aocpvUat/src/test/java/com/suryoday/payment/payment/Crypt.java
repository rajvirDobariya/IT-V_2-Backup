package com.suryoday.payment.payment;

import java.util.ArrayList;
import java.util.List;

public class Crypt {

	public static void main(String[] args) throws Exception {

		String module = "[LEAD-MANAGEMENT, AOCPV]";

		org.json.JSONArray userAccessInJson = new org.json.JSONArray(module);
		List<String> listUserAccess = new ArrayList<>();
		for (int n = 0; n < userAccessInJson.length(); n++) {
			String asset = userAccessInJson.getString(n);
			if (asset.equalsIgnoreCase("LEAD-MANAGEMENT")) {

				System.out.println("yes");
			}
			listUserAccess.add(asset);
		}
	}
}