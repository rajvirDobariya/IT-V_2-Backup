package com.suryoday.Counterfeit.ServiceImpl;

import java.time.YearMonth;
import java.util.ArrayList;
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

import com.suryoday.Counterfeit.Dto.BranchMasterDTO;
import com.suryoday.Counterfeit.Exception.CounterfeitException;
import com.suryoday.Counterfeit.Mapper.CounterfeitDailyMapper;
import com.suryoday.Counterfeit.Mapper.CounterfeitMonthlyMapper;
import com.suryoday.Counterfeit.Pojo.Counterfeit;
import com.suryoday.Counterfeit.Repository.CounterfeitRepository;
import com.suryoday.Counterfeit.Service.CounterfeitDenominationMediator;
import com.suryoday.Counterfeit.Service.CounterfeitService;
import com.suryoday.Counterfeit.Utils.Constants;
import com.suryoday.Counterfeit.Utils.DateUtils;
import com.suryoday.Counterfeit.Utils.EncryptDecryptHelper;
import com.suryoday.Counterfeit.Utils.ExcelService;
import com.suryoday.Counterfeit.dao.CounterfeitDailyDAO;
import com.suryoday.Counterfeit.dao.CounterfeitMonthlyDAO;
import com.suryoday.Counterfeit.enums.UserRole;

@Service
public class CounterfeitServiceImpl implements CounterfeitService {

	Logger LOG = LoggerFactory.getLogger(CounterfeitServiceImpl.class);

	@Autowired
	private CounterfeitDenominationMediator counterfeitDenominationMediator;

	@Autowired
	private CounterfeitRepository counterfeitRepository;
	@Autowired
	private CounterfeitDailyMapper counterfeitMapper;
	@Autowired
	private CounterfeitDailyDAO counterfeitDAO;
	@Autowired
	private CounterfeitMonthlyDAO monthlyReportDAO;
	@Autowired
	private CounterfeitMonthlyMapper monthlyReportMapper;

	@Override
	public JSONObject createCounterfeit(String encryptRequestString, String channel, String X_Session_ID,
			String X_encode_ID, HttpServletRequest request, boolean isEncrypt) {

		JSONObject response = new JSONObject();
		JSONObject responseData = new JSONObject();
		Counterfeit counterfeit = new Counterfeit();
		JSONObject requestJson = new JSONObject();

		// 1 VALIDATE & DECRYPT REQUEST
		if (isEncrypt) {
			requestJson = validateHeadersAndSessionId(encryptRequestString, channel, X_Session_ID, X_encode_ID,
					request);
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
		String dailyMonthly = data.optString("dailyMonthly").trim();
		String branchCode = data.optString("branchCode");
		Long roId = data.optLong("roId");
		String role = data.optString("role");
		String roRemarks = data.optString("roRemarks");

		// Validate UserRole
		UserRole userRoleEnum = counterfeitDenominationMediator.validateUserRole(role);

		// 3 VALIDATE Fields
		if (dailyMonthly == null || dailyMonthly.isEmpty() || branchCode == null || branchCode.isEmpty() || roId == null
				|| roId == 0L || role == null || role.isEmpty() || roRemarks == null || roRemarks.isEmpty()) {
			String errorMessage = String.format(
					"One or more required fields are null or empty in Data of Request: "
							+ "dailyMonthly=%s, branchCode=%s, roId=%s, role=%s, roRemarks=%s",
					dailyMonthly, branchCode, roId, role, roRemarks);
			LOG.debug("{}", errorMessage);
			throw new CounterfeitException(errorMessage);
		}

		// Validate branchCode: Only numeric values
		if (!branchCode.matches("\\d+")) {
			LOG.debug("branchCode contains non-numeric characters :: {}", HttpStatus.BAD_REQUEST);
			throw new CounterfeitException("branchCode can only contain numeric characters!");
		}

		// Check Remarks length
		if (roRemarks.length() > 200) {
			throw new CounterfeitException("Remarks not allowed more than 200 character!");
		}
		String branchName = counterfeitRepository.findBranchNameByBranchId(branchCode);

		if (!role.equals(UserRole.COUNTERFEIT_MAKER.getRole())) {
			LOG.debug("Your role is " + role + " and you have not authority to add counterfeit!",
					HttpStatus.BAD_REQUEST);
			throw new CounterfeitException("Your role is " + role + " and you have not authority to add counterfeit!");
		}

		// 4.1 DAILY
		if (dailyMonthly.equals("Daily")) {
			counterfeit = createDaily(data, counterfeit, branchCode, branchName, roId, roRemarks, role);
			// 4.2 MONTHLY
		} else if (dailyMonthly.equals("Monthly")) {
			counterfeit = createMonthly(data, counterfeit, branchCode, branchName, roId, roRemarks, role);
		} else {
			throw new CounterfeitException("dailyMonthly is :" + dailyMonthly + "!");
		}

		// 5 RESPONSE
		responseData.put("value", "Activity " + counterfeit.getId() + " created successfully!");
		response.put("Data", responseData);
		LOG.debug("Activity " + counterfeit.getId() + "created successfully!");
		return response;
	}

	public Counterfeit createDaily(JSONObject data, Counterfeit counterfeit, String branchCode, String branchName,
			Long roId, String roRemarks, String role) {

		// Validate Rate Limit
		Long count = counterfeitRepository.findCountByBranchCodeAndToday(branchCode, DateUtils.getStartOfDay(),
				DateUtils.getEndOfDay());
		if (count >= 3) {
			throw new CounterfeitException(
					String.format("Branch '%s' exceeded the daily activity limit of %d. Activities today: %d.",
							branchCode, 3, count));
		}

		// Check Is Monthly verified
		boolean isMonthlyVefiried = counterfeitRepository.existsByBranchCodeAndMonthAndStatusSubmitted(branchCode,
				YearMonth.now());
		if (isMonthlyVefiried) {
			throw new CounterfeitException(
					"Counterfeit items for this month have already been verified by the checker. Adding more counterfeit entries for this month is not allowed!");
		}

		// Validation 3 checks
		Boolean theNotesHaveBeenImpounded = data.optBoolean("theNotesHaveBeenImpounded");
		Boolean theRegisterUpdatedWithDetails = data.optBoolean("theRegisterUpdatedWithDetails");
		Boolean anAcknowledgmentReceiptPrepared = data.optBoolean("anAcknowledgmentReceiptPrepared");
		validateThreeChecks(theNotesHaveBeenImpounded, theRegisterUpdatedWithDetails, anAcknowledgmentReceiptPrepared);

		// Validate Denomination's
		JSONArray denominationsJsonArray = data.optJSONArray("denominations");
		counterfeitDenominationMediator.validateDenominations(denominationsJsonArray);

		if (denominationsJsonArray == null || denominationsJsonArray.length() <= 0) {
			LOG.debug("Denominations JSON array is null or empty :: {}");
			throw new CounterfeitException("Denominations JSON array is null or empty!");
		}

		// CREATE Counterfeit
		counterfeit.setCreatedBy(roId);
		counterfeit.setBranchName(branchName);
		counterfeit.setBranchCode(branchCode);
		counterfeit.setCreatedDate(new Date());
		counterfeit.setRoId(roId);
		counterfeit.setRoRemarks(roRemarks);
		counterfeit.setRoRole(role);
		counterfeit.setStatus("Pending at Checker");
		counterfeit.setDailyMonthly("Daily");
		counterfeit.setIsDetect("Yes");
		counterfeit.setTheRegisterUpdatedWithDetails(theRegisterUpdatedWithDetails);
		counterfeit.setTheNotesHaveBeenImpounded(theNotesHaveBeenImpounded);
		counterfeit.setAnAcknowledgmentReceiptPrepared(anAcknowledgmentReceiptPrepared);
		counterfeit = counterfeitRepository.save(counterfeit);

		// 2 CRAETE Denomination's
		counterfeitDenominationMediator.createDenomination(denominationsJsonArray, roId, counterfeit.getId());
		return counterfeit;
	}

	public Counterfeit createMonthly(JSONObject data, Counterfeit counterfeit, String branchCode, String branchName,
			Long roId, String roRemarks, String role) {

		// Validate Denominations
		JSONArray denominationsJsonArray = data.optJSONArray("denominations");
		boolean isDenominations = denominationsJsonArray != null && denominationsJsonArray.length() > 0;
		Boolean theNotesHaveBeenImpounded;
		Boolean theRegisterUpdatedWithDetails;
		Boolean anAcknowledgmentReceiptPrepared;
		if (isDenominations) {
			counterfeitDenominationMediator.validateDenominations(denominationsJsonArray);

			// Validation 3 checks
			theNotesHaveBeenImpounded = data.optBoolean("theNotesHaveBeenImpounded");
			theRegisterUpdatedWithDetails = data.optBoolean("theRegisterUpdatedWithDetails");
			anAcknowledgmentReceiptPrepared = data.optBoolean("anAcknowledgmentReceiptPrepared");
			validateThreeChecks(theNotesHaveBeenImpounded, theRegisterUpdatedWithDetails,
					anAcknowledgmentReceiptPrepared);
		} else {
			theNotesHaveBeenImpounded = false;
			theRegisterUpdatedWithDetails = false;
			anAcknowledgmentReceiptPrepared = false;
		}

		// Validate Counterfeit
		String monthString = data.optString("month");
		if (monthString == null || monthString.isEmpty()) {
			LOG.debug("month is null or empty in Data of Request :: {}");
			throw new CounterfeitException("month is null or empty in Data of Request!");
		}
		YearMonth month = DateUtils.convertMonthStringIntoYearMonth(monthString);
		counterfeit = counterfeitRepository.findByBranchCodeAndMonth(branchCode, month);

		// Create Monthly Counterfeit
		if (counterfeit == null) {
			String isDetect = data.optString("isDetect");
			if (isDetect == null || isDetect.isEmpty()) {
				throw new CounterfeitException("isDetect is :" + isDetect);
			}

			counterfeit = new Counterfeit();
			counterfeit.setDailyMonthly("Monthly");
			counterfeit.setMonth(month);
			counterfeit.setBranchName(branchName);
			counterfeit.setBranchCode(branchCode);
			counterfeit.setRoId(roId);
			counterfeit.setRoRole(role);
			counterfeit.setRoRemarks(roRemarks);
			counterfeit.setIsDetect(isDetect);
			counterfeit.setStatus("Pending at Checker");
			counterfeit.setCreatedBy(roId);
			counterfeit.setCreatedDate(new Date());
			counterfeit.setTheRegisterUpdatedWithDetails(theRegisterUpdatedWithDetails);
			counterfeit.setTheNotesHaveBeenImpounded(theNotesHaveBeenImpounded);
			counterfeit.setAnAcknowledgmentReceiptPrepared(anAcknowledgmentReceiptPrepared);
			// Save
			counterfeit = counterfeitRepository.save(counterfeit);

		} else if (!counterfeit.getStatus().equals("Pending")) {
			throw new CounterfeitException("Counterfeit for " + branchName + " for " + month + " already exist!");

		} else {
			String isDetect = data.optString("isDetect");
			if (isDetect == null || isDetect.isEmpty()) {
				throw new CounterfeitException("isDetect is :" + isDetect);
			}
			counterfeit.setRoId(roId);
			counterfeit.setRoRole(role);
			counterfeit.setRoRemarks(roRemarks);
			counterfeit.setIsDetect(isDetect);
			counterfeit.setStatus("Pending at Checker");
			counterfeit.setCreatedBy(roId);
			counterfeit.setUpdateBy(roId);
			counterfeit.setUpdatedDate(new Date());
			counterfeit.setTheRegisterUpdatedWithDetails(theRegisterUpdatedWithDetails);
			counterfeit.setTheNotesHaveBeenImpounded(theNotesHaveBeenImpounded);
			counterfeit.setAnAcknowledgmentReceiptPrepared(anAcknowledgmentReceiptPrepared);
			// Save
			counterfeitRepository.save(counterfeit);
		}

		// Create Denominations
		if (isDenominations) {
			counterfeitDenominationMediator.createDenomination(denominationsJsonArray, roId, counterfeit.getId());
		}
		return counterfeit;
	}

	public void validateThreeChecks(Boolean theNotesHaveBeenImpounded, Boolean theRegisterUpdatedWithDetails,
			Boolean anAcknowledgmentReceiptPrepared) {
		if (theNotesHaveBeenImpounded == null || theRegisterUpdatedWithDetails == null
				|| anAcknowledgmentReceiptPrepared == null || !theNotesHaveBeenImpounded
				|| !theRegisterUpdatedWithDetails || !anAcknowledgmentReceiptPrepared) {
			LOG.debug(
					"Please check all validation out of theNotesHaveBeenImpounded, theRegisterUpdatedWithDetails & anAcknowledgmentReceiptPrepared.");
			throw new CounterfeitException(
					"Please check all validation out of theNotesHaveBeenImpounded, theRegisterUpdatedWithDetails & anAcknowledgmentReceiptPrepared.");
		}

	}

	@Override
	public JSONObject getCounterfeit(String encryptRequestString, String channel, String X_Session_ID,
			String X_encode_ID, HttpServletRequest request, boolean isEncrypt) {
		JSONObject response = new JSONObject();
		JSONObject Data = new JSONObject();
		JSONObject requestJson = new JSONObject();
		// 1 VALIDATE & DECRYPT REQUEST
		if (isEncrypt) {
			requestJson = validateHeadersAndSessionId(encryptRequestString, channel, X_Session_ID, X_encode_ID,
					request);
		} else {
			requestJson = new JSONObject(encryptRequestString);
		}
		LOG.debug("DecryptRequestString response :: {}", requestJson.toString());

		// 4. PROCESS
		JSONObject data = requestJson.optJSONObject("Data");
		if (data == null) {
			LOG.debug("Data is null:: {}", data);
			throw new CounterfeitException("Data is :" + data);
		}

		String dateType = data.optString("dateType");
		String role = data.optString("role");

		// Validate User Role
		UserRole userRoleEnum = counterfeitDenominationMediator.validateUserRole(role);

		if (dateType == null || dateType.isEmpty() || role == null || role.isEmpty()) {
			LOG.debug("dateType is null or empty :: {} :: {}", dateType, role);
			throw new CounterfeitException("dateType is :" + dateType + " and role is :" + role + "!");
		}

		Data.put("dateType", dateType);
		if (dateType.equals("DAILY")) {
			// Get Counterfeits from DB
			List<Counterfeit> counterfeits = counterfeitDAO.findCounterfeitsByFilter(data);

			// Convert Counterfeits into JsonArray
			JSONArray counterfeitJsonArray = new JSONArray();
			if (!counterfeits.isEmpty()) {
				for (Counterfeit counterfeit : counterfeits) {
					JSONObject counterfeitJson = counterfeitMapper.convertToJson(counterfeit);
					counterfeitJsonArray.put(counterfeitJson);
				}
			}
			// Make Response
			Data.put("counterfeits", counterfeitJsonArray);

		} else if (dateType.equals("MONTHLY")) {
			// Get Counterfeits from DB
			List<Counterfeit> monthlyReports = monthlyReportDAO.findCounterfeitsByFilter(data);
			JSONArray monthlyReportJsonArray = new JSONArray();
			if (!monthlyReports.isEmpty()) {
				for (Counterfeit monthlyReport : monthlyReports) {
					JSONObject monthlyReportJson = monthlyReportMapper.convertToJson(monthlyReport, role);
					monthlyReportJsonArray.put(monthlyReportJson);
				}
			}
			Data.put("counterfeits", monthlyReportJsonArray);
			response.put("Data", Data);
		}
		response.put("Data", Data);

		return response;

	}

	@Override
	public JSONObject getCounterfeitExcel(String encryptRequestString, String channel, String X_Session_ID,
			String X_encode_ID, HttpServletRequest request, boolean isEncrypt) {
		JSONObject response = new JSONObject();
		JSONObject Data = new JSONObject();

		JSONObject result = getCounterfeit(encryptRequestString, channel, X_Session_ID, X_encode_ID, request,
				isEncrypt);
		String excelBase64 = ExcelService.createExcel(result);
		Data.put("excelBase64", excelBase64);
		response.put("Data", Data);

		return response;
	}

	@Override
	public JSONObject updateCounterfeitStatusAndBoRemarks(String encryptRequestString, String channel,
			String X_Session_ID, String X_encode_ID, HttpServletRequest request, boolean isEncrypt) {
		JSONObject requestJson = new JSONObject();
		// 1 VALIDATE & DECRYPT REQUEST
		if (isEncrypt) {
			requestJson = validateHeadersAndSessionId(encryptRequestString, channel, X_Session_ID, X_encode_ID,
					request);
		} else {
			requestJson = new JSONObject(encryptRequestString);
		}

		LOG.debug("DecryptRequestString response :: {}", requestJson.toString());

		// 4. PROCESS
		// 4.1 Get Data
		JSONObject data = requestJson.optJSONObject("Data");

		if (data == null) {
			LOG.debug("Data is null:: {}", data);
			throw new CounterfeitException("Data is :" + data);
		}

		Long counterfeitId = data.optLong("counterfeitId");
		Long bmId = data.optLong("bmId");
		String role = data.optString("role");
		String status = data.optString("status");
		String bmRemarks = data.optString("bmRemarks");

		// Validate User Role
		UserRole userRoleEnum = counterfeitDenominationMediator.validateUserRole(role);

		if (counterfeitId == null || counterfeitId == 0L || bmId == null || bmId == 0L || status == null
				|| status.isEmpty() || role == null || role.isEmpty() || bmRemarks == null || bmRemarks.isEmpty()) {
			// Create a detailed message
			String errorMessage = String.format(
					"One or more required fields are null or empty in Data of Request: "
							+ "counterfeitId=%s, bmId=%s, status=%s, role=%s, bmRemarks=%s",
					counterfeitId, bmId, status, role, bmRemarks);

			// Log the detailed message
			LOG.debug(errorMessage + " :: HttpStatus=" + HttpStatus.BAD_REQUEST);

			// Throw an exception with the detailed message
			throw new CounterfeitException(errorMessage + " :: HttpStatus=" + HttpStatus.BAD_REQUEST);
		}

		Counterfeit counterfeit = findById(counterfeitId);

		if (!role.equals(UserRole.COUNTERFEIT_CHECKER.getRole())) {
			LOG.debug("You are " + role + " and you have not authority to update BM Status & Remarks!",
					HttpStatus.BAD_REQUEST);
			throw new CounterfeitException(
					"You are " + role + " and you have not authority to update BM Status & Remarks!");
		}

		if (!counterfeit.getStatus().equals("Pending at Checker")) {
			LOG.debug("You are " + role + " and you have not authority to update BM Status & Remarks in status "
					+ counterfeit.getStatus() + "!", HttpStatus.BAD_REQUEST);
			throw new CounterfeitException(
					"You are " + role + " and you have not authority to update BM Status & Remarks in status "
							+ counterfeit.getStatus() + "!");

		}
		counterfeit.setBmId(bmId);
		counterfeit.setBmRole(role);
		counterfeit.setStatus(status.equals("Return") ? "Pending at Maker" : "Submitted");
		counterfeit.setBmRemarks(bmRemarks);
		counterfeit.setUpdateBy(bmId);
		counterfeit.setUpdatedDate(new Date());
		counterfeit = counterfeitRepository.save(counterfeit);

		// Response
		JSONObject response = new JSONObject();
		JSONObject Data = new JSONObject();
		Data.put("message", "Activity " + counterfeitId + " " + status + " by checker successfully!");
		response.put("Data", Data);
		return response;
	}

	@Override
	public JSONObject getMonthlyCount(String encryptRequestString, String channel, String X_Session_ID,
			String X_encode_ID, HttpServletRequest request, boolean isEncrypt) {
		JSONObject requestJson = new JSONObject();
		// 1 VALIDATE & DECRYPT REQUEST
		if (isEncrypt) {
			requestJson = validateHeadersAndSessionId(encryptRequestString, channel, X_Session_ID, X_encode_ID,
					request);
		} else {
			requestJson = new JSONObject(encryptRequestString);
		}

		LOG.debug("DecryptRequestString response :: {}", requestJson.toString());

		// 4. PROCESS
		// 4.1 Get Data
		JSONObject data = requestJson.optJSONObject("Data");

		if (data == null) {
			LOG.debug("Data is null:: {}", data);
			throw new CounterfeitException("Data is :" + data);
		}
		String branchCode = data.optString("branchCode");
		String monthString = data.optString("month");

		if (branchCode == null || branchCode.isEmpty() || monthString == null || monthString.isEmpty()) {
			LOG.debug("BranchCode or month is null or empty :: {}", data);
			throw new CounterfeitException("branchCode is :" + branchCode + " and month is :" + monthString + "!");
		}

		YearMonth month = DateUtils.convertMonthStringIntoYearMonth(monthString);
		Long count = counterfeitRepository.findCountByBranchCodeAndMonthAndStatusSubmitted(branchCode,
				DateUtils.getStartDateOfMonth(month), DateUtils.getEndDateOfMonth(month));

		// Response
		JSONObject response = new JSONObject();
		JSONObject Data = new JSONObject();

		Data.put("count", count);
		response.put("Data", Data);
		return response;
	}

	@Override
	public JSONObject deleteByDailyMonthly(String encryptRequestString, String channel, String X_Session_ID,
			String X_encode_ID, HttpServletRequest request, boolean isEncrypt) {
		JSONObject requestJson = new JSONObject();
		// 1 VALIDATE & DECRYPT REQUESTRequ
		if (isEncrypt) {
			requestJson = validateHeadersAndSessionId(encryptRequestString, channel, X_Session_ID, X_encode_ID,
					request);
		} else {
			requestJson = new JSONObject(encryptRequestString);
		}

		LOG.debug("DecryptRequestString response :: {}", requestJson.toString());

		// 4. PROCESS
		// 4.1 Get Data
		JSONObject data = requestJson.optJSONObject("Data");

		if (data == null) {
			LOG.debug("Data is null:: {}", data);
			throw new CounterfeitException("Data is :" + data);
		}

		String dailyMonthly = data.optString("dailyMonthly");
		if (dailyMonthly == null || dailyMonthly.isEmpty()
				|| !(dailyMonthly.equals("Daily") || dailyMonthly.equals("Monthly"))) {
			throw new CounterfeitException("dailyMonthly is " + dailyMonthly + " in Data of request!");
		}
		int result = counterfeitRepository.deleteByDailyMonthly(dailyMonthly);
		// Response
		JSONObject response = new JSONObject();
		JSONObject Data = new JSONObject();
		Data.put("message", dailyMonthly + " " + result + " reports deleted successfully!");
		response.put("Data", Data);
		return response;

	}

	public Counterfeit save(Counterfeit counterfeit) {
		return counterfeitRepository.save(counterfeit);
	}

	@Override
	public Counterfeit findById(long id) {
		Counterfeit counterfeit = counterfeitRepository.findById(id);
		if (counterfeit == null) {
			throw new CounterfeitException("Counterfeit not found by id!");
		}
		return counterfeit;
	}

	public JSONObject validateHeadersAndSessionId(String encryptRequestString, String channel, String X_Session_ID,
			String X_encode_ID, HttpServletRequest request) {
		// Prepare detailed message with field values
		String message = String.format(
				"One or more required fields are null or empty validateHeadersAndSessionId. Values - "
						+ "encryptRequestString: '%s', channel: '%s', X_Session_ID: '%s', X_encode_ID: '%s'",
				encryptRequestString, channel, X_Session_ID, X_encode_ID);
		LOG.debug("Validate Headers :" + message);

		// 1 Validate fields
		if (encryptRequestString == null || encryptRequestString.isEmpty() || channel == null || channel.isEmpty()
				|| X_Session_ID == null || X_Session_ID.isEmpty() || X_encode_ID == null || X_encode_ID.isEmpty()) {
			LOG.debug("Validate Headers :" + message);
			throw new CounterfeitException("Validate Headers :" + message);
		}

		// 2 Validate Session Id
		String dbSessionId = counterfeitDenominationMediator.fetchSessionId(X_Session_ID);
		if (dbSessionId == null || dbSessionId.isEmpty()) {
			LOG.debug("SessionId is expired or Invalid sessionId :: {}", HttpStatus.UNAUTHORIZED);
			throw new CounterfeitException("SessionId is expired or Invalid sessionId!");
		}
		// 3 DecryptRequest
		return EncryptDecryptHelper.decryptRequestString(encryptRequestString, channel, X_Session_ID, X_encode_ID);

	}

	@Override
	public boolean existbyById(Long counterfeitId) {
		return counterfeitRepository.existsById(counterfeitId);
	}

	@Override
	public Counterfeit getCounterfeitByMonthAndBranchCode(YearMonth month, String branchCode) {
		return counterfeitRepository.findByMonthAndBranchCode(month, branchCode);
	}

	@Override
	public boolean existsByBranchCodeAndMonthAndStatusSubmitted(String branchCode, YearMonth yearMonth) {
		return counterfeitRepository.existsByBranchCodeAndMonthAndStatusSubmitted(branchCode, yearMonth);
	}

	@Override
	public void addMonthlyCounterfeitsTesting(String month) {
		List<BranchMasterDTO> allBranches = counterfeitRepository.findAllBranches();
		YearMonth yearMonth = DateUtils.convertMonthStringIntoYearMonth(month);
		List<Counterfeit> counterfeits = counterfeitRepository.findAllMonthlyCountrfeitsByMonth(yearMonth);

		// Create Counterfeits
		List<Counterfeit> newCounterfeits = new ArrayList<>();
		for (BranchMasterDTO branch : allBranches) {
			boolean isContain = false;
			for (Counterfeit counterfeit : counterfeits) {
				if (branch.getBranchId().equals(counterfeit.getBranchCode())) {
					isContain = true;
				}
			}
			if (!isContain) {
				Counterfeit counterfeit = new Counterfeit();
				Long applicationId = Constants.getApplicationId(branch.getBranchId());

				counterfeit.setId(applicationId);
				counterfeit.setDailyMonthly("Monthly");
				counterfeit.setMonth(yearMonth);
				counterfeit.setBranchName(branch.getBranchName());
				counterfeit.setBranchCode(branch.getBranchId());
				counterfeit.setStatus("Pending");
				counterfeit.setIsDetect("No");
				counterfeit.setCreatedDate(new Date());
				newCounterfeits.add(counterfeit);
			}
		}
		// Save New Counterfeits
		counterfeitRepository.saveAll(newCounterfeits);
	}

	@Override
	public void delete(String request) {
		JSONObject requestJson = new JSONObject(request);
		// 2 VALIDATE DATA
		JSONObject data = requestJson.optJSONObject("Data");
		if (data == null) {
			LOG.debug("Data is null :: {}", data);
			throw new CounterfeitException("Data is null : " + data);
		}
		String type = data.optString("type");
		if (type.equals("All")) {
			counterfeitRepository.deleteAll();
			counterfeitDenominationMediator.denominationdeleteAll();
		} else {
			String branchCode = data.optString("branchCode");
			List<Counterfeit> counterfeits = counterfeitRepository.findByBranchCode(branchCode);
			if (counterfeits.isEmpty()) {
				throw new CounterfeitException("No counterfeit found for branchCode: " + branchCode);
			}
			for (Counterfeit counterfeit : counterfeits) {
				counterfeitDenominationMediator.deleteDenominationsByCounterfeitId(counterfeit.getId());
			}
			counterfeitRepository.deleteByBranchCode(branchCode);
		}

	}

}
