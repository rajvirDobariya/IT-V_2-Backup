package com.suryoday.travelvisit.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.travelvisit.dto.BranchDTO;
import com.suryoday.travelvisit.enums.UserRole;
import com.suryoday.travelvisit.exception.TravelVisitException;
import com.suryoday.travelvisit.model.BranchHygiene;
import com.suryoday.travelvisit.repo.BranchHygieneRepository;
import com.suryoday.travelvisit.service.BranchHygieneService;
import com.suryoday.travelvisit.utils.DateUtils;
import com.suryoday.travelvisit.utils.EncryptDecryptHelper;
import com.suryoday.travelvisit.utils.Excel;
import com.suryoday.travelvisit.utils.Image;

@Service
public class BranchHygieneServiceImpl implements BranchHygieneService {

	Logger LOG = LoggerFactory.getLogger(BranchHygieneServiceImpl.class);

	@Autowired
	private BranchHygieneRepository branchHygieneRepository;

	@Autowired
	private EncryptDecryptHelper encryptDecryptHelper;

	@Override
	public JSONObject getAllBranches(String request, String channel, String X_Session_ID, String x_User_ID,
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

		// 4. PROCESS
		LOG.debug("DecryptRequestString response :: {}", requestJson.toString());

		// 4. PROCESS
		// 4.1 Get & Validate Data
		JSONObject data = requestJson.optJSONObject("Data");
		if (data == null) {
			LOG.debug("Data is null:: {}", data);
			throw new TravelVisitException("Data is null :" + data);
		}
//		List<BranchDTO> branches = branchHygieneRepository.getAllBranches();
		List<Object[]> branches = branchHygieneRepository.getAllBranches();
		JSONArray brnachesJsonArray = convertBranchesObjectListToJsonArray(branches);

		// RESPONSE
		responseData.put("branches", brnachesJsonArray);
		response.put("Data", responseData);
		return response;

	}

	@Override
	public JSONObject addBranchHygiene(String request, String channel, String X_Session_ID, String X_User_ID,
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
		String latitude = data.optString("latitude");
		String longitude = data.optString("longitude");
		String branchId = data.optString("branchId");
		String branchName = data.optString("branchName");
		String remarks = data.optString("remarks");
		String receiptBookCheck = data.optString("receiptBookCheck");
		String dressCodeCheck = data.optString("dressCodeCheck");
		String keysCheck = data.optString("keysCheck");
		String cashVerification = data.optString("cashVerification");
		String distoApplicationDownloadedByAll = data.optString("distoApplicationDownloadedByAll");

		// VALIDATE
		validateBranchHygieneFields(userRole, latitude, longitude, branchId, branchName, remarks, receiptBookCheck,
				dressCodeCheck, keysCheck, cashVerification, distoApplicationDownloadedByAll);

		// IMAGE & LOCATION_URL
		String imageBase64 = Image.convertImageToBase64(image);
		String mapUrlLink = "https://www.google.com/maps?q=" + latitude + "," + longitude;

		// CREATE
		BranchHygiene branchHygiene = new BranchHygiene();
		branchHygiene.setImage(imageBase64);
		branchHygiene.setMapUrlLink(mapUrlLink);
		branchHygiene.setBranchId(branchId);
		branchHygiene.setBranchName(branchName);
		branchHygiene.setRemarks(remarks);
		branchHygiene.setReceiptBookCheck(receiptBookCheck);
		branchHygiene.setDressCodeCheck(dressCodeCheck);
		branchHygiene.setKeysCheck(keysCheck);
		branchHygiene.setCashVerification(cashVerification);
		branchHygiene.setDistoApplicationDownloadedByAll(distoApplicationDownloadedByAll);
		branchHygiene.setCreatedBy(Long.parseLong(X_User_ID));
		branchHygiene.setCreatedDate(new Date());
		branchHygiene = branchHygieneRepository.save(branchHygiene);

		// RESPONSE
		responseData.put("message", branchHygiene.getId() + " Activity created Successfully.");
		response.put("Data", responseData);
		return response;
	}

	@Override
	public JSONObject getBranchHygienes(String request, String channel, String X_Session_ID, String X_User_ID,
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

		validateGetBranchHygienesRequestFields(fromDateStr, toDateStr, userRole);

		// Validate and parse dates
		Date fromDate = DateUtils.getStartOfDay(fromDateStr);
		Date toDate = DateUtils.getEndOfDay(toDateStr);

		// Initialize result list
		List<BranchHygiene> activities = new ArrayList<>();

		// Fetch data based
		if (userRole.equals(UserRole.RO.getRole())) {
			activities = branchHygieneRepository.findByCreatedDateBetweenAndCreatedBy(fromDate, toDate,
					Long.parseLong(X_User_ID));

		} else if (userRole.equals(UserRole.CREDIT_OPS.getRole())) {
			activities = branchHygieneRepository.findByCreatedDateBetween(fromDate, toDate);
		} else {
			throw new TravelVisitException(
					"Check your role, You do not have the necessary permissions to get Branch Hygiene activities.");
		}

		// Remove Image
		for (BranchHygiene branchHygiene : activities) {
			branchHygiene.setImage("");
		}

		// RESPONSE
		responseData.put("activities", activities);
		response.put("Data", responseData);
		return response;

	}

	private void validateGetBranchHygienesRequestFields(String fromDateStr, String toDateStr, String userRole) {
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
	public JSONObject getBranchHygieneExcel(String request, String channel, String X_Session_ID, String X_User_ID,
			boolean isEncrypt) {
		JSONObject result = getBranchHygienes(request, channel, X_Session_ID, X_User_ID, isEncrypt);
		String excelBase64 = Excel.branchHygieneExcelBase64(result);
		// RESPONSE
		JSONObject response = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("excelBase64", excelBase64);
		response.put("Data", data);
		return response;

	}

	private void validateBranchHygieneFields(String userRole, String latitude, String longitude, String branchId,
			String branchName, String remarks, String receiptBookCheck, String dressCodeCheck, String keysCheck,
			String cashVerification, String distoApplicationDownloadedByAll) {
		if (userRole == null || userRole.isEmpty()) {
			throw new TravelVisitException("userRole is required and cannot be null or empty");
		}
		if (!userRole.equals(UserRole.RO.getRole())) {
			throw new TravelVisitException(
					"Check your role, You do not have the necessary permissions to create a 'Branch Hygiene' entry.");
		}
		if (latitude == null || latitude.isEmpty()) {
			throw new TravelVisitException("latitude is required and cannot be null or empty");
		}
		if (longitude == null || longitude.isEmpty()) {
			throw new TravelVisitException("longitude is required and cannot be null or empty");
		}
		if (branchId == null || branchId.isEmpty()) {
			throw new TravelVisitException("branchId is required and cannot be null or empty");
		}
		if (branchName == null || branchName.isEmpty()) {
			throw new TravelVisitException("branchName is required and cannot be null or empty");
		}
		if (remarks == null || remarks.isEmpty()) {
			throw new TravelVisitException("remarks is required and cannot be null or empty");
		}
		if (receiptBookCheck == null || receiptBookCheck.isEmpty()) {
			throw new TravelVisitException("receiptBookCheck is required and cannot be null or empty");
		}
		if (dressCodeCheck == null || dressCodeCheck.isEmpty()) {
			throw new TravelVisitException("dressCodeCheck is required and cannot be null or empty");
		}
		if (keysCheck == null || keysCheck.isEmpty()) {
			throw new TravelVisitException("keysCheck is required and cannot be null or empty");
		}
		if (cashVerification == null || cashVerification.isEmpty()) {
			throw new TravelVisitException("cashVerification is required and cannot be null or empty");
		}
		if (distoApplicationDownloadedByAll == null || distoApplicationDownloadedByAll.isEmpty()) {
			throw new TravelVisitException("distoApplicationDownloadedByAll is required and cannot be null or empty");
		}
	}

	private JSONArray convertBranchesObjectListToJsonArray(List<Object[]> branches) {
		JSONArray jsonArray = new JSONArray();

		for (Object[] branch : branches) {
			JSONObject branchJson = new JSONObject();
			branchJson.put("value", branch[0]);
			branchJson.put("label", branch[1]);

			jsonArray.put(branchJson);
		}

		return jsonArray;
	}

}
