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
import com.suryoday.CustomerIntraction.Pojo.MOMSummaryDetails;
import com.suryoday.CustomerIntraction.Util.DateUtils;

@Service
public class MOMDetailsMapper {

	private static Logger logger = LoggerFactory.getLogger(MOMDetailsMapper.class);

	public List<MOMSummaryDetails> convertToEntity(JSONObject decryptJson, String meetingNumber,
			List<MOMSummaryDetails> momSummary) {

		List<MOMSummaryDetails> MOMDetails = new ArrayList<>();

		logger.debug("MOMDetailsMapper :: Summary of MOM : " + decryptJson);

		JSONArray summaryJsonArray = decryptJson.optJSONArray("summaryMOM");
		MOMSummaryDetails summaryDetails = new MOMSummaryDetails();
		if (summaryJsonArray == null || summaryJsonArray.length() < 0) {
			logger.debug("summaryJsonArray is null or empty :: {}", HttpStatus.BAD_REQUEST);
			throw new CustomerIntractionException("MOM Summary is Null");
		}

		for (int i = 0; i < summaryJsonArray.length(); i++) {

			if (i < momSummary.size()) {
				summaryDetails = momSummary.get(i);
			} else {
				// Handle the case where `momSummary` does not have enough elements
				summaryDetails = new MOMSummaryDetails();
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

			logger.debug("topicOfDiscussion, branchActionable, feedback, ETA : " + topicOfDiscussion, branchActionable,
					feedback, ETA);

			if (topicOfDiscussion == null || topicOfDiscussion.isEmpty() || branchActionable == null
					|| branchActionable.isEmpty() || feedback == null || feedback.isEmpty()) {

				logger.debug("One or more required fields are null or empty in MemberList at index :: {} : {}", i,
						HttpStatus.BAD_REQUEST);
				throw new CustomerIntractionException("one or more MOM summary details are missing");
			}

			summaryDetails.setBranchActionable(branchActionable);
			summaryDetails.setTopicOfDiscussion(topicOfDiscussion);
			summaryDetails.setFeedback(feedback);
			summaryDetails.setETA(ETADate);
			summaryDetails.setMeetingNumber(meetingNo);
			summaryDetails.setRole(role);
			if (decryptJson.has("updatedBy")) {
				summaryDetails.setUpdatedBy(decryptJson.optLong("updatedBy"));
				summaryDetails.setUpdatedDate(currentDate);
			} else {
				summaryDetails.setCreatedDate(currentDate);
				summaryDetails.setCreatedBy(createdBy);
			}

			// Add summary data to the list
			MOMDetails.add(summaryDetails);
			logger.debug("Summary data : " + MOMDetails);
		}

		return MOMDetails;
	}
}
