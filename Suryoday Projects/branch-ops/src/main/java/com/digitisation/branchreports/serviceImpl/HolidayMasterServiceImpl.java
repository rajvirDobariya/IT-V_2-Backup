package com.digitisation.branchreports.serviceImpl;

import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.digitisation.branchreports.exception.DigitizationException;
import com.digitisation.branchreports.mapper.HolidayMasterMapper;
import com.digitisation.branchreports.model.HolidayMaster;
import com.digitisation.branchreports.repository.HolidayMasterRepo;
import com.digitisation.branchreports.service.BranchUserMakerService;
import com.digitisation.branchreports.service.HolidayMasterService;
import com.digitisation.branchreports.utils.DateUtils;

@Service
public class HolidayMasterServiceImpl implements HolidayMasterService {

	Logger LOG = LoggerFactory.getLogger(HolidayMasterServiceImpl.class);

	@Autowired
	public HolidayMasterRepo holidayrepo;

	@Autowired
	private HolidayMasterMapper holidayMasterMapper;

	@Autowired
	private BranchUserMakerService branchUserMakerService;

	@Override
	public JSONObject getAll(String requestString, String channel, String x_Session_ID, String x_encode_ID,
			HttpServletRequest request, boolean isEncy) {
		List<HolidayMaster> holidays = holidayrepo.findAll();
		JSONObject Data = new JSONObject();
		Data.put("holidays", holidays);
		return Data;
	}

	@Override
	public JSONObject getByDate(String requestString, String channel, String x_Session_ID, String x_encode_ID,
			HttpServletRequest request, boolean isEncy) {
		JSONObject requestJSON = new JSONObject(requestString);
		String date = requestJSON.getString("date");
		HolidayMaster holiday = holidayrepo.findByHolidayDate(DateUtils.getEndDateOfMonth(date));
		JSONObject Data = new JSONObject();
		Data.put("holiday", holiday);
		return Data;
	}

//	@Override
//	public String addHolidayYearly(int year) {
//		List<HolidayMaster> holidays = new ArrayList<>();
//
//		// Loop through each month of the specified year
//		for (int month = 1; month <= 12; month++) {
//			YearMonth yearMonthObject = YearMonth.of(year, month);
//			int daysInMonth = yearMonthObject.lengthOfMonth();
//
//			// Loop through each day of the month
//			for (int day = 1; day <= daysInMonth; day++) {
//				LocalDate date = LocalDate.of(year, month, day);
//
//				// Check if the date is a holiday (Sunday, 2nd Saturday, or 4th Saturday)
//				if (DateUtils.isDateHoliday(date)) {
//					HolidayMaster holiday = new HolidayMaster();
//					holiday.setHolidayDate(date);
//					holiday.setHoliday(date.getDayOfWeek() == DayOfWeek.SUNDAY ? "Sunday"
//							: (day <= 14 ? "2nd Saturday" : "4th Saturday"));
//					holiday.setHolidaystatus("Active");
//					holidays.add(holiday);
//				}
//			}
//		}
//		// Save all holidays at once
//		holidayrepo.saveAll(holidays);
//
//		return "Holidays added for year " + year + " successfully!";
//	}

	@Override
	public JSONObject addNationalHoliday(String requestString, String channel, String X_Session_ID, String X_encode_ID,
			HttpServletRequest request, boolean isEncy) {
		JSONObject requestJson = null;

		// 1 VALIDATE & DECRYPT REQUEST
		if (isEncy) {
			requestJson = branchUserMakerService.validateHeadersAndSessionId(requestString, channel, X_Session_ID,
					request);
		} else {
			requestJson = new JSONObject(requestString);
		}
		LOG.debug("DecryptRequestString response :: {}", requestJson.toString());

		// 2 VALIDATE DATA
		JSONObject data = requestJson.optJSONObject("Data");
		if (data == null) {
			LOG.debug("Data is null :: {}", data);
			throw new DigitizationException("Data is null : " + data);
		}

		// Get, Convert, Save
		JSONArray holidaysArray = data.getJSONArray("holidays");
		List<HolidayMaster> holidayMasters = new ArrayList();
		for (int i = 0; i < holidaysArray.length(); i++) {
			JSONObject holidayMasterJson = (JSONObject) holidaysArray.get(i);
			HolidayMaster holidayMaster = holidayMasterMapper.convertJsonToRepMaster(holidayMasterJson);
			holidayMasters.add(holidayMaster);
		}
		holidayrepo.saveAll(holidayMasters);

		// RESPONSE
		JSONObject response = new JSONObject();
		JSONObject responseData = new JSONObject();
		responseData.put("value", "Holidays added successfully!");
		response.put("Data", responseData);
		LOG.debug("Holidays added successfully!");
		return response;
	}

	@Override
	public JSONObject addYearlyHoliday(String encryptRequestString, String channel, String x_Session_ID,
			String x_encode_ID, HttpServletRequest request, boolean isEncy) {

		JSONObject requestJson = new JSONObject(encryptRequestString);
		LOG.debug("DecryptRequestString response :: {}", requestJson.toString());

		// 2 VALIDATE DATA
		JSONObject data = requestJson.optJSONObject("Data");
		if (data == null) {
			LOG.debug("Data is null :: {}", data);
			throw new DigitizationException("Data is null : " + data);
		}
		// Get
		int year = data.optInt("year");
		if (year == 0) {
			throw new DigitizationException("Please enter current year!");
		}
		List<HolidayMaster> holidays = new ArrayList<>();

		// Loop through each month of the specified year
		for (int month = 1; month <= 12; month++) {
			YearMonth yearMonthObject = YearMonth.of(year, month);
			int daysInMonth = yearMonthObject.lengthOfMonth();

			// Loop through each day of the month
			for (int day = 1; day <= daysInMonth; day++) {
				LocalDate date = LocalDate.of(year, month, day);

				// Check if the date is a holiday (Sunday, 2nd Saturday, or 4th Saturday)
				if (DateUtils.isDateHoliday(date)) {
					HolidayMaster holiday = new HolidayMaster();
					holiday.setHolidayDate(date);
					holiday.setHoliday(date.getDayOfWeek() == DayOfWeek.SUNDAY ? "Sunday"
							: (day <= 14 ? "2nd Saturday" : "4th Saturday"));
					holiday.setHolidaystatus("Active");
					holidays.add(holiday);
				}
			}
		}
		// Save all holidays at once
		holidayrepo.saveAll(holidays);

		// RESPONSE
		JSONObject response = new JSONObject();
		JSONObject responseData = new JSONObject();
		responseData.put("value", "Holidays added for year " + year + " successfully!");
		response.put("Data", responseData);
		LOG.debug("Holidays added for year " + year + " successfully!");
		return response;
	}

	@Override
	public JSONObject deleteHolidays(String requestString, String channel, String x_Session_ID, String x_encode_ID,
			HttpServletRequest request, boolean isEncy) {

		// DELETE
		holidayrepo.deleteAll();
		// RESPONSE
		JSONObject response = new JSONObject();
		JSONObject responseData = new JSONObject();
		responseData.put("value", "Holidays deleted successfully!");
		response.put("Data", responseData);
		LOG.debug("Holidays deleted successfully!");
		return response;
	}

	@Override
	public JSONObject addNationalHolidayByExcel(MultipartFile file, String channel, String x_Session_ID,
			String x_encode_ID, HttpServletRequest request, boolean isEncy) {

		// PROSESS
		JSONObject response = new JSONObject();
		JSONObject responseData = new JSONObject();
		List<HolidayMaster> holidayList = new ArrayList<>();
		try {
			// Step 1: Get the InputStream from the MultipartFile
			InputStream inputStream = file.getInputStream();

			// Step 2: Create a workbook from the InputStream
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0); // Assuming the first sheet contains data

			// Step 3: Iterate through rows and create HolidayMaster objects
			for (Row row : sheet) {
				// Skip the header row
				if (row.getRowNum() == 0) {
					continue;
				}

				// Get cell values
				String holiday = row.getCell(0).getStringCellValue();
				String dateCellValue = row.getCell(1).getStringCellValue();
				LocalDate holidayDate = DateUtils.getDateStringIntoLocalDate(dateCellValue);
				String holidayStatus = row.getCell(2).getStringCellValue();

				// Create HolidayMaster object
				HolidayMaster holidayMaster = new HolidayMaster();
				holidayMaster.setHoliday(holiday);
				holidayMaster.setHolidayDate(holidayDate);
				holidayMaster.setHolidaystatus(holidayStatus);

				// Add to list
				holidayList.add(holidayMaster);
			}
			holidayrepo.saveAll(holidayList);

			workbook.close();
		} catch (Exception e) {
			LOG.debug("Error processing file!");
			responseData.put("message", "Error processing file");
			response.put("Data", responseData);
			return response;
		}

		// RESPONSE
		responseData.put("value", "Holidays added for excel successfully!");
		response.put("Data", responseData);
		LOG.debug("Holidays added for year excel successfully!");
		return response;

	}

}
