package com.suryoday.travelvisit.serviceImpl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.travelvisit.enums.UserRole;
import com.suryoday.travelvisit.exception.TravelVisitException;
import com.suryoday.travelvisit.model.VisitTo;
import com.suryoday.travelvisit.repo.VisitToRepository;
import com.suryoday.travelvisit.service.VisitToService;
import com.suryoday.travelvisit.utils.DateUtils;
import com.suryoday.travelvisit.utils.EncryptDecryptHelper;
import com.suryoday.travelvisit.utils.Excel;
import com.suryoday.travelvisit.utils.Image;

@Service
public class VisitToServiceImpl implements VisitToService {

	Logger LOG = LoggerFactory.getLogger(VisitToServiceImpl.class);

	@Autowired
	private VisitToRepository visitToRepository;

	@Autowired
	private EncryptDecryptHelper encryptDecryptHelper;

	@Override
	public JSONObject addVisitTo(String request, String channel, String X_Session_ID, String X_User_ID,
			boolean isEncrypt, MultipartFile image) {
		JSONObject response = new JSONObject();
		JSONObject responseData = new JSONObject();
		JSONObject requestJson = new JSONObject();
		// 1 VALIDATE & DECRYPT REQUEST
		if (isEncrypt) {
			requestJson = encryptDecryptHelper.validateHeadersAndSessionId(request, channel, X_Session_ID);
		} else {
			requestJson = new JSONObject(request);
		}

		// 4. PROCESS
		LOG.debug("DecryptRequestString response :: {}", requestJson.toString());

		// 4. PROCESS
		// 4.1 Get & Validate Data
		JSONObject data = requestJson.optJSONObject("Data");
		if (data == null) {
			LOG.debug("Data is null:: {}", data);
			throw new TravelVisitException("Data is null :" + data);
		}

		// Extracting all values from data
		String userRole = data.optString("userRole");
		String cifNo = data.optString("cifNo");
		String name = data.optString("name");
		String address = data.optString("address");
		String phoneNumber = data.optString("phoneNumber");
		String alternativePhoneNumber = data.optString("alternativePhoneNumber");
		String product = data.optString("product");
		String centerReport = data.optString("centerReport");
		String remarks = data.optString("remarks");
		String latitude = data.optString("latitude");
		String longitude = data.optString("longitude");

		// VALIDATE
		validateVIsitToFields(userRole, cifNo, name, address, phoneNumber, alternativePhoneNumber, product,
				centerReport, remarks, latitude, longitude);

		// IMAGE & LOCATION_URL
		String imageBase64 = Image.convertImageToBase64(image);
		String mapUrlLink = "https://www.google.com/maps?q=" + latitude + "," + longitude;

		// CREATE
		VisitTo visitTo = new VisitTo();
		visitTo.setImage(imageBase64);
		visitTo.setMapUrlLink(mapUrlLink);
		visitTo.setCifNo(cifNo);
		visitTo.setName(name);
		visitTo.setAddress(address);
		visitTo.setPhoneNumber(phoneNumber);
		visitTo.setAlternativePhoneNumber(alternativePhoneNumber);
		visitTo.setProduct(product);
		visitTo.setCenterReport(centerReport);
		visitTo.setRemarks(remarks);
		visitTo.setCreatedBy(Long.parseLong(X_User_ID));
		visitTo.setCreatedDate(new Date());
		visitTo = visitToRepository.save(visitTo);

		// RESPONSE
		responseData.put("message", visitTo.getId() + " Activity created Successfully.");
		response.put("Data", responseData);
		return response;
	}

	@Override
	public JSONObject getVisitTos(String request, String channel, String X_Session_ID, String X_User_ID,
			boolean isEncrypt) {
		JSONObject response = new JSONObject();
		JSONObject responseData = new JSONObject();
		JSONObject requestJson = new JSONObject();

		// 1 VALIDATE & DECRYPT REQUEST
		if (isEncrypt) {
			requestJson = encryptDecryptHelper.validateHeadersAndSessionId(request, channel, X_Session_ID);
		} else {
			requestJson = new JSONObject(request);
		}
		LOG.debug("DecryptRequestString response :: {}", requestJson.toString());

		// 4. PROCESS
		// 4.1 Get & Validate Data
		JSONObject data = requestJson.optJSONObject("Data");
		if (data == null) {
			LOG.debug("Data is null:: {}", data);
			throw new TravelVisitException("Data is null :" + data);
		}

		String fromDateStr = data.optString("fromDate");
		String toDateStr = data.optString("toDate");
		String userRole = data.optString("userRole");

		validateGetVisitTosRequestFields(fromDateStr, toDateStr, userRole);

		// Validate and parse dates
		Date fromDate = DateUtils.getStartOfDay(fromDateStr);
		Date toDate = DateUtils.getEndOfDay(toDateStr);

		// Fetch data based
		List<VisitTo> activities = new ArrayList<>();
		if (userRole.equals(UserRole.RO.getRole())) {
			activities = visitToRepository.findByCreatedDateBetweenAndCreatedBy(fromDate, toDate,
					Long.parseLong(X_User_ID));

		} else if (userRole.equals(UserRole.CREDIT_OPS.getRole())) {
			activities = visitToRepository.findByCreatedDateBetween(fromDate, toDate);
		} else {
			throw new TravelVisitException(
					"Check your role, You do not have the necessary permissions to get Visit To activities.");
		}
		// Remove Image
		for (VisitTo visit : activities) {
			visit.setImage("");
		}

		// RESPONSE
		responseData.put("activities", activities);
		response.put("Data", responseData);
		return response;
	}

	@Override
	public JSONObject getVisitToExcel(String request, String channel, String X_Session_ID, String X_User_ID,
			boolean isEncy) {
		JSONObject result = getVisitTos(request, channel, X_Session_ID, X_User_ID, isEncy);
		String excelBase64 = Excel.visitToExcelBase64(result);
		// RESPONSE
		JSONObject response = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("excelBase64", excelBase64);
		response.put("Data", data);
		return response;
	}

	private void validateGetVisitTosRequestFields(String fromDateStr, String toDateStr, String userRole) {
		if (fromDateStr == null || fromDateStr.isEmpty()) {
			throw new TravelVisitException("fromDate is required and cannot be null or empty");
		}
		if (toDateStr == null || toDateStr.isEmpty()) {
			throw new TravelVisitException("toDate is required and cannot be null or empty");
		}
		if (userRole == null || userRole.isEmpty()) {
			throw new TravelVisitException("userRole is required and cannot be null or empty");
		}
	}

	public void validateVIsitToFields(String userRole, String cifNo, String name, String address, String phoneNumber,
			String alternativePhoneNumber, String product, String centerReport, String remarks, String latitude,
			String longitude) {
		if (userRole == null || userRole.isEmpty()) {
			throw new TravelVisitException("userRole is required and cannot be null or empty");
		}
		if (!userRole.equals(UserRole.RO.getRole())) {
			throw new TravelVisitException(
					"Check your role, You do not have the necessary permissions to create a 'Visit To' entry.");
		}
		if (cifNo == null || cifNo.isEmpty()) {
			throw new TravelVisitException("cifNo is required and cannot be null or empty");
		}
		if (name == null || name.isEmpty()) {
			throw new TravelVisitException("name is required and cannot be null or empty");
		}
		if (address == null || address.isEmpty()) {
			throw new TravelVisitException("address is required and cannot be null or empty");
		}
		if (phoneNumber == null || phoneNumber.isEmpty()) {
			throw new TravelVisitException("phoneNumber is required and cannot be null or empty");
		}
		if (alternativePhoneNumber == null || alternativePhoneNumber.isEmpty()) {
			throw new TravelVisitException("alternativePhoneNumber is required and cannot be null or empty");
		}
		if (product == null || product.isEmpty()) {
			throw new TravelVisitException("product is required and cannot be null or empty");
		}
		if (centerReport == null || centerReport.isEmpty()) {
			throw new TravelVisitException("centerReport is required and cannot be null or empty");
		}
		if (remarks == null || remarks.isEmpty()) {
			throw new TravelVisitException("remarks are required and cannot be null or empty");
		}
		if (latitude == null || latitude.isEmpty()) {
			throw new TravelVisitException("latitude is required and cannot be null or empty");
		}
		if (longitude == null || longitude.isEmpty()) {
			throw new TravelVisitException("longitude is required and cannot be null or empty");
		}
	}

	@Override
	public String base64ToExcel(String request) {
		JSONObject requestJson = new JSONObject(request);
		String base64String = requestJson.getString("excelBase64");
		// Output Excel file path
		String filePath = "C:\\Users\\92952\\Desktop\\Travel Visit.xlsx";

		// Convert base64 string to byte array
		byte[] decodedBytes = Base64.getDecoder().decode(base64String);

		// Write byte array to file
		try (FileOutputStream fos = new FileOutputStream(filePath)) {
			fos.write(decodedBytes);
			System.out.println("Excel file created successfully.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
