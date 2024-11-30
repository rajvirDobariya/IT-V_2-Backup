package com.suryoday.hastakshar.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suryoday.connector.rd.util.AppzillonAESUtils;
import com.suryoday.connector.service.UserService;
import com.suryoday.hastakshar.pojo.HastMaster;
import com.suryoday.hastakshar.repository.MasterRepo;
import com.suryoday.hastakshar.serviceImpl.ReqStatusServiceImpl;

@RestController
@RequestMapping("/hastakshar")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class FetchHastMasterDataEncy {

	private static Logger logger = LoggerFactory.getLogger(FetchHastMasterDataEncy.class);
	@Autowired
	private MasterRepo masterrepo;
	@Autowired
	UserService userService;
	@Autowired
	ReqStatusServiceImpl reqstatusserviceimpl;

	@PostMapping(value = "/fetchTxnTypeEncy", produces = "application/json")
	public ResponseEntity<Object> fetchTxnTypeEncy(
			@RequestHeader(name = "department", required = true) String department,
			@RequestHeader(name = "X-Correlation-ID", required = true) String X_CORRELATION_ID,
			@RequestHeader(name = "X-User-ID", required = true) String X_User_ID,
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request) {
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String data = "";
			String key = X_Session_ID;
			String State = reqstatusserviceimpl.getStateByEmpId(X_User_ID);
			List<String> TxnTypeList;
			if (department.equals("Business Team")) {
				TxnTypeList = masterrepo.fetchTxnTypewithOutState(department);
			} else {
				TxnTypeList = masterrepo.fetchTxnTypewithState(department, State);
			}
			org.json.simple.JSONObject outerData = new org.json.simple.JSONObject();
			outerData.put("Data", TxnTypeList);
			if (outerData.isEmpty()) {
				return new ResponseEntity("InValid Category ID", HttpStatus.BAD_REQUEST);
			}
			data = outerData.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
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
			@RequestHeader(name = "X-Session-ID", required = true) String X_Session_ID,
			@RequestHeader(name = "X-Request-ID", required = true) String X_Request_ID, HttpServletRequest request) {
		boolean sessionId = userService.validateSessionId(X_Session_ID, request);
		if (sessionId == true) {
			userService.getSessionId(X_User_ID, request);
			String data = "";
			String key = X_Session_ID;
			String State = reqstatusserviceimpl.getStateByEmpId(X_User_ID);
			List<HastMaster> TxnTypeList = masterrepo.fetchRowDetails(transactionTypes, State);
			JSONObject dataJson = new JSONObject();
			for (int i = 0; i < TxnTypeList.size(); i++) {
				dataJson.put("id", TxnTypeList.get(i).getId());
				dataJson.put("transactionTypes", TxnTypeList.get(i).getTransactionTypes());
				dataJson.put("transactionDescription", TxnTypeList.get(i).getTransactionDescription());
				dataJson.put("nature", TxnTypeList.get(i).getNature());
				dataJson.put("product", TxnTypeList.get(i).getProduct());
				dataJson.put("approver1", TxnTypeList.get(i).getApprover1());
				dataJson.put("approver2", TxnTypeList.get(i).getApprover2());
				dataJson.put("approver3", TxnTypeList.get(i).getApprover3());
				dataJson.put("approver4", TxnTypeList.get(i).getApprover4());
				dataJson.put("approver5", TxnTypeList.get(i).getApprover5());
				dataJson.put("requestflow", TxnTypeList.get(i).getRequestFlow());
			}
			org.json.simple.JSONObject outerData = new org.json.simple.JSONObject();
			outerData.put("Data", dataJson);
			if (outerData.isEmpty()) {
				return new ResponseEntity("InValid Category ID", HttpStatus.BAD_REQUEST);
			}
			data = outerData.toString();
			String encryptString2 = AppzillonAESUtils.encryptStringtoContainer(data, key);
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
}
