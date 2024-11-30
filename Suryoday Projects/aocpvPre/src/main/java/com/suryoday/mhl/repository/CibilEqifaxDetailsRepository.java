package com.suryoday.mhl.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.mhl.pojo.CibilEqifaxDetails;

@Repository
public interface CibilEqifaxDetailsRepository extends JpaRepository<CibilEqifaxDetails, Long> {
	
	@Query(value = "SELECT a from CibilEqifaxDetails a where a.applicationNo =:applicationNo and a.memberId =:memberId")
	public Optional<CibilEqifaxDetails> fetchByApplicationNoAndMemberId(String applicationNo, String memberId);

}
