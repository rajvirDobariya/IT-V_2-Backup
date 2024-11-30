package com.suryoday.goNogo.service;

import java.time.LocalDateTime;
import java.util.List;

import com.suryoday.goNogo.pojo.GoNogoCustomerDetails;
import com.suryoday.goNogo.pojo.GoNogoDetailsResponse;

public interface GoNogoCustDetailsService {

	GoNogoCustomerDetails getByApplicationNo(String applicationNo);

	String createLead(String mobileNo);

	void saveResponse(String proof, String panCardNo, String applicationNo, String response);

	void save(GoNogoCustomerDetails goNogoCustomerDetails);

	GoNogoCustomerDetails getByApplicationno(String applicationNo);

	List<GoNogoDetailsResponse> fetchByDateAndbranch(LocalDateTime startdate, LocalDateTime enddate, String status,
			String branchId);

}
