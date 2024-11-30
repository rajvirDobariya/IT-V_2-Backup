package com.suryoday.CustomerIntraction.Mapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.suryoday.CustomerIntraction.Exception.CustomerIntractionException;
import com.suryoday.CustomerIntraction.Pojo.MOMSummaryDetailsAudit;
import com.suryoday.CustomerIntraction.Util.DateUtils;

@Service
public class MOMDetailsAuditMapper {

	private static Logger logger = LoggerFactory.getLogger(MOMDetailsAuditMapper.class);

	public List<MOMSummaryDetailsAudit> convertToEntity(JSONObject decryptJson, String meetingNumber,
			List<MOMSummaryDetailsAudit> momAuditSummary) {

		List<MOMSummaryDetailsAudit> MOMAuditDetails = new ArrayList<>();

		logger.debug("MOMAuditMapper :: Summary of MOM : " + decryptJson);

		JSONArray summaryJsonArray = decryptJson.optJSONArray("summaryMOM");
		MOMSummaryDetailsAudit summaryDetailsAudit = new MOMSummaryDetailsAudit();
		if (summaryJsonArray == null || summaryJsonArray.length() < 0) {
			logger.debug("summaryJsonArray-Audit is null or empty :: {}", HttpStatus.BAD_REQUEST);
			throw new CustomerIntractionException("MOM Summary is Null");
		}

		for (int i = 0; i < summaryJsonArray.length(); i++) {

			if (i < momAuditSummary.size()) {
				summaryDetailsAudit = momAuditSummary.get(i);
			} else {
				// Handle the case where `momSummary` does not have enough elements
				summaryDetailsAudit = new MOMSummaryDetailsAudit();
			}
			JSONObject summaryJson = (JSONObject) summaryJsonArray.get(i);
			if (meetingNumber == null || meetingNumber.isEmpty()) {

				logger.debug(" meetingNumber is null or empty in Data of Request :: {}", HttpStatus.BAD_REQUEST);
				throw new CustomerIntractionException("Meeting Number is null");
			}

			String topicOfDiscussion = summaryJson.optString("topicOfDiscussion");
			String branchActionable = summaryJson.optString("branchActionable");
			String feedback = summaryJson.optString("feedback");
			String ETA = summaryJson.optString("ETA");
			LocalDate ETADate = null;
			if(!ETA.equalsIgnoreCase("")) {
				ETADate = DateUtils.convertDateStringIntoDate(ETA);
			}
			Long createdBy = decryptJson.optLong("createdBy");
			String meetingNo = meetingNumber;
			String role = decryptJson.optString("role");
			LocalDate currentDate = LocalDate.now();

			logger.debug("Audit : topicOfDiscussion, branchActionable, feedback, ETA : " + topicOfDiscussion,
					branchActionable, feedback, ETA);

			if (topicOfDiscussion == null || topicOfDiscussion.isEmpty() || branchActionable == null
					|| branchActionable.isEmpty() || feedback == null || feedback.isEmpty()) {

				logger.debug("One or more required fields are null or empty in MemberList at index :: {} : {}", i,
						HttpStatus.BAD_REQUEST);
				throw new CustomerIntractionException("one or more MOM summary details are missing");
			}

			summaryDetailsAudit.setBranchActionable(branchActionable);
			summaryDetailsAudit.setTopicOfDiscussion(topicOfDiscussion);
			summaryDetailsAudit.setFeedback(feedback);
			summaryDetailsAudit.setETA(ETADate);
			summaryDetailsAudit.setMeetingNumber(meetingNo);
			summaryDetailsAudit.setRole(role);
			if (decryptJson.has("updatedBy")) {
				summaryDetailsAudit.setUpdatedBy(decryptJson.optLong("updatedBy"));
				summaryDetailsAudit.setUpdatedDate(currentDate);
			} else {
				summaryDetailsAudit.setCreatedBy(createdBy);
				summaryDetailsAudit.setCreatedDate(currentDate);
			}

			// Add summary data to the list
			MOMAuditDetails.add(summaryDetailsAudit);
			logger.debug("Summary data : " + MOMAuditDetails);
		}

		return MOMAuditDetails;
	}
}
