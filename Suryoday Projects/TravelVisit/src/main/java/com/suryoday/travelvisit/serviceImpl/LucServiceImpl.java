package com.suryoday.travelvisit.serviceImpl;

import java.util.ArrayList;
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
import com.suryoday.travelvisit.model.Luc;
import com.suryoday.travelvisit.model.VisitTo;
import com.suryoday.travelvisit.repo.LucRepository;
import com.suryoday.travelvisit.service.LucService;
import com.suryoday.travelvisit.utils.DateUtils;
import com.suryoday.travelvisit.utils.EncryptDecryptHelper;
import com.suryoday.travelvisit.utils.Excel;
import com.suryoday.travelvisit.utils.Image;

@Service
public class LucServiceImpl implements LucService {

	Logger LOG = LoggerFactory.getLogger(LucServiceImpl.class);

	@Autowired
	private LucRepository lucRepository;

	@Autowired
	private EncryptDecryptHelper encryptDecryptHelper;

	@Override
	public JSONObject addLuc(String request, String channel, String X_Session_ID, String X_User_ID, boolean isEncrypt,
			MultipartFile image) {
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
		String remarks = data.optString("remarks");
		String latitude = data.optString("latitude");
		String longitude = data.optString("longitude");
		String business = data.optString("business");

		// VALIDATE
		validateLucFields(userRole, cifNo, name, address, phoneNumber, alternativePhoneNumber, remarks, latitude,
				longitude, business);

		// IMAGE & LOCATION_URL
		String imageBase64 = Image.convertImageToBase64(image);
		String mapUrlLink = "https://www.google.com/maps?q=" + latitude + "," + longitude;

		// CREATE
		Luc luc = new Luc();
		luc.setCifNo(cifNo);
		luc.setName(name);
		luc.setAddress(address);
		luc.setPhoneNumber(phoneNumber);
		luc.setAlternativePhoneNumber(alternativePhoneNumber);
		luc.setRemarks(remarks);
		luc.setImage(imageBase64);
		luc.setMapUrlLink(mapUrlLink);
		luc.setBusiness(business);
		luc.setCreatedBy(Long.parseLong(X_User_ID));
		luc.setCreatedDate(new Date());
		luc = lucRepository.save(luc);

		// RESPONSE
		responseData.put("message", luc.getId() + " Activity created Successfully.");
		response.put("Data", responseData);
		return response;
	}

	@Override
	public JSONObject getLucs(String request, String channel, String X_Session_ID, String X_User_ID,
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

		validateGetLucRequestFields(fromDateStr, toDateStr, userRole);

		// Validate and parse dates
		Date fromDate = DateUtils.getStartOfDay(fromDateStr);
		Date toDate = DateUtils.getEndOfDay(toDateStr);

		// Fetch data based
		List<Luc> activities = new ArrayList<>();
		if (userRole.equals(UserRole.RO.getRole())) {
			activities = lucRepository.findByCreatedDateBetweenAndCreatedBy(fromDate, toDate,
					Long.parseLong(X_User_ID));

		} else if (userRole.equals(UserRole.CREDIT_OPS.getRole())) {
			activities = lucRepository.findByCreatedDateBetween(fromDate, toDate);
		}else {
			throw new TravelVisitException(
					"Check your role, You do not have the necessary permissions to get Luc activities.");
		}

		// Remove Image
		for (Luc luc : activities) {
			luc.setImage("");
		}

		// RESPONSE
		responseData.put("activities", activities);
		response.put("Data", responseData);
		return response;
	}

	private void validateGetLucRequestFields(String fromDateStr, String toDateStr, String userRole) {
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

	@Override
	public JSONObject getExcelLuc(String request, String channel, String X_Session_ID, String X_User_ID,
			boolean isEncrypt) {
		JSONObject result = getLucs(request, channel, X_Session_ID, X_User_ID, isEncrypt);
		String excelBase64 = Excel.lucExcelBase64(result);
		// RESPONSE
		JSONObject response = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("excelBase64", excelBase64);
		response.put("Data", data);
		return response;
	}

	public void validateLucFields(String userRole, String cifNo, String name, String address, String phoneNumber,
			String alternativePhoneNumber, String remarks, String latitude, String longitude, String business) {
		if (userRole == null || userRole.isEmpty()) {
			throw new TravelVisitException("userRole is required and cannot be null or empty");
		}
		if (!userRole.equals(UserRole.RO.getRole())) {
			throw new TravelVisitException(
					"Check your role, You do not have the necessary permissions to create a 'Luc' entry.");
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
		if (remarks == null || remarks.isEmpty()) {
			throw new TravelVisitException("remarks are required and cannot be null or empty");
		}
		if (latitude == null || latitude.isEmpty()) {
			throw new TravelVisitException("latitude is required and cannot be null or empty");
		}
		if (longitude == null || longitude.isEmpty()) {
			throw new TravelVisitException("longitude is required and cannot be null or empty");
		}
		if (business == null || business.isEmpty()) {
			throw new TravelVisitException("business is required and cannot be null or empty");
		}
	}
}
