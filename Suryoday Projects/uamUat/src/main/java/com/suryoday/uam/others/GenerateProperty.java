package com.suryoday.uam.others;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class GenerateProperty {

	
	private static Logger logger = LoggerFactory.getLogger(GenerateProperty.class);
	

	
	private static GenerateProperty single_instance = null;
	
	
	public static String BASEURL="";
	public static String BASEURLV2="";
	
	public static String BASEURL1="";
	public static String api_key="";
	public static String TabEnv="";
	public static String  EKYURL = "";
	public static String LDAPURLDC="";
	public static String LDAPURLDR="";
	public static String BASEURLLOCAL="";
	public static String reports="";
	
	//
	//10 user api cala 10 bar 1 object  property  url
	//singlt 10 user  1 object url 
	
	private GenerateProperty()
    {
		
    }
	
	public static GenerateProperty getInstance()
    {
        if (single_instance == null)
            single_instance = new GenerateProperty();
 
        return single_instance;
    }
	
	
	public static void getappprop()
	{
		Properties prop = new Properties();
		try {
		    //load a properties file from class path, inside static method
		    prop.load(GenerateProperty.class.getClassLoader().getResourceAsStream("application.properties"));

		   // prop.load(new FileInputStream("conf/application.properties"));
		    
		    //get the property value and print it out
		    //logger.debug(prop.getProperty("userBucket.path"));
		    
		    
		    BASEURL=prop.getProperty("BASEURL");
		    BASEURL1=prop.getProperty("BASEURL1");
		    api_key=prop.getProperty("api_key");
		    TabEnv=prop.getProperty("TABENV");
		    EKYURL =prop.getProperty("EKYURL");
		    BASEURLLOCAL =prop.getProperty("BASEURLLOCAL");
		    BASEURLV2=prop.getProperty("BASEURLV2");
		    LDAPURLDC=prop.getProperty("LDAPURLDC");
		    LDAPURLDR=prop.getProperty("LDAPURLDR");
		    reports=prop.getProperty("reports");
		    System.out.println(prop.getProperty("BASEURLV2"));
		    System.out.println(BASEURLV2);
		   // logger.debug(prop.getProperty("BASEURL"));

		   		} 
		catch (IOException ex) {
		    ex.printStackTrace();
		    logger.debug(""+ex.getStackTrace());
		}
		
	}
	
	public static void bypassssl()
	{
		logger.debug("SSL Started");
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
						logger.debug(""+e.getStackTrace());
						// TODO Auto-generated catch block
						
					}
				} catch (NoSuchAlgorithmException e1) {
					// TODO Auto-generated catch block
					logger.debug(""+e1.getStackTrace());
				}
				

				
				logger.debug("SSL Created");
				

	}
	
	
	public static JSONObject GetResponseData(JSONObject parent, JSONObject sendauthenticateResponse,
			HttpURLConnection con,String MethodType) throws IOException {
		
		System.out.println(con.toString());

		if(MethodType.equals("POST") || MethodType.equals("PUT"))
		{
		// For POST only - START
			
			System.out.println(parent.toString());
		con.setDoOutput(true);
		OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
		os.write(parent.toString());
		os.flush();
		os.close();
		// For POST only - END
		}

		int responseCode = con.getResponseCode();
		//logger.debug("POST Response Code :: " + responseCode);

	  
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//logger.debug("RequestJson type is  ::" + response);
		   JSONObject 	sendauthenticateResponse1  = new JSONObject();
		   sendauthenticateResponse1.put("data", response.toString());
		   sendauthenticateResponse = sendauthenticateResponse1;
		   //logger.debug("response added  ::");
			// print result
			//System.out.println(response.toString());
		}
		
		else {
			logger.debug("POST request not worked");
			 JSONObject 	sendauthenticateResponse1  = new JSONObject();
			 
			 JSONObject errr = new JSONObject();
			 errr.put("Description","Server Error "+responseCode);
			 
			 JSONObject j = new JSONObject();
			 j.put("Error",errr);
			 
			sendauthenticateResponse1.put("data", ""+j);
			sendauthenticateResponse = sendauthenticateResponse1;
			   logger.debug("response added  ::");
		}
		return sendauthenticateResponse;
	}
	
	public static JSONObject GetResponseData1(JSONObject parent, JSONObject sendauthenticateResponse,
			HttpURLConnection con,String MethodType) throws IOException {
		
		System.out.println(con.toString());

		if(MethodType.equals("POST") || MethodType.equals("PUT"))
		{
		// For POST only - START
			
			System.out.println(parent.toString());
		con.setDoOutput(true);
		OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
		os.write(parent.toString());
		os.flush();
		os.close();
		// For POST only - END
		}

		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

	  
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			System.out.println("Response   ::" + response);
		   JSONObject 	sendauthenticateResponse1  = new JSONObject();
		   sendauthenticateResponse1.put("data", response.toString());
		   sendauthenticateResponse = sendauthenticateResponse1;
		   //logger.debug("response added  ::");
			// print result
			//System.out.println(response.toString());
		} else {
			System.out.println("POST request not worked");
			 JSONObject 	sendauthenticateResponse1  = new JSONObject();
			 
			 JSONObject errr = new JSONObject();
			 errr.put("Description","Server Error "+responseCode);
			 
			 JSONObject j = new JSONObject();
			 j.put("Error",errr);
			 
			sendauthenticateResponse1.put("data", ""+j);
			sendauthenticateResponse = sendauthenticateResponse1;
			System.out.println("response added  ::");
		}
		return sendauthenticateResponse;
	}


	

	
}
