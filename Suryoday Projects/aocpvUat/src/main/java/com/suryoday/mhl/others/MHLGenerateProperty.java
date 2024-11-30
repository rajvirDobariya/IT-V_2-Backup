package com.suryoday.mhl.others;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MHLGenerateProperty {

	private static Logger logger = LoggerFactory.getLogger(MHLGenerateProperty.class);

	private static MHLGenerateProperty single_instance = null;

	private MHLGenerateProperty() {

	}

	public static MHLGenerateProperty getInstance() {
		if (single_instance == null)
			single_instance = new MHLGenerateProperty();

		return single_instance;
	}

	public static void bypassssl() {
		System.out.println("SSL Started");
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		// Install the all-trusting trust manager
		SSLContext sc;
		try {
			sc = SSLContext.getInstance("SSL");
			try {
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			} catch (KeyManagementException e) {
				System.out.println("" + e.getStackTrace());
				// TODO Auto-generated catch block

			}
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			System.out.println("" + e1.getStackTrace());
		}

		System.out.println("SSL Created");

	}

}
