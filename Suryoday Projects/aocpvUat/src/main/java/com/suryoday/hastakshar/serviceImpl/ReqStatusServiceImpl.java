package com.suryoday.hastakshar.serviceImpl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.aocpv.exceptionhandling.DuplicateEntryException;
import com.suryoday.aocpv.exceptionhandling.NoSuchElementException;
import com.suryoday.aocpv.exceptionhandling.TimeOutException;
import com.suryoday.hastakshar.pojo.HastProxyUser;
import com.suryoday.hastakshar.pojo.HastReqAttachment;
import com.suryoday.hastakshar.pojo.HastReqStatus;
import com.suryoday.hastakshar.pojo.HastUserLog;
import com.suryoday.hastakshar.pojo.HastUserName;
import com.suryoday.hastakshar.pojo.NewBranchMaster;
import com.suryoday.hastakshar.repository.ProxyUserRepo;
import com.suryoday.hastakshar.repository.ReqAttachmentRepo;
import com.suryoday.hastakshar.repository.ReqStatusRepo;
import com.suryoday.hastakshar.repository.UserLogRepo;
import com.suryoday.hastakshar.repository.UserNameRepo;
import com.suryoday.hastakshar.service.EmailService;
import com.suryoday.hastakshar.service.ReqStatusService;
import com.suryoday.hastakshar.utils.Constants;
import com.suryoday.mhl.others.MHLGenerateProperty;

@Service
public class ReqStatusServiceImpl implements ReqStatusService {

	public static final Logger logger = LoggerFactory.getLogger(ReqStatusServiceImpl.class);

	@Value("${BASEURL}")
	private String BASEURL;

	@Value("${api_key}")
	private String api_key;

	@Autowired
	private com.suryoday.hastakshar.repository.NewBranchMasterRepo newBranchMasterRepo;
	@Autowired
	private ReqStatusRepo reqstatusrepo;
	@Autowired
	private UserLogRepo userlogrepo;
	@Autowired
	private ReqAttachmentRepo reqattachmentrepo;
	@Autowired
	private ProxyUserRepo proxyUserRepo;
	@Autowired
	private UserNameRepo usernamerepo;
	@Autowired
	private EmailNotifictionService emailNotifictionService;
	@Autowired
	private EmailService emailService;

	@Override
	public JSONObject saveNewRequestData(JSONObject jsonRequest, String userId) {
		// Logging the start of the method
		logger.debug("Starting saveNewRequestData method for userId: {}", userId);

		// Get String from JSON
		logger.debug("Extracting fields from jsonRequest.");
		String amount = jsonRequest.getJSONObject("Data").getString("amount");
		String approver1 = jsonRequest.getJSONObject("Data").getString("approver1");
		String approver2 = jsonRequest.getJSONObject("Data").getString("approver2");
		String approver3 = jsonRequest.getJSONObject("Data").getString("approver3");
		String approver4 = jsonRequest.getJSONObject("Data").getString("approver4");
		String approver5 = jsonRequest.getJSONObject("Data").getString("approver5");
		String applictioNO = jsonRequest.getJSONObject("Data").getString("applictioNO");
		String accountNo = jsonRequest.getJSONObject("Data").getString("accountNo");
		String department = jsonRequest.getJSONObject("Data").getString("department");
		String nature = jsonRequest.getJSONObject("Data").getString("nature");
		String remark = jsonRequest.getJSONObject("Data").getString("remark");
		String product = jsonRequest.getJSONObject("Data").getString("product");
		String policyNo = jsonRequest.getJSONObject("Data").getString("policyNo");
		String requestBy = jsonRequest.getJSONObject("Data").getString("requestBy");
		String transactionTypes = jsonRequest.getJSONObject("Data").getString("transactionTypes");
		String transactionDescription = jsonRequest.getJSONObject("Data").getString("transactionDescription");
		String requestflow = jsonRequest.getJSONObject("Data").getString("requestflow");
		String keyword = jsonRequest.getJSONObject("Data").getString("keyword");
		String CIF = jsonRequest.getJSONObject("Data").getString("CIF");
		String lan = jsonRequest.getJSONObject("Data").getString("lan");

		// LINK_MOBLIE_NUMBER
		String linkToCIF = "";
		String moblieNo = "";
		if (Constants.TransctionType.LINK_MOBLIE_NUMBER.equals(transactionTypes)) {
			linkToCIF = jsonRequest.getJSONObject("Data").getString("linkToCIF");
			moblieNo = jsonRequest.getJSONObject("Data").getString("registeredMobile");
			if (linkToCIF.isEmpty() || moblieNo.isEmpty()) {
				throw new DuplicateEntryException("linkToCIF or registeredMobile is empty in Link Mobile Number!");
			}
		}

//		FD_OD
		String branchCode = "";
		String margin = "";
		String fdValue = "";
		if (Constants.TransctionType.FD_OD.equals(transactionTypes)) {

			// Branch Code, OD Value, Margin, FD Value
			// 1
			branchCode = jsonRequest.getJSONObject("Data").getString("branchCode");

			// 2 Get : OD Value : from : amount
			String odValueV2 = getOdValueFromAmount(amount);

			// 3 Get : Margin_V2 : from : margin
			margin = jsonRequest.getJSONObject("Data").getString("margin");
			String marginV2 = getMarginV2FromMargin(margin);

			// 4 Get : FD_Value_V2 : from : fdValue
//			fdValue = getOriginalBalanceAmountByCustomerNo(accountNo);
			fdValue = "2000.00";
			if (fdValue == null || fdValue.isEmpty()) {
				throw new NoSuchElementException("Fd Value not found with this account no :" + accountNo + "!");
			}
			String fDValueV2 = getFDValueV2FromFDValue(fdValue, amount);

			// APPROVERS
			String highestApprover = getApproversLevelForFD_OD(odValueV2, fDValueV2, marginV2);
			// CBM(1), CBO(2), CFO(3), CEO(4)
			NewBranchMaster newBranchMaster = newBranchMasterRepo.findByBranchCode(branchCode);
			if (newBranchMaster == null) {
				throw new TimeOutException("Branch not found with branch code ::" + branchCode + "!");
			}
			if (highestApprover.equals(Constants.FD_OD.Roles.CBM)) {
				// 1
				approver1 = newBranchMaster.getCbmEmpId();
				approver2 = "NA";
				approver3 = "NA";
				approver4 = "NA";
				approver5 = "NA";
			} else if (highestApprover.equals(Constants.FD_OD.Roles.CBO)) {
				// 2
				approver1 = newBranchMaster.getCbmEmpId();
				approver3 = "NA";
				approver4 = "NA";
				approver5 = "NA";
			} else if (highestApprover.equals(Constants.FD_OD.Roles.CFO)) {
				// 3
				approver1 = newBranchMaster.getCbmEmpId();
				approver4 = "NA";
				approver5 = "NA";
			} else if (highestApprover.equals(Constants.FD_OD.Roles.CEO)) {
				// 4
				approver1 = newBranchMaster.getCbmEmpId();
				approver5 = "NA";
			}
		}

		// validate amount base on transaction type
		Long amountLong = 0L;
		if (!amount.isEmpty()) {
			amountLong = Long.parseLong(amount);
		}
		String dailyLimit = "";

		if (transactionTypes.equals(Constants.TransctionType.LIMIT_ENHANCEMENT_10L_TO_25L)) {
			if (amountLong < 1_000_000 || amountLong > 2_500_000) {
				throw new NoSuchElementException(
						"Amount should be between 10 lakh and 25 lakh for 'Limit Enhancement from 10 lakh up to 25 lakh'.");
			}
			dailyLimit = "2500000";
		} else if (transactionTypes.equals(Constants.TransctionType.LIMIT_ENHANCEMENT_25L_TO_2CR)) {
			if (amountLong <= 2_500_000 || amountLong > 20_000_000) {
				throw new NoSuchElementException(
						"Amount should be between 25 lakh 1 rupee and 2 crore for 'Limit Enhancement from 25 lakh 1 rupee up to 2 crore'.");
			}
			dailyLimit = "20000000";
		}

		// Set and save data to Request Table
		logger.debug("Populating HastReqStatus entity with extracted data.");
		HastReqStatus hastreqstatus = new HastReqStatus();
//		hastreqstatus.setApplictioNO(applictioNO);
		hastreqstatus.setAmount(amount);
		hastreqstatus.setApprover1(approver1);
		hastreqstatus.setApprover2(approver2);
		hastreqstatus.setApprover3(approver3);
		hastreqstatus.setApprover4(approver4);
		hastreqstatus.setApprover5(approver5);
		hastreqstatus.setDepartment(department);
		hastreqstatus.setNature(nature);
		hastreqstatus.setProduct(product);
		hastreqstatus.setRequestBy(requestBy);
		hastreqstatus.setRemark(remark);
		hastreqstatus.setStatus("INITIATED");
		hastreqstatus.setTransactionDescription(transactionDescription);
		hastreqstatus.setTransactionTypes(transactionTypes);
		hastreqstatus.setRequestFlow(requestflow);
		String requestState = getStateByEmpId(requestBy);
		hastreqstatus.setRequestState(requestState);
		LocalDateTime currentTime = LocalDateTime.now();
		hastreqstatus.setCreateDate(currentTime);
		hastreqstatus.setUpdateDate(currentTime);

		// LINK MULTIPLE CIF TO MOILE
		hastreqstatus.setLinkToCIF(linkToCIF);
		hastreqstatus.setMobileNo(moblieNo);
		// FD OD
		hastreqstatus.setBranchCode(branchCode);
		hastreqstatus.setMargin(margin);
		hastreqstatus.setFdValue(fdValue);

		logger.debug("Validating amount and application number.");
		int appLength = applictioNO.length();
		JSONObject response = new JSONObject();
		if (!isNumeric(amount) && nature.equals("Financial")) {
			logger.error("Invalid amount provided: {}", amount);
			response.put("message", "Invalid Amount Number");
			response.put("status", "error");
			return response;
		}

		if (appLength != 0 || !policyNo.equals("")) {
			logger.debug("Processing application with number: {}", applictioNO);
			hastreqstatus.setPolicyNo(policyNo);

			// Generate Application No
			Long count = reqstatusrepo.count();
			String applicationNo = "FIN" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + count;
			hastreqstatus.setApplictioNO(applicationNo);
			hastreqstatus.setAccountNo(accountNo);
			hastreqstatus.setCIF(CIF);
			hastreqstatus.setLan(lan);
			hastreqstatus.setKeyword(keyword);
			reqstatusrepo.save(hastreqstatus);
			logger.info("Request data saved to the database successfully for applicationNo: {}", applicationNo);

			// LIMIT ENHANCEMENT EMAIL
			if (transactionTypes.equals(Constants.TransctionType.LIMIT_ENHANCEMENT_10L_TO_25L)
					|| transactionTypes.equals(Constants.TransctionType.LIMIT_ENHANCEMENT_25L_TO_2CR)) {
				emailService.sendLimitEnhancementEmailToApprover(approver1, accountNo, transactionDescription,
						":: Send email to approver 1 ::");
			}

			// Logging the user activity
			HastUserLog hastuserlog = new HastUserLog();
			hastuserlog.setApplictioNO(applictioNO);
			hastuserlog.setRemark(remark);
			hastuserlog.setStatus("INITIATED");
			hastuserlog.setUpdateDate(currentTime);
			hastuserlog.setEmpId(requestBy);
			logger.debug("User log entry created.");

			if (requestflow.equals("PARALLEL")) {
				logger.info("Triggering email notifications for approvers in parallel.");
				if (!approver1.equals("NA") && approver1 != null) {
					String app1Email = getEmail(approver1);
					emailNotifictionService.NewReqEmail(app1Email, policyNo, applictioNO, nature, transactionTypes,
							transactionDescription, department, requestBy);
					logger.info("Email sent to approver1: {}", approver1);
				}
				if (!approver2.equals("NA") && approver2 != null) {
					String app2Email = getEmail(approver2);
					emailNotifictionService.NewReqEmail(app2Email, policyNo, applictioNO, nature, transactionTypes,
							transactionDescription, department, requestBy);
					logger.info("Email sent to approver2: {}", approver2);
				}
				if (!approver3.equals("NA") && approver3 != null) {
					String app3Email = getEmail(approver3);
					emailNotifictionService.NewReqEmail(app3Email, policyNo, applictioNO, nature, transactionTypes,
							transactionDescription, department, requestBy);
					logger.info("Email sent to approver3: {}", approver3);
				}
				if (!approver4.equals("NA") && approver4 != null) {
					String app4Email = getEmail(approver4);
					emailNotifictionService.NewReqEmail(app4Email, policyNo, applictioNO, nature, transactionTypes,
							transactionDescription, department, requestBy);
					logger.info("Email sent to approver4: {}", approver4);
				}
				if (!approver5.equals("NA") && approver5 != null) {
					String app5Email = getEmail(approver5);
					emailNotifictionService.NewReqEmail(app5Email, policyNo, applictioNO, nature, transactionTypes,
							transactionDescription, department, requestBy);
					logger.info("Email sent to approver5: {}", approver5);
				}
			}
			userlogrepo.save(hastuserlog);
			logger.info("User log entry saved successfully for applicationNo: {}", applictioNO);

			response.put("message", "Data Saved");
			response.put("status", "200 OK");
		} else {
			logger.error("Application number or policy number missing for request.");
			response.put("message", "please enter application number!");
			response.put("status", "error");
		}

		logger.debug("Returning response: {}", response);
		return response;
	}

	private String getOdValueFromAmount(String amount) {
		try {
			double amountValue = Double.parseDouble(amount);

			double fiftyL = 50_00_000;
			double oneCr = 1_00_00_000;

			if (amountValue < fiftyL) {
				return Constants.FD_OD.TransactionType.FD_OD_LESS_THAN_50_L;
			} else if (amountValue <= oneCr) {
				return Constants.FD_OD.TransactionType.FD_OD_50_L_TO_1_CR;
			} else {
				return Constants.FD_OD.TransactionType.FD_OD_ABOVE_1_CR;
			}
		} catch (NumberFormatException e) {
			logger.error("Invalid amount format: " + amount, e);
			return null;
		}
	}

	private String getFDValueV2FromFDValue(String fdValue, String amount) {
		try {
			double fdValueNumeric = Double.parseDouble(fdValue);
			double amountNumeric = Double.parseDouble(amount);

			if (fdValueNumeric == 0) {
				throw new IllegalArgumentException("FD Value cannot be zero.");
			}

			double result = (amountNumeric / fdValueNumeric) * 100;

			if (result < 90) {
				throw new IllegalArgumentException("Result is less than 90%");
			} else if (result == 90) {
				return Constants.FD_OD.TransactionDescription.NINETY_PERCENTAGE;
			} else if (result > 90 && result <= 95) {
				return Constants.FD_OD.TransactionDescription.IF_MORE_THAN_90_TO_95_PERCENTAGE;
			} else if (result > 95) {
				return Constants.FD_OD.TransactionDescription.ABOVE_95_PERCENTAGE;

			}

		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid amount or FD Value format.", e);
		}
		return null;
	}

	@Override
	public String getOriginalBalanceAmountByCustomerNo(String accountNo) {
		logger.debug("getOriginalBalanceAmountByCustomerNo :: Initiating request to fetch account details");
		String amount = "";

		try {
			// Set up SSL context to trust all certificates
			SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial((chain, authType) -> true).build();
			CloseableHttpClient httpClient = HttpClientBuilder.create().setSSLContext(sslContext)
					.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();

			// Create GET request
			String url = Constants.UAT.FD_OD_PREFIX + accountNo + Constants.UAT.FD_OD_SUFFIX;
//			String url = "https://intramashery.suryodaybank.co.in/ssfbuat/accounts/TDRD/" + accountNo + "/v2?api_key=kyqak5muymxcrjhc5q57vz9v";
			HttpGet getRequest = new HttpGet(url);

			// Set headers
			getRequest.setHeader("Content-Type", "application/json");
			getRequest.setHeader("X-From-ID", "SER");
			getRequest.setHeader("X-Request-ID", "SER");
			getRequest.setHeader("X-To-ID", "Other");
			getRequest.setHeader("X-User-ID", "S7070");

			// Execute request and get response
			HttpResponse response = httpClient.execute(getRequest);
			String result = EntityUtils.toString(response.getEntity());

			// Parse and return the response as JSON
			JSONObject jsonResponse = new JSONObject(result);

			// GET BALANCE
			if (jsonResponse.has("Data")) {
				JSONObject data = jsonResponse.getJSONObject("Data");
				if (data.has("Account")) {
					JSONArray accountArray = data.getJSONArray("Account");
					if (accountArray.length() > 0) {
						JSONObject account = accountArray.getJSONObject(0); // First account in the array
						if (account.has("Balance")) {
							JSONArray balanceArray = account.getJSONArray("Balance");
							if (balanceArray.length() > 0) {
								JSONObject balance = balanceArray.getJSONObject(2); // Third element in Balance array
								amount = balance.getString("Amount");
								logger.debug("Balance[2]: " + balance.toString());
							}
						}
					}
				}
			}

		} catch (Exception e) {
			logger.debug("Exception occurred during fetch FD Value: " + e.getMessage());
			throw new RuntimeException("Error occurred while fetch FD Value: " + e.getMessage());
		}

		logger.debug("Fetch account details Response ::" + amount);
		return amount;

	}

	private String getMarginV2FromMargin(String margin) {
		if (margin == null || margin.isEmpty()) {
			return null;
		}

		try {
			double value = Double.parseDouble(margin);

			if (value >= 2.0) {
				return Constants.FD_OD.TransactionDescriptionV2.TWO_PERCENTAGE_OR_MORE;
			} else if (value < 2.0 && value >= 1.0) {
				return Constants.FD_OD.TransactionDescriptionV2.LESS_THAN_2_PERCENTAGE;
			} else if (value < 1.0) {
				return Constants.FD_OD.TransactionDescriptionV2.LESS_THAN_1_PERCENTAGE;
			} else {
				return null;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String getApproversLevelForFD_OD(String transactionTypes, String transactionDescription,
			String transactionDescriptionV2) {
		String approver = "";
		switch (transactionTypes) {
		case Constants.FD_OD.TransactionType.FD_OD_LESS_THAN_50_L:
			//
			switch (transactionDescriptionV2) {
			case Constants.FD_OD.TransactionDescriptionV2.TWO_PERCENTAGE_OR_MORE:
				//
				switch (transactionDescription) {
				case Constants.FD_OD.TransactionDescription.NINETY_PERCENTAGE:
					// Condition 1: FD OD Less than 50 L, 2% or more, 90%
					approver = Constants.FD_OD.Roles.CBM;

					break;
				case Constants.FD_OD.TransactionDescription.IF_MORE_THAN_90_TO_95_PERCENTAGE:
					// Condition 2: FD OD Less than 50 L, 2% or more, If more than 90 to 95%
					approver = Constants.FD_OD.Roles.CFO;

					break;
				case Constants.FD_OD.TransactionDescription.ABOVE_95_PERCENTAGE:
					// Condition 3: FD OD Less than 50 L, 2% or more, Above 95%
					approver = Constants.FD_OD.Roles.CEO;

					break;
				}
				break;
			case Constants.FD_OD.TransactionDescriptionV2.LESS_THAN_2_PERCENTAGE:
				switch (transactionDescription) {
				case Constants.FD_OD.TransactionDescription.NINETY_PERCENTAGE:
					// Condition 4: FD OD Less than 50 L, Less than 2%, 90%
					approver = Constants.FD_OD.Roles.CBO;
					break;
				case Constants.FD_OD.TransactionDescription.IF_MORE_THAN_90_TO_95_PERCENTAGE:
					// Condition 5: FD OD Less than 50 L, Less than 2%, If more than 90 to 95%
					approver = Constants.FD_OD.Roles.CFO;
					break;
				case Constants.FD_OD.TransactionDescription.ABOVE_95_PERCENTAGE:
					// Condition 6: FD OD Less than 50 L, Less than 2%, Above 95%
					approver = Constants.FD_OD.Roles.CEO;
					break;
				}
				break;
			case Constants.FD_OD.TransactionDescriptionV2.LESS_THAN_1_PERCENTAGE:
				switch (transactionDescription) {
				case Constants.FD_OD.TransactionDescription.NINETY_PERCENTAGE:
					// Condition 7: FD OD Less than 50 L, Less than 1%, 90%
					approver = Constants.FD_OD.Roles.CEO;
					break;
				case Constants.FD_OD.TransactionDescription.IF_MORE_THAN_90_TO_95_PERCENTAGE:
					// Condition 8: FD OD Less than 50 L, Less than 1%, If more than 90 to 95%
					approver = Constants.FD_OD.Roles.CEO;
					break;
				case Constants.FD_OD.TransactionDescription.ABOVE_95_PERCENTAGE:
					// Condition 9: FD OD Less than 50 L, Less than 1%, Above 95%
					approver = Constants.FD_OD.Roles.CEO;
					break;
				}
				break;
			}
			break;
		case Constants.FD_OD.TransactionType.FD_OD_50_L_TO_1_CR:
			switch (transactionDescriptionV2) {
			//
			case Constants.FD_OD.TransactionDescriptionV2.TWO_PERCENTAGE_OR_MORE:
				//
				switch (transactionDescription) {
				case Constants.FD_OD.TransactionDescription.NINETY_PERCENTAGE:
					// Condition 10: FD OD 50 L to 1 Cr, 2% or more, 90%
					approver = Constants.FD_OD.Roles.CBO;
					break;
				case Constants.FD_OD.TransactionDescription.IF_MORE_THAN_90_TO_95_PERCENTAGE:
					// Condition 11: FD OD 50 L to 1 Cr, 2% or more, If more than 90 to 95%
					approver = Constants.FD_OD.Roles.CFO;
					break;
				case Constants.FD_OD.TransactionDescription.ABOVE_95_PERCENTAGE:
					// Condition 12: FD OD 50 L to 1 Cr, 2% or more, Above 95%
					approver = Constants.FD_OD.Roles.CEO;
					break;
				}
				break;
			case Constants.FD_OD.TransactionDescriptionV2.LESS_THAN_2_PERCENTAGE:
				switch (transactionDescription) {
				case Constants.FD_OD.TransactionDescription.NINETY_PERCENTAGE:
					// Condition 13: FD OD 50 L to 1 Cr, Less than 2%, 90%
					approver = Constants.FD_OD.Roles.CBO;
					break;
				case Constants.FD_OD.TransactionDescription.IF_MORE_THAN_90_TO_95_PERCENTAGE:
					// Condition 14: FD OD 50 L to 1 Cr, Less than 2%, If more than 90 to 95%
					approver = Constants.FD_OD.Roles.CFO;
					break;
				case Constants.FD_OD.TransactionDescription.ABOVE_95_PERCENTAGE:
					// Condition 15: FD OD 50 L to 1 Cr, Less than 2%, Above 95%
					approver = Constants.FD_OD.Roles.CEO;
					break;
				}
				break;
			case Constants.FD_OD.TransactionDescriptionV2.LESS_THAN_1_PERCENTAGE:
				switch (transactionDescription) {
				case Constants.FD_OD.TransactionDescription.NINETY_PERCENTAGE:
					// Condition 16: FD OD 50 L to 1 Cr, Less than 1%, 90%
					approver = Constants.FD_OD.Roles.CEO;
					break;
				case Constants.FD_OD.TransactionDescription.IF_MORE_THAN_90_TO_95_PERCENTAGE:
					// Condition 17: FD OD 50 L to 1 Cr, Less than 1%, If more than 90 to 95%
					approver = Constants.FD_OD.Roles.CEO;
					break;
				case Constants.FD_OD.TransactionDescription.ABOVE_95_PERCENTAGE:
					// Condition 18: FD OD 50 L to 1 Cr, Less than 1%, Above 95%
					approver = Constants.FD_OD.Roles.CEO;
					break;
				}
				break;
			}
			break;
		case Constants.FD_OD.TransactionType.FD_OD_ABOVE_1_CR:
			//
			switch (transactionDescriptionV2) {
			//
			case Constants.FD_OD.TransactionDescriptionV2.TWO_PERCENTAGE_OR_MORE:
				switch (transactionDescription) {
				case Constants.FD_OD.TransactionDescription.NINETY_PERCENTAGE:
					// Condition 19: FD OD Above 1 Cr, 2% or more, 90%
					approver = Constants.FD_OD.Roles.CFO;
					break;
				case Constants.FD_OD.TransactionDescription.IF_MORE_THAN_90_TO_95_PERCENTAGE:
					// Condition 20: FD OD Above 1 Cr, 2% or more, If more than 90 to 95%
					approver = Constants.FD_OD.Roles.CFO;
					break;
				case Constants.FD_OD.TransactionDescription.ABOVE_95_PERCENTAGE:
					// Condition 21: FD OD Above 1 Cr, 2% or more, Above 95%
					approver = Constants.FD_OD.Roles.CEO;
					break;
				}
				break;
			case Constants.FD_OD.TransactionDescriptionV2.LESS_THAN_2_PERCENTAGE:
				switch (transactionDescription) {
				case Constants.FD_OD.TransactionDescription.NINETY_PERCENTAGE:
					// Condition 22: FD OD Above 1 Cr, Less than 2%, 90%
					approver = Constants.FD_OD.Roles.CFO;
					break;
				case Constants.FD_OD.TransactionDescription.IF_MORE_THAN_90_TO_95_PERCENTAGE:
					// Condition 23: FD OD Above 1 Cr, Less than 2%, If more than 90 to 95%
					approver = Constants.FD_OD.Roles.CFO;
					break;
				case Constants.FD_OD.TransactionDescription.ABOVE_95_PERCENTAGE:
					// Condition 24: FD OD Above 1 Cr, Less than 2%, Above 95%
					approver = Constants.FD_OD.Roles.CEO;
					break;
				}
				break;
//			  
			case Constants.FD_OD.TransactionDescriptionV2.LESS_THAN_1_PERCENTAGE:
				switch (transactionDescription) {
				case Constants.FD_OD.TransactionDescription.NINETY_PERCENTAGE:
					// Condition 25: FD OD Above 1 Cr, Less than 1%, 90%
					approver = Constants.FD_OD.Roles.CEO;
					break;
				case Constants.FD_OD.TransactionDescription.IF_MORE_THAN_90_TO_95_PERCENTAGE:
					// Condition 26: FD OD Above 1 Cr, Less than 1%, If more than 90 to 95%
					approver = Constants.FD_OD.Roles.CEO;
					break;
				case Constants.FD_OD.TransactionDescription.ABOVE_95_PERCENTAGE:
					// Condition 27: FD OD Above 1 Cr, Less than 1%, Above 95%
					approver = Constants.FD_OD.Roles.CEO;
					break;
				}
				break;
			}
			break;

		default:

			break;
		}

		return approver;
	}

	public String calculateTAT48(LocalDateTime currentTime) {
		LocalDateTime tat48Time = currentTime.plusHours(48);
		LocalDateTime tempTime = currentTime;
		long extraHours = 0;
		while (tempTime.isBefore(tat48Time)) {
			if (reqstatusrepo.existsHolidayByHolidayDate(tempTime.toLocalDate()) == 1) {
				extraHours += 24;
			}
			tempTime = tempTime.plusDays(1);
		}
		tat48Time = tat48Time.plusHours(extraHours);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH");
		return tat48Time.format(formatter);
	}

	public JSONArray callList(List<HastReqStatus> list) {
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			JSONObject innerData = new JSONObject();
			innerData.put("applictioNO", list.get(i).getApplictioNO());
			innerData.put("amount", list.get(i).getAmount());
			innerData.put("department", list.get(i).getDepartment());
			innerData.put("transactionTypes", list.get(i).getTransactionTypes());
			innerData.put("transactionDescription", list.get(i).getTransactionDescription());
			innerData.put("product", list.get(i).getProduct());
			innerData.put("policyNo", list.get(i).getPolicyNo());
			innerData.put("approver1", list.get(i).getApprover1());
			innerData.put("approver2", list.get(i).getApprover2());
			innerData.put("approver3", list.get(i).getApprover3());
			innerData.put("approver4", list.get(i).getApprover4());
			innerData.put("approver5", list.get(i).getApprover5());
			innerData.put("requestBy", list.get(i).getRequestBy());
			innerData.put("remark", list.get(i).getRemark());
			innerData.put("status", list.get(i).getStatus());
			innerData.put("nature", list.get(i).getNature());
			innerData.put("createDate", list.get(i).getCreateDate());
			innerData.put("requestflow", list.get(i).getRequestFlow());
			innerData.put("requestState", list.get(i).getRequestState());

			// LINK_MOBLIE_NUMBER
			if (Constants.TransctionType.LINK_MOBLIE_NUMBER.equals(list.get(i).getTransactionTypes())) {
				innerData.put("linkToCIF", list.get(i).getLinkToCIF());
				innerData.put("registeredMobile", list.get(i).getMobileNo());
			}

			// FD_OD
			if (Constants.TransctionType.FD_OD.equals(list.get(i).getTransactionTypes())) {
				innerData.put("branchCode", list.get(i).getBranchCode());
				innerData.put("margin", list.get(i).getMargin());
				innerData.put("fdValue", list.get(i).getFdValue());
				System.out.println("::" + list.get(i).getMargin() + "::" + innerData.getString("margin"));
				System.out.println("::" + list.get(i).getFdValue() + "::" + innerData.getString("fdValue"));
			}

			if (list.get(i).getLan() == null) {
				innerData.put("lan", "NA");
			} else {
				innerData.put("lan", list.get(i).getLan());
			}
			if (list.get(i).getCIF() == null) {
				innerData.put("cif", "NA");
			} else {
				innerData.put("cif", list.get(i).getCIF());
			}
			if (list.get(i).getAccountNo() == null) {
				innerData.put("accountNo", "NA");
			} else {
				innerData.put("accountNo", list.get(i).getAccountNo());
			}
			if (list.get(i).getKeyword() == null) {
				innerData.put("keyword", "NA");
			} else {
				innerData.put("keyword", list.get(i).getKeyword());
			}
			List<HastUserLog> list1 = userlogrepo.fetchStatusByEmpID(list.get(i).getApplictioNO(),
					list.get(i).getApprover1());
			List<HastUserLog> list2 = userlogrepo.fetchStatusByEmpID(list.get(i).getApplictioNO(),
					list.get(i).getApprover2());
			List<HastUserLog> list3 = userlogrepo.fetchStatusByEmpID(list.get(i).getApplictioNO(),
					list.get(i).getApprover3());
			List<HastUserLog> list4 = userlogrepo.fetchStatusByEmpID(list.get(i).getApplictioNO(),
					list.get(i).getApprover4());
			List<HastUserLog> list5 = userlogrepo.fetchStatusByEmpID(list.get(i).getApplictioNO(),
					list.get(i).getApprover5());
			if (list.get(i).getApprover1().length() != 0 && !list1.isEmpty()) {
				innerData.put("approver1status", list.get(i).getApprover1() + "|" + list1.get(0).getStatus() + "|"
						+ getEmpName(list.get(i).getApprover1()));
				innerData.put("approverStatus1", list1.get(0).getStatus());
				innerData.put("approver1EmpName", getEmpName(list.get(i).getApprover1()));
			} else {
				innerData.put("approver1status",
						list.get(i).getApprover1() + "|PENDING|" + getEmpName(list.get(i).getApprover1()));
				innerData.put("approverStatus1", "PENDING");
				innerData.put("approver1EmpName", getEmpName(list.get(i).getApprover1()));
			}
			if (list.get(i).getApprover2().length() != 0 && !list2.isEmpty()) {
				innerData.put("approver2status", list.get(i).getApprover2() + "|" + list2.get(0).getStatus() + "|"
						+ getEmpName(list.get(i).getApprover2()));
				innerData.put("approverStatus2", list2.get(0).getStatus());
				innerData.put("approver2EmpName", getEmpName(list.get(i).getApprover2()));
			} else {
				innerData.put("approver2status",
						list.get(i).getApprover2() + "|PENDING|" + getEmpName(list.get(i).getApprover2()));
				innerData.put("approverStatus2", "PENDING");
				innerData.put("approver2EmpName", getEmpName(list.get(i).getApprover2()));
			}
			if (list.get(i).getApprover3().length() != 0 && !list3.isEmpty()) {
				innerData.put("approver3status", list.get(i).getApprover3() + "|" + list3.get(0).getStatus() + "|"
						+ getEmpName(list.get(i).getApprover3()));
				innerData.put("approverStatus3", list3.get(0).getStatus());
				innerData.put("approver3EmpName", getEmpName(list.get(i).getApprover3()));
			} else {
				innerData.put("approver3status",
						list.get(i).getApprover3() + "|PENDING|" + getEmpName(list.get(i).getApprover3()));
				innerData.put("approverStatus3", "PENDING");
				innerData.put("approver3EmpName", getEmpName(list.get(i).getApprover3()));
			}
			if (list.get(i).getApprover4().length() != 0 && !list4.isEmpty()) {
				innerData.put("approver4status", list.get(i).getApprover4() + "|" + list4.get(0).getStatus() + "|"
						+ getEmpName(list.get(i).getApprover4()));
				innerData.put("approverStatus4", list4.get(0).getStatus());
				innerData.put("approver4Status", getEmpName(list.get(i).getApprover4()));
			} else {
				innerData.put("approver4status",
						list.get(i).getApprover4() + "|PENDING|" + getEmpName(list.get(i).getApprover4()));
				innerData.put("approverStatus4", "PENDING");
				innerData.put("approver4Status", getEmpName(list.get(i).getApprover4()));
			}
			if (list.get(i).getApprover5().length() != 0 && !list5.isEmpty()) {
				innerData.put("approver5status", list.get(i).getApprover5() + "|" + list5.get(0).getStatus() + "|"
						+ getEmpName(list.get(i).getApprover5()));
				innerData.put("approverStatus5", list5.get(0).getStatus());
				innerData.put("approver5Status", getEmpName(list.get(i).getApprover5()));
			} else {
				innerData.put("approver5status",
						list.get(i).getApprover5() + "|PENDING|" + getEmpName(list.get(i).getApprover5()));
				innerData.put("approverStatus5", "PENDING");
				innerData.put("approver5Status", getEmpName(list.get(i).getApprover5()));
			}
			array.put(innerData);
		}
		return array;
	}

	private int countOccurrences(String[] array, String target) {
		int count = 0;
		for (String element : array) {
			if (element.equals(target)) {
				count++;
			}
		}
		return count;
	}

	@Override
	public JSONArray fetchList(JSONObject jsonRequest) {
		String empId = jsonRequest.getJSONObject("Data").getString("empId");
		String role = jsonRequest.getJSONObject("Data").getString("role");
		JSONArray array = new JSONArray();
		if (role.equals("HAST_USERMAKER") || role.equals("ADMIN")) {
			List<HastReqStatus> list = reqstatusrepo.fetchReqList(empId);
			array = callList(list);
		} else if (role.equals("HAST_USERCHECKER") || role.equals("ADMIN")) {
			List<HastReqStatus> list = reqstatusrepo.fetchAprList(empId);
			// Check State is MATCH or NOT
			array = UserMakerProxy(list, empId);
		} else if (role.equals("HAST_USEROPS") || role.equals("ADMIN")) {
			List<HastReqStatus> list = reqstatusrepo.fetchOps();
			array = callList(list);
		} else if (role.equals("HAST_USERPROXY") || role.equals("ADMIN")) {
			List<HastProxyUser> proxyUser = proxyUserRepo.fetchApproverbyProxy(empId);
			if (!proxyUser.isEmpty()) {
				empId = proxyUser.get(0).getApproverUser();
			}
			List<HastReqStatus> list = reqstatusrepo.fetchAprList(empId);
			array = UserMakerProxy(list, empId);
		}
		return array;
	}

	// Check State is MATCH or NOT (PROXY AND CHECKER LIST IS SAME)
	public JSONArray UserMakerProxy(List<HastReqStatus> list, String empId) {
		JSONArray array = new JSONArray();
		String loginEmpState = new String();
		JSONArray checkarray = callList(list);
		for (int i = 0; i < checkarray.length(); i++) {
			if (checkarray.getJSONObject(i).has("requestState")) {
				String reqState = checkarray.getJSONObject(i).getString("requestState");
				if (empId.equals(checkarray.getJSONObject(i).getString("approver1"))) {
					empId = checkarray.getJSONObject(i).getString("approver1");
					loginEmpState = getStateByEmpId(empId);
					if (reqState.equals(loginEmpState)) {
						array.put(checkarray.getJSONObject(i));
					}
				} else if (empId.equals(checkarray.getJSONObject(i).getString("approver2"))) {
					empId = checkarray.getJSONObject(i).getString("approver2");
					loginEmpState = getStateByEmpId(empId);
					if (reqState.equals(loginEmpState)) {
						array.put(checkarray.getJSONObject(i));
					}
				} else {
					array.put(checkarray.getJSONObject(i));
				}
			}
		}
		return array;
	}

	@Override
	public JSONArray fetchBySearch(JSONObject jsonRequest) {
		String applicationNo = jsonRequest.getJSONObject("Data").getString("applicationNo");
		String empId = jsonRequest.getJSONObject("Data").getString("empId");
		String role = jsonRequest.getJSONObject("Data").getString("role");

		/*
		 * String department =
		 * jsonRequest.getJSONObject("Data").getString("department"); String
		 * transactionTypes =
		 * jsonRequest.getJSONObject("Data").getString("transactionTypes"); String
		 * nature = jsonRequest.getJSONObject("Data").getString("nature"); String
		 * product = jsonRequest.getJSONObject("Data").getString("product"); String
		 * requestBy = jsonRequest.getJSONObject("Data").getString("requestBy"); String
		 * status = jsonRequest.getJSONObject("Data").getString("status");
		 */
		JSONArray array = new JSONArray();
		if (role.equals("HAST_USERMAKER")) {
			List<HastReqStatus> list = reqstatusrepo.fetchBySearchReq(applicationNo, empId);
			array = callList(list);
		} else if (role.equals("HAST_USERCHECKER")) {
			List<HastReqStatus> list = reqstatusrepo.fetchBySearchApr(applicationNo, empId);
			array = UserMakerProxy(list, empId);
		} else if (role.equals("HAST_USERPROXY")) {
			List<HastProxyUser> proxyUser = proxyUserRepo.fetchApproverbyProxy(empId);
			if (!proxyUser.isEmpty()) {
				empId = proxyUser.get(0).getApproverUser();
			}
			List<HastReqStatus> list = reqstatusrepo.fetchBySearchApr(applicationNo, empId);
			array = UserMakerProxy(list, empId);
		}
		return array;
	}

	@Override
	public JSONArray fetchByDate(JSONObject jsonRequest) {
		// String policyNo = jsonRequest.getJSONObject("Data").getString("policyNo");
		String empId = jsonRequest.getJSONObject("Data").getString("empId");
		String role = jsonRequest.getJSONObject("Data").getString("role");
		String department = jsonRequest.getJSONObject("Data").getString("department");
		String transactionTypes = jsonRequest.getJSONObject("Data").getString("transactionTypes");
		String nature = jsonRequest.getJSONObject("Data").getString("nature");
		String product = jsonRequest.getJSONObject("Data").getString("product");
		String requestBy = jsonRequest.getJSONObject("Data").getString("requestBy");
		String status = jsonRequest.getJSONObject("Data").getString("status");
		String startDate = jsonRequest.getJSONObject("Data").getString("startDate");
		String endDate = jsonRequest.getJSONObject("Data").getString("endDate");
		// String branchId = jsonRequest.getJSONObject("Data").getString("branchId");
		String applictiono = jsonRequest.getJSONObject("Data").getString("applictiono");
		String keyword = jsonRequest.getJSONObject("Data").getString("keyword");
		String keywordInput = jsonRequest.getJSONObject("Data").getString("keywordInput");

		/*
		 * String cif = jsonRequest.getJSONObject("Data").getString("cif"); String lan =
		 * jsonRequest.getJSONObject("Data").getString("lan"); String policy =
		 * jsonRequest.getJSONObject("Data").getString("policy"); String accountno =
		 * jsonRequest.getJSONObject("Data").getString("accountNo");
		 */

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		JSONArray array = new JSONArray();
		if (role.equals("HAST_USERMAKER")) {
			if (startDate != null && endDate != null && !startDate.isEmpty()) {
				LocalDateTime startdate = LocalDateTime.parse(startDate, formatter);
				LocalDateTime enddate = LocalDateTime.parse(endDate, formatter);
				List<HastReqStatus> list = reqstatusrepo.fetchBydate(startdate, enddate, status);
				array = callList(list);
			} else if (applictiono != null && !applictiono.isEmpty()) {
				LocalDateTime enddate = LocalDateTime.now().minusDays(90);
				List<HastReqStatus> list = null;
				if (keyword.equalsIgnoreCase("lan")) {
					list = reqstatusrepo.checkByLanTranxtype(enddate, applictiono, nature, requestBy, transactionTypes,
							department, keyword, keywordInput);
				} else if (keyword.equalsIgnoreCase("accountNo")) {
					list = reqstatusrepo.checkByAccountTranxtype(enddate, applictiono, nature, requestBy,
							transactionTypes, department, keyword, keywordInput);
				} else if (keyword.equalsIgnoreCase("policy")) {
					list = reqstatusrepo.checkByPolicyTranxtype(enddate, applictiono, nature, requestBy,
							transactionTypes, department, keyword, keywordInput);
				} else if (keyword.equalsIgnoreCase("cif")) {
					list = reqstatusrepo.checkByCifTranxtype(enddate, applictiono, nature, requestBy, transactionTypes,
							department, keyword, keywordInput);
				}

				if (list == null || list.size() == 0) {
					throw new NoSuchElementException("No Record Found");
				}

				array = callList(list);
			}
		} else if (role.equals("HAST_USERCHECKER")) {
			if (startDate != null && endDate != null && !startDate.isEmpty()) {
				LocalDateTime startdate = LocalDateTime.parse(startDate, formatter);
				LocalDateTime enddate = LocalDateTime.parse(endDate, formatter);
				List<HastReqStatus> list = reqstatusrepo.fetchBydate(startdate, enddate, status);
				array = UserMakerProxy(list, empId);
			} else if (applictiono != null && !applictiono.isEmpty() || nature != null && !nature.isEmpty()
					|| requestBy != null && !requestBy.isEmpty()
					|| transactionTypes != null && !transactionTypes.isEmpty()) {
				LocalDateTime enddate = LocalDateTime.now().minusDays(90);

				List<HastReqStatus> list = null;
				if (keyword.equalsIgnoreCase("lan")) {
					list = reqstatusrepo.checkByLanTranxtype(enddate, applictiono, nature, requestBy, transactionTypes,
							department, keyword, keywordInput);
				} else if (keyword.equalsIgnoreCase("accountNo")) {
					list = reqstatusrepo.checkByAccountTranxtype(enddate, applictiono, nature, requestBy,
							transactionTypes, department, keyword, keywordInput);
				} else if (keyword.equalsIgnoreCase("policy")) {
					list = reqstatusrepo.checkByPolicyTranxtype(enddate, applictiono, nature, requestBy,
							transactionTypes, department, keyword, keywordInput);
				} else if (keyword.equalsIgnoreCase("cif")) {
					list = reqstatusrepo.checkByCifTranxtype(enddate, applictiono, nature, requestBy, transactionTypes,
							department, keyword, keywordInput);
				}
				if (list == null || list.size() == 0) {
					throw new NoSuchElementException("No Record Found");
				}

				array = UserMakerProxy(list, empId);
			}
		} else if (role.equals("HAST_USEROPS")) {
			if (startDate != null && endDate != null && !startDate.isEmpty()) {
				LocalDateTime startdate = LocalDateTime.parse(startDate, formatter);
				LocalDateTime enddate = LocalDateTime.parse(endDate, formatter);
				List<HastReqStatus> list = reqstatusrepo.fetchBydate(startdate, enddate, status);
				array = UserMakerProxy(list, empId);
			} else if (applictiono != null && !applictiono.isEmpty() || nature != null && !nature.isEmpty()
					|| requestBy != null && !requestBy.isEmpty()
					|| transactionTypes != null && !transactionTypes.isEmpty()
					|| department != null && !department.isEmpty()) {
				LocalDateTime enddate = LocalDateTime.now().minusDays(90);

				List<HastReqStatus> list = null;
				if (keyword.equalsIgnoreCase("lan")) {
					list = reqstatusrepo.checkByLanTranxtype(enddate, applictiono, nature, requestBy, transactionTypes,
							department, keyword, keywordInput);
				} else if (keyword.equalsIgnoreCase("accountNo")) {
					list = reqstatusrepo.checkByAccountTranxtype(enddate, applictiono, nature, requestBy,
							transactionTypes, department, keyword, keywordInput);
				} else if (keyword.equalsIgnoreCase("policy")) {
					list = reqstatusrepo.checkByPolicyTranxtype(enddate, applictiono, nature, requestBy,
							transactionTypes, department, keyword, keywordInput);
				} else if (keyword.equalsIgnoreCase("cif")) {
					list = reqstatusrepo.checkByCifTranxtype(enddate, applictiono, nature, requestBy, transactionTypes,
							department, keyword, keywordInput);
				} else if (applictiono != null && !applictiono.isEmpty()) {
					list = reqstatusrepo.fetchByApplicationNo(applictiono);
				} else if (nature != null && !nature.isEmpty()) {
					list = reqstatusrepo.fetchByNature(nature);
				} else if (requestBy != null && !requestBy.isEmpty()) {
					list = reqstatusrepo.fetchByRequest(requestBy);
				} else {
					list = reqstatusrepo.fetchByNatureAndRequestBy(nature, requestBy);
				}
				if (list == null || list.size() == 0) {
					throw new NoSuchElementException("No Record Found");
				}

				array = UserMakerProxy(list, empId);
			}
		} else if (role.equals("HAST_USERPROXY")) {
			List<HastProxyUser> proxyUser = proxyUserRepo.fetchApproverbyProxy(empId);
			if (!proxyUser.isEmpty()) {
				empId = proxyUser.get(0).getApproverUser();
			}
			if (startDate != null && endDate != null) {
				LocalDateTime startdate = LocalDateTime.parse(startDate, formatter);
				LocalDateTime enddate = LocalDateTime.parse(endDate, formatter);
				List<HastReqStatus> list = reqstatusrepo.fetchBydate(startdate, enddate, status);
				array = UserMakerProxy(list, empId);
			} else if (applictiono != null && !applictiono.isEmpty() || nature != null && !nature.isEmpty()
					|| requestBy != null && !requestBy.isEmpty()
					|| transactionTypes != null && !transactionTypes.isEmpty()) {
				LocalDateTime enddate = LocalDateTime.now().minusDays(90);
				List<HastReqStatus> list = reqstatusrepo.fetchBySearchApr(keywordInput, empId);
				array = UserMakerProxy(list, empId);
			}
		}
		return array;
	}

	@Override
	public JSONArray fetchByStatus(JSONObject jsonRequest) {
		String status = jsonRequest.getJSONObject("Data").getString("status");
		String empId = jsonRequest.getJSONObject("Data").getString("empId");
		String role = jsonRequest.getJSONObject("Data").getString("role");
		JSONArray array = new JSONArray();
		if (role.equals("HAST_USERMAKER")) {
			List<HastReqStatus> list = reqstatusrepo.fetchByStatusReq(status, empId);
			array = callList(list);
		} else if (role.equals("HAST_USERCHECKER")) {
			List<HastReqStatus> list = reqstatusrepo.fetchByStatusApr(status, empId);
			array = UserMakerProxy(list, empId);
		} else if (role.equals("HAST_USERPROXY")) {
			List<HastProxyUser> proxyUser = proxyUserRepo.fetchApproverbyProxy(empId);
			if (!proxyUser.isEmpty()) {
				empId = proxyUser.get(0).getApproverUser();
			}
			List<HastReqStatus> list = reqstatusrepo.fetchByStatusApr(status, empId);
			array = UserMakerProxy(list, empId);
		}
		return array;
	}

	@Override
	public JSONObject updateStatus(JSONObject jsonRequest, String userId) {
		String applictioNO = jsonRequest.getJSONObject("Data").getString("applictioNO");
		String status = jsonRequest.getJSONObject("Data").getString("status");

		JSONObject response = new JSONObject();

		if (status != null) {
			HastReqStatus hastReqStatus = reqstatusrepo.findByApplictioNO(applictioNO);
			if (hastReqStatus == null) {
				throw new DuplicateEntryException("Data not found with applicationNo :" + applictioNO + "!");
			}
			// UPDATE STATUS
			String savedStatus = hastReqStatus.getStatus();
			switch (savedStatus.toUpperCase()) {
			case "INITIATED":
				if (status.equalsIgnoreCase("APPROVED") || status.equalsIgnoreCase("REJECTED")
						|| status.equalsIgnoreCase("REFERBACK")) {
					return updateStatus1(jsonRequest, hastReqStatus, userId);
//					response.put("message", "status update");
//					response.put("status", "200 OK");
//					return response;
				}
				break;

			case "APPROVED":
				switch (status.toUpperCase()) {
				case "FINAL APPROVED":
					return updateStatus1(jsonRequest, hastReqStatus, userId);
//					response.put("message", "FINAL APPROVED");
//					response.put("status", "200 OK");
//					return response;

				case "PARTIALAPPROVED":
					return updateStatus1(jsonRequest, hastReqStatus, userId);
//					response = updateStatus1;
//					response.put("message", "PARTIALAPPROVED");
//					response.put("status", "200 OK");
//					return response;

				default:
					response.put("message", "APPROVED");
					response.put("status", "200 OK");
					return response;
				}

			case "REJECTED":
				response.put("message", "REJECTED");
				response.put("status", "200 OK");
				return response;

			case "REFERBACK":
				if (status.equalsIgnoreCase("RESUBMIT")) {
					updateStatus1(jsonRequest, hastReqStatus, userId);
					response.put("message", "RESUBMIT");
					response.put("status", "200 OK");
					return response;
				} else {
					response.put("message", "Error status");
					response.put("status", "200 OK");
					return response;
				}

			case "RESUBMIT":
				switch (status.toUpperCase()) {
				case "APPROVED":
					updateStatus1(jsonRequest, hastReqStatus, userId);
					response.put("message", "APPROVED");
					response.put("status", "200 OK");
					return response;

				case "REJECTED":
					updateStatus1(jsonRequest, hastReqStatus, userId);
					response.put("message", "REJECTED");
					response.put("status", "200 OK");
					return response;

				case "REFERBACK":
					updateStatus1(jsonRequest, hastReqStatus, userId);
					response.put("message", "REFERBACK");
					response.put("status", "200 OK");
					return response;

				default:
					response.put("message", "Error status");
					response.put("status", "200 OK");
					return response;
				}

			case "FINAL APPROVED":
				// Handle FINAL APPROVED case (if needed)
				break;

			case "PARTIALAPPROVED":
				switch (status.toUpperCase()) {
				case "APPROVED":
					return updateStatus1(jsonRequest, hastReqStatus, userId);
//					response.put("message", "APPROVED");
//					response.put("status", "200 OK");
//					return response;

				case "PARTIALAPPROVED":
					return updateStatus1(jsonRequest, hastReqStatus, userId);
//					response.put("message", "PARTIALAPPROVED");
//					response.put("status", "200 OK");
//					return response;

				default:
					response.put("message", "Error status");
					response.put("status", "200 OK");
					return response;
				}

			default:
				response.put("message", "Error status");
				response.put("status", "200 OK");
				return response;
			}
		}

		response.put("message", "missing Status field");
		response.put("status", "400 Bad Request");

		//
		return response;
	}

	public JSONObject updateStatus1(JSONObject jsonRequest, HastReqStatus hastReqStatus, String userId) {
		String applictioNO = jsonRequest.getJSONObject("Data").getString("applictioNO");
		String status = jsonRequest.getJSONObject("Data").getString("status");
		String empId = jsonRequest.getJSONObject("Data").getString("empId");
		String role = jsonRequest.getJSONObject("Data").getString("role");
		String remark = jsonRequest.getJSONObject("Data").getString("remark");
		LocalDateTime currentTime = LocalDateTime.now();
		JSONObject response = new JSONObject();
		HastUserLog hastuserlog = new HastUserLog();
		String getApproverByproxy = new String();

		if (role.equals("HAST_USERMAKER")) {
			reqstatusrepo.updateStatusReq(status, empId, applictioNO);
			response.put("message", "Status Updated");
			response.put("status", "200 OK");
			hastuserlog.setRemark(remark);
			hastuserlog.setEmpId(empId);
		} else if (role.equals("HAST_USERCHECKER")) {
			String approver1 = hastReqStatus.getApprover1();
			String approver2 = hastReqStatus.getApprover2();
			String approver3 = hastReqStatus.getApprover3();
			String approver4 = hastReqStatus.getApprover4();
			String approver5 = hastReqStatus.getApprover5();
			String[] approverArray = { approver1, approver2, approver3, approver4, approver5 };
			int countofNA = countOccurrences(approverArray, "NA");
			int numberOfapprover = 5 - countofNA;
			List<HastUserLog> userlogList = userlogrepo.fetchBystatusCount(applictioNO, "APPROVED");
			if (status.equals("APPROVED")) {
				if (userlogList.size() + 1 == numberOfapprover) {

					// LIMIT ENHANCEMENT
					if (hastReqStatus.getTransactionTypes()
							.equals(Constants.TransctionType.LIMIT_ENHANCEMENT_10L_TO_25L)
							|| hastReqStatus.getTransactionTypes()
									.equals(Constants.TransctionType.LIMIT_ENHANCEMENT_25L_TO_2CR)) {
						updateMoblieLimitAndEnhanceLimit(hastReqStatus);
					}

					// LINK_MOBLIE_NUMBER
					if (hastReqStatus.getTransactionTypes().equals(Constants.TransctionType.LINK_MOBLIE_NUMBER)) {
						String caseID = linkMobileToMultipleCIF(hastReqStatus.getMobileNo(), hastReqStatus.getCIF(),
								hastReqStatus.getLinkToCIF());
						response.put("caseId", caseID);
					}

					// SAVE IN DB
					reqstatusrepo.updateStatusApr("APPROVED", applictioNO);

					// SEND EMAIL TO OPS TEAM
					emailService.sendEmailToOpsTeam(applictioNO, status, hastReqStatus.getTransactionTypes());
				} else {
					reqstatusrepo.updateStatusApr("PARTIALAPPROVED", applictioNO);
				}
			} else if (status.equals("REFERBACK")) {
				userlogrepo.updateUserLogStatus("PENDING", applictioNO, "APPROVED");
				reqstatusrepo.updateStatusApr("REFERBACK", applictioNO);
			} else if (status.equals("REJECTED")) {
				reqstatusrepo.updateStatusApr("REJECTED", applictioNO);
			}
			response.put("message", "Status Updated");
			response.put("status", "200 OK");
			hastuserlog.setRemark(remark);
			hastuserlog.setEmpId(empId);
		} else if (role.equals("HAST_USERPROXY")) {
			List<HastProxyUser> proxyUser = proxyUserRepo.fetchApproverbyProxy(empId);
			if (!proxyUser.isEmpty()) {
				getApproverByproxy = proxyUser.get(0).getApproverUser();
			}
			List<HastReqStatus> list = reqstatusrepo.checkApplictionNo(applictioNO);
			String approver1 = list.get(0).getApprover1();
			String approver2 = list.get(0).getApprover2();
			String approver3 = list.get(0).getApprover3();
			String approver4 = list.get(0).getApprover4();
			String approver5 = list.get(0).getApprover5();
			String[] approverArray = { approver1, approver2, approver3, approver4, approver5 };
			int countofNA = countOccurrences(approverArray, "NA");
			int numberOfapprover = 5 - countofNA;
			List<HastUserLog> userlogList = userlogrepo.fetchBystatusCount(applictioNO, "APPROVED");
			if (status.equals("APPROVED")) {
				if (userlogList.size() + 1 == numberOfapprover) {
					reqstatusrepo.updateStatusApr("APPROVED", applictioNO);

					// LIMIT ENHANCEMENT
					if (hastReqStatus.getTransactionTypes()
							.equals(Constants.TransctionType.LIMIT_ENHANCEMENT_10L_TO_25L)
							|| hastReqStatus.getTransactionTypes()
									.equals(Constants.TransctionType.LIMIT_ENHANCEMENT_25L_TO_2CR)) {
						updateMoblieLimitAndEnhanceLimit(hastReqStatus);
					}

					// SEND EMAIL TO OPS TEAM
					emailService.sendEmailToOpsTeam(applictioNO, status, hastReqStatus.getTransactionTypes());
				} else {
					reqstatusrepo.updateStatusApr("PARTIALAPPROVED", applictioNO);
				}

			} else if (status.equals("REFERBACK")) {
				userlogrepo.updateUserLogStatus("PENDING", applictioNO, "APPROVED");
				reqstatusrepo.updateStatusApr("REFERBACK", applictioNO);
			} else if (status.equals("REJECTED")) {
				reqstatusrepo.updateStatusApr("REJECTED", applictioNO);
			}
			response.put("message", "Status Updated");
			response.put("status", "200 OK");
			hastuserlog.setRemark("(Proxy User: " + empId + ") " + remark);
			hastuserlog.setEmpId(getApproverByproxy);
		} else if (role.equals("HAST_USEROPS")) {
			reqstatusrepo.updateStatusOps(status, applictioNO);
			response.put("message", "Status Updated");
			response.put("status", "200 OK");
			hastuserlog.setRemark(remark);
			hastuserlog.setEmpId(empId);
		}
		hastuserlog.setApplictioNO(applictioNO);
		hastuserlog.setStatus(status);
		hastuserlog.setUpdateDate(currentTime);
		userlogrepo.save(hastuserlog);

		// WRONG ENTRY TAT_48_HOURS
		if (hastReqStatus.getTransactionTypes().equals(Constants.WRONG_ENTRY)
				&& hastReqStatus.getApprover1().equals(userId) && status.equalsIgnoreCase(Constants.APPROVED)) {
			String tat48Date = calculateTAT48(LocalDateTime.now());
			hastReqStatus.setTat48Date(tat48Date);
			hastReqStatus.setEmailSent(false);
			reqstatusrepo.save(hastReqStatus);
			logger.debug(
					"WRONG ENTRY TAT_48_HOURS ::" + hastReqStatus.getTat48Date() + "::" + hastReqStatus.getEmailSent());
		}

		// LIMIT INHANCEMENT EMAIL
		if ((hastReqStatus.getTransactionTypes().equals(Constants.TransctionType.LIMIT_ENHANCEMENT_10L_TO_25L)
				|| hastReqStatus.getTransactionTypes().equals(Constants.TransctionType.LIMIT_ENHANCEMENT_25L_TO_2CR))) {

			if (hastReqStatus.getApprover1().equals(userId) && !Constants.NA.equals(hastReqStatus.getApprover2())) {
				emailService.sendLimitEnhancementEmailToApprover(hastReqStatus.getApprover2(),
						hastReqStatus.getAccountNo(), hastReqStatus.getTransactionDescription(),
						"::sendLimitEnhancementEmailToApprover Approver 2 ::");

			} else if (hastReqStatus.getApprover2().equals(userId)
					&& !Constants.NA.equals(hastReqStatus.getApprover3())) {
				emailService.sendLimitEnhancementEmailToApprover(hastReqStatus.getApprover3(),
						hastReqStatus.getAccountNo(), hastReqStatus.getTransactionDescription(),
						"::sendLimitEnhancementEmailToApprover 3 ::");

			} else if (hastReqStatus.getApprover3().equals(userId)
					&& !Constants.NA.equals(hastReqStatus.getApprover4())) {
				emailService.sendLimitEnhancementEmailToApprover(hastReqStatus.getApprover4(),
						hastReqStatus.getAccountNo(), hastReqStatus.getTransactionDescription(),
						"::sendLimitEnhancementEmailToApprover 4 ::");

			} else if (hastReqStatus.getApprover4().equals(userId)
					&& !Constants.NA.equals(hastReqStatus.getApprover5())) {
				emailService.sendLimitEnhancementEmailToApprover(hastReqStatus.getApprover5(),
						hastReqStatus.getAccountNo(), hastReqStatus.getTransactionDescription(),
						"::sendLimitEnhancementEmailToApprover 5 ::");
			}
		}
		return response;
	}

	@Override
	public JSONArray fetchUserLog(JSONObject jsonRequest) {
		String applictioNO = jsonRequest.getJSONObject("Data").getString("applictioNO");
		List<HastUserLog> list = userlogrepo.fetchUserLog(applictioNO);
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			JSONObject innerData = new JSONObject();
			innerData.put("applictioNO", list.get(i).getApplictioNO());
			innerData.put("empId", list.get(i).getEmpId());
			innerData.put("empName", getEmpName(list.get(i).getEmpId()));
			innerData.put("remark", list.get(i).getRemark());
			innerData.put("status", list.get(i).getStatus());
			innerData.put("updateDate", list.get(i).getUpdateDate());
			array.put(innerData);
		}
		return array;
	}

	@Override
	public JSONObject saveAttachment(JSONObject jsonRequest, List<MultipartFile> files) {
		String applictioNO = jsonRequest.getJSONObject("Data").getString("applictioNO");
		String latitude = jsonRequest.getJSONObject("Data").getString("latitude");
		String longitude = jsonRequest.getJSONObject("Data").getString("longitude");
		for (MultipartFile file : files) {
			try {
				HastReqAttachment hastreqattachment = new HastReqAttachment();
				LocalDateTime currentTime = LocalDateTime.now();
				String contentType = file.getContentType();
				String[] parts = contentType.split("/");
				// String extension = parts.length > 1 ? parts[1] : "";
				String fileName = file.getOriginalFilename();
				if (isPdf(file.getBytes()) && fileName.endsWith(".pdf")
						|| isJpeg(file.getBytes()) && fileName.endsWith(".jpeg")
						|| isPng(file.getBytes()) && fileName.endsWith(".png")
						|| isXlsx(file.getBytes()) && fileName.endsWith(".xlsx")) {
					hastreqattachment.setAttachmentType(file.getContentType());
				} else {
					throw new UnsupportedOperationException("Unsupported content type: " + contentType);
				}
				hastreqattachment.setId(applictioNO + getApplicationId());
				hastreqattachment.setApplictioNO(applictioNO);
				hastreqattachment.setAttachmentName(file.getOriginalFilename());
				hastreqattachment.setAttachment(file.getBytes());
				hastreqattachment.setCreateDate(currentTime);
				hastreqattachment.setLatitude(latitude);
				hastreqattachment.setLongitude(longitude);
				reqattachmentrepo.save(hastreqattachment);
			} catch (IOException e) {
			}
		}
		JSONObject response = new JSONObject();
		response.put("message", files.size() + " Attachments saved successfully!");
		response.put("status", "200 OK");
		return response;
	}

	public JSONObject fetchAttachment(JSONObject jsonRequest) {
		JSONObject responseData = new JSONObject();
		String applictioNO = jsonRequest.getJSONObject("Data").getString("applictioNO");
		List<HastReqAttachment> list = reqattachmentrepo.fetchAttachment(applictioNO);

		// ByteArrayOutputStream to hold the ZIP file in memory
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try (ZipOutputStream zos = new ZipOutputStream(baos)) {
			for (HastReqAttachment hastReqAttachment : list) {
				String attachmentName = hastReqAttachment.getAttachmentName(); // e.g., "file.pdf"
				byte[] attachment = hastReqAttachment.getAttachment(); // Binary content of the file

				// Create a new ZipEntry for each file
				ZipEntry zipEntry = new ZipEntry(attachmentName);
				zos.putNextEntry(zipEntry);

				// Write the binary content to the ZIP file
				try (InputStream inputStream = new ByteArrayInputStream(attachment)) {
					byte[] buffer = new byte[1024];
					int len;
					while ((len = inputStream.read(buffer)) > 0) {
						zos.write(buffer, 0, len);
					}
				}

				// Close the current entry
				zos.closeEntry();
			}

			// Finish the ZIP file creation
			zos.finish();

		} catch (IOException e) {
			e.printStackTrace();
		}

		// Convert the ZIP file's byte array to a Base64 string
		String base64EncodedZip = Base64.getEncoder().encodeToString(baos.toByteArray());

		// Create the JSON response containing the Base64 encoded string
		JSONObject responseJson = new JSONObject();
		responseJson.put("file", base64EncodedZip);

		return responseJson;
	}

	@Override
	public JSONArray fetchStatusByEmpID(JSONObject jsonRequest) {
		String approver1 = jsonRequest.getJSONObject("Data").getString("approver1");
		String approver2 = jsonRequest.getJSONObject("Data").getString("approver2");
		String approver3 = jsonRequest.getJSONObject("Data").getString("approver3");
		String approver4 = jsonRequest.getJSONObject("Data").getString("approver4");
		String approver5 = jsonRequest.getJSONObject("Data").getString("approver5");
		String applictioNO = jsonRequest.getJSONObject("Data").getString("applictioNO");
		List<HastUserLog> list1 = userlogrepo.fetchStatusByEmpID(applictioNO, approver1);
		List<HastUserLog> list2 = userlogrepo.fetchStatusByEmpID(applictioNO, approver2);
		List<HastUserLog> list3 = userlogrepo.fetchStatusByEmpID(applictioNO, approver3);
		List<HastUserLog> list4 = userlogrepo.fetchStatusByEmpID(applictioNO, approver4);
		List<HastUserLog> list5 = userlogrepo.fetchStatusByEmpID(applictioNO, approver5);
		JSONArray array = new JSONArray();
		if (approver1.length() != 0 && !list1.isEmpty()) {
			JSONObject innerData1 = new JSONObject();
			innerData1.put("approver1", approver1);
			innerData1.put("approver1Name", getEmpName(approver1));
			innerData1.put("approver1status", list1.get(0).getStatus());
			innerData1.put("approver1updateDate", list1.get(0).getUpdateDate());
			array.put(innerData1);
		} else {
			JSONObject innerData1 = new JSONObject();
			innerData1.put("approver1", approver1);
			innerData1.put("approver1Name", getEmpName(approver1));
			innerData1.put("approver1status", "PENDING");
			innerData1.put("approver1updateDate", "NA");
			array.put(innerData1);
		}
		if (approver2.length() != 0 && !list2.isEmpty()) {
			JSONObject innerData1 = new JSONObject();
			innerData1.put("approver2", approver2);
			innerData1.put("approver2Name", getEmpName(approver2));
			innerData1.put("approver2status", list2.get(0).getStatus());
			innerData1.put("approver2updateDate", list2.get(0).getUpdateDate());
			array.put(innerData1);
		} else {
			JSONObject innerData1 = new JSONObject();
			innerData1.put("approver2", approver2);
			innerData1.put("approver2Name", getEmpName(approver2));
			innerData1.put("approver2status", "PENDING");
			innerData1.put("approver2updateDate", "NA");
			array.put(innerData1);
		}
		if (approver3.length() != 0 && !list3.isEmpty()) {
			JSONObject innerData1 = new JSONObject();
			innerData1.put("approver3", approver3);
			innerData1.put("approver3Name", getEmpName(approver3));
			innerData1.put("approver3status", list3.get(0).getStatus());
			innerData1.put("approver3updateDate", list3.get(0).getUpdateDate());
			array.put(innerData1);
		} else {
			JSONObject innerData1 = new JSONObject();
			innerData1.put("approver3", approver3);
			innerData1.put("approver3Name", getEmpName(approver3));
			innerData1.put("approver3status", "PENDING");
			innerData1.put("approver3updateDate", "NA");
			array.put(innerData1);
		}
		if (approver4.length() != 0 && !list4.isEmpty()) {
			JSONObject innerData1 = new JSONObject();
			innerData1.put("approver4", approver4);
			innerData1.put("approver4Name", getEmpName(approver4));
			innerData1.put("approver4status", list4.get(0).getStatus());
			innerData1.put("approver4updateDate", list4.get(0).getUpdateDate());
			array.put(innerData1);
		} else {
			JSONObject innerData1 = new JSONObject();
			innerData1.put("approver4", approver4);
			innerData1.put("approver4Name", getEmpName(approver4));
			innerData1.put("approver4status", "PENDING");
			innerData1.put("approver4updateDate", "NA");
			array.put(innerData1);
		}
		if (approver5.length() != 0 && !list5.isEmpty()) {
			JSONObject innerData1 = new JSONObject();
			innerData1.put("approver5", approver5);
			innerData1.put("approver5Name", getEmpName(approver5));
			innerData1.put("approver5status", list5.get(0).getStatus());
			innerData1.put("approver5updateDate", list5.get(0).getUpdateDate());
			array.put(innerData1);
		} else {
			JSONObject innerData1 = new JSONObject();
			innerData1.put("approver5", approver5);
			innerData1.put("approver5Name", getEmpName(approver5));
			innerData1.put("approver5status", "PENDING");
			innerData1.put("approver5updateDate", "NA");
			array.put(innerData1);
		}
		return array;
	}

	@Override
	public JSONObject reAssignApprover(JSONObject jsonRequest) {
		String applictioNO = jsonRequest.getJSONObject("Data").getString("applictioNO");
		String approverEmpId = jsonRequest.getJSONObject("Data").getString("approverEmpId");
		String status = "APPROVED";
		List<HastReqStatus> list = reqstatusrepo.checkApplictionNo(applictioNO);
		String approver1 = list.get(0).getApprover1();
		String approver2 = list.get(0).getApprover2();
		String approver3 = list.get(0).getApprover3();
		String approver4 = list.get(0).getApprover4();
		String approver5 = list.get(0).getApprover5();
		JSONObject response = new JSONObject();
		if (!isNumeric(approverEmpId)) {
			response.put("message", "Invalid Employee ID");
			response.put("status", "error");
			return response;
		}
		if (!approverEmpId.equals(null) && approverEmpId.length() <= 4) {
			response.put("message", "An employee ID must consist of 5 digits");
			response.put("status", "error");
			return response;
		} else {
			if (approverEmpId.equals(approver1) || approverEmpId.equals(approver2) || approverEmpId.equals(approver3)
					|| approverEmpId.equals(approver4) || approverEmpId.equals(approver5)) {
				response.put("message", "Employee Alredy Exist");
				response.put("status", "error");
				return response;
			}
			if (approver1.equals("NA")) {
				reqstatusrepo.reassignApprover1(approverEmpId, applictioNO, status);
				response.put("message", "Data updated");
				response.put("status", "200 OK");
			} else if (approver2.equals("NA")) {
				reqstatusrepo.reassignApprover2(approverEmpId, applictioNO, status);
				response.put("message", "Data updated");
				response.put("status", "200 OK");
			} else if (approver3.equals("NA")) {
				reqstatusrepo.reassignApprover3(approverEmpId, applictioNO, status);
				response.put("message", "Data updated");
				response.put("status", "200 OK");
			} else if (approver4.equals("NA")) {
				reqstatusrepo.reassignApprover4(approverEmpId, applictioNO, status);
				response.put("message", "Data updated");
				response.put("status", "200 OK");
			} else if (approver5.equals("NA")) {
				reqstatusrepo.reassignApprover5(approverEmpId, applictioNO, status);
				response.put("message", "Data updated");
				response.put("status", "200 OK");
			} else {
				response.put("message", "Approver MAX Limit is exceed");
				response.put("status", "error");
			}
		}
		return response;
	}

	@Override
	public JSONObject getAccountDetails(JSONObject Header, JSONObject request) {

		String accountNumber = request.getJSONObject("Data").getString("accountNumber");
		JSONObject sendResponse = null;
		URL obj = null;

		MHLGenerateProperty x = MHLGenerateProperty.getInstance();
		x.bypassssl();
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		try {
			obj = new URL(BASEURL + "fetch/mis/" + accountNumber + "?api_key=" + api_key);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) obj.openConnection();
		} catch (IOException e) {
			logger.debug("STEP 1 Exception occur while getAccountDetails ::" + e.getMessage());
		}
		try {
			con.setRequestMethod("GET");
		} catch (ProtocolException e) {
			logger.debug("STEP 1 Exception occur while getAccountDetails ::" + e.getMessage());
		}
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("X-Request-ID", "COR");
		con.setDoOutput(true);
		try {
			int responseCode = 0;
			try {
				responseCode = con.getResponseCode();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (responseCode == HttpURLConnection.HTTP_OK) {
				String response = null;
				try {
					response = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
					logger.debug("STEP 1 getAccountDetails Response  ::" + response);
				} catch (IOException e) {
					logger.debug("STEP 1 Exception occur while getAccountDetails ::" + e.getMessage());
					throw new NoSuchElementException("Exception occur while getAccountDetails ::" + e.getMessage());
				}
				return new JSONObject(response);
			} else {
				logger.debug("STEP 1 Exception occur while getAccountDetails ::" + responseCode);
				throw new IOException("GET request failed with code: " + responseCode);
			}
		} catch (IOException e) {
			logger.debug("STEP 1 Exception occur while getAccountDetails ::" + e.getMessage());
		} finally {
			con.disconnect();
		}
		logger.debug("STEP 1 Get AccountDetails Response ::" + sendResponse);
		return sendResponse;

	}

	public String getEmpName(String empId) {
		String empName = new String();
		List<HastUserName> nameList = usernamerepo.fetchNameByEmpid(empId);
		if (nameList.isEmpty()) {
			empName = "Name not found";
		} else {
			empName = nameList.get(0).getEmpName();
		}
		return empName;
	}

	public String getStateByEmpId(String empId) {
		String empState = new String();
		List<HastUserName> nameList = usernamerepo.fetchNameByEmpid(empId);
		if (nameList.isEmpty()) {
			empState = "Employee Data Not found";
		} else {
			empState = ((nameList.get(0).getEmpState() == null) ? null : nameList.get(0).getEmpState());
		}
		return empState;
	}

	public String getEmail(String empId) {
		String empName = new String();
		List<HastUserName> nameList = usernamerepo.fetchNameByEmpid(empId);
		if (nameList.isEmpty()) {
			empName = "Name not found";
		} else {
			empName = nameList.get(0).getEmpEmailid();
		}
		return empName;
	}

	@Override
	public JSONObject getApprovalOfProxy(JSONObject jsonRequest) {
		// logger.debug("Trigger Email Call Start");
		// triggeremail();
		// logger.debug("Trigger Email Call End");
		String loginEmpId = jsonRequest.getJSONObject("Data").getString("loginEmpId");
		String role = jsonRequest.getJSONObject("Data").getString("role");
		JSONObject response = new JSONObject();
		if (role.equals("HAST_USERPROXY")) {
			List<HastProxyUser> proxyUser = proxyUserRepo.fetchApproverbyProxy(loginEmpId);
			if (!proxyUser.isEmpty()) {
				response.put("ProxyUser", proxyUser.get(0).getProxyUser());
				response.put("ApproverUser", proxyUser.get(0).getApproverUser());
			}
		}
		return response;
	}

	public boolean isNumeric(String inupt) {
		// Check Input is number then True else False using regular expression.
		return inupt.matches("\\d+");
	}

	// Helper method to check for PDF using magic bytes
	private boolean isPdf(byte[] bytes) throws IOException {
		if (bytes.length < 4) {
			return false;
		}
		// Check the first four bytes for the PDF magic number (%.PDF)
		return bytes[0] == 0x25 && bytes[1] == 0x50 && bytes[2] == 0x44 && bytes[3] == 0x46;
	}

	public boolean isPng(byte[] fileBytes) {
		if (fileBytes == null || fileBytes.length < 8) {
			return false; // Insufficient bytes for PNG signature
		}

		// PNG magic bytes (first 8 bytes)
		byte[] pngMagicBytes = new byte[] { (byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47, (byte) 0x0D,
				(byte) 0x0A, (byte) 0x1A, (byte) 0x0A };

		// Check for magic bytes
		for (int i = 0; i < 8; i++) {
			if (fileBytes[i] != pngMagicBytes[i]) {
				return false;
			}
		}

		return true;
	}

	public boolean isXlsx(byte[] fileBytes) {
		if (fileBytes == null || fileBytes.length < 8) {
			return false; // Insufficient bytes for XLSX signature
		}

		// XLSX magic bytes (first 8 bytes)
		byte[] xlsxMagicBytes = new byte[] { (byte) 0x50, (byte) 0x4B, (byte) 0x03, (byte) 0x04 };

		// Check for magic bytes
		for (int i = 0; i < xlsxMagicBytes.length; i++) {
			if (fileBytes[i] != xlsxMagicBytes[i]) {
				return false;
			}
		}

		return true;
	}

	public static String getApplicationId() {
		String uniqueIdString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"))
				+ String.format("%03d", ThreadLocalRandom.current().nextInt(1000));
		return uniqueIdString;
	}

	public void updateMoblieLimitAndEnhanceLimit(HastReqStatus hastReqStatus) {
		String amount = hastReqStatus.getAmount();
		String transactionType = hastReqStatus.getTransactionTypes();
		String accountNo = hastReqStatus.getAccountNo();

		JSONObject request = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("accountNumber", accountNo);
		request.put("Data", data);
		// STEP :: 1 GET CIF
		JSONObject response = getAccountDetails(null, request);
		logger.debug("STEP 1 CUSTOMER DETAILS ::" + response);
		JSONObject responseData = response.optJSONObject("Data");
		if (responseData == null) {
			logger.debug("STEP 1 Customer details not found for the provided account number: " + accountNo + ".");
			throw new NoSuchElementException(
					"STEP 1 Customer details not found for the provided account number: " + accountNo + ".");
		}
		String cif = responseData.getString("CustomerID");
		System.out.println("STEP 1 CIF ::" + cif);
		logger.debug("STEP 1 CIF ::" + cif);
		// TESTING
		String dailyLimit = "";

		if (transactionType.equals(Constants.TransctionType.LIMIT_ENHANCEMENT_10L_TO_25L)) {
			dailyLimit = "2500000";
		} else if (transactionType.equals(Constants.TransctionType.LIMIT_ENHANCEMENT_25L_TO_2CR)) {
			dailyLimit = "20000000";
		}

		if (!dailyLimit.isEmpty()) {
			JSONObject result = new JSONObject();

			// STEP :: 2 UPDATE MOBILE NUMBER LIMIT
			result = updateMobileNumberLimit(cif, dailyLimit);
			String transactionStatus = result.getString("TransactionStatus");
			String transactionMessage = result.getString("TransactionMessage");
			System.out.println("STEP 2 Update Mobile Number Limit result ::" + result);
			logger.debug("STEP 2 Update Mobile Number Limit result ::" + result);
			if (!transactionStatus.equals("success")) {
				logger.debug("STEP 2 Exception occur while update mobileNumberLimit ::" + transactionMessage + " " + cif
						+ "!");
				throw new NoSuchElementException("STEP 2 Exception occur while update mobileNumberLimit ::"
						+ transactionMessage + " " + cif + "!");
			}

			// STEP :: 3 ENHANCE LIMIT
			result = enhanceLimit(accountNo, dailyLimit);
			System.out.println("RESULT ::" + result);
			String responseMessage = result.optJSONObject("Data").optString("ResponseMessage");
			System.out.println("STEP 3 Enhance Limit result ::" + result);
			logger.debug("STEP 3 Enhance Limit result ::" + result);
			if (!responseMessage.equalsIgnoreCase("SUCCESS")) {
				logger.debug("STEP 3 Exception occur while enhanceLimit ::" + responseMessage + "!");
				throw new NoSuchElementException(
						"STEP 3 Exception occur while enhanceLimit ::" + responseMessage + "!");
			}
		}
	}

	@Override
	public JSONObject updateMobileNumberLimit(String cif, String dailyLimit) {
		JSONObject request = new JSONObject();
		JSONObject requestData = new JSONObject();
		requestData.put("customerId", cif);
		requestData.put("dailyLimit", dailyLimit);
		requestData.put("maxLimit", dailyLimit); // Assuming maxLimit is the same as dailyLimit in your case
		request.put("Data", requestData);

		JSONObject jsonResponse = new JSONObject();
		try {
			SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial((chain, authType) -> true).build();
			CloseableHttpClient httpClient = HttpClientBuilder.create().setSSLContext(sslContext)
					.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
			// Create the POST request
			HttpPost post = new HttpPost(Constants.UAT.MB);

			// Set headers
			post.setHeader("Content-Type", "application/json");
			post.setHeader("api_key", Constants.UAT.API_KEY);

			// Add JSON body to the request
			StringEntity entity = new StringEntity(request.toString());
			post.setEntity(entity);

			// Execute the request and get the response
			HttpResponse response = httpClient.execute(post);
			String result = EntityUtils.toString(response.getEntity());

			// Parse and format the response body as JSON
			jsonResponse = new JSONObject(result);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error occurred while updating mobile number limit: " + e.getMessage());
		}

		return jsonResponse;
	}

	@Override
	public JSONObject enhanceLimit(String accountNumber, String limit) {
		JSONObject request = new JSONObject();
		JSONObject requestData = new JSONObject();
		requestData.put("AccountNumber", accountNumber);
		requestData.put("Limit", limit);
		request.put("Data", requestData);

		JSONObject jsonResponse = new JSONObject();
		try {
			SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial((chain, authType) -> true).build();
			CloseableHttpClient httpClient = HttpClientBuilder.create().setSSLContext(sslContext)
					.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();

			// Create the POST request
			HttpPost postRequest = new HttpPost(Constants.UAT.ENHANCE);

			// Set headers
			postRequest.setHeader("accept", "application/json");
			postRequest.setHeader("Content-Type", "application/json");
			postRequest.setHeader("X-Request-ID", "COR");
			postRequest.setHeader("api_key", Constants.UAT.API_KEY);

			// Add JSON body to the request
			StringEntity entity = new StringEntity(request.toString());
			postRequest.setEntity(entity);

			// Execute the request and get the response
			HttpResponse response = httpClient.execute(postRequest);
			String result = EntityUtils.toString(response.getEntity());

			// Parse and return the response body as JSON
			jsonResponse = new JSONObject(result);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error occurred while enhancing limit: " + e.getMessage());
		}

		return jsonResponse;
	}

	@Override
	public String linkMobileToMultipleCIF(String moblieNo, String existingCIF, String linkToCIF) {
		logger.debug("linkMobileToMultipleCIF :: moblieNo :: " + moblieNo + " :: existingCIF :: " + existingCIF
				+ " :: linkToCIF :: " + linkToCIF);
		// Validation
		if (moblieNo == null || moblieNo.isEmpty() || existingCIF == null || existingCIF.isEmpty() || linkToCIF == null
				|| linkToCIF.isEmpty()) {
			throw new DuplicateEntryException(
					"moblieNo or existingCIF or linkToCIF null or empty in Link Mobile Number!");
		}

		JSONObject request = new JSONObject();
		JSONObject data = new JSONObject();
		String caseID = "";
		// Setting up the "Data" object
		data.put("Status", "U");
		data.put("Type", "CREATE");
		data.put("Category", "Update/Change in Customer A/C");
		data.put("Comments", "Interest cert Issue");
		data.put("TempID", "1");
		data.put("Channel", "IBR");
		data.put("SubCategory", "Link Mobile Number to Multiple CIF");

		// Custom fields array
		JSONArray customFields = new JSONArray();
		customFields.put(new JSONObject().put("Name", "Mobile Number").put("Value", moblieNo));
		customFields.put(new JSONObject().put("Name", "Existing CIF").put("Value", existingCIF));
		customFields.put(new JSONObject().put("Name", "Link To CIF").put("Value", linkToCIF));
		data.put("CustomField", customFields);

		data.put("UserCode", "S1961");
		data.put("Severity", "Normal");
		data.put("CustomerComment", "");
		data.put("File", new JSONArray().put(new JSONObject()));
		data.put("Subject", "Complaint");

		request.put("Data", data);

		JSONObject jsonResponse = new JSONObject();
		try {
			// Set up SSL context to trust all certificates
			SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial((chain, authType) -> true).build();
			CloseableHttpClient httpClient = HttpClientBuilder.create().setSSLContext(sslContext)
					.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();

			// Create POST request
			HttpPost postRequest = new HttpPost(Constants.UAT.LINK_TO_CIF);

			// Set headers
			postRequest.setHeader("X-Request-ID", "IEXCEED");
			postRequest.setHeader("X-User-ID", "S7171");
			postRequest.setHeader("X-From-ID", "CB");
			postRequest.setHeader("X-To-ID", "IEXCEED");
			postRequest.setHeader("X-Transaction-ID", "EabeDcEE-db3c-BddD-CbD7-4bAA992c75d4");
			postRequest.setHeader("Content-Type", "application/json");

			// Add JSON body to the request
			StringEntity entity = new StringEntity(request.toString());
			postRequest.setEntity(entity);

			// Execute request and get the response
			HttpResponse response = httpClient.execute(postRequest);
			String result = EntityUtils.toString(response.getEntity());
			System.out.println(result);
			// Parse and return the response as JSON
			jsonResponse = new JSONObject(result);
			caseID = jsonResponse.getJSONObject("Data").getString("CaseID");

		} catch (Exception e) {
			logger.debug("Exception occur during link mobile to multiple CIF ::" + e.getMessage());
			throw new DuplicateEntryException("Exception occur during link mobile to multiple CIF ::" + e.getMessage());
		}
		logger.debug("Link mobile to multiple CIF Response ::" + caseID);
		return caseID;
	}

	public boolean isJpeg(byte[] fileBytes) {
		if (fileBytes == null || fileBytes.length < 4) {
			return false; // Insufficient bytes for JPEG signature
		}

		// JPEG magic bytes (start with 0xFF, 0xD8 and end with 0xFF, 0xD9)
		return (fileBytes[0] == (byte) 0xFF && fileBytes[1] == (byte) 0xD8)
				&& (fileBytes[fileBytes.length - 2] == (byte) 0xFF && fileBytes[fileBytes.length - 1] == (byte) 0xD9);
	}
}