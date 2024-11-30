package com.suryodaybank.enduse.serviceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.suryodaybank.enduse.dto.GetActivitiesDTO;
import com.suryodaybank.enduse.exception.EndUseException;
import com.suryodaybank.enduse.model.EndUseActivity;
import com.suryodaybank.enduse.repo.EndUseActivityRepo;

@Service
public class EndUseActivityServiceImpl implements com.suryodaybank.enduse.service.EndUseActivityService {

	Logger LOG = LoggerFactory.getLogger(EndUseActivityServiceImpl.class);

	@Autowired
	private com.suryodaybank.enduse.utils.EncryptDecryptHelper encryptDecryptHelper;

	@Autowired
	private EndUseActivityRepo endUseActivityRepository;

	@Override
	public JSONObject add(MultipartFile file) {
		JSONObject response = new JSONObject();
		List<EndUseActivity> activities = new ArrayList<>();

		try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();

			// Skip header row
			if (rowIterator.hasNext()) {
				rowIterator.next();
			}

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				EndUseActivity activity = new EndUseActivity();

				activity.setAccountNo(getLongValue(row.getCell(0)));
				activity.setCustId(getLongValue(row.getCell(1)));
				activity.setAccountName(getStringValue(row.getCell(2)));
				activity.setPslCategory(getStringValue(row.getCell(3)));
				activity.setPurcd(getLongValue(row.getCell(4)));
				activity.setPurposeDesc(getStringValue(row.getCell(5)));
				activity.setType(getStringValue(row.getCell(6)));
				activity.setProductDesc(getStringValue(row.getCell(7)));
				activity.setSanctionDate(getLocalDateValue(row.getCell(8)));
				activity.setSanctionAmt(getDoubleValue(row.getCell(9)));
				activity.setDisbursedDate(getLocalDateValue(row.getCell(10)));
				activity.setDisbursedAmt(getDoubleValue(row.getCell(11)));
				activity.setDpd(getIntegerValue(row.getCell(12)));
				activity.setPos(getDoubleValue(row.getCell(13)));
				activity.setActiveClosed(getStringValue(row.getCell(14)));
				activity.setBranchCode(getLongValue(row.getCell(15)));
				activity.setMobilizeBranch(getLongValue(row.getCell(16)));
				activity.setCreatedDate(LocalDateTime.now());
				activity.setCreatedBy(1L);
				activities.add(activity);
				endUseActivityRepository.save(activity);
			}

			response.put("status", "success");
			response.put("message", "Data imported successfully.");
		} catch (IOException e) {
			e.printStackTrace();
			response.put("status", "error");
			response.put("message", "Failed to process the file.");
		}

		return response;
	}

	@Override
	public JSONObject getActivities(String request, String channel, String X_Session_ID, String x_User_ID,
			boolean isEncrypt) {
		JSONObject response = new JSONObject();
		JSONObject responseData = new JSONObject();
		JSONObject requestJson = new JSONObject();
		List<EndUseActivity> activities = new ArrayList<>();

		// 1 VALIDATE & DECRYPT REQUEST
		if (isEncrypt) {
			requestJson = encryptDecryptHelper.validateHeadersAndSessionId(request, channel, X_Session_ID);
		} else {
			requestJson = new JSONObject(request);
		}
		LOG.debug("DecryptRequestString response :: {}", requestJson.toString());

		// 4. PROCESS
		// 4.1 Get & Validate Data
		JSONObject data = requestJson.optJSONObject("Data");
		if (data == null) {
			LOG.debug("Data is null:: {}", data);
			throw new EndUseException("Data is null :" + data);
		}

		if (data != null) {
			// Gson instance with LocalDate adapter
			Gson gson = new GsonBuilder()
					.registerTypeAdapter(LocalDate.class, new com.google.gson.JsonDeserializer<LocalDate>() {
						@Override
						public LocalDate deserialize(com.google.gson.JsonElement json, java.lang.reflect.Type typeOfT,
								com.google.gson.JsonDeserializationContext context) {
							return LocalDate.parse(json.getAsString());
						}
					}).create();

			// Convert to DTO
			GetActivitiesDTO dto = gson.fromJson(data.toString(), GetActivitiesDTO.class);

			// Print DTO
			String fetchType = dto.getFetchType();
			//
			if (fetchType.equals("byDate")) {
				LocalDate startDate = dto.getStartDate();
				LocalDate endDate = dto.getEndDate();
				activities = endUseActivityRepository.findByDisbursedDateBetween(startDate, endDate);
			} else if (fetchType.equals("byAccountNo")) {
				Long accountNo = dto.getAccountNo();
				activities = endUseActivityRepository.findByAccountNo(accountNo);
			} else {
				throw new EndUseException("Please enter valid fetch type!");
			}
		}
		// RESPONSE
		responseData.put("activities", activities);
		response.put("Data", responseData);
		return response;
	}

// Helper methods for other data types
	private Long getLongValue(Cell cell) {
		return cell != null && cell.getCellType() == CellType.NUMERIC ? (long) cell.getNumericCellValue() : null;
	}

	private Double getDoubleValue(Cell cell) {
		return cell != null && cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : null;
	}

	private Integer getIntegerValue(Cell cell) {
		return cell != null && cell.getCellType() == CellType.NUMERIC ? (int) cell.getNumericCellValue() : null;
	}

	private String getStringValue(Cell cell) {
		if (cell == null)
			return null;

		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue().trim();
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				return sdf.format(cell.getDateCellValue());
			} else {
				return String.valueOf(cell.getNumericCellValue());
			}
		default:
			return null;
		}
	}

	private LocalDate getLocalDateValue(Cell cell) {
		String date = null;
		if (cell.getCellType() == CellType.STRING) {
			date = cell.getStringCellValue();
		} else if (cell.getCellType() == CellType.NUMERIC) {
			date = formatNumericDate(cell.getNumericCellValue());
		}
		String formattedDate = null;

		if (date.contains("-")) {
			formattedDate = convertMonthNameToNumber(date);
		} else {
			formattedDate = formatDateWithLeadingZero(date);
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate localDate = LocalDate.parse(formattedDate, formatter);
		System.out.println("Parsed Date: " + localDate);

		return localDate;
	}

	private String convertMonthNameToNumber(String date) {
		String[] dateParts = date.split("-");
		String day = dateParts[0].length() == 1 ? "0" + dateParts[0] : dateParts[0];
		String monthName = dateParts[1];
		String year = dateParts[2];
		Month month = Month.valueOf(monthName.toUpperCase());
		String monthNumber = String.format("%02d", month.getValue());
		return monthNumber + "/" + day + "/" + year;
	}

	private String formatDateWithLeadingZero(String date) {
		String[] dateParts = date.split("/");
		String month = dateParts[0].length() == 1 ? "0" + dateParts[0] : dateParts[0];
		String day = dateParts[1].length() == 1 ? "0" + dateParts[1] : dateParts[1];
		return month + "/" + day + "/" + dateParts[2];
	}

	private String formatNumericDate(double numericDate) {
		java.util.Date utilDate = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(numericDate);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
		return sdf.format(utilDate);
	}

}
