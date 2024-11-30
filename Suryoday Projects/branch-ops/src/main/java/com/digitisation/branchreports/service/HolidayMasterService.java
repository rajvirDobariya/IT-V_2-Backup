package com.digitisation.branchreports.service;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public interface HolidayMasterService {

	public JSONObject getAll(String requestString, String channel, String x_Session_ID, String x_encode_ID,
			HttpServletRequest request, boolean isEncy);

	public JSONObject getByDate(String requestString, String channel, String x_Session_ID, String x_encode_ID,
			HttpServletRequest request, boolean isEncy);

	public JSONObject addNationalHoliday(String requestString, String channel, String x_Session_ID, String x_encode_ID,
			HttpServletRequest request, boolean isEncy);

	public JSONObject addYearlyHoliday(String requestString, String channel, String x_Session_ID, String x_encode_ID,
			HttpServletRequest request, boolean isEncy);

	public JSONObject deleteHolidays(String requestString, String channel, String x_Session_ID, String x_encode_ID,
			HttpServletRequest request, boolean isEncy);

	public JSONObject addNationalHolidayByExcel(MultipartFile file, String channel, String x_Session_ID,
			String x_encode_ID, HttpServletRequest request, boolean isEncy);
}
