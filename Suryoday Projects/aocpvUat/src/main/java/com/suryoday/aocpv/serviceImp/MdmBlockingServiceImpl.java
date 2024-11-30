package com.suryoday.aocpv.serviceImp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.service.MdmBlockingService;

@Service
public class MdmBlockingServiceImpl implements MdmBlockingService {
	private static Logger logger = LoggerFactory.getLogger(MdmBlockingServiceImpl.class);

	@Override
	public JSONObject tab(String bearer, String deviceId) throws IOException {
		JSONObject sendAuthenticateResponse = new JSONObject();
		try {
			GenerateProperty x = GenerateProperty.getInstance();
			x.bypassssl();
			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			// https://eu04.manage.samsungknox.com/emm/oapi/device/selectDeviceInfoByGoogleDeviceId
			URL url = new URL("https://eu04.manage.samsungknox.com/emm/oapi/device/selectDeviceInfoByGoogleDeviceId");
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("POST");

			httpConn.setRequestProperty("cache-control", "no-cache");
			httpConn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			httpConn.setRequestProperty("Authorization", "Bearer " + bearer);
			httpConn.setRequestProperty("Cookie",
					"AWSALB=6YVIGrzm11TwUppV8hebBEGI5pw9GX1muDQ6RoM/dohIFl7LNG/lJRZ5zHzMIVV7SwV0ZZw5MegWISXSAOjVgF5OJxOgeZX4YxLS1VXTkdD24ZjezSqLabGbpRoe; AWSALBCORS=6YVIGrzm11TwUppV8hebBEGI5pw9GX1muDQ6RoM/dohIFl7LNG/lJRZ5zHzMIVV7SwV0ZZw5MegWISXSAOjVgF5OJxOgeZX4YxLS1VXTkdD24ZjezSqLabGbpRoe");

			httpConn.setDoOutput(true);
			OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
			writer.write("googleDeviceId=" + deviceId);
			writer.flush();
			writer.close();
			httpConn.getOutputStream().close();

			InputStream responseStream = httpConn.getResponseCode() / 100 == 2 ? httpConn.getInputStream()
					: httpConn.getErrorStream();
			Scanner s = new Scanner(responseStream).useDelimiter("\\A");
			String response = s.hasNext() ? s.next() : "";
			sendAuthenticateResponse = new JSONObject(response);
			return sendAuthenticateResponse;
		} catch (Exception e) {
			// TODO: handle exception
		}
		logger.debug("Service END here");
		return sendAuthenticateResponse;
	}

	@Override
	public JSONObject samsungknox() throws IOException {
		JSONObject sendAuthenticateResponse = new JSONObject();
		try {
			GenerateProperty x = GenerateProperty.getInstance();
			x.bypassssl();
			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			URL url = new URL(
					"https://eu04.manage.samsungknox.com/emm/oauth/token?grant_type=client_credentials&client_id=Jyotibyod@byod.suryodaybank.com&client_secret=Suryoday@124");
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("GET");

			httpConn.setRequestProperty("Cookie",
					"AWSALB=kUNftkLnPrxi+t8bj3sDTiCX4gh4M14SUQd/orrr2XL4nZ7KCwTmSXSd4ANf6dyFrnv0U1R1BrhGz6/mWU70ZSkYrKwSYAzLbUHt0izVBCZzNuOxgu2eTKRwc2Ki; AWSALBCORS=kUNftkLnPrxi+t8bj3sDTiCX4gh4M14SUQd/orrr2XL4nZ7KCwTmSXSd4ANf6dyFrnv0U1R1BrhGz6/mWU70ZSkYrKwSYAzLbUHt0izVBCZzNuOxgu2eTKRwc2Ki; AWSALB=P/vrxwKLaLnz9rfRynLu3EFlzI1fygfQmHseoi0AsFrXHplHUtUT1znsFMLZEa8e6q2Cr9TogUiKEQR0HZR/A50zcxY6M92C/BTZ/METlgLV3FT4p0yUksktxzvd; AWSALBCORS=P/vrxwKLaLnz9rfRynLu3EFlzI1fygfQmHseoi0AsFrXHplHUtUT1znsFMLZEa8e6q2Cr9TogUiKEQR0HZR/A50zcxY6M92C/BTZ/METlgLV3FT4p0yUksktxzvd");

			InputStream responseStream = httpConn.getResponseCode() / 100 == 2 ? httpConn.getInputStream()
					: httpConn.getErrorStream();
			Scanner s = new Scanner(responseStream).useDelimiter("\\A");
			String response = s.hasNext() ? s.next() : "";
			sendAuthenticateResponse = new JSONObject(response);
			return sendAuthenticateResponse;
		} catch (Exception e) {
			// TODO: handle exception
		}
		logger.debug("Service END here");
		return sendAuthenticateResponse;
	}

}
