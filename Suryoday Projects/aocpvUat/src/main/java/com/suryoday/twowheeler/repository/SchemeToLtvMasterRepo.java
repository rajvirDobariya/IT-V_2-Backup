package com.suryoday.twowheeler.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.suryoday.twowheeler.pojo.SchemeToLtvMaster;

public interface SchemeToLtvMasterRepo extends JpaRepository<SchemeToLtvMaster, String> {

	@Query(value = "Select a.ltvPolicy from SchemeToLtvMaster a where a.ltvPolicyDescription =:ltvPolicy and a.creditStatusDescription =:breCustCatagory")
	Optional<String> getLtv(String ltvPolicy, String breCustCatagory);

}
