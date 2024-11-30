package com.suryoday.Counterfeit.ServiceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.suryoday.Counterfeit.Exception.CounterfeitException;
import com.suryoday.Counterfeit.Exception.DenominationException;
import com.suryoday.Counterfeit.Mapper.CounterfeitDailyMapper;
import com.suryoday.Counterfeit.Mapper.DenominationMapper;
import com.suryoday.Counterfeit.Mapper.DenominationSummaryMapper;
import com.suryoday.Counterfeit.Pojo.Counterfeit;
import com.suryoday.Counterfeit.Repository.DenominationRepository;
import com.suryoday.Counterfeit.Service.CounterfeitDenominationMediator;
import com.suryoday.Counterfeit.Service.DenominationService;
import com.suryoday.Counterfeit.Utils.EncryptDecryptHelper;
import com.suryoday.Counterfeit.Utils.PDFGenerator;
import com.suryoday.Counterfeit.dao.DenominationDAO;
import com.suryoday.Counterfeit.dao.DenominationSummaryDAO;
import com.suryoday.Counterfeit.enums.UserRole;

@Service
public class DenominationServiceImpl implements DenominationService {

	Logger LOG = LoggerFactory.getLogger(DenominationServiceImpl.class);

	@Autowired
	private CounterfeitDenominationMediator counterfeitDenominationMediator;
	@Autowired
	private DenominationRepository denominationRepository;
	@Autowired
	private DenominationMapper denominationMapper;
	@Autowired
	private DenominationDAO denominationDAO;
	@Autowired
	private DenominationSummaryDAO denominationSummaryDAO;
	@Autowired
	private CounterfeitDailyMapper counterfeitDailyMapper;

	@Override
	public JSONObject getDenominations(String encryptRequestString, String channel, String X_Session_ID,
			String X_encode_ID, HttpServletRequest request, boolean isDownload, boolean isEncrypt) {
		JSONObject response = new JSONObject();
		JSONObject requestJson = new JSONObject();
		// 1 VALIDATE & DECRYPT REQUEST
		if (isEncrypt) {
			requestJson = validateRequest(encryptRequestString, channel, X_Session_ID, X_encode_ID, request);
		} else {
			requestJson = new JSONObject(encryptRequestString);
		}
		LOG.debug("DecryptRequestString response :: {}", requestJson.toString());

		// 2 PROCESS
		JSONObject data = requestJson.optJSONObject("Data");
		if (data == null) {
			LOG.debug("Data is null :: {}", data);
			throw new CounterfeitException("Data is null : " + data);
		}

		String role = data.optString("role");
		UserRole userRoleEnum = counterfeitDenominationMediator.validateUserRole(role);

		// Is Download
		if (isDownload) {
			if (!role.equals(UserRole.COUNTERFEIT_HO.getRole())) {
				LOG.debug("Your role is " + role + " and you have not authority to download PDF!",
						HttpStatus.BAD_REQUEST);
				throw new CounterfeitException("Your role is " + role + " and you have not authority to download PDF!");
			}
		}

		// GET Counterfeit
		Long counterfeitId = data.optLong("counterfeitId");
		Counterfeit counterfeit = null;
		JSONObject counterfeitJson = new JSONObject();
		if (counterfeitId != 0) {
			counterfeit = counterfeitDenominationMediator.getCounterfeitById(counterfeitId);
			counterfeitJson = counterfeitDailyMapper.convertToJson(counterfeit);
		}

		// GET Denominations from DB
		JSONArray denominationJsonArray = new JSONArray();

		// Daily
		List<Object[]> dailyDenominations = denominationDAO.findDenominationByFilter(data, counterfeit);
		for (Object[] denomination : dailyDenominations) {
			JSONObject denominationJson = denominationMapper.convertToJson(denomination);
			denominationJsonArray.put(denominationJson);
		}
		// GET Daily Summary
		List<Object[]> dailySummary = denominationSummaryDAO.findSummryByFilter(data, counterfeit);

		// Monthly
		List<Object[]> monthlySummary = null;
		if (counterfeitId != 0 && counterfeit.getDailyMonthly().equals("Monthly")
				&& !counterfeit.getStatus().equals("Submitted")) {
			// GET Denominations
			List<Object[]> monthlyDenominations = denominationRepository.findByCounterfeitId(counterfeitId);
			for (Object[] denomination : monthlyDenominations) {
				JSONObject denominationJson = denominationMapper.convertToJson(denomination);
				denominationJsonArray.put(denominationJson);
			}
			// GET Summary
			monthlySummary = denominationRepository.findSummaryByCounterfeitId(counterfeitId);
		}

		JSONObject summary = DenominationSummaryMapper.convertToJsonObject(dailySummary, monthlySummary);

		// Make Response Data
		JSONObject Data = new JSONObject();
		Data.put("denominations", denominationJsonArray);
		Data.put("summary", summary.optJSONArray("summary"));
		Data.put("grandTotal", summary.optLong("grandTotal"));
		Data.put("grandCount", summary.optLong("grandCount"));
		Data.put("counterfeit", counterfeitJson);
		response.put("Data", Data);
		return response;
	}

	@Override
	public JSONObject updateDenominations(String encryptRequestString, String channel, String X_Session_ID,
			String X_encode_ID, HttpServletRequest request, boolean isEncrypt) {
		JSONObject requestJson = new JSONObject();
		// 1 VALIDATE & DECRYPT REQUEST
		if (isEncrypt) {
			requestJson = validateRequest(encryptRequestString, channel, X_Session_ID, X_encode_ID, request);
		} else {
			requestJson = new JSONObject(encryptRequestString);
		}
		LOG.debug("DecryptRequestString response :: {}", requestJson.toString());

		// 2 VALIDATE DATA
		JSONObject data = requestJson.optJSONObject("Data");
		if (data == null) {
			LOG.debug("Data is null :: {}", data);
			throw new CounterfeitException("Data is null : " + data);
		}

		Long counterfeitId = data.optLong("counterfeitId");
		Long roId = data.optLong("roId");
		String role = data.optString("role");
		String roRemarks = data.optString("roRemarks");
		String isDetect = "No";

		// Validate User Role
		UserRole userRoleEnum = counterfeitDenominationMediator.validateUserRole(role);

		if (counterfeitId == null || counterfeitId == 0 || roId == null || roId == 0 || role == null || role.isEmpty()
				|| roRemarks == null || roRemarks.isEmpty()) {
			// Create a detailed message
			String errorMessage = String.format("One or more required fields are null or empty in Data of Request: "
					+ "counterfeitId=%s, roId=%s, role=%s, roRemarks=%s", counterfeitId, roId, role, roRemarks);

			// Log the detailed message
			LOG.debug(errorMessage + " :: HttpStatus=" + HttpStatus.BAD_REQUEST);

			// Throw an exception with the detailed message
			throw new CounterfeitException(errorMessage);
		}
		Counterfeit counterfeit = counterfeitDenominationMediator.getCounterfeitById(counterfeitId);

		// Validate Role
		if (!role.equals(UserRole.COUNTERFEIT_MAKER.getRole())) {
			LOG.debug("Your role is " + role + " and you have not authority to update counterfeit!",
					HttpStatus.BAD_REQUEST);
			throw new CounterfeitException(
					"Your role is " + role + " and you have not authority to update counterfeit!");
		}

		// Validate Status
		if (!counterfeit.getStatus().equals("Pending at Maker")) {
			LOG.debug("You can not update denomination's of activity " + counterfeitId + " because status is "
					+ counterfeit.getStatus() + "! :: {}", HttpStatus.BAD_REQUEST);
			throw new DenominationException("You can not update denomination's of activityId " + counterfeitId
					+ " because status is " + counterfeit.getStatus() + "!");
		}

		// PROCESS

		// Validate denomination's
		JSONArray denominations = data.optJSONArray("denominations");
		if (denominations != null && denominations.length() > 0) {
			counterfeitDenominationMediator.validateDenominations(denominations);
			// 1 delete old denomination's
			denominationRepository.deleteAllByCounterfeitId(counterfeitId);
			// 2 create new denomination's
			counterfeitDenominationMediator.createDenomination(denominations, roId, counterfeitId);
			isDetect = "Yes";
		}

		// UPDATE status in counterfeit
		counterfeit.setIsDetect(isDetect);
		counterfeit.setStatus("Pending at Checker");
		counterfeit.setRoRemarks(roRemarks);
		counterfeit.setUpdateBy(roId);
		counterfeit.setUpdatedDate(new Date());
		counterfeitDenominationMediator.saveCounterfeit(counterfeit);
		// RESPONSE
		JSONObject response = new JSONObject();
		JSONObject Data = new JSONObject();
		Data.put("message", " Activity " + counterfeitId + " updated sccessfully!");
		response.put("Data", Data);
		return response;

	}

	@Override
	public JSONObject downloadPDF(String encryptRequestString, String channel, String X_Session_ID, String X_encode_ID,
			HttpServletRequest request, boolean isEncrypt) {

		JSONObject result = getDenominations(encryptRequestString, channel, X_Session_ID, X_encode_ID, request, true,
				isEncrypt);

		// Get Counterfeits from DB
		String pdfBase64 = PDFGenerator.generatePDF(result);
		JSONObject response = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("pdfBase64", pdfBase64);
		response.put("Data", data);
		return response;
	}

	public JSONObject validateRequest(String encryptRequestString, String channel, String X_Session_ID,
			String X_encode_ID, HttpServletRequest request) {
		String message = String.format(
				"One or more required fields are null or empty. Values - "
						+ "encryptRequestString: '%s', channel: '%s', X_Session_ID: '%s', X_encode_ID: '%s'",
				encryptRequestString, channel, X_Session_ID, X_encode_ID);
		LOG.debug("Validate Headers :" + message);

		// 1 Check fields null and empty
		if (encryptRequestString == null || encryptRequestString.isEmpty() || channel == null || channel.isEmpty()
				|| X_Session_ID == null || X_Session_ID.isEmpty() || X_encode_ID == null || X_encode_ID.isEmpty()) {

			LOG.debug("Validate Headers :" + message);
			throw new CounterfeitException("Validate Headers :" + message);
		}

		// 2 Check DB session ID
		String dbSessionId = counterfeitDenominationMediator.fetchSessionId(X_Session_ID);
		if (dbSessionId == null || dbSessionId.isEmpty()) {
			LOG.debug("SessionId is expired or Invalid sessionId :: {}", HttpStatus.UNAUTHORIZED);
			throw new CounterfeitException("SessionId is expired or Invalid sessionId!");
		}
		// 3 DecryptRequestString
		return EncryptDecryptHelper.decryptRequestString(encryptRequestString, channel, X_Session_ID, X_encode_ID);

	}

	public Long getApplicationId() {
		return Long.parseLong(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmssSSS")));
	}

	@Override
	public void deleteAll() {
		denominationRepository.deleteAll();
	}

	@Override
	public void deleteByCounterfeitId(Long counterfeitId) {
		denominationRepository.deleteAllByCounterfeitId(counterfeitId);
	}

}
