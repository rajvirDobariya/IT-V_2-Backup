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

	public String getJsonRequest(String jsonString) {

		StringBuffer cibilRequest = new StringBuffer();
		cibilRequest.append(
				"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://services.equifax.com/eport/ws/schemas/1.0\">");
		cibilRequest.append("<soapenv:Header />");
		cibilRequest.append("<soapenv:Body>");
		cibilRequest.append("<ns:InquiryRequest xmlns:ns=\"http://services.equifax.com/eport/ws/schemas/1.0\">");
		cibilRequest.append("<ns:RequestHeader>");
		cibilRequest.append("<ns:CustomerId>" + 21 + "</ns:CustomerId>");
		cibilRequest.append("<ns:UserId>" + "UAT_SURYO" + "</ns:UserId>");
		cibilRequest.append("<ns:Password>" + "V2*Pdhbr" + "</ns:Password>");
		cibilRequest.append("<ns:MemberNumber>" + "028FZ00016" + "</ns:MemberNumber>");
		cibilRequest.append("<ns:SecurityCode>" + "FR7" + "</ns:SecurityCode>");
		cibilRequest.append("<ns:ProductVersion>" + 1 + "</ns:ProductVersion>");
		cibilRequest.append("<ns:ReportFormat>" + "XML" + "</ns:ReportFormat>");
		cibilRequest.append("<ns:ProductCode>" + "CCR" + "</ns:ProductCode>");
		cibilRequest.append("<ns:CustRefField />");
		cibilRequest.append("</ns:RequestHeader>");
		cibilRequest.append("<ns:RequestAccountDetails seq=\"1\">");
		cibilRequest.append("<ns:KendraIDMFI>" + 220 + "</ns:KendraIDMFI>");
		cibilRequest.append("<ns:BranchIDMFI>" + 220 + "</ns:BranchIDMFI>");
		cibilRequest.append("</ns:RequestAccountDetails>");
		cibilRequest.append("<ns:RequestBody>");
		cibilRequest.append("<ns:InquiryPurpose>" + "0E" + "</ns:InquiryPurpose>");
		cibilRequest.append("<ns:FullName>" + "SANDYA M M" + "</ns:FullName>");
		cibilRequest.append("<ns:FirstName>" + "SANDYA M M" + "</ns:FirstName>");
		cibilRequest.append("<ns:MiddleName>" + "M" + "</ns:MiddleName>");
		cibilRequest.append("<ns:LastName>" + "M" + "</ns:LastName>");
		cibilRequest.append("<ns:FamilyDetails>");
		cibilRequest.append("<ns:AdditionalNameInfo seq=\"1\">");
		cibilRequest.append("<ns:AdditionalName>" + "SPOUSE M M" + "</ns:AdditionalName>");
		cibilRequest.append("<ns:AdditionalNameType>" + "H" + "</ns:AdditionalNameType>");
		cibilRequest.append("</ns:AdditionalNameInfo>");
		cibilRequest.append("</ns:FamilyDetails>");
		cibilRequest.append("<ns:AddrLine1>" + "AREA IS NEAR JGIHIH VKUVYFI" + "</ns:AddrLine1>");
		cibilRequest.append("<ns:State>" + "MH" + "</ns:State>");
		cibilRequest.append("<ns:Postal>" + "560048" + "</ns:Postal>");
		cibilRequest.append("<ns:DOB>" + "1997-08-12" + "</ns:DOB>");
		cibilRequest.append("<ns:Gender>" + "F" + "</ns:Gender>");
		cibilRequest.append("<ns:VoterId>" + "csp3116258" + "</ns:VoterId>");
		cibilRequest.append("<ns:MobilePhone>" + "8963525256" + "</ns:MobilePhone>");
		cibilRequest.append("<ns:RequestAccountDetails />");
		cibilRequest.append("<InquiryCommonAccountDetails>");
		cibilRequest.append("<ns:InquiryAccount />");
		cibilRequest.append("</InquiryCommonAccountDetails>");
		cibilRequest.append("</ns:RequestBody>");
		cibilRequest.append("</ns:InquiryRequest>");
		cibilRequest.append("</soapenv:Body>");
		cibilRequest.append("</soapenv:Envelope>");

		String cibilrequest = cibilRequest.toString();
		cibilrequest = cibilrequest.replaceAll("\t", "");
		return cibilrequest;
	}

}
