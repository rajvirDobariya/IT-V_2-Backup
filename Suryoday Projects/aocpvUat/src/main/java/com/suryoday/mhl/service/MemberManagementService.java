package com.suryoday.mhl.service;

import java.util.List;

import com.suryoday.mhl.pojo.MemberManagement;

public interface MemberManagementService {

	String savePersonalData(MemberManagement memberManagement);

	MemberManagement findMemberByApplicationNo(String appln, String member);

	String deleteByAppNoAndMember(String applicationNo, String member);

	List<MemberManagement> findMemberByApplicationNo(String applicationNo);

	MemberManagement fetchByApplicationAndMemId(String applicationNo, long memberIdInLong);
}
