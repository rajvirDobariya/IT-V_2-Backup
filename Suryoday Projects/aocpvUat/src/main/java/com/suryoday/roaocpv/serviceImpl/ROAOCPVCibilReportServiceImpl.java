package com.suryoday.roaocpv.serviceImpl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.suryoday.connector.rd.util.CustomProperties;
import com.suryoday.connector.serviceImpl.CibilReportServiceImpl;
import com.suryoday.roaocpv.service.ROAOCPVCibilReportService;

@Service
public class ROAOCPVCibilReportServiceImpl implements ROAOCPVCibilReportService {

	private static Logger logger = LoggerFactory.getLogger(ROAOCPVCibilReportServiceImpl.class);

	public String getCibilReport(String request) {

		String response = null;
		URL obj = null;
		try {

			// GenerateProperty x = GenerateProperty.getInstance();
			// x.bypassssl();
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			CustomProperties prop = loadpropertyfile();

			obj = new URL(prop.getCibilUrl());

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "text/plain");
			// con.setRequestProperty("Cookie", header.getString("Cookie"));
			response = getResponseData(request, response, con, "POST");

		} catch (Exception e) {

			e.printStackTrace();
		}
		return response;
	}

	private static String getResponseData(String parent, String response2, HttpURLConnection con, String MethodType)
			throws IOException {

		con.setDoOutput(true);
		DataOutputStream os = new DataOutputStream(con.getOutputStream());
		os.writeBytes(parent);
		os.flush();
		os.close();

		int responseCode = con.getResponseCode();
		logger.debug("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(response.toString());
			response2 = stringBuffer.toString();

		} else {
			logger.debug("POST request not worked");
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("<Description><Error>");
			stringBuffer.append(" Server Error  " + responseCode + "</Error></Description>");
			response2 = stringBuffer.toString();

		}

		return response2;

	}

	public CustomProperties loadpropertyfile() throws Exception {
		String sConfigFile = "props/uidai_admin_iris.properties";

		InputStream in = CibilReportServiceImpl.class.getClassLoader().getResourceAsStream(sConfigFile);
		if (in == null) {

		}
		Properties prop = new Properties();
		prop.load(in);

		CustomProperties prop1 = new CustomProperties();
		try {

			prop1.setAc(prop.getProperty("ac"));
			prop1.setSa(prop.getProperty("sa"));
			prop1.setMcc(prop.getProperty("Mcc"));
			prop1.setAcqId(prop.getProperty("AcqId"));
			prop1.settId(prop.getProperty("tid"));
			prop1.setRc(prop.getProperty("rc"));
			prop1.setPos_entry_mode(prop.getProperty("pos_entry_mode"));
			prop1.setPos_code(prop.getProperty("pos_code"));
			prop1.setcA_Tid(prop.getProperty("cA_Tid"));
			prop1.setcA_ID(prop.getProperty("cA_ID"));
			prop1.setcA_TA(prop.getProperty("cA_TA"));
			prop1.setAua_lk(prop.getProperty("aua_lk"));
			prop1.setProc_code(prop.getProperty("proc_code"));
			prop1.setVer(prop.getProperty("ver"));
			prop1.setPi(prop.getProperty("pi"));
			prop1.setPa(prop.getProperty("pa"));
			prop1.setPfa(prop.getProperty("pfa"));
			prop1.setBio(prop.getProperty("bio"));
			prop1.setBt(prop.getProperty("bt"));
			prop1.setPin(prop.getProperty("pin"));
			prop1.setOtp(prop.getProperty("otp"));
			prop1.setAuthUrl(prop.getProperty("authUrl"));
			prop1.setKycUrl(prop.getProperty("kycUrl"));
			prop1.setCibilUrl(prop.getProperty("cibilUrl"));

		} catch (Exception e) {

			e.printStackTrace();
		}

		return prop1;

	}

}
