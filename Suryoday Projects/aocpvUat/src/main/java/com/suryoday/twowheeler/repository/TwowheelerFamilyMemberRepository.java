package com.suryoday.twowheeler.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.twowheeler.pojo.TwoWheelerFamilyMember;

@Repository
public interface TwowheelerFamilyMemberRepository extends JpaRepository<TwoWheelerFamilyMember, Integer> {

	@Query(value = "SELECT top 1 a.id from [two_wheeler_family_member] a order by a.id desc", nativeQuery = true)
	Optional<Integer> fetchLastId();

	@Query(value = "SELECT a from TwoWheelerFamilyMember a where a.applicationNo =:applicationNo and a.member =:member")
	Optional<TwoWheelerFamilyMember> findBymember(String applicationNo, String member);

	List<TwoWheelerFamilyMember> getByApplicationNo(String applicationNo);

	@Query(value = "SELECT a from TwoWheelerFamilyMember a where a.applicationNo =:applicationNo")
	Optional<TwoWheelerFamilyMember> fetchByApplicationId(String applicationNo);

	@Query(value = "select * from [two_wheeler_family_member] where [application_no] =:applicationNo and [member] in ('APPLICANT','CO_APPLICANT')", nativeQuery = true)
	List<TwoWheelerFamilyMember> fetchByApplication(String applicationNo);

	@Query(value = "SELECT a from TwoWheelerFamilyMember a where a.applicationNo =:applicationNo and a.panCard =:pancard")
	Optional<TwoWheelerFamilyMember> fetchByAppAndPan(String applicationNo, String pancard);

}
