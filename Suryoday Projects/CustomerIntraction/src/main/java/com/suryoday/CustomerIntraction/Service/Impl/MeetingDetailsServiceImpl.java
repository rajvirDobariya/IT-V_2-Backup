package com.suryoday.CustomerIntraction.Service.Impl;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.suryoday.CustomerIntraction.Exception.Error;
import com.suryoday.CustomerIntraction.Mapper.CustomerIntractionDetailsMapper;
import com.suryoday.CustomerIntraction.Pojo.CustomerIntractionDetails;
import com.suryoday.CustomerIntraction.Pojo.MeetingImage;
import com.suryoday.CustomerIntraction.Repository.CustomerIntractionImageRepository;
import com.suryoday.CustomerIntraction.Repository.CustomerIntractionMemberRepository;
import com.suryoday.CustomerIntraction.Repository.CustomerIntractionRepository;
import com.suryoday.CustomerIntraction.Repository.MOMSummaryRepository;
import com.suryoday.CustomerIntraction.Service.MeetingDetailsService;
import com.suryoday.CustomerIntraction.Util.DateUtils;
import com.suryoday.CustomerIntraction.Util.EncryptDecryptHelper;
import com.suryoday.CustomerIntraction.Util.PDFGenerator;

@Service
public class MeetingDetailsServiceImpl implements MeetingDetailsService {

	private static Logger logger = LoggerFactory.getLogger(MeetingDetailsServiceImpl.class);

	@Autowired
	private CustomerIntractionRepository customerIntracionDetailRepo;

	@Autowired
	private CustomerIntractionDetailsMapper detailsMapper;

	@Autowired
	private CustomerIntractionMemberRepository memberRepo;

	@Autowired
	private MOMSummaryRepository momRepo;

	@Autowired
	private CustomerIntractionImageRepository imageRepo;

	@Override
	public JSONObject getMeetingDetails(String requestString, String channel, String x_Session_ID, String x_encode_ID,
			HttpServletRequest request) throws Exception {

		logger.debug(
				"MeetingDetailsServiceImpl :: Get Meeting Data :: Fields :: encryptRequestString : {} : channel : {}: X_Session_ID : {}: "
						+ "X_encode_ID : {}",
				requestString, channel, x_Session_ID, x_encode_ID);

		JSONObject requestJson = new JSONObject(requestString);

		logger.debug("EncryptedRequestString response :: {}", requestJson.toString());

		JSONObject responseJson = new JSONObject();
		JSONObject data = requestJson.optJSONObject("Data");
		if (data == null) {
			logger.debug("Data is null :: {}", data);
			return Error.createError("Data is null ", HttpStatus.BAD_REQUEST);
		}

		// 2 Check DB session ID
		String dbSessionId = fetchSessionId(x_Session_ID);

		if (dbSessionId == null || dbSessionId.isEmpty()) {
			logger.debug("SessionId is expired or Invalid sessionId :: {}", HttpStatus.UNAUTHORIZED);
			return Error.createError("SessionId is expired or Invalid sessionId ", HttpStatus.UNAUTHORIZED);
		}

		// 3 DecryptRequestString
		JSONObject decryptJson = new JSONObject();
		if (requestString.contains("value")) {
			JSONObject requestJsonDecrypt = EncryptDecryptHelper.decryptRequestString(requestString, channel,
					x_Session_ID, x_encode_ID);
			decryptJson = requestJsonDecrypt.optJSONObject("Data");
		} else {
			JSONObject json = new JSONObject(requestString);
			decryptJson = json.getJSONObject("Data");
		}

		String role = decryptJson.optString("role");
		String meetingNumber = decryptJson.optString("meetingNumber");
		String StartDateString = decryptJson.optString("startDate");
		String endDateString = decryptJson.optString("endDate");
		LocalDate startDate = null, endDate = null;
		List<Object[]> member = null;
		if (!StartDateString.isEmpty()) {
			startDate = DateUtils.convertDateStringIntoDate(StartDateString);
		}
		if (!endDateString.isEmpty()) {
			endDate = DateUtils.convertDateStringIntoDate(endDateString);
		}

		if (role.equalsIgnoreCase("RO")) {
			member = customerIntracionDetailRepo.getByROMeetingNumber(startDate, endDate, meetingNumber);
		} else if (role.equalsIgnoreCase("BM")) {
			member = customerIntracionDetailRepo.getByBMMeetingNumber(startDate, endDate, meetingNumber);
		} else if (role.equalsIgnoreCase("CREDIT_OPS")) {
			member = customerIntracionDetailRepo.getByCrMeetingNumber(startDate, endDate, meetingNumber);
		}

		logger.debug("MeetingDetailsServiceImpl :: get pending meeting data:: member : {}", member);

		JSONArray meetingdataJsonArray = new JSONArray();
		for (Object[] meetingData : member) {
			JSONObject dataJson = detailsMapper.convertToJson(meetingData, role);
			meetingdataJsonArray.put(dataJson);
			responseJson.put("Data", meetingdataJsonArray);
		}

		// DecrypeRequestString
		if (requestString.contains("value")) {
			responseJson = EncryptDecryptHelper.encryptResponseString(responseJson, channel, x_Session_ID, x_encode_ID);
		}

		return responseJson;

	}

	public String fetchSessionId(String sessionId) {
		return customerIntracionDetailRepo.getAllSessionIds(sessionId);
	}

	@Override
	public JSONObject downloadPDF(String requestString, String channel, String x_Session_ID, String x_encode_ID,
			HttpServletRequest request) throws Exception {

		logger.debug(
				"MeetingDetailsServiceImpl :: download PDF :: Fields :: requestString : {} : channel : {}: X_Session_ID : {}: "
						+ "X_encode_ID : {}",
				requestString, channel, x_Session_ID, x_encode_ID);

		JSONObject requestJson = new JSONObject(requestString);

		logger.debug("EncryptedRequestString response :: {}", requestJson.toString());

		JSONObject data = requestJson.optJSONObject("Data");
		if (data == null) {
			logger.debug("Data is null :: {}", data);
			return Error.createError("Data is null ", HttpStatus.BAD_REQUEST);
		}

		// 2 Check DB session ID
		String dbSessionId = fetchSessionId(x_Session_ID);

		if (dbSessionId == null || dbSessionId.isEmpty()) {
			logger.debug("SessionId is expired or Invalid sessionId :: {}", HttpStatus.UNAUTHORIZED);
			return Error.createError("SessionId is expired or Invalid sessionId ", HttpStatus.UNAUTHORIZED);
		}

		// 3 DecryptRequestString
		JSONObject decryptJson = new JSONObject();
		if (requestString.contains("value")) {
			JSONObject requestJsonDecrypt = EncryptDecryptHelper.decryptRequestString(requestString, channel,
					x_Session_ID, x_encode_ID);
			decryptJson = requestJsonDecrypt.optJSONObject("Data");
		} else {
			JSONObject json = new JSONObject(requestString);
			decryptJson = json.getJSONObject("Data");
		}

		JSONObject result = getMeetingData(decryptJson, channel, x_Session_ID, x_encode_ID, request);

		if (result.has("Error")) {
			logger.debug("Data is null :: {}", result);
			return Error.createError("Data Not Found", HttpStatus.BAD_REQUEST);
		}

		String pdfBase64 = PDFGenerator.generatePDF(result);
		JSONObject response = new JSONObject();
		JSONObject responseData = new JSONObject();
		responseData.put("pdfBase64", pdfBase64);
		response.put("Data", responseData);

		// DecrypeRequestString
		if (requestString.contains("value")) {
			response = EncryptDecryptHelper.encryptResponseString(response, channel, x_Session_ID, x_encode_ID);
		}

		return response;
	}

	@Override
	public JSONObject getMeetingData(JSONObject decryptJson, String channel, String X_Session_ID, String X_encode_ID,
			HttpServletRequest request) throws Exception {
		JSONObject responseObj = new JSONObject();

		logger.debug("MEETING_DETAILS_SERVICE_IMPL :: request response :: {}", decryptJson.toString());

		JSONObject response = new JSONObject();
		
		if (decryptJson.isEmpty()) {
			logger.debug("Data is null :: {}", decryptJson);
			return Error.createError("Data is null ", HttpStatus.BAD_REQUEST);
		}

		// Get Meeting Details from DB
		CustomerIntractionDetails meetingDetails = customerIntracionDetailRepo
				.findByMeetingNumber(decryptJson.optString("meetingNumber"));
		if (meetingDetails == null) {
			logger.debug("Download PDF : Data Not Found :: {}", meetingDetails);
			return Error.createError("Data Not Found ", HttpStatus.BAD_REQUEST);
		}

		responseObj.put("customerIntraction", detailsMapper.convertDataToJson(meetingDetails));

		// Get Member List from DB
		responseObj.put("memberList", memberRepo.findByMeetingNumber(decryptJson.optString("meetingNumber")));

		// Get MOM Summary from DB
		responseObj.put("momSummary", momRepo.findByMeetingNumber(decryptJson.optString("meetingNumber")));

		// get Images
		List<MeetingImage> image = imageRepo.findByMeetingNumber(decryptJson.optString("meetingNumber"));

		responseObj.put("image", detailsMapper.convertImageDataToJson(image));

		response.put("Data", responseObj);

		return response;
	}

}
