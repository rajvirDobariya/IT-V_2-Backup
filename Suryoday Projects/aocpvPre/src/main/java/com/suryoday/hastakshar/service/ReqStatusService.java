package com.suryoday.hastakshar.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.hastakshar.pojo.HastReqStatus;

@Service
public interface ReqStatusService {

	public JSONObject saveNewRequestData(JSONObject jsonRequest, String userId);

	public JSONArray fetchList(JSONObject jsonRequest);

	public JSONArray fetchBySearch(JSONObject jsonRequest);

	public JSONArray fetchByDate(JSONObject jsonRequest);

	public JSONArray fetchByStatus(JSONObject jsonRequest);

	public JSONObject updateStatus(JSONObject jsonRequest, String userId);

	public JSONArray fetchUserLog(JSONObject jsonRequest);

	public JSONObject saveAttachment(JSONObject jsonRequest, List<MultipartFile> file);

	public JSONArray fetchStatusByEmpID(JSONObject jsonRequest);

	public JSONObject reAssignApprover(JSONObject jsonRequest);

	public JSONObject getApprovalOfProxy(JSONObject jsonRequest);

	public JSONObject getAccountDetails(JSONObject Header, JSONObject requestBody);

	public JSONObject fetchAttachment(JSONObject jsonRequest);

	public void updateMoblieLimitAndEnhanceLimit(HastReqStatus hastReqStatus);

	public JSONObject updateMobileNumberLimit(String cif, String dailyLimit);

	public JSONObject enhanceLimit(String accountNo, String dailyLimit);

	public JSONObject linkMobileToMultipleCIF(String moblieNo, String existingCIF, String linkToCIF);

	public String getOriginalBalanceAmountByCustomerNo(String accountNo);

}