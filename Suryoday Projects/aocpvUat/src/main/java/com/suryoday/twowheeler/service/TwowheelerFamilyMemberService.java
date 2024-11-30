package com.suryoday.twowheeler.service;

import java.util.List;
import java.util.Optional;

import com.suryoday.twowheeler.pojo.TWNominee;
import com.suryoday.twowheeler.pojo.TwoWheelerFamilyMember;
import com.suryoday.twowheeler.pojo.TwoWheelerFamilyMemberResponse;

public interface TwowheelerFamilyMemberService {

	String saveData(TwoWheelerFamilyMember familyMember);

	List<TwoWheelerFamilyMember> getByApplicationNo(String applicationNo);

	Optional<TwoWheelerFamilyMember> fetchByApplicationNo(String applicationNo);

	void saveResponse(String proof, String proofId, String applicationNo, String response, String member);

	TwoWheelerFamilyMember getByApplicationNoAndMember(String applicationNo, String member);

	void save(TwoWheelerFamilyMember familyMember);

	TwoWheelerFamilyMember fetchByApplicationNoAndMember(String applicationNo, String member);

	List<TWNominee> getNomineeDetails(String applicationNo);

	List<TwoWheelerFamilyMemberResponse> getFamilyDetails(String applicationNo);

	TwoWheelerFamilyMember fetchByAppAndPan(String applicationNo, String pancard);

	TwoWheelerFamilyMember findById(int id);

	void updateMember(String member, String applicationNo, String bureauaScore);
}
