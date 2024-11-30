package com.digitisation.branchreports.serviceImpl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.digitisation.branchreports.dao.BranchUserMakerDAO;
import com.digitisation.branchreports.enums.UserRole;
import com.digitisation.branchreports.exception.DigitizationException;
import com.digitisation.branchreports.mapper.BranchUserMakerModelMapper;
import com.digitisation.branchreports.mapper.FiletableMapper;
import com.digitisation.branchreports.model.BranchMaster;
import com.digitisation.branchreports.model.BranchUserMakerModel;
import com.digitisation.branchreports.model.Filetable;
import com.digitisation.branchreports.model.NewBranchMaster;
import com.digitisation.branchreports.model.RepMaster;
import com.digitisation.branchreports.repository.BranchMasterRepo;
import com.digitisation.branchreports.repository.BranchUserMakerRepository;
import com.digitisation.branchreports.repository.HolidayMasterRepo;
import com.digitisation.branchreports.repository.NewBranchMasterRepo;
import com.digitisation.branchreports.repository.ReportMasterRepository;
import com.digitisation.branchreports.service.BranchMasterService;
import com.digitisation.branchreports.service.BranchUserMakerService;
import com.digitisation.branchreports.service.DocumentFileService;
import com.digitisation.branchreports.service.ExcelExportService;
import com.digitisation.branchreports.service.FiletableService;
import com.digitisation.branchreports.utils.Constants;
import com.digitisation.branchreports.utils.DateUtils;
import com.digitisation.branchreports.utils.EXCELService;
import com.digitisation.branchreports.utils.EncryptDecryptHelper;
import com.digitisation.branchreports.utils.FileUtils;

@Service
public class BranchUserMakerServiceImpl implements BranchUserMakerService {

	private static final Logger LOG = LoggerFactory.getLogger(BranchUserMakerServiceImpl.class.getName());

	@Value("${file-path}")
	private String filePath;

	@Autowired
	private BranchUserMakerRepository branchusermakerrepos;

	@Autowired
	private NewBranchMasterRepo newBranchMasterRepo;

	@Autowired
	private ReportMasterRepository repmasterrepo;

	@Autowired
	private BranchMasterRepo branchMasterRepo;

	@Autowired
	private BranchMasterService branchMasterService;

	@Autowired
	private HolidayMasterRepo holidayMasterRepo;

	@Autowired
	private BranchUserMakerDAO branchUserMakerDAO;

	@Autowired
	private BranchUserMakerModelMapper branchUserMakerModelMapper;

	@Autowired
	private FiletableService filetableService;

	@Autowired
	private DocumentFileService documentService;

	@Autowired
	private FiletableMapper filetableMapper;

	@Override
	public JSONObject getReports(String requestString, String channel, String X_Session_ID, String X_encode_ID,
			HttpServletRequest request, boolean isDownload, boolean isEncrypt) {
		JSONObject response = new JSONObject();
		JSONObject Data = new JSONObject();
		JSONObject requestJson = null;
		// 1 VALIDATE & DECRYPT REQUEST
		if (isEncrypt) {
			requestJson = validateHeadersAndSessionId(requestString, channel, X_Session_ID, request);
		} else {
			requestJson = new JSONObject(requestString);
		}
		LOG.debug("DecryptRequestString response :: {}", requestJson.toString());

		// 4. PROCESS
		// 4.1 Get & Validate Data
		JSONObject data = requestJson.optJSONObject("Data");
		if (data == null) {
			LOG.debug("Data is null:: {}", data);
			throw new DigitizationException("Data is null :" + data);
		}

		// Validate userRole
		String userRole = data.optString("userRole");
		UserRole userRoleEnum = validateUserRole(userRole);

		// Download EXEL
		if (isDownload) {
			if (!(userRoleEnum.equals(UserRole.BRANCH_HO) || userRoleEnum.equals(UserRole.BRANCH_AUDITOR))) {
				throw new DigitizationException(
						"Your role is " + userRole + " and you are not authorised to download exel!");
			}
		}

		// Get and Convert
		List<Object[]> branchUserMakerModels = branchUserMakerDAO.findBranchUserMakerModelByFilter(data, userRole);
		// Convert
		JSONArray denominationJsonArray = new JSONArray();
		for (Object[] denomination : branchUserMakerModels) {
			JSONObject denominationJson = branchUserMakerModelMapper.convertToJson(denomination);
			denominationJsonArray.put(denominationJson);
		}

		// Make Response
		Data.put("reports", denominationJsonArray);
		response.put("Data", Data);
		return response;
	}

	@Override
	public JSONObject updateReport(String requestString, String channel, String X_User_ID, String X_Session_ID,
			String X_encode_ID, MultipartFile file, HttpServletRequest request, boolean isEncrypt) {
		JSONObject response = new JSONObject();
		JSONObject Data = new JSONObject();
		JSONObject requestJson = null;

		// 1 VALIDATE & DECRYPT REQUEST
		if (isEncrypt) {
			requestJson = validateHeadersAndSessionId(requestString, channel, X_Session_ID, request);
			LOG.debug("updateReport : Request JSON is :: {}", requestJson);
		} else {
			LOG.debug("updateReport : Request JSON is :: {}", requestJson);
			requestJson = new JSONObject(requestString);
		}
		LOG.debug("DecryptRequestString response :: {}", requestJson.toString());

		// 4. PROCESS
		// 4.1 Validate Data
		JSONObject data = requestJson.optJSONObject("Data");
		if (data == null) {
			LOG.debug("Data is :: {}", data);
			throw new DigitizationException("Data is :" + data);
		}
		Long userId = Long.parseLong(X_User_ID);
		String userRole = data.optString("userRole");
		Long reportId = data.optLong("reportId");
		String remarks = data.optString("remarks");

		// Validate userRole
		UserRole userRoleEnum = validateUserRole(userRole);

		// Validate Remarks
		if (!remarks.matches("^[a-zA-Z0-9 .,]*$")) {
			throw new DigitizationException(
					"Invalid remarks: only alphabetic characters, numeric characters, spaces, dots, and commas are allowed.");
		}

		BranchUserMakerModel report = branchusermakerrepos.findById(reportId)
				.orElseThrow(() -> new DigitizationException("Report not found with id " + reportId));

		switch (userRoleEnum) {
		case BRANCH_MAKER: {
			// Validate Status
			if (!(report.getStatus().equals("Not Submitted") || report.getStatus().equals("Pending")
					|| report.getStatus().equals("Pending at Maker"))) {
				throw new DigitizationException(userRole + " have not authority to do modification in report status "
						+ report.getStatus() + "!");
			}

			// Validate Action and Remarks
			String action = data.optString("action");
			if (remarks == null || remarks.isEmpty() || action == null || action.isEmpty()) {
				LOG.debug("One or more fiends are null or empty out of action, document and remarks :: {}", data);
				throw new DigitizationException("One or more fiends are null or empty out of action or remarks!");
			}
			// Validate Document
			if (action.equals("YES")) {
				// Create Document
				Filetable docuement = documentService.createDocument(file);
				System.out.println("DOCUMENT ::" + docuement);
				report.setRoDocument(docuement.getId());
				LOG.debug("Report Ro Docuemt ID ::" + report.getRoDocument());
			} else if (!action.equals("NIL")) {
				throw new DigitizationException("Please enter valid action!");
			}

			report.setAction(action);
			report.setRoId(userId);
			report.setRoRemarks(remarks);
			report.setStatus("Pending at Checker");
			report.setSubmissiondate(LocalDate.now());
			break;
		}
		case BRANCH_CHECKER: {
			// Validate Status
			if (!report.getStatus().equals("Pending at Checker")) {
				throw new DigitizationException(userRole + " have not authority to do modification in report status "
						+ report.getStatus() + "!");
			}

			// Validate remarks & status
			String status = data.optString("status");
			if (remarks == null || remarks.isEmpty() || status == null || status.isEmpty()) {
				LOG.debug("Status or Remarks is null or empty :: {}", data);
				throw new DigitizationException("Status or Remarks is null or empty!");
			}
			if (!(status.equals("Return") || status.equals("Verify"))) {
				LOG.debug("Please select correct status!");
				throw new DigitizationException("Please select correct status!");
			}

			report.setStatus((status.equals("Return") ? "Pending at Maker" : "Submitted"));
			report.setBmId(userId);
			report.setBmRemarks(remarks);
			// RETURN OR VERIFY
			break;
		}
		case BRANCH_HO: {
			// Validate Status
			if (!report.getStatus().equals("Submitted")) {
				throw new DigitizationException(userRole + " have not authority to do modification in report status "
						+ report.getStatus() + "!");
			}

			// Validate Remarks
			if (remarks != null && !remarks.isEmpty()) {
				report.setHoRemarks(remarks);
			}
			report.setHoId(userId);
			break;
		}
		case BRANCH_AUDITOR: {
			// Validate Status
			if (!report.getStatus().equals("Submitted")) {
				throw new DigitizationException(userRole + " have not authority to do modification in report status "
						+ report.getStatus() + "!");
			}

			// Validate Remarks
			if (remarks != null && !remarks.isEmpty()) {
				report.setAuditorRemarks(remarks);
			}

			// Validate Auditor Document
			JSONObject documentJson = data.optJSONObject("document");
			if (documentJson != null && !documentJson.isEmpty()) {
				Filetable docuement = documentService.createDocument(file);
				report.setAuditorDocument(docuement.getId());
			}
			report.setAuditorId(userId);
			break;
		}
		default: {
			throw new DigitizationException("Your role is " + userRole + " and you are not authorised person!");
		}
		}
		report.setUpdatedDate(new Date());
		report.setUpdatedby(userRole);
		branchusermakerrepos.save(report);

		// Make Response
		Data.put("message", "Report " + reportId + " updated successfully!");
		response.put("Data", Data);
		return response;

	}

	@Override
	public JSONObject downloadExel(String requestString, String channel, String x_Session_ID, String x_encode_ID,
			HttpServletRequest request, boolean isEncrypt) {
		JSONObject result = getReports(requestString, channel, x_Session_ID, x_encode_ID, request, true, isEncrypt);
		String exelBase64 = EXCELService.generateEXEL(result);
		JSONObject response = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("exelBase64", exelBase64);
		response.put("Data", data);
		return response;
	}

	// EVERY DAY
	@Override
	public String addDailyReports(LocalDate date, Long branchCode) {
		JSONObject response = new JSONObject();
		JSONObject data = new JSONObject();
		LocalDate reportDate = null;
		if (date != null) {
			reportDate = date;
		} else {
			reportDate = LocalDate.now();
		}

		// 4 : VALIDATE HOLIDAY
		boolean isHoliday = holidayMasterRepo.existsByHolidayDate(reportDate);
		if (isHoliday) {
			LOG.debug(reportDate + " is holiday!");
			data.put("message", reportDate + " is holiday!");
			response.put("Data", data);
			return response.toString();
		}

		// 1 : Get Branches & Reports
		List<NewBranchMaster> newBranchMasters = newBranchMasterRepo.findAll();
		List<RepMaster> repMasters = repmasterrepo.findByReportfrequency("Daily");
		List<BranchUserMakerModel> reports = new ArrayList<>();
		Long count = branchusermakerrepos.getCountByReportdateAndReporeFrequency(reportDate, "Daily");
		if (count == 0) {
			for (NewBranchMaster branch : newBranchMasters) {
				String branchCode2 = String.valueOf(branch.getBranchCode());
				for (RepMaster repoMaster : repMasters) {
					BranchUserMakerModel report = new BranchUserMakerModel();
					report.setReportdate(reportDate);
					report.setBranchcode(branchCode2);
					report.setBranchname(branch.getBranchName());
					report.setReportId(repoMaster.getReportid());
					report.setStatus("Not Submitted");
					report.setCreatedby("Scheduler");
					report.setCreatedDate(new Date());
					reports.add(report);
//					}
				}
				branchusermakerrepos.saveAll(reports);
				LOG.debug(reports.size() + " :: Reports added for branch ::" + branchCode2 + " successfully!");
			}
		}
		LOG.debug("Daily Reports added for " + reportDate + "!");
		data.put("message", "Daily Reports added for " + reportDate + "!");
		response.put("Data", data);
		return response.toString();
	}

	// EVERY MONDAY
	@Override
	public String addWeeklyReports(LocalDate date) {
		JSONObject response = new JSONObject();
		JSONObject data = new JSONObject();
		LocalDate reportDate = null;

		if (date != null) {
			reportDate = date;
		} else {
			reportDate = LocalDate.now();
		}

		// VALIDATE Holiday
		boolean isHoliday;
		while (true) {
			isHoliday = holidayMasterRepo.existsByHolidayDate(reportDate);
			if (!isHoliday) {
				break;
			}
			reportDate = reportDate.plusDays(1);
		}

		// 1 : Get Branches & Reports
		List<BranchMaster> branchMasters = branchMasterRepo.findByBranches(branchMasterService.getTestingBranches());
		List<RepMaster> repMasters = repmasterrepo.findByReportfrequency("Weekly");
		List<BranchUserMakerModel> reports = new ArrayList<>();

		for (BranchMaster branch : branchMasters) {
			String branchCode2 = String.valueOf(branch.getBranchid());
			Long count = branchusermakerrepos.getCountByReportdateAndReporeFrequencyAndByBranchCode(reportDate,
					"Weekly", branchCode2);
			LOG.debug("REPORT COUNT ::" + reportDate + "::" + count);
			if (count == 0) {
				for (RepMaster repoMaster : repMasters) {
					BranchUserMakerModel report = new BranchUserMakerModel();
					report.setReportdate(reportDate);
					report.setReportdate(reportDate);
					report.setReportId(repoMaster.getReportid());
					report.setBranchcode(String.valueOf(branch.getBranchid()));
					report.setBranchname(branch.getBranchname());
					report.setStatus("Not Submitted");
					report.setCreatedby("Scheduler");
					report.setCreatedDate(new Date());
					reports.add(report);
				}
				branchusermakerrepos.saveAll(reports);
			}
		}
		LOG.debug("Weekly Reports added for " + reportDate + "!");
		data.put("message", "Weekly reports added succesfully!");
		response.put("Data", data);
		return response.toString();
	}

	// EVERY 1st of every month
	@Override
	public String addMonthlyReports(LocalDate date) {
		JSONObject response = new JSONObject();
		JSONObject data = new JSONObject();
		LocalDate reportDate = null;

		if (date != null) {
			reportDate = date;
		} else {
			reportDate = LocalDate.now();
		}

		boolean isHoliday;
		while (true) {
			isHoliday = holidayMasterRepo.existsByHolidayDate(reportDate);
			if (!isHoliday) {
				break;
			}
			reportDate = reportDate.plusDays(1);
		}

		// 1 : Get Branches & Reports
		List<BranchMaster> branchMasters = branchMasterRepo.findByBranches(branchMasterService.getTestingBranches());
		List<RepMaster> repMasters = repmasterrepo.findByReportfrequency("Monthly");
		List<BranchUserMakerModel> reports = new ArrayList<>();

		for (BranchMaster branch : branchMasters) {
			String branchCode2 = String.valueOf(branch.getBranchid());
			Long count = branchusermakerrepos.getCountByReportdateAndReporeFrequencyAndByBranchCode(reportDate,
					"Monthly", branchCode2);
			LOG.debug("REPORT COUNT ::" + reportDate + "::" + count);
			if (count == 0) {
				for (RepMaster repoMaster : repMasters) {
					BranchUserMakerModel report = new BranchUserMakerModel();
					report.setReportdate(reportDate);
					report.setReportdate(reportDate);
					report.setBranchcode(String.valueOf(branch.getBranchid()));
					report.setBranchname(branch.getBranchname());
					report.setReportId(repoMaster.getReportid());
					report.setStatus("Not Submitted");
					report.setCreatedby("Scheduler");
					report.setCreatedDate(new Date());
					reports.add(report);
				}
				branchusermakerrepos.saveAll(reports);
			}
		}
		LOG.debug("Monthly Reports added for " + reportDate + "!");
		data.put("message", "Monthly Reports added for " + reportDate + "!");
		response.put("Data", data);
		return response.toString();
	}

	// EVERY 1st of every Quarterly
	@Override
	public String addQuarterlyReports(LocalDate date) {
		JSONObject response = new JSONObject();
		JSONObject data = new JSONObject();
		LocalDate reportDate = null;

		if (date != null) {
			reportDate = date;
		} else {
			reportDate = LocalDate.now();
		}

		boolean isHoliday;
		while (true) {
			isHoliday = holidayMasterRepo.existsByHolidayDate(reportDate);
			if (!isHoliday) {
				break;
			}
			reportDate = reportDate.plusDays(1);
		}

		// 1 : Get Branches & Reports
		List<BranchMaster> branchMasters = branchMasterRepo.findByBranches(branchMasterService.getTestingBranches());
		List<RepMaster> repMasters = repmasterrepo.findByReportfrequency("Quarterly");
		List<BranchUserMakerModel> reports = new ArrayList<>();

		for (BranchMaster branch : branchMasters) {
			String branchCode2 = String.valueOf(branch.getBranchid());
			Long count = branchusermakerrepos.getCountByReportdateAndReporeFrequencyAndByBranchCode(reportDate,
					"Quarterly", branchCode2);
			LOG.debug("REPORT COUNT ::" + reportDate + "::" + count);
			if (count == 0) {
				for (RepMaster repoMaster : repMasters) {
					BranchUserMakerModel report = new BranchUserMakerModel();
					report.setReportdate(reportDate);
					report.setReportdate(reportDate);
					report.setBranchcode(String.valueOf(branch.getBranchid()));
					report.setBranchname(branch.getBranchname());
					report.setReportId(repoMaster.getReportid());
					report.setStatus("Not Submitted");
					report.setCreatedby("Scheduler");
					report.setCreatedDate(new Date());
					reports.add(report);
				}
			}
		}
		branchusermakerrepos.saveAll(reports);
		LOG.debug("Qurterly Reports added for " + reportDate + "!");
		data.put("message", "Qurterly Reports added for " + reportDate + "!");
		response.put("Data", data);
//			}
//		}
		return response.toString();

	}

	@Override
	public String updateStatusNotsubmittedToPending() {
		LocalDate reportDate = LocalDate.now();

		boolean isHoliday = holidayMasterRepo.existsByHolidayDate(reportDate);
		if (isHoliday) {
			LOG.debug(reportDate + " is holiday!");
			return reportDate + " is holiday!";
		}
		branchusermakerrepos.updateStatusNotsubmittedToPending();
		return "Reports status update Not Submitted to Pending successfully on date " + reportDate + "!";
	}

	public JSONObject validateHeadersAndSessionId(String encryptRequestString, String channel, String X_Session_ID,
			HttpServletRequest request) {
		// Prepare detailed message with field values
		String message = String.format(
				"One or more required fields are null or empty in validateHeadersAndSessionId. Values - "
						+ "encryptRequestString: '%s', channel: '%s', X_Session_ID: '%s'",
				encryptRequestString, channel, X_Session_ID);
		LOG.debug("Validate Headers :" + message);

		// 1 Check fields null and empty
		if (encryptRequestString == null || encryptRequestString.isEmpty() || channel == null || channel.isEmpty()
				|| X_Session_ID == null || X_Session_ID.isEmpty()) {
			LOG.debug("Validate Headers :" + message);
			throw new DigitizationException("Validate Headers :" + message);
		}

		// 2 Check DB session ID
		String dbSessionId = fetchSessionId(X_Session_ID);
		if (dbSessionId == null || dbSessionId.isEmpty()) {
			LOG.debug("SessionId is expired or Invalid sessionId :: {}", HttpStatus.UNAUTHORIZED);
			throw new DigitizationException("SessionId is expired or Invalid sessionId!");
		}
		// 3 DecryptRequestString
		return EncryptDecryptHelper.decryptRequestString(encryptRequestString, channel, X_Session_ID);
	}

	public String fetchSessionId(String sessionId) {
		return branchusermakerrepos.getAllSessionIds(sessionId);
	}

	@Override
	public String testingAddDailyReports(String todayDate, Long branchCode) {
		LocalDate reportDate = LocalDate.parse(todayDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		addDailyReports(reportDate, branchCode);
		return "testingAddDailyReports";
	}

	@Override
	public String testingAddWeeklyReports() {

		// TESTING
		for (int i = 1; i < 30; i++) {
			String todayDate = "";
			if (i < 10) {
				todayDate = "0" + i + "-07-2024";
			} else {
				todayDate = i + "-07-2024";
			}
			LocalDate reportDate = LocalDate.parse(todayDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
			// Check if the reportDate is a Monday
			if (reportDate.getDayOfWeek() == DayOfWeek.MONDAY) {
				addWeeklyReports(reportDate);
			}
		}
		return "testingAddWeeklyReports";

	}

	@Override
	public String testingAddMonthlyReports() {

		// TESTING
		for (int j = 1; j <= 12; j++) {
			for (int i = 1; i <= 30; i++) {
				String todayDate = "";

				String day = (i < 10) ? "0" + i : String.valueOf(i);
				String month = (j < 10) ? "0" + j : String.valueOf(j);

				todayDate = day + "-" + month + "-2024";

				LocalDate reportDate = LocalDate.parse(todayDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

				// Check if the reportDate is the 1st day of the month
				if (reportDate.getDayOfMonth() == 1) {
					addMonthlyReports(reportDate);
				}
			}
		}
		return "testingAddMonthlyReports";

	}

	@Override
	public String testingAddQurterlyReports() {

		// TESTING
		for (int j = 1; j <= 12; j++) {
			for (int i = 1; i <= 30; i++) {
				String todayDate = "";

				String day = (i < 10) ? "0" + i : String.valueOf(i);
				String month = (j < 10) ? "0" + j : String.valueOf(j);

				todayDate = day + "-" + month + "-2024";

				LocalDate reportDate = LocalDate.parse(todayDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

				// Check if the reportDate is the 1st day of a quarter
				if (reportDate.getDayOfMonth() == 1 && isQuarterStart(reportDate.getMonthValue())) {
					addQuarterlyReports(reportDate);
				}
			}
		}
		return "testingAddQurterlyReports";
	}

	private boolean isQuarterStart(int month) {
		return month == 1 || month == 4 || month == 7 || month == 10;
	}

	@Override
	public JSONObject getReportDocumentById(String requestString, String channel, String X_Session_ID,
			String X_encode_ID, HttpServletRequest request, boolean isEncrypt) {
		JSONObject response = new JSONObject();
		JSONObject Data = new JSONObject();
		JSONObject requestJson = null;

		// 1 VALIDATE & DECRYPT REQUEST
		if (isEncrypt) {
			requestJson = validateHeadersAndSessionId(requestString, channel, X_Session_ID, request);
		} else {
			requestJson = new JSONObject(requestString);
		}
		LOG.debug("DecryptRequestString response :: {}", requestJson.toString());

		// 4. PROCESS
		// 4.1 Get & Validate Data
		JSONObject data = requestJson.optJSONObject("Data");
		if (data == null) {
			LOG.debug("Data is null:: {}", data);
			throw new DigitizationException("Data is null :" + data);
		}

		// Validate documentId
		Long documentId = data.optLong("documentId");
		if (documentId == 0) {
			throw new DigitizationException("documentId is :" + documentId);
		}

		// Get and Convert
		Filetable filetable = filetableService.getFiletableById(documentId);
		JSONObject document = filetableMapper.convertToJson(filetable);
		// Make Response
		Data.put("document", document);
		response.put("Data", Data);
		return response;

	}

	@Override
	public JSONObject encryption(String requestString, String channel, String X_Session_ID, HttpServletRequest request,
			boolean isEncrypt) {
		JSONObject requestJson = new JSONObject();
		// 1 VALIDATE & DECRYPT REQUEST
		requestJson = validateHeadersAndSessionId(requestString, channel, X_Session_ID, request);
		LOG.debug("DecryptRequestString response :: {}", requestJson.toString());
		return requestJson;
	}

	public List<Long> getFiveBranches() {
		return Arrays.asList(10079L, 10015L, 10020L, 10163L, 10012L);
	}

	@Override
	public JSONObject getPendingReportsExcel(String requestString, String channel, String X_Session_ID,
			String X_encode_ID, HttpServletRequest request, boolean isEncrypt) {
		JSONObject response = new JSONObject();
		JSONObject Data = new JSONObject();
		JSONObject requestJson = null;

		// 1 VALIDATE & DECRYPT REQUEST
		if (isEncrypt) {
			requestJson = validateHeadersAndSessionId(requestString, channel, X_Session_ID, request);
		} else {
			requestJson = new JSONObject(requestString);
		}
		LOG.debug("DecryptRequestString response :: {}", requestJson.toString());

		// 4. PROCESS
		List<Object[]> reportsCount = branchusermakerrepos.getPendingReportsData();
		System.out.println("REPORTS ::" + reportsCount.size());
		String excelBase64 = ExcelExportService.exportReportsToExcelBase64(reportsCount);
		// RESPONSE
		Data.put("excelBase64", excelBase64);
		response.put("Data", Data);
		return response;
	}

	public UserRole validateUserRole(String userRole) {
		if (userRole == null) {
			throw new DigitizationException("User Role null!");
		}

		if (userRole == null || userRole.isEmpty()) {
			LOG.debug("User role is null or empty :: {}", userRole);
			throw new DigitizationException("User role is null or empty : " + userRole);
		}

		UserRole userRoleEnum = null;
		for (UserRole role : UserRole.values()) {
			if (role.getRole().equals(userRole)) {
				userRoleEnum = role;
				break;
			}
		}
		if (userRoleEnum == null) {
			throw new DigitizationException("Wrong User role!");
		}
		return userRoleEnum;

	}

	@Override
	public String runDeleteOtherBranchesReports() {
		List<Long> testingBranchesLong = branchMasterService.getTestingBranches();
		List<String> branchCodes = testingBranchesLong.stream().map(String::valueOf).collect(Collectors.toList());
		branchusermakerrepos.deleteOtherBranchesReports(branchCodes);
		return "runDeleteOtherBranchesReports";
	}

	@Override
	public String deleteReportsByDate(String dateStr) {
		LocalDate date = DateUtils.getDateStringIntoLocalDate(dateStr);
		branchusermakerrepos.deleteByReportdate(date);
		return null;
	}

	@Override
	public Long getCountByDate(String dateStr) {
		// yyyy-MM-dd
		LocalDate date = DateUtils.getDateStringIntoLocalDate(dateStr);
		return branchusermakerrepos.getCountByReportDate(date);
	}

	@Override
	public String base64ToExcel(String request) {
		JSONObject requestJson = new JSONObject(request);
		String base64String = requestJson.getString("base64");
		// Output Excel file path
		String filePath = "C:\\Users\\92952\\Desktop\\Branches pending reports " + LocalDate.now() + ".xlsx";

		// Convert base64 string to byte array
		byte[] decodedBytes = Base64.getDecoder().decode(base64String);

		// Write byte array to file
		try (FileOutputStream fos = new FileOutputStream(filePath)) {
			fos.write(decodedBytes);
			System.out.println("Excel file created successfully.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String deleteListByIds(String request) {
		JSONObject requestJson = new JSONObject(request);
		String idsStr = requestJson.getString("ids");
		List<Long> idsList = Arrays.stream(idsStr.split(",")).map(String::trim).map(Long::valueOf)
				.collect(Collectors.toList());
		branchusermakerrepos.deleteByIds(idsList);
		return "Reports deleted successfully.";
	}

	@Override
	public String getExcelByDate(String request) {
		JSONObject requestJson = new JSONObject(request);
		String reportDateStr = requestJson.getString("reportDate");
		LocalDate reportDate = DateUtils.getDateStringIntoLocalDate(reportDateStr);
		List<BranchUserMakerModel> reportsList = branchusermakerrepos.findByReportdate(reportDate);
		String exel = EXCELService.generateEXEL(reportsList);
		return exel;
	}

	@Override
	public void existingBase64ToFile() {
		List<Filetable> files = filetableService.getAll();
		for (Filetable file : files) {
			if (!file.getBase64().contains(Constants.documentExtention)) {
				file.setBase64(FileUtils.convertBase64ToFile(file));
				filetableService.save(file);
			}
		}
	}

	@Override
	public String getTodayBranchesList() {
		List<Object[]> todayBranchesList = branchusermakerrepos.getTodayBranchesList();
		byte[] excelBytes = EXCELService.generateTodayBranchExcelBytes(todayBranchesList);
		JSONObject response = new JSONObject();
		response.put("ExcelBytes", excelBytes);
		return response.toString();
	}

	@Override
	public void getBytesToExcel(String request) {
		JSONObject jsonRequest = new JSONObject(request);
		JSONArray jsonArray = jsonRequest.getJSONArray("ExcelBytes");

		// Convert JSONArray to byte array
		byte[] excelBytes = new byte[jsonArray.length()];
		for (int i = 0; i < jsonArray.length(); i++) {
			excelBytes[i] = (byte) jsonArray.getInt(i);
		}
		FileUtils.bytesToExcelFile(excelBytes, "D:\\");
	}

	@Override
	public Long getCountByReportdateAndReporeFrequencyAndByBranchCodeAndByReportId(String request) {
		JSONObject requestJson = new JSONObject(request);
		JSONObject data = requestJson.optJSONObject("Data");

		if (data == null) {
			LOG.debug("Data is null:: {}", data);
			throw new DigitizationException("Data is null :" + data);
		}

		String reportDate = data.optString("reportDate");
		String reportFrequency = data.optString("reportFrequency");
		String branchCode = data.optString("branchCode");
		String reportId = data.optString("reportId");

		return branchusermakerrepos.getCountByReportdateAndReporeFrequencyAndByBranchCodeAndByReportId(
				LocalDate.parse(reportDate), reportFrequency, branchCode, Long.parseLong(reportId));
	}

}
