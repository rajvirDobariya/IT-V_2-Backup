package com.suryoday.familyMember.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.familyMember.pojo.FamilyMember;

@Repository
public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Integer> {

	@Query(value = "SELECT a from FamilyMember a where a.customerId =:customerId and a.member =:member")
	Optional<FamilyMember> fetchByCustomerIdAndMember(String customerId, String member);

	@Query(value = "SELECT top 1 a.id from family_member a order by a.id desc", nativeQuery = true)
	Optional<Integer> fetchLastId();

	@Query(value = "SELECT a from FamilyMember a where a.panCard =:panCardNo")
	Optional<FamilyMember> getByPanCard(String panCardNo);

	@Query(value = "SELECT a from FamilyMember a where a.voterId =:voterId")
	Optional<FamilyMember> getByVoterId(String voterId);

	@Query(value = "SELECT a from FamilyMember a where a.aadharCard =:aadhar")
	Optional<FamilyMember> getByAadharCard(String aadhar);

	List<FamilyMember> getByCustomerId(String customerId);
}
