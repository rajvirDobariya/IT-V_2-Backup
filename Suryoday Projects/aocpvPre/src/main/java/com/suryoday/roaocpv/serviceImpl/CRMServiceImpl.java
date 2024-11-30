package com.suryoday.roaocpv.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.pojo.AocpCustomer;
import com.suryoday.aocpv.service.AocpCustomerDataService;
import com.suryoday.connector.serviceImpl.GenerateProperty;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.roaocpv.service.CRMService;


@Service
public class CRMServiceImpl implements CRMService{
	

	@Autowired
	AocpCustomerDataService aocpCustomerDataService;
	
	 Logger logger = LoggerFactory.getLogger(CRMServiceImpl.class);

	@Override
	public JSONObject crmData(String applicationNO, String x_User_ID) throws IOException {
	 
		
		JSONObject obk= new JSONObject();	
		JSONObject sendResponse = new JSONObject();
		URL obj = null;
		try {
			long AppicationNoInLong = Long.parseLong(applicationNO);
			
			AocpCustomer customerData = aocpCustomerDataService.getByApplicationNo(AppicationNoInLong);
			  String customerNo = Long.toString(customerData.getCustomerId());
			  JSONObject request = getRequest(customerData,x_User_ID);
			  ROAOCPVGenerateProperty x = ROAOCPVGenerateProperty.getInstance();
			  x.getappprop();
            x.bypassssl();
            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
           logger.debug(x.BASEURL + "customer/v2?api_key=" + x.api_key);
           obj = new URL(x.BASEURL + "customer/"+customerData.getCustomerId()+"/modify/v2?api_key=" + x.api_key);
        	HttpURLConnection httpConn = (HttpURLConnection) obj.openConnection();
            httpConn.setRequestMethod("PUT");

            httpConn.setRequestProperty("X-Correlation-ID", "DIG123456789056");
            httpConn.setRequestProperty("X-Request-ID", "IEXCEED");
            httpConn.setRequestProperty("X-User-ID", "14508");
            httpConn.setRequestProperty("X-From-ID", "IEXCEED");
            httpConn.setRequestProperty("X-To-ID", "IEXCEED");
            httpConn.setRequestProperty("X-Transaction-ID", "EabeDcEE-db3c-BddD-CbD7-4bAA992c75d4");
            httpConn.setRequestProperty("Content-Type", "application/json");

            sendResponse = getResponseData(request, sendResponse, httpConn, "PUT");
            
		}
		 catch (Exception e) {
	            // TODO: handle exception
	        }
	    	logger.debug("Service END");
			return sendResponse;
	}

	private static JSONObject getResponseData(JSONObject parent, JSONObject sendAuthenticateResponse,
			HttpURLConnection con, String MethodType) throws IOException {

		con.setDoOutput(true);
		OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
		os.write(parent.toString());
		os.flush();
		os.close();

		int responseCode = con.getResponseCode();
		//logger.debug("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			JSONObject sendauthenticateResponse1 = new JSONObject();
			sendauthenticateResponse1.put("data", response.toString());
			sendAuthenticateResponse = sendauthenticateResponse1;
		} else {
		//	logger.debug("POST request not worked");

			JSONObject sendauthenticateResponse1 = new JSONObject();

			JSONObject errr = new JSONObject();
			errr.put("Description", "Server Error " + responseCode);

			JSONObject j = new JSONObject();
			j.put("Error", errr);

			sendauthenticateResponse1.put("data", "" + j);
			sendAuthenticateResponse = sendauthenticateResponse1;
		}

		return sendAuthenticateResponse;

	}

	@Override
	public JSONObject getBranchNameCode(JSONObject header) throws IOException {
		
		JSONObject sendResponse = new JSONObject();	
		URL obj = null;
		try {			
			GenerateProperty x = GenerateProperty.getInstance();
			x.getappprop();
            x.bypassssl();
            
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };      
            
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            logger.debug(x.BASEURL+"banking/BRANCH/1/masterData?api_key="+ x.api_key);

			obj = new URL(x.BASEURL+"banking/BRANCH/1/masterData?api_key="+ x.api_key);
     
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("X-Request-ID", header.getString("X-Request-ID"));
			con.setRequestProperty("X-User-ID", header.getString("X-User-ID"));
			con.setRequestProperty("X-From-ID", header.getString("X-From-ID"));
			con.setRequestProperty("X-To-ID", header.getString("X-To-ID"));
			con.setRequestProperty("X-Transaction-ID", header.getString("X-Transaction-ID"));
			con.setRequestProperty("X-Correlation-ID", header.getString("X-Correlation-ID"));
			
			sendResponse = getResponse1("", sendResponse, con, "GET");
		}
		 catch (Exception e) {
	            // TODO: handle exception
	        }
	    	
			return sendResponse;
			
	}

	@Override
	public JSONObject getRepayment() {


JSONObject objN= new JSONObject();	
		
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

            URL url = new URL("https://intramashery.suryodaybank.com/ssfb/repayment/schedule/generation?api_key=sbx8chmdwa9f82du6snc3624");
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("POST");

            httpConn.setRequestProperty("X-Request-ID", "HOTFOOT");
            httpConn.setRequestProperty("X-User-ID", "S7402");
            httpConn.setRequestProperty("X-From-ID", "HOTFOOT");
            httpConn.setRequestProperty("X-To-ID", "Other");
            httpConn.setRequestProperty("X-Correlation-ID", "Paytm001");
            httpConn.setRequestProperty("X-Transaction-ID", "EabeDcEE-db3c-BddD-CbD7-4bAA992c75d4");
            httpConn.setRequestProperty("Content-Type", "application/json");

            httpConn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
            writer.write("{\\n\\n    \\\"Data\\\": {\\n\\n        \\\"AccountNumber\\\": \\\"208000000386\\\",\\n\\n        \\\"EndDate\\\": \\\"\\\",\\n\\n        \\\"Branch\\\": \\\"10011\\\",\\n\\n        \\\"ProductType\\\": \\\"\\\",\\n\\n        \\\"Amount\\\": \\\"\\\",\\n\\n        \\\"Frequency\\\": \\\"\\\",\\n\\n        \\\"Term\\\": \\\"\\\",\\n\\n        \\\"InterestRate\\\": \\\"\\\",\\n\\n        \\\"PrincipalMoratorium\\\": \\\"\\\",\\n\\n        \\\"InterestMoratorium\\\": \\\"\\\",\\n\\n        \\\"EmiChange\\\": \\\"\\\",\\n\\n        \\\"UserID\\\": \\\"S7403\\\"\\n\\n    }\\n\\n}\\n\\n");
            writer.flush();
            writer.close();
            httpConn.getOutputStream().close();

            InputStream responseStream = httpConn.getResponseCode() / 100 == 2 ?
                httpConn.getInputStream() :
                httpConn.getErrorStream();
            try (Scanner s = new Scanner(responseStream).useDelimiter("\\A")) {
                String response = s.hasNext() ? s.next() : "";
               
                System.out.println("1"+response);
                
                objN= new JSONObject(response);
                System.out.println("2"+objN);
                return objN;
            }
		}
		 catch (Exception e) {
	            // TODO: handle exception
	        }
	    	logger.debug("Service END");
	    	System.out.println(objN);
			return objN;
	}
	@Override
	public JSONObject fetchAllBranch() {


	
StringBuffer response = null;	
URL obj = null;
		try {			
			GenerateProperty x = GenerateProperty.getInstance();
			x.getappprop();
            x.bypassssl();
            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };      
            
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            logger.debug(x.BASEURL+"BRANCH/1/masterData/v2?api_key="+x.api_key);
			 obj =new URL(x.BASEURL+"BRANCH/1/masterData/v2?api_key="+x.api_key);
            HttpURLConnection httpConn = (HttpURLConnection) obj.openConnection();
            httpConn.setRequestMethod("GET");

            httpConn.setRequestProperty("X-Request-ID", "TIBCO");
            httpConn.setRequestProperty("X-User-ID", "10617");
            httpConn.setRequestProperty("X-From-ID", "IBR");           
            httpConn.setRequestProperty("X-Transaction-ID", "4324324");
            httpConn.setRequestProperty("Content-Type", "application/json");

            httpConn.setDoOutput(true);
           // OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
         //   writer.write(" {\\r\\n     \\\"Data\\\":\\r\\n     {\\r\\n         \\\"ProductType\\\":\\\"KYCTYP\\\"\\r\\n     }\\r\\n}");
          //  writer.flush();
        //    writer.close();
       //   httpConn.getOutputStream().close();

            int responseCode = httpConn.getResponseCode();
          //logger.debug("POST Response Code :: " + responseCode);

            System.out.println(""+responseCode);
          if (responseCode == HttpURLConnection.HTTP_OK) { // success
          BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
          String inputLine;
          response = new StringBuffer();

          while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
          }
          in.close();

          //logger.debug("RequestJson type is ::" + response);
        
          //logger.debug("response added ::");
          // print result
          System.out.println(response.toString());
          }

          else {
          logger.debug("POST request not worked");
          JSONObject sendauthenticateResponse1 = new JSONObject();

          JSONObject errr = new JSONObject();
          errr.put("Description","Server Error "+responseCode);

         
          logger.debug("response added ::");
          }
		}
		catch(Exception e)
		{
			
			e.printStackTrace();
			}
		JSONObject objectBranch= new JSONObject(response.toString());
		System.out.println("Final String "+objectBranch);
		return objectBranch;
		}
                
			private JSONObject getRequest(AocpCustomer customerData,String x_User_ID) {
				
				JSONObject Data = new JSONObject();
	            Data.put("RegisteredMobile", customerData.getMobileNo());
	        //    Data.put("EmailAddress", allHostsValid);
	            Data.put("Source", "IEX");
	            Data.put("UserId", x_User_ID);
	            Data.put("CRMUserId", x_User_ID);
	            Data.put("CustomerType", "R");
	          //  Data.put("MaritalStatus", allHostsValid);
	            String address1 = customerData.getAddress();
	       
				if(address1 == null || address1.isEmpty()) {
					
				}
				else {
					org.json.JSONArray addressInJson =new org.json.JSONArray(address1);
	  			for(int n=0;n<addressInJson.length();n++) {
	  			JSONObject jsonObject2 = addressInJson.getJSONObject(n);
	  			String	addressType=jsonObject2.getString("ADDRESSTYPE");
	  			if(addressType.equalsIgnoreCase("CURRENT ADDRESS")) {
	  				addressType="R";
	  				Data.put("AddressType2", addressType);
	  	            Data.put("Address21", jsonObject2.getString("ADDRESS_LINE1"));
	  	            Data.put("Address22", jsonObject2.getString("ADDRESS_LINE2"));
	  	            Data.put("Address23", jsonObject2.getString("ADDRESS_LINE3"));
	  	            Data.put("Landmark2", jsonObject2.getString("LANDMARK"));
	  	            Data.put("District2", jsonObject2.getString("DISTRICT"));
	  	            Data.put("PinCode2", jsonObject2.getString("PINCODE"));
	  	            Data.put("City2", jsonObject2.getString("CITY"));
	  	            Data.put("State2", jsonObject2.getString("STATE"));
	  	            Data.put("Country2", jsonObject2.getString("COUNTRY"));
	  			}
	  			else if(addressType.equalsIgnoreCase("PERMANENT ADDRESS")) {
	  				addressType="P";
	  				 Data.put("AddressType", addressType);
	  	  		     Data.put("Address1", jsonObject2.getString("ADDRESS_LINE1"));
	  	             Data.put("Address2", jsonObject2.getString("ADDRESS_LINE2"));
	  	             Data.put("Address3", jsonObject2.getString("ADDRESS_LINE3"));
	  	             Data.put("Landmark", jsonObject2.getString("LANDMARK"));
	  	             Data.put("District", jsonObject2.getString("DISTRICT"));
	  	             Data.put("PinCode", jsonObject2.getString("PINCODE"));
	  	             Data.put("City", jsonObject2.getString("CITY"));
	  	             Data.put("State", jsonObject2.getString("STATE"));
	  	             Data.put("Country", jsonObject2.getString("COUNTRY"));
	  			}
	  			else if(addressType.equalsIgnoreCase("OFFICE ADDRESS")) {
	  				addressType="O";
	  				Data.put("AddressType1", addressType);
	  	            Data.put("Address11", jsonObject2.getString("ADDRESS_LINE1"));
	  	            Data.put("Address12", jsonObject2.getString("ADDRESS_LINE2"));
	  	            Data.put("Address13", jsonObject2.getString("ADDRESS_LINE3"));       
	  	            Data.put("Landmark1", jsonObject2.getString("LANDMARK"));
	  	            Data.put("District1", jsonObject2.getString("DISTRICT"));
	  	            Data.put("PinCode1", jsonObject2.getString("PINCODE"));
	  	            Data.put("City1", jsonObject2.getString("CITY"));
	  	            Data.put("State1", jsonObject2.getString("STATE"));
	  	            Data.put("Country1", jsonObject2.getString("COUNTRY"));
	  			}
	  				
	  			}
				}
				return Data;
		
				}
		
			private static JSONObject getResponse1(String parent, JSONObject sendAuthenticateResponse,
					HttpURLConnection con,String MethodType) throws IOException {
				
				con.setDoOutput(true);
				//OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
				//os.write(parent.toString());
				//os.flush();
				//os.close();
				
				int responseCode = con.getResponseCode();
//				logger.debug("POST Response Code :: " + responseCode);
				
				
				if (responseCode == HttpURLConnection.HTTP_OK) {
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer response = new StringBuffer();
					
					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					in.close();
					
					
					JSONObject 	sendauthenticateResponse1  = new JSONObject();
					   sendauthenticateResponse1.put("data", response.toString());
					   sendAuthenticateResponse = sendauthenticateResponse1;
				}
				else
				{
	//						logger.debug("GET request not worked");
					
					JSONObject 	sendauthenticateResponse1  = new JSONObject();
					 
					 JSONObject errr = new JSONObject();
					 errr.put("Description","Server Error "+responseCode);
					 
					 JSONObject j = new JSONObject();
					 j.put("Error",errr);
					 
					sendauthenticateResponse1.put("data", ""+j);
					sendAuthenticateResponse = sendauthenticateResponse1;
				}
				
						return sendAuthenticateResponse;
				
			}
}
