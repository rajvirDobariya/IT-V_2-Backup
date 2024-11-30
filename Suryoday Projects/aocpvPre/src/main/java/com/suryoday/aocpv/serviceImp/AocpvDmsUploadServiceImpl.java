package com.suryoday.aocpv.serviceImp;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.aocpv.pojo.AocpvImages;
import com.suryoday.aocpv.service.AocpvImageService;
import com.suryoday.aocpv.service.DmsUploadService;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;

@Service
public class AocpvDmsUploadServiceImpl implements DmsUploadService{

	private static Logger logger = LoggerFactory.getLogger(AocpvDmsUploadServiceImpl.class);
	
	@Autowired
	AocpvImageService aocpvImageService;
	
	@Override
	public JSONObject dmsupload(long applicationNO, long customerId) {
		
		JSONObject sendAuthenticateResponse = new JSONObject();
		JSONObject request = getRequest(applicationNO,customerId);
		URL obj = null;
		try {

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
			obj = new URL(x.BASEURL +"esb/workitem/CAO/createWorkitem?api_key=" + x.api_key);
		
			HttpURLConnection httpConn = (HttpURLConnection) obj.openConnection();
			httpConn.setRequestMethod("POST");

			httpConn.setRequestProperty("Content-Type", "application/json");
			httpConn.setRequestProperty("X-Request-ID", "TAB");
			
			httpConn.setDoOutput(true);
			OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
			writer.write(request.toString());
			writer.flush();
			writer.close();
			httpConn.getOutputStream().close();

			InputStream responseStream = httpConn.getResponseCode() / 100 == 2 ? httpConn.getInputStream()
					: httpConn.getErrorStream();
			try (Scanner s = new Scanner(responseStream).useDelimiter("\\A")) {
				String response = s.hasNext() ? s.next() : "";
				sendAuthenticateResponse = new JSONObject(response);

				return sendAuthenticateResponse;
			}

		} catch (Exception e) {

		}

		logger.debug("Service END here");
		return sendAuthenticateResponse;
	}

	private JSONObject getRequest(long applicationNO,long customerId) {
		List<AocpvImages> byAppNO = aocpvImageService.getByApplicationId(applicationNO);

		JSONObject parent = new JSONObject();
		parent.put("processInstanceId", "");
		parent.put("stage", "END");
		parent.put("processName", "CAO");
		parent.put("processID", "3058");
		parent.put("videoKyc", "N");
		parent.put("workstep", "CREATE");
		parent.put("userID", "20512");
		JSONObject accountData = new JSONObject();
		accountData.put("formType", "INDIVIDUAL");
		accountData.put("channel", "SARATHI");
		accountData.put("additionalFormType", "SAV");
		accountData.put("accountNumber", applicationNO);
		accountData.put("applicationId", "");
		accountData.put("productType", "");
		parent.put("accountData", accountData);
		JSONArray customerData = new JSONArray();
		JSONObject customerData1 = new JSONObject();
		customerData1.put("firstName", "");
		customerData1.put("lastName", "");
		customerData1.put("customerType", "IND");
		customerData1.put("customerOrder", "CUST_1");
		customerData1.put("middleName", "");
		customerData1.put("cifId", customerId);
		JSONArray files = new JSONArray();
		
		for (AocpvImages aocpvImages : byAppNO) {
			JSONObject file = new JSONObject();
			byte[] images = aocpvImages.getImages();
			String	image = Base64.getEncoder().encodeToString(images);
			String imageName = aocpvImages.getImageName();
			String[] split = imageName.split("\\.");
			String extension = split[split.length-1];
			file.put("fileName", "CUST_1_"+aocpvImages.getDocumenttype()+"."+extension);
			file.put("fileString", image);
			files.put(file);
		}
		customerData1.put("files", files);
		customerData.put(customerData1);
		parent.put("customerData", customerData);
		
		
		

		return parent;
	}

}
