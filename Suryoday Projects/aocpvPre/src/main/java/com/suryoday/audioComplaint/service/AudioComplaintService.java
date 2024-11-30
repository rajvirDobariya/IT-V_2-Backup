package com.suryoday.audioComplaint.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.springframework.web.multipart.MultipartFile;

import com.suryoday.audioComplaint.entity.AudioComplaintDetails;
import com.suryoday.audioComplaint.entity.AudioComplaintResponse;

public interface AudioComplaintService {

	void saveAudioComplaint(MultipartFile[] files, String complaintNo, JSONArray document, String x_User_ID, String branchId);

	List<AudioComplaintResponse> fetchByBranchId(String userId, String branchId);

	AudioComplaintDetails fetchByComplaintNo(String complaintNo);

	void save(AudioComplaintDetails audioComplaintDetails);

	List<AudioComplaintResponse> fetchByDate(LocalDateTime startdate, LocalDateTime enddate);

	boolean validateSessionId(String x_Session_ID, HttpServletRequest request);

	String getSessionId(String x_User_ID, HttpServletRequest request);

}
