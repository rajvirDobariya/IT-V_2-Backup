package com.suryoday.dsaOnboard.serviceImpl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.dsaOnboard.pojo.DsaImage;
import com.suryoday.dsaOnboard.service.DmsUploadService;
import com.suryoday.dsaOnboard.service.DsaImageService;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
@Service
public class DmsUploadServiceImpl implements DmsUploadService {

	@Autowired
	DsaImageService imageService;
	
	@Override
	public JSONObject dmsUpload(String applicationNo, String dsaCode) {
		JSONObject request = getRequest(applicationNo,dsaCode);
		JSONObject sendAuthenticateResponse = new JSONObject();
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
			obj = new URL(x.BASEURLV3 + "banking/7358/dms/upload?api_key=" + x.api_key);
			
			HttpURLConnection httpConn = (HttpURLConnection) obj.openConnection();
			httpConn.setRequestMethod("PUT");

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
		return sendAuthenticateResponse;
	}

	private JSONObject getRequest(String applicationNo,String dsaCode) {
		List<DsaImage> images = imageService.getByApplicationNo(applicationNo);
		JSONObject parent = new JSONObject();
		JSONArray data = new JSONArray();
		for (DsaImage image:images) {
			JSONObject jsonObject = new JSONObject();
			byte[] imageInByte = image.getImages();
			String imageName = image.getImageName();
			String[] split = imageName.split("\\.");
			String extension = split[split.length-1];
			String imageInBase64 = Base64.getEncoder().encodeToString(imageInByte);
			jsonObject.put("DocName", dsaCode+"_"+image.getDocumenttype()+"."+extension);
			jsonObject.put("DocType", "application/"+extension);
			jsonObject.put("FileContent", imageInBase64);
			jsonObject.put("Extension", "jpg");
			jsonObject.put("Root", false);
			JSONArray MetaData =new JSONArray();
			JSONObject metaData = new JSONObject();
			metaData.put("meta-Id", "339");
			metaData.put("meta-Name", "PAYOUT");
			JSONObject properties = new JSONObject();
			JSONObject PRO310 = new JSONObject();
			PRO310.put("DataName", "Agencyid");
			PRO310.put("Value", dsaCode);
			properties.put("PRO310", PRO310);
			metaData.put("Properties", properties);
			MetaData.put(metaData);
			jsonObject.put("Metadata", MetaData);
			data.put(jsonObject);
		}
		parent.put("Data", data);
		return parent;
	}

}
