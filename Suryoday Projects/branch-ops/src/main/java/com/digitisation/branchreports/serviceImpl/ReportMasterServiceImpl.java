package com.digitisation.branchreports.serviceImpl;

import java.io.InputStream;
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
import com.digitisation.branchreports.mapper.ReportMasterMapper;
import com.digitisation.branchreports.model.RepMaster;
import com.digitisation.branchreports.repository.ReportMasterRepository;
import com.digitisation.branchreports.service.ReportMasterService;

@Service
public class ReportMasterServiceImpl implements ReportMasterService {

	Logger LOG = LoggerFactory.getLogger(ReportMasterServiceImpl.class);

	@Autowired
	private ReportMasterRepository reportres;

	@Autowired
	private ReportMasterMapper reportMasterMapper;

	@Override
	public JSONObject getAllReports(String requestString, String channel, String x_Session_ID, String x_encode_ID,
			HttpServletRequest request, boolean isEncy) {

		JSONObject requestJson = new JSONObject(requestString);
		LOG.debug("DecryptRequestString response :: {}", requestJson.toString());

		// 2 VALIDATE DATA
		JSONObject data = requestJson.optJSONObject("Data");
		if (data == null) {
			LOG.debug("Data is null :: {}", data);
			throw new DigitizationException("Data is null : " + data);
		}
		// Get, Convert, Save
		List<RepMaster> reportMasters = new ArrayList<>();
		String type = data.getString("type");
		if (type == null || type.isEmpty()) {
			LOG.debug("type is null or empty!");
			throw new DigitizationException("type is null or empty!");
		}

		if (!type.equals("All")) {
			String frequency = data.getString("frequency");
			if (frequency == null || frequency.isEmpty()) {
				LOG.debug("frequency is null or empty!");
				throw new DigitizationException("frequency is null or empty!");
			}
			reportMasters = reportres.findByReportfrequency(frequency);
		} else {
			reportMasters = reportres.findAll();
		}

		JSONObject Data = new JSONObject();
		Data.put("reportMasters", reportMasters);
		JSONObject response = new JSONObject();
		response.put("Data", Data);
		return response;
	}

	@Override
	public JSONObject updateReports(String encryptRequestString, String channel, String X_Session_ID,
			String X_encode_ID, HttpServletRequest request, boolean isEncy) {

		JSONObject requestJson = new JSONObject(encryptRequestString);
		LOG.debug("DecryptRequestString response :: {}", requestJson.toString());

		// 2 VALIDATE DATA
		JSONObject data = requestJson.optJSONObject("Data");
		if (data == null) {
			LOG.debug("Data is null :: {}", data);
			throw new DigitizationException("Data is null : " + data);
		}
		// Get, Convert, Save
		JSONArray reportsArray = data.getJSONArray("reports");
		List<RepMaster> repMasters = new ArrayList();
		for (int i = 0; i < reportsArray.length(); i++) {
			JSONObject repMasterJson = (JSONObject) reportsArray.get(i);
			RepMaster repMaster = reportMasterMapper.convertJsonToRepMaster(repMasterJson);
			repMasters.add(repMaster);
		}
		reportres.deleteAll();
		reportres.saveAll(repMasters);

		// RESPONSE
		JSONObject response = new JSONObject();
		JSONObject responseData = new JSONObject();
		responseData.put("value", "Reports added successfully!");
		response.put("Data", responseData);
		LOG.debug("Reports added successfully!");
		return response;

	}

	public JSONObject updateReportsByExcel(MultipartFile file, String channel, String x_Session_ID, String x_encode_ID,
			HttpServletRequest request, boolean isEncy) {
		List<RepMaster> reportList = new ArrayList<>();
		JSONObject response = new JSONObject();
		JSONObject responseData = new JSONObject();

		try {
			InputStream inputStream = file.getInputStream();
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			for (Row row : sheet) {
				if (row.getRowNum() == 0) {
					continue;
				}

				long reportid = (long) row.getCell(0).getNumericCellValue();
				String reportfrequency = row.getCell(1).getStringCellValue();
				String reportname = row.getCell(2).getStringCellValue();
				if (reportid == 0 || reportfrequency == null || reportfrequency.isEmpty() || reportname == null
						|| reportname.isEmpty()) {
					throw new DigitizationException("Invalid data in row: " + (row.getRowNum() + 1));
				}
				RepMaster report = new RepMaster();
				report.setReportid(reportid);
				report.setReportfrequency(reportfrequency);
				report.setReportname(reportname);
				reportList.add(report);
			}
			workbook.close();
			reportres.deleteAll();
			reportres.saveAll(reportList);

			// RESPONSE
			responseData.put("value", "Reports added successfully!");
			response.put("Data", responseData);
			LOG.debug("Reports added successfully!");

		} catch (Exception e) {
			responseData.put("Error", "Error processing file: " + e.getMessage());
			response.put("Data", responseData);
			LOG.debug("Error processing file: " + e.getMessage());
		}
		return response;
	}

}