package com.suryoday.payment.payment;

public class IncryptionDemoMB {

	public static void main(String[] args) {

		String key = "a78c48c5-5756-4399-ac65-20c1142f8d4d";
		String data = "{\r\n" + "    \"mobileNo\":\"\",\r\n" + "    \"clientId\":\"\",\r\n"
				+ "    \"loanAccNo\":\"237000000470\",\r\n" + "    \"custName\":\"\"\r\n" + "}";
		String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);

		System.out.println(encryptString2);

	}
}
