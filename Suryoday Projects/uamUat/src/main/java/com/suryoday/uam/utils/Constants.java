package com.suryoday.uam.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.suryoday.uam.pojo.UserAccessResponse;

@Service
public class Constants {

	public static List<UserAccessResponse> getAllAccessModulesMap() {
		List<UserAccessResponse> map = new ArrayList<>();
		map.add(new UserAccessResponse("AOCPV", "format_align_justify"));
		map.add(new UserAccessResponse("NUDGE", "format_align_justify"));
		map.add(new UserAccessResponse("LOAN-TRACKING", "monitoring"));
		map.add(new UserAccessResponse("TWO-WHEELER", "two_wheeler"));
		map.add(new UserAccessResponse("DSA-ONBOARDING", "connect_without_contact"));
		map.add(new UserAccessResponse("LEAD-MANAGEMENT", "supervisor_account"));
		map.add(new UserAccessResponse("HASTAKSHAR", "edit_note"));
		map.add(new UserAccessResponse("GO-NO-GO", "credit_score"));
		map.add(new UserAccessResponse("INSURANCE", "clinical_notes"));
		map.add(new UserAccessResponse("USER-CREATION", "person_add"));
		map.add(new UserAccessResponse("JLG", "collections_bookmark"));
		map.add(new UserAccessResponse("EDD", "collections_bookmark"));
		map.add(new UserAccessResponse("COUNTERFEIT", "collections_bookmark"));
		map.add(new UserAccessResponse("OTS", "collections_bookmark"));
		map.add(new UserAccessResponse("COLLECTION", "collections_bookmark"));
		map.add(new UserAccessResponse("CUSTOMER-ONBOARD", "collections_bookmark"));
		map.add(new UserAccessResponse("CUSTOMER-INTERACTION", "person_add"));
		map.add(new UserAccessResponse("BRANCH-DIGITIZATION", "collections_bookmark"));
		map.add(new UserAccessResponse("BRANCH-VISIT", "collections_bookmark"));
		map.add(new UserAccessResponse("MORTGAGES", "collections_bookmark"));
		map.add(new UserAccessResponse("HEALTH-CHECK-TRACKER", "collections_bookmark"));
		map.add(new UserAccessResponse("CPV", "collections_bookmark"));
		map.add(new UserAccessResponse("AUDIO-COMPLAINT", "collections_bookmark"));
		map.add(new UserAccessResponse("TRAVEL-VISIT", "collections_bookmark"));
		map.add(new UserAccessResponse("MSME", "collections_bookmark"));
		return map;
	}

}
