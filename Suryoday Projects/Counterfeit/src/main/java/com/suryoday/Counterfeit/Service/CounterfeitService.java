package com.suryoday.Counterfeit.Service;

import java.time.YearMonth;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import com.suryoday.Counterfeit.Pojo.Counterfeit;

public interface CounterfeitService {

	public JSONObject createCounterfeit(String encryptRequestString, String channel, String X_Session_ID,
			String X_encode_ID, HttpServletRequest request, boolean isEncrypt);

	public JSONObject getCounterfeit(String encryptRequestString, String channel, String X_Session_ID,
			String X_encode_ID, HttpServletRequest request, boolean isEncrypt);

	public JSONObject getCounterfeitExcel(String encryptRequestString, String channel, String X_Session_ID,
			String X_encode_ID, HttpServletRequest request, boolean isEncrypt);

	public JSONObject updateCounterfeitStatusAndBoRemarks(String encryptRequestString, String channel,
			String X_Session_ID, String X_encode_ID, HttpServletRequest request, boolean isEncrypt);

	public JSONObject getMonthlyCount(String requestString, String channel, String x_Session_ID, String x_encode_ID,
			HttpServletRequest request, boolean isEncrypt);

	public Counterfeit getCounterfeitByMonthAndBranchCode(YearMonth month, String branchCode);

	public boolean existbyById(Long counterfeitId);

	public JSONObject deleteByDailyMonthly(String requestString, String channel, String x_Session_ID,
			String x_encode_ID, HttpServletRequest request, boolean isEncrypt);

	public boolean existsByBranchCodeAndMonthAndStatusSubmitted(String branchCode, YearMonth yearMonth);

	public void addMonthlyCounterfeitsTesting(String month);

	public void delete(String request);

	Counterfeit findById(long id);


}
