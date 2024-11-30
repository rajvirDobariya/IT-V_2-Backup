package com.suryoday.familyMember.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.familyMember.pojo.FamilyMemberImages;

@Repository
public interface MemberImageRepository extends JpaRepository<FamilyMemberImages, Integer> {

	@Query(value = "SELECT top 1 a.id from family_member_images a order by a.id desc", nativeQuery = true)
	Optional<Integer> fetchLastId();

	@Query(value = "SELECT a from FamilyMemberImages a where a.customerId =:customerId and a.documenttype =:documenttype and a.member =:member")
	Optional<FamilyMemberImages> getByApplicationNoMember(String customerId, String documenttype, String member);

	@Query(value = "SELECT a from FamilyMemberImages a where a.customerId =:customerId and a.member =:member")
	List<FamilyMemberImages> getByCustomerIdMember(String customerId, String member);
}
