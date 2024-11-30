package com.digitisation.branchreports.mapper;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.digitisation.branchreports.exception.DigitizationException;
import com.digitisation.branchreports.model.RepMaster;

@Service
public class ReportMasterMapper {

	public RepMaster convertJsonToRepMaster(JSONObject object) {
		RepMaster report = new RepMaster();
		Long reportid = object.optLong("reportid");
		String reportfrequency = object.optString("reportfrequency");
		String reportname = object.optString("reportname");
		if (reportid == null || reportid == 0 || reportfrequency == null || reportfrequency.isEmpty()
				|| reportname == null || reportname.isEmpty()) {
			throw new DigitizationException(
					"Invalid input: reportid must not be null or zero, reportfrequency must not be null or empty, and reportname must not be null or empty.");
		}
		report.setReportid(reportid);
		report.setReportfrequency(reportfrequency);
		report.setReportname(reportname);
		return report;
	}

}
