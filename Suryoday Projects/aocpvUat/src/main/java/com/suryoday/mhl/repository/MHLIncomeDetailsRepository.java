package com.suryoday.mhl.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.mhl.pojo.MHLIncomeDetails;

@Repository
public interface MHLIncomeDetailsRepository extends JpaRepository<MHLIncomeDetails, Integer> {

	@Query(value = "SELECT a from MHLIncomeDetails a where a.aadharCard =:aadharCard")
	Optional<MHLIncomeDetails> getByAadharNo(String aadharCard);

}
