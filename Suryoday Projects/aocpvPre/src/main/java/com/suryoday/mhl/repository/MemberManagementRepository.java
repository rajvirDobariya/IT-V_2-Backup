package com.suryoday.mhl.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.mhl.pojo.MemberManagement;
@Repository
public interface MemberManagementRepository extends JpaRepository<MemberManagement, Integer>{

	
	@Query(value = "SELECT a from MemberManagement a where a.applicationNo =:applicationNo and a.applicationRole =:applicationRole")
	Optional<MemberManagement> getByApplicationNoandRole(String applicationNo, String applicationRole);

	@Query(value = "Select a from MemberManagement a where a.applicationNo =:appln")
	List<MemberManagement> getMemberByApplication(String appln);

//	@Modifying
//	@Transactional
//	@Query(value = "DELETE MemberManagement a where a.applicationNo =:applicationNo and a.applicationRole =:member")
//	void deleteMember(String applicationNo, String member);
	@Modifying
	@Transactional
	void deleteByMmId(int mmId);

	@Query(value = "SELECT a from MemberManagement a where a.applicationNo =:applicationNo and a.memberId =:memberIdInLong")
	Optional<MemberManagement> fetchByApplicationAndMemId(String applicationNo, long memberIdInLong);

}
