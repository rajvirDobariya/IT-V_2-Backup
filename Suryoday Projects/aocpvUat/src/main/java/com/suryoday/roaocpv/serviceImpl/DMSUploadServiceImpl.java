package com.suryoday.roaocpv.serviceImpl;

import java.io.IOException;
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
import com.suryoday.aocpv.pojo.AocpvLoanCreation;
import com.suryoday.aocpv.service.AocpvImageService;
import com.suryoday.aocpv.service.AocpvLoanCreationService;
import com.suryoday.roaocpv.others.ROAOCPVGenerateProperty;
import com.suryoday.roaocpv.service.DMSUploadService;

@Service
public class DMSUploadServiceImpl implements DMSUploadService {

	private static Logger logger = LoggerFactory.getLogger(DMSUploadServiceImpl.class);

	@Autowired
	AocpvImageService aocpvImageService;

	@Autowired
	AocpvLoanCreationService aocpvLoanCreationService;

	@Override
	public JSONObject dmsupload(long applicationNO) throws IOException {
		JSONObject sendAuthenticateResponse = new JSONObject();
		JSONObject request = getHardCodeValue(applicationNO);
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
			// URL url =new URL(x.BASEURLV2+"customer/workItem?api_key="+x.api_key);
			obj = new URL(x.BASEURLV2 + "banking/2/dms/upload?api_key=" + x.api_key);
			// URL url = new URL(x.BASEURLV2+"banking/2/dms/upload?api_key="+x.api_key);
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

				// Is DMS upload status maintain
				JSONArray jsonArray = sendAuthenticateResponse.getJSONArray("Response");

				for (int n = 0; n < jsonArray.length(); n++) {
					JSONObject jsonResponse = jsonArray.getJSONObject(n);

					if (jsonResponse.has("Errors")) {
						aocpvLoanCreationService.updateStatusbyapplicaton(applicationNO, "NO");
					} else {
						aocpvLoanCreationService.updateStatusbyapplicaton(applicationNO, "YES");
					}
				}

				return sendAuthenticateResponse;
			}

		} catch (Exception e) {

		}

		logger.debug("Service END here");
		return sendAuthenticateResponse;
	}

	private JSONObject getHardCodeValue(long applicationNO) {
		List<AocpvImages> byAppNO = aocpvImageService.getImageBydocType(applicationNO);
		AocpvLoanCreation loanCreation = aocpvLoanCreationService.findByApplicationNo(applicationNO);
		// Optional<AocpvImages> byApplicationNoAndName =
		// imagerepo.getByApplicationNoAndName(applicationNO,"CUST_1_PHOTOGRAPH");
		JSONObject parent = new JSONObject();
		String customerPhoto = "";
		String cifPdf = "";
		String ekyc_aadhar = "";
		String accountOpenpdf = "";
		String panCardPhoto = "";
		JSONArray data = new JSONArray();

		for (AocpvImages aocpvImages : byAppNO) {
			JSONObject data1 = new JSONObject();
			byte[] images = aocpvImages.getImages();
			JSONObject pro221 = new JSONObject();
			pro221.put("DataName", "Document_Type");

			if (aocpvImages.getDocumenttype().equalsIgnoreCase("customerPhoto")) {
				customerPhoto = Base64.getEncoder().encodeToString(images);
				data1.put("DocName", "CUST_1_PHOTOGRAPH.jpg");
				data1.put("DocType", "application/jpg");
				data1.put("FileContent", customerPhoto);
				pro221.put("Value", "application/jpg");
				data1.put("Extension", "jpg");
			} else if (aocpvImages.getDocumenttype().equalsIgnoreCase("cifPdf")) {
				cifPdf = Base64.getEncoder().encodeToString(images);
				data1.put("DocName", "CUST_1_CIF_FORM.pdf");
				data1.put("DocType", "application/pdf");
				data1.put("FileContent", cifPdf);
				pro221.put("Value", "application/pdf");
				data1.put("Extension", "pdf");
			} else if (aocpvImages.getDocumenttype().equalsIgnoreCase("ekyc_aadhar")) {
				ekyc_aadhar = Base64.getEncoder().encodeToString(images);
				data1.put("DocName", "UID_3.pdf");
				data1.put("DocType", "application/pdf");
				data1.put("FileContent", ekyc_aadhar);
				pro221.put("Value", "application/pdf");
				data1.put("Extension", "pdf");
			} else if (aocpvImages.getDocumenttype().equalsIgnoreCase("accountOpenpdf")) {
				accountOpenpdf = Base64.getEncoder().encodeToString(images);
				data1.put("DocName", "CUST_1_AO_FORM.pdf");
				data1.put("DocType", "application/pdf");
				data1.put("FileContent", accountOpenpdf);
				pro221.put("Value", "application/pdf");
				data1.put("Extension", "pdf");
			} else if (aocpvImages.getDocumenttype().equalsIgnoreCase("panCardPhoto")) {
				accountOpenpdf = Base64.getEncoder().encodeToString(images);
				data1.put("DocName", "CUST_1_PANCARD.jpg");
				data1.put("DocType", "application/jpg");
				data1.put("FileContent", panCardPhoto);
				pro221.put("Value", "application/jpg");
				data1.put("Extension", "jpg");
			}

			JSONArray metadata = new JSONArray();
			JSONObject metadata1 = new JSONObject();
			metadata1.put("meta-Name", "MD_Customer");
			JSONObject properties = new JSONObject();
			JSONObject pro233 = new JSONObject();
			pro233.put("DataName", "Application_Number");
			pro233.put("Value", loanCreation.getApplicationNo());
			properties.put("PRO233", pro233);
			JSONObject pro223 = new JSONObject();
			pro223.put("DataName", "Account_Number");
			pro223.put("Value", loanCreation.getAccountNumber());
			properties.put("PRO223", pro223);
			properties.put("PRO221", pro221);
			JSONObject pro220 = new JSONObject();
			pro220.put("DataName", "CIF_Number");
			pro220.put("Value", loanCreation.getCifNumber());
			properties.put("PRO220", pro220);
			metadata1.put("Properties", properties);
			metadata.put(metadata1);
			data1.put("Metadata", metadata);
			data1.put("Root", false);
			data.put(data1);
			parent.put("Data", data);
		}

		return parent;

	}
}