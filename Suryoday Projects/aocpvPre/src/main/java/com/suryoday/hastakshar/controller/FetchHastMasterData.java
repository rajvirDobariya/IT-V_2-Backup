package com.suryoday.hastakshar.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
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

import com.suryoday.hastakshar.pojo.HastMaster;
import com.suryoday.hastakshar.pojo.HastProductType;
import com.suryoday.hastakshar.repository.MasterRepo;
import com.suryoday.hastakshar.repository.ProductTypeRepo;
import com.suryoday.hastakshar.serviceImpl.ReqStatusServiceImpl;

@RestController
@RequestMapping("/hastakshar")
@SuppressWarnings("unchecked")
public class FetchHastMasterData {

	private static Logger logger = LoggerFactory.getLogger(FetchHastMasterData.class);

	@Value("${Approval}")
	private String approval;

	@Autowired
	MasterRepo masterrepo;
	@Autowired
	ReqStatusServiceImpl reqstatusserviceimpl;
	@Autowired
	private ProductTypeRepo productTypeRepo;

	@PostMapping(value = "/fetchDepartment", produces = "application/json")
	public ResponseEntity<Object> fetchDepartment(HttpServletRequest req) {
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
		JSONObject jsonResp = new JSONObject();
		jsonResp.put("Data", dataJson);
		if (!jsonResp.equals(null)) {
			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (jsonResp.has("Data")) {
				h = HttpStatus.OK;
			} else if (jsonResp.has("Description")) {
				h = HttpStatus.BAD_REQUEST;
			}
			logger.debug("Main Response : " + jsonResp.toString());
			return new ResponseEntity<>(jsonResp.toString(), h);
		} else {
			logger.debug("GATEWAY_TIMEOUT");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@PostMapping(value = "/fetchKeyword", produces = "application/json")
	public ResponseEntity<Object> fetchKeyword(
			@RequestHeader(name = "transactionType", required = true) String transactionType, HttpServletRequest req) {
		List<String> keyword = masterrepo.fetchKeyword(transactionType);
		org.json.simple.JSONObject jsonResp = new org.json.simple.JSONObject();
		jsonResp.put("Data", keyword);
		String removeUnwanted = new String(jsonResp.toString());
		String response = new String(removeUnwanted.replace("\\r\\n", ""));
		JSONObject jsonResponse = new JSONObject(response);
		if (!jsonResponse.equals(null)) {
			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (jsonResponse.has("Data")) {
				h = HttpStatus.OK;
			} else if (jsonResponse.has("Description")) {
				h = HttpStatus.BAD_REQUEST;
			}
			logger.debug("Main Response : " + jsonResponse.toString());
			return new ResponseEntity<>(jsonResponse.toString(), h);
		} else {
			logger.debug("GATEWAY_TIMEOUT");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@PostMapping(value = "/fetchTxnType", produces = "application/json")
	public ResponseEntity<Object> fetchTxnType(@RequestHeader(name = "department", required = true) String department,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID, HttpServletRequest req) {
		String State = reqstatusserviceimpl.getStateByEmpId(X_User_ID);
		List<String> TxnTypeList;
		if (department.equals("Business Team")) {
			TxnTypeList = masterrepo.fetchTxnTypewithOutState(department);
		} else {
			TxnTypeList = masterrepo.fetchTxnTypewithState(department, State);
		}
		org.json.simple.JSONObject jsonResp = new org.json.simple.JSONObject();
		jsonResp.put("Data", TxnTypeList);
		String removeUnwanted = new String(jsonResp.toString());
		String response = new String(removeUnwanted.replace("\\r\\n", ""));
		JSONObject jsonResponse = new JSONObject(response);
		if (!jsonResponse.equals(null)) {
			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (jsonResponse.has("Data")) {
				h = HttpStatus.OK;
			} else if (jsonResponse.has("Description")) {
				h = HttpStatus.BAD_REQUEST;
			}
			logger.debug("Main Response : " + jsonResponse.toString());
			return new ResponseEntity<>(jsonResponse.toString(), h);
		} else {
			logger.debug("GATEWAY_TIMEOUT");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@PostMapping(value = "/fetchRowDetails", produces = "application/json")
	public ResponseEntity<Object> fetchRowDetails(
			@RequestHeader(name = "transactionTypes", required = true) String transactionTypes,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID, HttpServletRequest req) {
		String State = reqstatusserviceimpl.getStateByEmpId(X_User_ID);
		List<HastMaster> TxnTypeList = masterrepo.fetchRowDetails(transactionTypes, State);
		System.out.println(TxnTypeList.size());
		JSONArray keyword = new JSONArray();

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
			if (TxnTypeList.get(i) != null && TxnTypeList.get(i).getKeyword() != null) {
				if (TxnTypeList.get(i).getKeyword().equalsIgnoreCase("") || TxnTypeList.get(i).getKeyword().isEmpty()
						|| TxnTypeList.get(i).getKeyword() == null) {

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
		JSONObject jsonResp = new JSONObject();
		jsonResp.put("Data", dataJson);
		if (!jsonResp.equals(null)) {
			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (jsonResp.has("Data")) {
				h = HttpStatus.OK;
			} else if (jsonResp.has("Description")) {
				h = HttpStatus.BAD_REQUEST;
			}
			logger.debug("Main Response : " + jsonResp.toString());
			return new ResponseEntity<>(jsonResp.toString(), h);
		} else {
			logger.debug("GATEWAY_TIMEOUT");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@PostMapping(value = "/fetchRowDetailsOps", produces = "application/json")
	public ResponseEntity<Object> fetchRowDetailsOps(
			@RequestHeader(name = "transactionTypes", required = true) String transactionTypes,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID, HttpServletRequest req) {
		String State = reqstatusserviceimpl.getStateByEmpId(X_User_ID);
		List<HastMaster> TxnTypeList = masterrepo.fetchRowDetails(transactionTypes, State);
		JSONArray keyword = new JSONArray();

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
		JSONObject jsonResp = new JSONObject();
		jsonResp.put("Data", dataJson);
		if (!jsonResp.equals(null)) {
			HttpStatus h = HttpStatus.BAD_GATEWAY;
			if (jsonResp.has("Data")) {
				h = HttpStatus.OK;
			} else if (jsonResp.has("Description")) {
				h = HttpStatus.BAD_REQUEST;
			}
			logger.debug("Main Response : " + jsonResp.toString());
			return new ResponseEntity<>(jsonResp.toString(), h);
		} else {
			logger.debug("GATEWAY_TIMEOUT");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	@PostMapping(value = "/fetchProductType", produces = "application/json")
	public ResponseEntity<Object> fetchProductType(@RequestBody String requestBody, HttpServletRequest req) {
		JSONObject jsonRequest = new JSONObject(requestBody);

		String productCode = jsonRequest.getJSONObject("Data").getString("SchemeCode");
		String transactionType = jsonRequest.getJSONObject("Data").getString("transactionType");

		JSONObject outerData = new JSONObject();
		HastProductType productType = null;

		if (isAlphaNumeric(productCode)) {
			productType = productTypeRepo.fetchProductTypeByApproval(approval);

		} else {
			productType = productTypeRepo.fetchProductTypeByProductCode(productCode);
		}
		JSONObject productTypeJson = new JSONObject(productType);
		// 10-10-2024
		productTypeJson = new JSONObject();
		outerData.put("Data", productTypeJson);

		if (!outerData.equals(null)) {
			logger.debug("Main Response : " + outerData.toString());
			return new ResponseEntity<>(outerData.toString(), HttpStatus.OK);
		} else {
			logger.debug("GATEWAY_TIMEOUT");
			return new ResponseEntity<Object>("timeout", HttpStatus.GATEWAY_TIMEOUT);
		}
	}

	// regex pattern to check productCode is AlphaNumeric
	public static boolean isAlphaNumeric(String str) {
		return str.matches("^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z0-9]+$");
	}
}
