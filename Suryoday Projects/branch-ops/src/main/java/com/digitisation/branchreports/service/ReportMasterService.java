package com.digitisation.branchreports.service;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public interface ReportMasterService {

	public JSONObject updateReports(String encryptRequestString, String channel, String X_Session_ID,
			String X_encode_ID, HttpServletRequest request, boolean isEncy);

	public JSONObject getAllReports(String requestString, String channel, String x_Session_ID, String x_encode_ID,
			HttpServletRequest request, boolean isEncy);

	public JSONObject updateReportsByExcel(MultipartFile file, String channel, String x_Session_ID, String x_encode_ID,
			HttpServletRequest request, boolean isEncy);

}
