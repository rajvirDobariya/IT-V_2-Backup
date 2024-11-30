


package com.suryoday.mhl.serviceImpl;



import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryoday.mhl.pojo.MemberManagement;
import com.suryoday.mhl.repository.MemberManagementRepository;
import com.suryoday.mhl.service.MemberManagementService;

@Service
public class MemberManagementServiceImpl implements MemberManagementService{
	
	@Autowired
	MemberManagementRepository memberManagementRepository;

	@Override
	public String savePersonalData(MemberManagement memberManagement) {
		
		Optional<MemberManagement> optional=memberManagementRepository.getByApplicationNoandRole(memberManagement.getApplicationNo(),memberManagement.getApplicationRole());
		if(optional.isPresent()) {
			MemberManagement member = optional.get();
			memberManagement.setMmId(member.getMmId());
		}
		memberManagementRepository.save(memberManagement);
		return "Saved Successfully";
	}
	@Override
	public MemberManagement findMemberByApplicationNo(String appln,String member) {
		
		Optional<MemberManagement> optional = memberManagementRepository.getByApplicationNoandRole(appln,member);
		if(optional.isPresent()) {
			return optional.get();
		}
		throw new NoSuchElementException("List is Empty");
	}
	@Override
	public String deleteByAppNoAndMember(String applicationNo, String member) {
		Optional<MemberManagement> optional=memberManagementRepository.getByApplicationNoandRole(applicationNo,member);
		if(optional.isPresent()) {
			MemberManagement memberManagement = optional.get();
			memberManagementRepository.deleteByMmId(memberManagement.getMmId());
			return "deleted";
		}
		throw new NoSuchElementException("member not present");
	}
	@Override
	public List<MemberManagement> findMemberByApplicationNo(String applicationNo) {
		
		List<MemberManagement> memberList = memberManagementRepository.getMemberByApplication(applicationNo);
		if(memberList.isEmpty()) {
			throw new NoSuchElementException("List is Empty");
		}
		return memberList;
	}
	@Override
	public MemberManagement fetchByApplicationAndMemId(String applicationNo, long memberIdInLong) {
		Optional<MemberManagement> optional = memberManagementRepository.fetchByApplicationAndMemId(applicationNo,memberIdInLong);
		if(optional.isPresent()) {
			return optional.get();
		}
		throw new NoSuchElementException("List is Empty");
	}
}
