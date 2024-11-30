package com.suryoday.mhl.service;

import com.suryoday.mhl.pojo.CibilEqifaxDetails;
import com.suryoday.mhl.pojo.MemberManagement;

public interface MHLCibilReportService {

	public String getCibilReport(String request);

	public String getJsonRequest(String jsonString);
	
	public String getPersonalDetails(MemberManagement memberManagement);
	
	public CibilEqifaxDetails saveCibilReportData(CibilEqifaxDetails cibilEqifaxDetails);

	public CibilEqifaxDetails fetchByapplicationNoAndMemberId(String applicationNo, long memberId);

}
