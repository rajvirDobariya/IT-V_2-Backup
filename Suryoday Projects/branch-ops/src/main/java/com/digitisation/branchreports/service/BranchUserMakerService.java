package com.digitisation.branchreports.service;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public interface BranchUserMakerService {

	public JSONObject getReports(String requestString, String channel, String x_Session_ID, String x_encode_ID,
			HttpServletRequest request, boolean isDownload, boolean isEncrypt);

	public JSONObject getReportDocumentById(String requestString, String channel, String x_Session_ID, String x_encode_ID,
			HttpServletRequest request, boolean isEncrypt);

	public JSONObject updateReport(String requestString, String channel, String X_User_ID, String x_Session_ID,
			String x_encode_ID, MultipartFile document, HttpServletRequest request, boolean isEncrypt);

	public JSONObject downloadExel(String requestString, String channel, String x_Session_ID, String x_encode_ID,
			HttpServletRequest request, boolean isEncrypt);

	public JSONObject getPendingReportsExcel(String requestString, String channel, String x_Session_ID,
			String x_encode_ID, HttpServletRequest request, boolean isEncrypt);

	public String addDailyReports(LocalDate date, Long branchCode);

	public String addWeeklyReports(LocalDate date);

	public String addMonthlyReports(LocalDate date);

	public String addQuarterlyReports(LocalDate date);

	public String testingAddDailyReports(String date, Long branchCode);

	public String testingAddWeeklyReports();

	public String testingAddMonthlyReports();

	public String testingAddQurterlyReports();

	public String updateStatusNotsubmittedToPending();

	public JSONObject encryption(String requestString, String channel, String x_Session_ID, HttpServletRequest request,
			boolean isEncrypt);

	public JSONObject validateHeadersAndSessionId(String requestString, String channel, String x_Session_ID,
			HttpServletRequest request);

	public String runDeleteOtherBranchesReports();

	public String deleteReportsByDate(String date);

	public Long getCountByDate(String date);

	public String base64ToExcel(String request);

	public String deleteListByIds(String request);

	public String getExcelByDate(String request);

	public void existingBase64ToFile();

	public String getTodayBranchesList();

	public void getBytesToExcel(String request);

	public Long getCountByReportdateAndReporeFrequencyAndByBranchCodeAndByReportId(String request);


}
