package com.suryoday.hastakshar.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.connector.rd.util.Crypt;
import com.suryoday.connector.service.UserService;
import com.suryoday.hastakshar.pojo.HastMaster;
import com.suryoday.hastakshar.pojo.HastProductType;
import com.suryoday.hastakshar.repository.MasterRepo;
import com.suryoday.hastakshar.repository.ProductTypeRepo;
import com.suryoday.hastakshar.serviceImpl.ReqStatusServiceImpl;

@RestController
@RequestMapping("/hastakshar/web")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class FetchHastMasterDataWebEncy {

	@Value("${Approval}")
	private String approval;

	private static Logger logger = LoggerFactory.getLogger(FetchHastMasterDataWebEncy.class);
	@Autowired
	private MasterRepo masterrepo;
	@Autowired
	UserService userService;
	@Autowired
	ReqStatusServiceImpl reqstatusserviceimpl;
	@Autowired
	private ProductTypeRepo productTypeRepo;

	@PostMapping(value = "/fetchDepartmentEncy", produces = "application/json")
	public ResponseEntity<Object> fetchDepartment(
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_Encode_ID, HttpServletRequest request)
			throws Exception {

		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		// boolean sessionId=true;
		if (sessionId == true) {
			String data = "";
			List<String> departmentList = masterrepo.fetchDepartment();

			List<JSONObject> keywordJsonArray = new ArrayList<>();

			JSONObject dataJson = new JSONObject();
			for (int i = 0; i < departmentList.size(); i++) {
				List<String> keywordList = Arrays.asList(departmentList.get(i));
				JSONObject keywordObject = new JSONObject();
				for (String key : keywordList) {
					keywordObject.put("code", key.trim());
					keywordObject.put("Value", key.trim());
					keywordJsonArray.add(keywordObject);
					dataJson.put("department", keywordJsonArray);
				}
			}
			org.json.simple.JSONObject outerData = new org.json.simple.JSONObject();
			outerData.put("Data", dataJson);
			if (outerData.isEmpty()) {
				return new ResponseEntity("InValid Category ID", HttpStatus.BAD_REQUEST);
			}
			data = outerData.toString();
			String encryptString2 = Crypt.encrypt(data, X_Encode_ID);
			JSONObject data2 = new JSONObject();
			data2.put("value", encryptString2);
			JSONObject data3 = new JSONObject();
			data3.put("Data", data2);
			logger.debug("response : " + data3.toString());
			return new ResponseEntity<>(data3.toString(), HttpStatus.OK);
		} else {
			JSONObject data2 = new JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			JSONObject data3 = new JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping(value = "/fetchTxnTypeEncy", produces = "application/json")
	public ResponseEntity<Object> fetchTxnTypeEncy(
			@RequestHeader(name = "department", required = true) String department,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_Encode_ID, HttpServletRequest request)
			throws Exception {
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		// boolean sessionId=true;
		if (sessionId == true) {
			String data = "";
			String State = reqstatusserviceimpl.getStateByEmpId(X_User_ID);
			List<String> TxnTypeList;
			logger.info("Received fetchTxnTypeEncy request with department: {}", department);

			if (department.equals("Business Team")) {
				logger.info("Fetching transaction types without state");
				TxnTypeList = masterrepo.fetchTxnTypewithOutState(department);
				logger.info("Received resultset: {}", TxnTypeList.toString());
			} else {
				logger.info("Fetching transaction types with state");
				TxnTypeList = masterrepo.fetchTxnTypewithState(department, State);
				logger.info("Received resultset: {}", TxnTypeList.toString());
			}
			org.json.simple.JSONObject outerData = new org.json.simple.JSONObject();
			outerData.put("Data", TxnTypeList);
			if (outerData.isEmpty()) {
				return new ResponseEntity("InValid Category ID", HttpStatus.BAD_REQUEST);
			}
			data = outerData.toString();
			String encryptString2 = Crypt.encrypt(data, X_Encode_ID);
			JSONObject data2 = new JSONObject();
			data2.put("value", encryptString2);
			JSONObject data3 = new JSONObject();
			data3.put("Data", data2);
			logger.debug("response : " + data3.toString());
			return new ResponseEntity<>(data3.toString(), HttpStatus.OK);
		} else {
			JSONObject data2 = new JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			JSONObject data3 = new JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping(value = "/fetchKeywordEncy", produces = "application/json")
	public ResponseEntity<Object> fetchKeywordEncy(
			@RequestHeader(name = "department", required = true) String transactionType,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_Encode_ID, HttpServletRequest request)
			throws Exception {
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
//		boolean sessionId=true;
		if (sessionId == true) {
			String data = "";
			List<String> TxnTypeList = masterrepo.fetchKeyword(transactionType);
			org.json.simple.JSONObject outerData = new org.json.simple.JSONObject();
			outerData.put("Data", TxnTypeList);
			if (outerData.isEmpty()) {
				return new ResponseEntity("InValid Category ID", HttpStatus.BAD_REQUEST);
			}
			data = outerData.toString();
			String encryptString2 = Crypt.encrypt(data, X_Encode_ID);
			JSONObject data2 = new JSONObject();
			data2.put("value", encryptString2);
			JSONObject data3 = new JSONObject();
			data3.put("Data", data2);
			logger.debug("response : " + data3.toString());
			return new ResponseEntity<>(data3.toString(), HttpStatus.OK);
		} else {
			JSONObject data2 = new JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			JSONObject data3 = new JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping(value = "/fetchRowDetailsEncyOps", produces = "application/json")
	public ResponseEntity<Object> fetchRowDetailsEncyOps(
			@RequestHeader(name = "transactionTypes", required = true) String transactionTypes,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_Encode_ID, HttpServletRequest request)
			throws Exception {
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		// boolean sessionId =true;
		if (sessionId == true) {
			String data = "";
			String State = reqstatusserviceimpl.getStateByEmpId(X_User_ID);
			List<HastMaster> TxnTypeList = masterrepo.fetchRowDetails(transactionTypes, State);
			JSONObject dataJson = new JSONObject();
			for (int i = 0; i < TxnTypeList.size(); i++) {
				// dataJson.put("id", TxnTypeList.get(i).getId());
				// dataJson.put("transactionTypes", TxnTypeList.get(i).getTransactionTypes());
				// dataJson.put("transactionDescription",
				// TxnTypeList.get(i).getTransactionDescription());
				// dataJson.put("nature", TxnTypeList.get(i).getNature());
				// dataJson.put("product", TxnTypeList.get(i).getProduct());
				// dataJson.put("approver1", TxnTypeList.get(i).getApprover1());
				// dataJson.put("approver2", TxnTypeList.get(i).getApprover2());
				// dataJson.put("approver3", TxnTypeList.get(i).getApprover3());
				// dataJson.put("approver4", TxnTypeList.get(i).getApprover4());
				// dataJson.put("approver5", TxnTypeList.get(i).getApprover5());
				// dataJson.put("requestflow", TxnTypeList.get(i).getRequestFlow());
				List<JSONObject> keywordJsonArray = new ArrayList<>();
				if (TxnTypeList.get(i) != null && TxnTypeList.get(i).getKeyword() != null) {
					if (TxnTypeList.get(i).getKeyword().equalsIgnoreCase("")
							|| TxnTypeList.get(i).getKeyword().isEmpty()) {

						System.out.println(TxnTypeList.get(i).getKeyword());
					} else {
						List<String> keywordList = Arrays.asList(TxnTypeList.get(i).getKeyword().split(","));
						System.out.println(keywordList.size() + "*************************************");

						if (keywordList.size() == 0) {
							System.out.println("blank");
						} else {
							System.out.println("************Insside loop");
							JSONObject keywordObject = new JSONObject();

							for (String key : keywordList) {
								keywordObject.put("code", key.trim());
								keywordObject.put("Value", key.trim());
								keywordJsonArray.add(keywordObject);
								dataJson.put("keyword", keywordJsonArray);
							}

						}

					}
				}
			}
			org.json.simple.JSONObject outerData = new org.json.simple.JSONObject();
			outerData.put("Data", dataJson);
			if (outerData.isEmpty()) {
				return new ResponseEntity("InValid Category ID", HttpStatus.BAD_REQUEST);
			}
			data = outerData.toString();
			String encryptString2 = Crypt.encrypt(data, X_Encode_ID);
			JSONObject data2 = new JSONObject();
			data2.put("value", encryptString2);
			JSONObject data3 = new JSONObject();
			data3.put("Data", data2);
			logger.debug("response : " + data3.toString());
			return new ResponseEntity<>(data3.toString(), HttpStatus.OK);
		} else {
			JSONObject data2 = new JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			JSONObject data3 = new JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping(value = "/fetchRowDetailsEncy", produces = "application/json")
	public ResponseEntity<Object> fetchRowDetailsEncy(
			@RequestHeader(name = "transactionTypes", required = true) String transactionTypes,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_Encode_ID, HttpServletRequest request)
			throws Exception {
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		// boolean sessionId =true;
		if (sessionId == true) {
			String data = "";
			String State = reqstatusserviceimpl.getStateByEmpId(X_User_ID);
			List<HastMaster> TxnTypeList = masterrepo.fetchRowDetails(transactionTypes, State);
			JSONObject dataJson = new JSONObject();
			for (int i = 0; i < TxnTypeList.size(); i++) {
				dataJson.put("id", TxnTypeList.get(i).getId());
				dataJson.put("transactionTypes", TxnTypeList.get(i).getTransactionTypes());
				dataJson.put("transactionDescription", TxnTypeList.get(i).getTransactionDescription());
				dataJson.put("transactionDescriptionV2", TxnTypeList.get(i).getTransactionDescriptionV2());
				dataJson.put("nature", TxnTypeList.get(i).getNature());
				dataJson.put("product", TxnTypeList.get(i).getProduct());
				dataJson.put("approver1", TxnTypeList.get(i).getApprover1());
				dataJson.put("approver2", TxnTypeList.get(i).getApprover2());
				dataJson.put("approver3", TxnTypeList.get(i).getApprover3());
				dataJson.put("approver4", TxnTypeList.get(i).getApprover4());
				dataJson.put("approver5", TxnTypeList.get(i).getApprover5());
				dataJson.put("requestflow", TxnTypeList.get(i).getRequestFlow());
				List<JSONObject> keywordJsonArray = new ArrayList<>();
				if (TxnTypeList.get(i).getKeyword().equalsIgnoreCase("") || TxnTypeList.get(i).getKeyword().isEmpty()) {

					System.out.println(TxnTypeList.get(i).getKeyword());
				} else {
					List<String> keywordList = Arrays.asList(TxnTypeList.get(i).getKeyword().split(","));
					System.out.println(keywordList.size() + "*************************************");

					if (keywordList.size() == 0) {
						System.out.println("blank");
					} else {
						System.out.println("************Insside loop");
						JSONObject keywordObject = new JSONObject();

						for (String key : keywordList) {
							keywordObject.put("code", key.trim());
							keywordObject.put("Value", key.trim());
							keywordJsonArray.add(keywordObject);
							dataJson.put("keyword", keywordJsonArray);
						}

					}

				}

			}
			org.json.simple.JSONObject outerData = new org.json.simple.JSONObject();
			outerData.put("Data", dataJson);
			if (outerData.isEmpty()) {
				return new ResponseEntity("InValid Category ID", HttpStatus.OK);
			}
			data = outerData.toString();
			String encryptString2 = Crypt.encrypt(data, X_Encode_ID);
			JSONObject data2 = new JSONObject();
			data2.put("value", encryptString2);
			JSONObject data3 = new JSONObject();
			data3.put("Data", data2);
			logger.debug("response : " + data3.toString());
			return new ResponseEntity<>(data3.toString(), HttpStatus.OK);
		} else {
			JSONObject data2 = new JSONObject();
			data2.put("value", "SessionId is expired or Invalid sessionId");
			JSONObject data3 = new JSONObject();
			data3.put("Error", data2);
			logger.debug("SessionId is expired or Invalid sessionId");
			return new ResponseEntity<Object>(data3.toString(), HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping(value = "/fetchProductTypeEncy", produces = "application/json")
	public ResponseEntity<Object> fetchProductType(@RequestBody String requestBody,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Encode-ID", required = true) String X_Encode_ID, HttpServletRequest request)
			throws Exception {
		org.json.JSONObject encryptJSONObject = new org.json.JSONObject(requestBody);
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		// boolean sessionId = true;
		if (sessionId == true) {
			String data = "";
			String encryptString = encryptJSONObject.getJSONObject("Data").getString("value");
			String decryptContainerString = Crypt.decrypt(encryptString, X_Encode_ID);
			JSONObject jsonObject = new JSONObject(decryptContainerString);
			String productCode = jsonObject.getJSONObject("Data").getString("SchemeCode");
			String transactionType = jsonObject.getJSONObject("Data").getString("transactionType");
			HastProductType productType = null;

			if (isAlphaNumeric(productCode)) {
				productType = productTypeRepo.fetchProductTypeByApproval(approval);

			} else {
				productType = productTypeRepo.fetchProductTypeByProductCode(productCode);
			}
			logger.debug("productType: " + productType.toString());
			JSONObject productTypeJson = new JSONObject(productType);
			// 10-10-2024
			productTypeJson = new JSONObject();
			org.json.simple.JSONObject outerData = new org.json.simple.JSONObject();
			outerData.put("Data", productTypeJson);
			data = outerData.toString();
			String encryptString2 = Crypt.encrypt(data, X_Encode_ID);
			org.json.JSONObject data2 = new org.json.JSONObject();
			data2.put("value", encryptString2);
			org.json.JSONObject data3 = new org.json.JSONObject();
			data3.put("Data", data2);

			if (!data3.equals(null)) {
				logger.debug("Main Response : " + data3.toString());
				return new ResponseEntity<>(data3.toString(), HttpStatus.OK);
			} else {
				logger.debug("GATEWAY_TIMEOUT");
				return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
			}
		}
		return new ResponseEntity("InValid SchemeCode", HttpStatus.BAD_REQUEST);
	}

	// regex pattern to check productCode is AlphaNumeric
	public static boolean isAlphaNumeric(String str) {
		return str.matches("^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z0-9]+$");
	}

}
