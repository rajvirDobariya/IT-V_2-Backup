package com.suryoday.connector.controller;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.connector.rd.util.CustomProperties;

@RestController
@RequestMapping("/uidauth")
public class PidOptionController {

	static Logger logger = LoggerFactory.getLogger(PidOptionController.class);

	@RequestMapping(value = "/getpiddata", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> serviceRequestDispatcher(@RequestBody String jsonRequest)
//			@RequestHeader(name = "X-Correlation-ID", required = true) String X_Correlation_ID,
//			 @RequestHeader(name = "X-From-ID", required = true) String X_From_ID,
//			 @RequestHeader(name = "X-To-ID", required = true) String X_To_ID,
//			 @RequestHeader(name = "X-Transaction-ID", required = true) String X_Transaction_ID,
//			 @RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
//			 @RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID)
			throws ClassNotFoundException {

		JSONObject lResponse = new JSONObject();

		try {

			logger.debug("Request json:" + jsonRequest);

			JSONObject jsonObject = new JSONObject(jsonRequest);

			// HashGenerator hashGenerator = new HashGenerator();
			JSONObject lRequest = jsonObject.getJSONObject("Data").getJSONObject("PidOptions");

			CustomProperties prop = loadpropertyfile();

			// CustomProperties prop = new CustomProperties();

			String isKyc = lRequest.getString("isKyc");
			String fcount = "";
			String wadh = "";

			if (isKyc.equals("1")) {
				wadh = prop.getWadhVer();
			} else {
				wadh = prop.getWadhVer();
				// wadh = "2.1";
			}

			if (lRequest.has("fcount") && StringUtils.isNotBlank(lRequest.getString("fcount"))) {
				logger.debug("fcount:" + lRequest.getString("fcount"));
				fcount = lRequest.getString("fcount");
			} else {
				logger.debug("fcount from proprties file:" + prop.getFingerPrintCount());
				fcount = prop.getFingerPrintCount();
			}

			// String lUid=lRequest.getString("uid");
			// TODO properties file

			StringBuffer lPidOptions = new StringBuffer();
			logger.debug("PidOptsVer:" + prop.getPidOptsVer());
			lPidOptions.append("<PidOptions ver=\"" + prop.getPidOptsVer() + "\">");
			lPidOptions.append("<Opts ");

			if ("FINGERPRINT".equals(lRequest.getString("serviceType"))) {
				lPidOptions.append(" fCount=\"" + fcount + "\"");
				logger.debug("FingerPrintType:" + prop.getFingerPrintType());
				lPidOptions.append(" fType=\"" + prop.getFingerPrintType() + "\"");
				wadh = wadh + "F";
			}
			if ("IRIS".equals(lRequest.getString("serviceType"))) {
				logger.debug("IrirsCount:" + prop.getIrirsCount());
				logger.debug("IrisType:" + prop.getIrisType());
				logger.debug("irisLicenseKey:" + new CustomProperties().getIrisLicenseKey());
				lPidOptions.append(" iCount=\"" + prop.getIrirsCount() + "\"");
				lPidOptions.append(" iType=\"" + prop.getIrisType() + "\"");
				lResponse.put("irisLicenseKey", new CustomProperties().getIrisLicenseKey());
				wadh = wadh + "I";
			}
			/**
			 * Not Supported yet. lPidOptions.append(" pCount=\""+fpCount+"\"");
			 * lPidOptions.append(" pType=\""+pType+"\"");
			 */
			logger.debug("PidFormat:" + prop.getPidFormat());
			logger.debug("Timeout:" + prop.getTimeout());
			lPidOptions.append(" format=\"" + prop.getPidFormat() + "\"");
			lPidOptions.append(" pidVer=\"2.0\"");
			lPidOptions.append(" timeout=\"" + prop.getTimeout() + "\"");

			if (lRequest.has("otp") && StringUtils.isNotBlank(lRequest.getString("otp"))) {
				String otp = new String(Base64.decodeBase64(lRequest.getString("otp").getBytes()));
				logger.debug("otp:" + otp);
				lPidOptions.append(" otp=\"" + otp + "\"");
				wadh = wadh + "O";
			}
			logger.debug("GetEnv :" + prop.getEnv());
			lPidOptions.append(" env=\"" + prop.getEnv() + "\"");

			wadh = wadh + "YNYY";
			if ("1".equals(lRequest.getString("isKyc"))) {
				lPidOptions.append(" wadh=\"" + getWadhHash(wadh) + "\"");
			}
			// for aeps
			else {
				lPidOptions.append(" wadh=\"\"");
			}
			logger.debug("Posh :" + prop.getPosh());
			lPidOptions.append(" posh=\"" + prop.getPosh() + "\"/>");
			lPidOptions.append("<Demo></Demo>");
			/**
			 * <CustOpts> <Param name="" value="" /> </CustOpts>
			 */
			lPidOptions.append("</PidOptions>");
			lResponse.put("status", "success");
			lResponse.put("pidoptions", lPidOptions.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Exception :" + e.getMessage());
			lResponse.put("status", "failure");
		}
		logger.debug("Response json :" + lResponse.toString());

//		pMessage.getResponseObject().getResponseJson().put("response", lResponse);
		return new ResponseEntity<Object>(lResponse.toString(), HttpStatus.OK);
	}

	public static String getWadhHash(String wadh) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		digest.reset();
		// return digest.digest(wadh.getBytes());

		logger.debug("getWadhHash :" + new String(wadh.getBytes()));
		return new String(Base64.encodeBase64(getHash(wadh))).trim();
	}

	public static byte[] getHash(String wadh) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		digest.reset();
		return digest.digest(wadh.getBytes());
	}

	public static CustomProperties loadpropertyfile() throws Exception {
		String sConfigFile = "props/uidai_admin_iris.properties";

		InputStream in = PidOptionController.class.getClassLoader().getResourceAsStream(sConfigFile);
		if (in == null) {
			// File not found! (Manage the problem)
		}
		Properties prop = new Properties();
		prop.load(in);

//		String filePath = getClass().getClassLoader().getResource("props/uidai_admin_iris.properties").getPath();
//		Properties prop = new Properties();
//		prop.load(new FileInputStream(new File(filePath)));

		logger.debug("myUrl" + prop.getProperty("otpUrl"));
		CustomProperties prop1 = new CustomProperties();
		try {

			prop1.setOtpUrl(prop.getProperty("otpUrl"));

			prop1.setKycUrl(prop.getProperty("kycUrl"));
			prop1.setPublicKeyFile(prop.getProperty("publicKeyFile"));
			prop1.setPidOptsVer(prop.getProperty("optionsver"));
			prop1.setFingerPrintCount(prop.getProperty("fCount"));
			prop1.setFingerPrintType(prop.getProperty("fType"));
			prop1.setIrirsCount(prop.getProperty("iCount"));
			prop1.setIrisType(prop.getProperty("iType"));
			prop1.setPidFormat(prop.getProperty("format"));
			prop1.setTimeout(prop.getProperty("timeout"));
			prop1.setPosh(prop.getProperty("posh"));
			prop1.setAuthVer(prop.getProperty("authVer"));
			prop1.setOtpChannel(prop.getProperty("otpchannel"));
			prop1.setKycVer(prop.getProperty("kycVer"));
			prop1.setEnv(prop.getProperty("env"));
			prop1.setPublicIP(prop.getProperty("publicIP"));
			prop1.setLot(prop.getProperty("lot"));
			prop1.setLov(prop.getProperty("lov"));
			prop1.setUseSSK(prop.getProperty("useSSK"));
			prop1.setIrisLicenseKey(prop.getProperty("irisLicenseKey"));
			prop1.setAc(prop.getProperty("ac"));
			prop1.setSa(prop.getProperty("sa"));
			prop1.setIIN(prop.getProperty("IIN"));
			prop1.setMcc(prop.getProperty("Mcc"));
			prop1.setAcqId(prop.getProperty("AcqId"));
			prop1.settId(prop.getProperty("tid"));
			prop1.setRc(prop.getProperty("rc"));
			prop1.setKi(prop.getProperty("ki"));
			prop1.setPos_entry_mode(prop.getProperty("pos_entry_mode"));
			prop1.setPos_code(prop.getProperty("pos_code"));
			prop1.setcA_Tid(prop.getProperty("cA_Tid"));
			prop1.setcA_ID(prop.getProperty("cA_ID"));
			prop1.setcA_TA(prop.getProperty("cA_TA"));
			prop1.setAua_lk(prop.getProperty("aua_lk"));
			prop1.setKua_lk(prop.getProperty("kua_lk"));
			prop1.setWadhVer(prop.getProperty("wadhVer"));
			prop1.setAuthUrl(prop.getProperty("authUrl"));
			prop1.setEsbUrl(prop.getProperty("esbUrl"));
			prop1.setUrlVersion(prop.getProperty("urlVersion"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return prop1;

	}

	public static void main(String args[]) {
		String a = "{\r\n" + "    \"Data\": {\r\n" + "        \"PidOptions\": {\r\n"
				+ "            \"serviceType\": \"FINGERPRINT\",\r\n" + "            \"fcount\": \"1\",\r\n"
				+ "            \"isKyc\": \"1\"\r\n" + "        }\r\n" + "    }\r\n" + "}";

		JSONObject lResponse = new JSONObject();

		try {

			logger.debug("Request json:" + a);

			JSONObject jsonObject = new JSONObject(a);

			// HashGenerator hashGenerator = new HashGenerator();
			JSONObject lRequest = jsonObject.getJSONObject("Data").getJSONObject("PidOptions");

			CustomProperties prop = loadpropertyfile();

			// CustomProperties prop = new CustomProperties();

			String isKyc = lRequest.getString("isKyc");
			String fcount = "";
			String wadh = "";

			if (isKyc.equals("1")) {
				wadh = prop.getWadhVer();
			} else {
				wadh = prop.getWadhVer();
				// wadh = "2.1";
			}

			if (lRequest.has("fcount") && StringUtils.isNotBlank(lRequest.getString("fcount"))) {
				logger.debug("fcount:" + lRequest.getString("fcount"));
				fcount = lRequest.getString("fcount");
			} else {
				logger.debug("fcount from proprties file:" + prop.getFingerPrintCount());
				fcount = prop.getFingerPrintCount();
			}

			// String lUid=lRequest.getString("uid");
			// TODO properties file

			StringBuffer lPidOptions = new StringBuffer();
			logger.debug("PidOptsVer:" + prop.getPidOptsVer());
			lPidOptions.append("<PidOptions ver=\"" + prop.getPidOptsVer() + "\">");
			lPidOptions.append("<Opts ");

			if ("FINGERPRINT".equals(lRequest.getString("serviceType"))) {
				lPidOptions.append(" fCount=\"" + fcount + "\"");
				logger.debug("FingerPrintType:" + prop.getFingerPrintType());
				lPidOptions.append(" fType=\"" + prop.getFingerPrintType() + "\"");
				wadh = wadh + "F";
			}
			if ("IRIS".equals(lRequest.getString("serviceType"))) {
				logger.debug("IrirsCount:" + prop.getIrirsCount());
				logger.debug("IrisType:" + prop.getIrisType());
				logger.debug("irisLicenseKey:" + new CustomProperties().getIrisLicenseKey());
				lPidOptions.append(" iCount=\"" + prop.getIrirsCount() + "\"");
				lPidOptions.append(" iType=\"" + prop.getIrisType() + "\"");
				lResponse.put("irisLicenseKey", new CustomProperties().getIrisLicenseKey());
				wadh = wadh + "I";
			}
			/**
			 * Not Supported yet. lPidOptions.append(" pCount=\""+fpCount+"\"");
			 * lPidOptions.append(" pType=\""+pType+"\"");
			 */
			logger.debug("PidFormat:" + prop.getPidFormat());
			logger.debug("Timeout:" + prop.getTimeout());
			lPidOptions.append(" format=\"" + prop.getPidFormat() + "\"");
			lPidOptions.append(" pidVer=\"2.0\"");
			lPidOptions.append(" timeout=\"" + prop.getTimeout() + "\"");

			if (lRequest.has("otp") && StringUtils.isNotBlank(lRequest.getString("otp"))) {
				String otp = new String(Base64.decodeBase64(lRequest.getString("otp").getBytes()));
				logger.debug("otp:" + otp);
				lPidOptions.append(" otp=\"" + otp + "\"");
				wadh = wadh + "O";
			}
			logger.debug("GetEnv :" + prop.getEnv());
			lPidOptions.append(" env=\"" + prop.getEnv() + "\"");

			wadh = wadh + "YNYY";
			System.out.println("v1" + wadh);
			if ("1".equals(lRequest.getString("isKyc"))) {
				lPidOptions.append(" wadh=\"" + getWadhHash(wadh) + "\"");
				System.out.println("v1" + getWadhHash(wadh));
			}
			// for aeps
			else {
				lPidOptions.append(" wadh=\"\"");
			}
			logger.debug("Posh :" + prop.getPosh());
			lPidOptions.append(" posh=\"" + prop.getPosh() + "\"/>");
			lPidOptions.append("<Demo></Demo>");
			/**
			 * <CustOpts> <Param name="" value="" /> </CustOpts>
			 */
			lPidOptions.append("</PidOptions>");
			lResponse.put("status", "success");
			lResponse.put("pidoptions", lPidOptions.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Exception :" + e.getMessage());
			lResponse.put("status", "failure");
		}
		logger.debug("Response json :" + lResponse.toString());
	}

}
