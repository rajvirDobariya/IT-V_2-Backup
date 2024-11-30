package com.suryoday.familyMember.service;

import java.util.List;

import com.suryoday.familyMember.pojo.CustomerData;
import com.suryoday.familyMember.pojo.FamilyMember;
import com.suryoday.familyMember.pojo.FamilyMemberResponse;

public interface CustomerDataService {

	CustomerData fetchBycustomerId(String customerId);

	String saveFamilyMember(FamilyMember familyMember);

	List<FamilyMemberResponse> fetchFamilyMember(String customerId);

	String deleteMember(String customerId, String member);

	String statuschange(String customerId, String status);

	void validatePancard(String panCardNo);

	void validateAadharCard(String referenceNo);

}
