package com.suryoday.CustomerIntraction.Service.Impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.suryoday.CustomerIntraction.Exception.Error;
import com.suryoday.CustomerIntraction.Mapper.CustomerIntractionDetailsMapper;
import com.suryoday.CustomerIntraction.Mapper.MOMDetailsAuditMapper;
import com.suryoday.CustomerIntraction.Mapper.MOMDetailsMapper;
import com.suryoday.CustomerIntraction.Pojo.CustomerIntractionDetails;
import com.suryoday.CustomerIntraction.Pojo.CustomerIntractionMember;
import com.suryoday.CustomerIntraction.Pojo.MOMSummaryDetails;
import com.suryoday.CustomerIntraction.Pojo.MOMSummaryDetailsAudit;
import com.suryoday.CustomerIntraction.Pojo.MeetingImage;
import com.suryoday.CustomerIntraction.Repository.CustomerIntractionImageRepository;
import com.suryoday.CustomerIntraction.Repository.CustomerIntractionMemberRepository;
import com.suryoday.CustomerIntraction.Repository.CustomerIntractionRepository;
import com.suryoday.CustomerIntraction.Repository.MOMSummaryAuditRepository;
import com.suryoday.CustomerIntraction.Repository.MOMSummaryRepository;
import com.suryoday.CustomerIntraction.Service.CustomerIntractionMemberService;
import com.suryoday.CustomerIntraction.Service.CustomerIntractionService;
import com.suryoday.CustomerIntraction.Util.DateUtils;
import com.suryoday.CustomerIntraction.Util.EncryptDecryptHelper;
import com.suryoday.CustomerIntraction.Util.GenerateProperty;

@Service
public class CustomerIntractionServiceImpl implements CustomerIntractionService {

	private static Logger logger = LoggerFactory.getLogger(CustomerIntractionServiceImpl.class);

	@Autowired
	private CustomerIntractionDetailsMapper deatilsMapper;

	@Autowired
	private CustomerIntractionRepository customerIntractionRepository;

	@Autowired
	private CustomerIntractionMemberService memberService;

	@Autowired
	private MOMSummaryRepository momRepo;

	@Autowired
	private MOMSummaryAuditRepository momAuditRepo;

	@Autowired
	private ImageServiceImpl imageService;

	@Autowired
	private MOMDetailsMapper momDetailsMapper;

	@Autowired
	private MOMDetailsAuditMapper auditMapper;

	@Autowired
	private CustomerIntractionMemberRepository memberRepo;

	@Autowired
	private CustomerIntractionImageRepository imageRepo;

	@Override
	public JSONObject fetchCustomerName(String encryptRequestString, String channel, String x_Session_ID,
			String x_encode_ID, HttpServletRequest request) throws Exception {

		logger.debug(
				"CUSTOMERINTRACTION_SERVICEIMPL :: fetchName :: Fields :: encryptRequestString : {} : channel : {}: X_Session_ID : {}: "
						+ "X_encode_ID : {}",
				encryptRequestString, channel, x_Session_ID, x_encode_ID);

		JSONObject requestJson = new JSONObject(encryptRequestString);

		logger.debug("EncryptedRequestString response :: {}", requestJson.toString());

		JSONObject sendAuthenticateResponse = new JSONObject();
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
		if (encryptRequestString.contains("value")) {
			JSONObject requestJsonDecrypt = EncryptDecryptHelper.decryptRequestString(encryptRequestString, channel,
					x_Session_ID, x_encode_ID);
			decryptJson = requestJsonDecrypt.optJSONObject("Data");
		} else {
			JSONObject json = new JSONObject(encryptRequestString);
			decryptJson = json.getJSONObject("Data");
		}
		String role = decryptJson.optString("memberRole");

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

			obj = new URL(x.BASEURL + "customers/getData/v2?api_key=" + x.api_key);

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("X-Request-ID", "IBR");
			con.setRequestProperty("X-Transaction-ID", "ABC");
			con.setRequestProperty("X-User-ID", "30639");
			con.setRequestProperty("X-From-ID", "TAB");
			con.setRequestProperty("Content-Type", "application/json");

			JSONObject getCustDataRequest = new JSONObject();
			JSONObject dataJsonObject = new JSONObject();
			dataJsonObject.putOpt("CustomerNo", decryptJson.opt("CIFNo"));
			dataJsonObject.putOpt("memberRole", decryptJson.opt("memberRole"));
			getCustDataRequest.put("Data", dataJsonObject);

			logger.debug("role : " + role);

			if (role.equalsIgnoreCase("Staff")) {
				String employeeName = customerIntractionRepository.getEmployeeData(decryptJson.optString("CIFNo"));

				logger.debug("employeeName : " + employeeName);

				if (employeeName == null) {
					logger.debug("employeeName : " + employeeName);
					sendAuthenticateResponse = sendAuthenticateResponse.put("Data", "Employee does not Exist");
				} else {
					logger.debug("employeeName : " + employeeName);
					JSONObject response = new JSONObject();
					response.put("employeeName", employeeName);

					sendAuthenticateResponse = sendAuthenticateResponse.put("Data", response);
					logger.debug("Employee Data Response : " + sendAuthenticateResponse);

				}
				// return sendAuthenticateResponse;
			} else {
				sendAuthenticateResponse = GetResponseData(decryptJson, sendAuthenticateResponse, con, "POST");
				System.out.println(sendAuthenticateResponse);

				sendAuthenticateResponse = getCustDetails(decryptJson, sendAuthenticateResponse);
				logger.debug("Customer Data Response : " + sendAuthenticateResponse);
			}

			if (encryptRequestString.contains("value")) {
				sendAuthenticateResponse = EncryptDecryptHelper.encryptResponseString(sendAuthenticateResponse, channel,
						x_Session_ID, x_encode_ID);
			}

		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sendAuthenticateResponse;
	}

	public String fetchSessionId(String sessionId) {
		return customerIntractionRepository.getAllSessionIds(sessionId);
	}

	private static JSONObject GetResponseData(JSONObject parent, JSONObject sendAuthenticateResponse,
			HttpURLConnection con, String MethodType) throws IOException {

		JSONObject getCustDataRequest = new JSONObject();
		JSONObject dataJsonObject = new JSONObject();
		dataJsonObject.putOpt("CustomerNo", parent.opt("CIFNo"));
		getCustDataRequest.put("Data", dataJsonObject);

		con.setDoOutput(true);
		OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
		os.write(getCustDataRequest.toString());
		os.flush();
		os.close();

		int responseCode = con.getResponseCode();
		logger.debug("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			JSONObject sendauthenticateResponse1 = new JSONObject();
			sendauthenticateResponse1.put("Data", response.toString());
			
			if (response.toString().contains("TIBCO-BW-CORE-500638")) {
				//sendauthenticateResponse1.put("Error", HttpStatus.BAD_REQUEST);
				return Error.createError("Does Not Exist", HttpStatus.BAD_GATEWAY);
			}

			logger.debug("Customer Data : " + sendauthenticateResponse1);

			sendAuthenticateResponse = sendauthenticateResponse1;

		} else {
			logger.debug("POST request not worked");

			JSONObject sendauthenticateResponse1 = new JSONObject();

			JSONObject errr = new JSONObject();
			errr.put("Description", "Server Error " + responseCode);

			JSONObject j = new JSONObject();
			j.put("Error", errr);

			sendauthenticateResponse1.put("Data", "" + j);
			sendAuthenticateResponse = sendauthenticateResponse1;
		}

		return sendAuthenticateResponse;

	}

	public JSONObject getCustDetails(JSONObject custDetails, JSONObject sendauthenticateResponse) {
		JSONObject responseData = new JSONObject();
		JSONObject sendCustDetails = new JSONObject();
		HttpStatus h = HttpStatus.BAD_GATEWAY;
		String Data = sendauthenticateResponse.getString("Data");
		JSONObject Data1 = new JSONObject(Data);

		if (Data1.has("Data")) {
			h = HttpStatus.OK;
			JSONArray jsonArray = Data1.getJSONObject("Data").getJSONArray("CRMCustDataResponse");
			JSONObject object = jsonArray.getJSONObject(0);
			String memberType = "";

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject customer = jsonArray.getJSONObject(i);
				if (!customer.getString("EMPLOYEECODE").contains("")) {
					memberType = "Staff";
				} else {
					memberType = "Customer";
				}

				String cifNumber = customer.getString("CIFNUMBER");
				String firstName = customer.getString("FIRSTNAME");
				String middleName = customer.getString("MIDDLENAME");
				String lastName = customer.getString("LASTNAME");
				String dateOfBirth = customer.getString("DOB");

				StringBuilder fullNameBuilder = new StringBuilder();
				fullNameBuilder.append(firstName).append(" ").append(middleName).append(" ").append(lastName);
				String fullName = fullNameBuilder.toString();

				// Parse the date of birth
				LocalDate dob = LocalDate.parse(dateOfBirth);

				// Get the current date
				LocalDate currentDate = LocalDate.now();

				// Calculate the age
				Period age = Period.between(dob, currentDate);
				int years = age.getYears();

				System.out.println("Age: " + years);

				responseData.put("Name", fullName);
				responseData.put("memberType", memberType);
				responseData.put("age", years);
				sendCustDetails.put("Data", responseData);

			}

		} else if (Data1.has("Error")) {
			h = HttpStatus.BAD_REQUEST;
		}

		return sendCustDetails;
	}

	@Transactional
	@Override
	public JSONObject saveMeetingDetails(String encryptRequestString, String channel, String x_Session_ID,
			String x_encode_ID, HttpServletRequest request) throws Exception {

		logger.debug(
				"CUSTOMERINTRACTION_SERVICEIMPL :: saveMeeting :: Fields :: encryptRequestString : {} : channel : {}: X_Session_ID : {}: "
						+ "X_encode_ID : {}",
				encryptRequestString, channel, x_Session_ID, x_encode_ID);

		JSONObject requestJson = new JSONObject(encryptRequestString);
		JSONObject responseJson = new JSONObject();

		logger.debug("DecryptRequestString response :: {}", requestJson.toString());

		JSONObject data = requestJson.optJSONObject("Data");
		if (data == null) {
			logger.debug("Data is null :: {}", data);
			return Error.createError("Data is null ", HttpStatus.BAD_GATEWAY);
		}

		// 2 Check DB session ID
		String dbSessionId = fetchSessionId(x_Session_ID);
		if (dbSessionId == null || dbSessionId.isEmpty()) {
			logger.debug("SessionId is expired or Invalid sessionId :: {}", HttpStatus.UNAUTHORIZED);
			return Error.createError("SessionId is expired or Invalid sessionId ", HttpStatus.UNAUTHORIZED);
		}

		// 3 DecryptRequestString
		JSONObject decryptJson = new JSONObject();
		if (encryptRequestString.contains("value")) {
			JSONObject requestJsonDecrypt = EncryptDecryptHelper.decryptRequestString(encryptRequestString, channel,
					x_Session_ID, x_encode_ID);
			decryptJson = requestJsonDecrypt.optJSONObject("Data");
		} else {
			JSONObject json = new JSONObject(encryptRequestString);
			decryptJson = json.getJSONObject("Data");
		}

		String meetingNumber = decryptJson.optString("meetingNumber");
		String condition = "Add";
		String role = decryptJson.optString("role");
		if (!meetingNumber.equals("")) {
			condition = "Edit";
		} else {
			String monthName = "", year = "";
			try {
				monthName = DateUtils.getMonthName(decryptJson.optString("meetingDate"));
				year = DateUtils.getYear(decryptJson.optString("meetingDate"));
				logger.debug("CUSTOMERINTRACTION_SERVICEIMPL :: saveMeeting :: Fields :: monthName : {} : year : {}",
						monthName, year);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			meetingNumber = "BLCSCM" + "/" + decryptJson.optString("branchCode") + "/" + monthName + "/" + year;
			decryptJson.put("meetingNumber", meetingNumber);
			logger.debug("CUSTOMERINTRACTION_SERVICEIMPL :: saveMeeting :: Fields :: meetingNumber : {}",
					meetingNumber);

			CustomerIntractionDetails customerIntractionMeetingData = customerIntractionRepository
					.findByMeetingNumber(meetingNumber);

			logger.debug("Meeting Number data : " + customerIntractionMeetingData);

			if (customerIntractionMeetingData == null) {

				List<CustomerIntractionMember> member  = new ArrayList<>();
				
				CustomerIntractionDetails customerIntractionDetails = new CustomerIntractionDetails();

				customerIntractionDetails = deatilsMapper.convertToEntity(decryptJson, customerIntractionDetails);

				logger.debug("DB Meeting Number : " + customerIntractionMeetingData);

				List<MOMSummaryDetails> momDetails = new ArrayList<>();
				momDetails = momDetailsMapper.convertToEntity(decryptJson, meetingNumber, momDetails);

				List<MOMSummaryDetailsAudit> momAuditDetails = new ArrayList<>();
				momAuditDetails = auditMapper.convertToEntity(decryptJson, meetingNumber, momAuditDetails);
				// add members in db
				responseJson = memberService.createMembers(decryptJson, meetingNumber, member);
				logger.debug("Create Members : " + responseJson);

				// add meeting deatils in detail table
				customerIntractionDetails = customerIntractionRepository.save(customerIntractionDetails);

				// add summary of MOM
				momRepo.saveAll(momDetails);

				// add summary of MOM in Audit table
				momAuditRepo.saveAll(momAuditDetails);

				// add image
				List<MeetingImage> image = new ArrayList<>();
				responseJson = saveImage(decryptJson, meetingNumber, image);

				responseJson.put("Data", "Meeting Details Successfully save");
			} else {
				responseJson.put("Data", "Meeting Number Already Exists");
			}
		}
		if (role.equalsIgnoreCase("BM")) {
			meetingNumber = decryptJson.optString("meetingNumber");
			String bmStatus = decryptJson.optString("status");
			
			CustomerIntractionDetails customerIntractionMeetingData = customerIntractionRepository
					.findByMeetingNumber(meetingNumber);
			if(bmStatus.equalsIgnoreCase("verify")){
				customerIntractionMeetingData.setStatus("Submitted");
			}else if(bmStatus.equalsIgnoreCase("return")){
				customerIntractionMeetingData.setStatus("Pending At Maker");
			}
			customerIntractionMeetingData.setUpdatedBy(decryptJson.optLong("updatedBy"));
			//customerIntractionMeetingData.setUpdatedByChecker(decryptJson.optLong("updatedBy"));
			customerIntractionMeetingData.setBmRemark(decryptJson.optString("checkerRemark"));
			//customerIntractionMeetingData.setUpdatedBy(decryptJson.optLong("updatedBy"));
			LocalDate currentDate = LocalDate.now();
			//customerIntractionMeetingData.setUpdatedDateChecker(currentDate);
			customerIntractionMeetingData.setUpdatedDate(currentDate);
			customerIntractionMeetingData = customerIntractionRepository.save(customerIntractionMeetingData);
			responseJson.put("Data", "status updated");
		}

		if (condition.equalsIgnoreCase("Edit") && role.equalsIgnoreCase("RO") || role.equalsIgnoreCase("CREDIT_OPS")  ) {
			CustomerIntractionDetails customerIntractionData = customerIntractionRepository
					.findByMeetingNumber(meetingNumber);
			List<MOMSummaryDetails> momDetails = momRepo.findByMeetingNumber(meetingNumber);
			List<CustomerIntractionMember> member = memberRepo.findByMeetingNumber(meetingNumber);
			List<MOMSummaryDetailsAudit> momAuditDetails = momAuditRepo.findByMeetingNumber(meetingNumber);
			List<MeetingImage> image = imageRepo.findByMeetingNumber(meetingNumber);
			
			if(decryptJson.optString("role").equalsIgnoreCase("CREDIT_OPS")) {
				momDetails = momDetailsMapper.convertToEntity(decryptJson, meetingNumber, momDetails);
				customerIntractionData.setStatus("Submitted");
				customerIntractionData.setCreditOpsRemark(decryptJson.optString("hoRemark")); 
				customerIntractionRepository.save(customerIntractionData);

				
			}else {
				customerIntractionData = deatilsMapper.convertToEntity(decryptJson, customerIntractionData);
				
				momDetails = momDetailsMapper.convertToEntity(decryptJson, meetingNumber, momDetails);

				momAuditDetails = auditMapper.convertToEntity(decryptJson, meetingNumber, momAuditDetails);
				
				responseJson = saveImage(decryptJson, meetingNumber, image);
				
				memberService.createMembers(decryptJson, meetingNumber, member);
				
				customerIntractionData = customerIntractionRepository.save(customerIntractionData);

				// add summary of MOM
				momRepo.saveAll(momDetails);
				
				// add summary of MOM in Audit table
				momAuditRepo.saveAll(momAuditDetails);

			}
			
			responseJson.put("Data", "Edited Successfully");
		}

		if (encryptRequestString.contains("value")) {
			responseJson = EncryptDecryptHelper.encryptResponseString(responseJson, channel, x_Session_ID, x_encode_ID);
		}
		return responseJson;
	}

	public JSONObject saveImage(JSONObject requestJsonDecrypt, String meetingNumber, List<MeetingImage> image) {

		JSONObject response = new JSONObject();
		
		JSONArray imageArray = requestJsonDecrypt.getJSONArray("image");

		if (imageArray != null || imageArray.length() < 0) {
			imageService.saveImageWeb(imageArray, meetingNumber, image);
		}

		return response;
	}

	@Override
	public JSONObject getById(String requestString, @NotBlank String channel, @NotBlank String x_Session_ID,
			@NotBlank String x_encode_ID, HttpServletRequest request) throws Exception {
		JSONObject response = new JSONObject();
		JSONObject responseJson = new JSONObject();

		logger.debug(
				"CUSTOMERINTRACTION_SERVICEIMPL :: GET BY MEETING NUMBER :: Fields :: encryptRequestString : {} : channel : {}: X_Session_ID : {}: "
						+ "X_encode_ID : {}",
				requestString, channel, x_Session_ID, x_encode_ID);
		// 2 Check DB session ID
		String dbSessionId = fetchSessionId(x_Session_ID);

		if (dbSessionId == null || dbSessionId.isEmpty()) {
			logger.debug("SessionId is expired or Invalid sessionId :: {}", HttpStatus.UNAUTHORIZED);
			return Error.createError("SessionId is expired or Invalid sessionId ", HttpStatus.UNAUTHORIZED);
		}

		logger.debug("CUSTOMERINTRACTION_SERVICEIMPL :: GET BY MEETING NUMBER :: Fields :: encryptRequestString : {}",
				requestString);
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

		logger.debug("CUSTOMERINTRACTION_SERVICEIMPL :: GET BY MEETING NUMBER :: Fields :: decreptedData : {}",
				decryptJson);
		String meetingNumber = decryptJson.optString("meetingNumber");

		logger.debug("CUSTOMERINTRACTION_SERVICEIMPL :: GET BY MEETING NUMBER :: Fields :: MeetingNumber : {}",
				meetingNumber);
		// get meeting deatils
		CustomerIntractionDetails customerIntractionMeetingData = customerIntractionRepository
				.findByMeetingNumber(meetingNumber);

		// get members deatails
		List<CustomerIntractionMember> memberData = memberRepo.findByMeetingNumber(meetingNumber);

		// get MOM Summary
		List<MOMSummaryDetails> MOMData = momRepo.findByMeetingNumber(meetingNumber);

		// get Images
		List<MeetingImage> image = imageRepo.findByMeetingNumber(meetingNumber);

		logger.debug(
				"CUSTOMERINTRACTION_SERVICEIMPL :: GET BY MEETING NUMBER :: Fields :: customerIntractionMeetingData : {} : MOMData : {}: memberData : {}: "
						+ "image : {}",
				customerIntractionMeetingData, MOMData, memberData, image);

		if (customerIntractionMeetingData != null) {

			response.put("customerIntraction", deatilsMapper.convertDataToJson(customerIntractionMeetingData));
		}
		if (memberData != null && !memberData.isEmpty()) {

			response.put("member", deatilsMapper.convertMemberDataToJson(memberData));
		}
		if (MOMData != null && !MOMData.isEmpty()) {

			response.put("MOMSummary", deatilsMapper.convertMOMDataToJson(MOMData));
		}
		if (image != null && !image.isEmpty()) {

			response.put("image", deatilsMapper.convertImageDataToJson(image));
		}

		responseJson.put("Data", response);

		// encrypt data
		if (requestString.contains("value")) {
			responseJson = EncryptDecryptHelper.encryptResponseString(response, channel, x_Session_ID, x_encode_ID);
		}
		return responseJson;
	}

}
