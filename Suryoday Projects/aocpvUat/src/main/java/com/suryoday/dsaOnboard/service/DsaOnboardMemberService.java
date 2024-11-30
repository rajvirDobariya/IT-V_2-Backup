package com.suryoday.dsaOnboard.service;

import java.util.List;

import com.suryoday.dsaOnboard.pojo.DeviationDetailsResponse;
import com.suryoday.dsaOnboard.pojo.DsaOnboardMember;
import com.suryoday.dsaOnboard.pojo.DsaOnboardMemberResponse;

public interface DsaOnboardMemberService {

	DsaOnboardMember getByApplicationNoAndMember(String applicationNo, String member);

	String saveData(DsaOnboardMember dsaOnboardMember);

	DsaOnboardMember getByApplicationnoAndMember(String applicationNo, String member);

	List<DsaOnboardMemberResponse> getByApplicationNo(long applicationno);

	List<DsaOnboardMember> getByApplication(long applicationno);

	void saveResponse(String proof, String proofIdNo, String applicationNo, String response, String member);

	List<DeviationDetailsResponse> getDeviationDetails(String applicationNo);

	DsaOnboardMember findByApplicationNoAndIsPrimary(long applicationNo, String string);

}
