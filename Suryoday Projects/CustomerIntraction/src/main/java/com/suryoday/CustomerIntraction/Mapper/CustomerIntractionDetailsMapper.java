package com.suryoday.CustomerIntraction.Mapper;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.suryoday.CustomerIntraction.Pojo.CustomerIntractionDetails;
import com.suryoday.CustomerIntraction.Pojo.CustomerIntractionMember;
import com.suryoday.CustomerIntraction.Pojo.MOMSummaryDetails;
import com.suryoday.CustomerIntraction.Pojo.MeetingImage;
import com.suryoday.CustomerIntraction.Util.DateUtils;
import com.suryoday.CustomerIntraction.Util.TimeUtils;

@Service
public class CustomerIntractionDetailsMapper {

	private static Logger logger = LoggerFactory.getLogger(CustomerIntractionDetailsMapper.class);

	public CustomerIntractionDetails convertToEntity(JSONObject jsonData,
			CustomerIntractionDetails customerIntractionData) {

		logger.debug("CustomerIntractionDetailsMapper :: save meeting deatils :: Fields :: jsonData : {}", jsonData);
		CustomerIntractionDetails meetingdata = new CustomerIntractionDetails();
		if (customerIntractionData == null) {
			meetingdata = customerIntractionData;
		} else {
			meetingdata = new CustomerIntractionDetails();
		}
		String dateString = jsonData.optString("meetingDate");
		String startTimeString = jsonData.optString("meetingStartTime");
		String endTimeString = jsonData.optString("meetingEndTime");
		String currentMonth = jsonData.optString("currentMonth");
		LocalDate meetingDate = DateUtils.convertDateStringIntoDate(dateString);
		Timestamp startTimestamp = TimeUtils.convertTimeStringToTimestamp(startTimeString, meetingDate);
		Timestamp endTimestamp = TimeUtils.convertTimeStringToTimestamp(endTimeString, meetingDate);
		LocalDate currentDate = LocalDate.now();

		if (jsonData.has("updatedBy")) {
			meetingdata.setCreatedBy(customerIntractionData.getCreatedBy());
			meetingdata.setCreatedDate(customerIntractionData.getCreatedDate());
//			meetingdata.setUpdatedBy(jsonData.optLong("updatedBy"));
//			meetingdata.setUpdatedDate(currentDate);
		} else {
			meetingdata.setCreatedBy(jsonData.optLong("createdBy"));
			meetingdata.setCreatedDate(currentDate);
		}
		meetingdata.setMeetingDate(meetingDate);
		meetingdata.setMeetingStartTime(startTimestamp);
		meetingdata.setMeetingEndTime(endTimestamp);
		meetingdata.setMeetingNumber(jsonData.optString("meetingNumber"));
		meetingdata.setStatus("Pending At Checker");
		meetingdata.setBranchCode(jsonData.optString("branchCode"));

		if (jsonData.optString("role").equalsIgnoreCase("RO")) {
			meetingdata.setRoRemark(jsonData.optString("makerRemark"));
		} else if (jsonData.optString("role").equalsIgnoreCase("BM")) {
			meetingdata.setRoRemark(jsonData.optString("checkerRemark"));
		} else if (jsonData.optString("role").equalsIgnoreCase("CREDIT_OPS")) {
			meetingdata.setRoRemark(jsonData.optString("hoRemark"));
		}
		meetingdata.setCurrentMonth(currentMonth);

		return meetingdata;

	}

	public JSONObject convertToJson(Object[] meetingData, String role) {
		JSONObject dataJson = new JSONObject();
		int i = 0;
		dataJson.put("meetingNumber", meetingData[i++]);
		dataJson.put("createdBy", meetingData[i++]);
		dataJson.put("status", meetingData[i++]);
		dataJson.put("createdDate", meetingData[i++]);

		if (role.equalsIgnoreCase("CREDIT_OPS") && !dataJson.optString("status").equalsIgnoreCase("Submitted")) {
			dataJson.put("status", "Pending");
		}

		return dataJson;
	}

	public JSONObject convertToJson(CustomerIntractionDetails customerIntractionDetails) {

		JSONObject customerIntractionJson = new JSONObject();
		customerIntractionJson.put("branchCode", customerIntractionDetails.getBranchCode());
		customerIntractionJson.put("meetingDate", customerIntractionDetails.getMeetingDate());
		return customerIntractionJson;
	}

	public JSONObject convertDataToJson(CustomerIntractionDetails customerIntractionMeetingData) {

		JSONObject customerIntractionJson = new JSONObject();

		customerIntractionJson.putOpt("meetingDate", customerIntractionMeetingData.getMeetingDate());
		customerIntractionJson.putOpt("meetingStartDate", customerIntractionMeetingData.getMeetingStartTime());
		customerIntractionJson.putOpt("meetingEndTime", customerIntractionMeetingData.getMeetingEndTime());
		customerIntractionJson.putOpt("makerRemark", customerIntractionMeetingData.getRoRemark());
		customerIntractionJson.putOpt("checkerRemark", customerIntractionMeetingData.getBmRemark());
		customerIntractionJson.putOpt("hoRemark", customerIntractionMeetingData.getCreditOpsRemark());
		customerIntractionJson.putOpt("status", customerIntractionMeetingData.getStatus());
		customerIntractionJson.putOpt("meetingNumber", customerIntractionMeetingData.getMeetingNumber());
		customerIntractionJson.putOpt("currentMonth", customerIntractionMeetingData.getCurrentMonth());
		customerIntractionJson.putOpt("makerDate", customerIntractionMeetingData.getCreatedDate());
		customerIntractionJson.putOpt("makerId", customerIntractionMeetingData.getCreatedBy());
		customerIntractionJson.putOpt("checkerId", customerIntractionMeetingData.getUpdatedBy());
		customerIntractionJson.putOpt("checkerDate", customerIntractionMeetingData.getUpdatedDate());

		logger.debug("CUSTOMERINTRACTION_DETAILS_MAPPER :: GET BY MEETING NUMBER :: Fields :: Meeting Data : {}",
				customerIntractionJson);

		return customerIntractionJson;

	}

	public JSONArray convertMemberDataToJson(List<CustomerIntractionMember> memberData) {

		JSONArray membersArray = new JSONArray();

		memberData.stream().map(member -> {
			JSONObject memberJson = new JSONObject();
			memberJson.putOpt("memberId", member.getMemberId());
			memberJson.putOpt("memberName", member.getMemberName());
			memberJson.putOpt("type", member.getRole());
			return memberJson;
		}).forEach(membersArray::put);

		logger.debug("CUSTOMERINTRACTION_DETAILS_MAPPER :: GET BY MEETING NUMBER :: Fields :: MEMBER DATA : {}",
				membersArray);

		return membersArray;
	}

	public JSONArray convertMOMDataToJson(List<MOMSummaryDetails> MOMData) {

		JSONArray momArray = new JSONArray();

		MOMData.stream().map(mom -> {
			JSONObject memberJson = new JSONObject();
			memberJson.putOpt("branchActionable", mom.getBranchActionable());
			memberJson.putOpt("topicOfDiscussion", mom.getTopicOfDiscussion());
			memberJson.putOpt("feedback", mom.getFeedback());
			memberJson.putOpt("ETA", mom.getETA());
			return memberJson;
		}).forEach(momArray::put);

		logger.debug("CUSTOMERINTRACTION_DETAILS_MAPPER :: GET BY MEETING NUMBER :: Fields :: MEMBER DATA : {}",
				momArray);

		return momArray;

	}

	public JSONArray convertImageDataToJson(List<MeetingImage> image) {

		JSONArray imageArray = new JSONArray();

		image.stream().map(meetingImage -> {
			JSONObject imageJson = new JSONObject();

			byte[] imageString = meetingImage.getImage();
			String base64Image = Base64.getEncoder().encodeToString(imageString);

			imageJson.put("base64Image", base64Image);
			imageJson.put("documentType", meetingImage.getType());

			return imageJson;
		}).forEach(imageArray::put);

		logger.debug("CUSTOMERINTRACTION_DETAILS_MAPPER :: GET BY MEETING NUMBER :: Fields :: MEMBER DATA : {}", image);

		return imageArray;

	}

}
