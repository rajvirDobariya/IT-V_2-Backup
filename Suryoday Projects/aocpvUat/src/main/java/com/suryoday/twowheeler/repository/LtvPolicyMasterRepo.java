package com.suryoday.twowheeler.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.suryoday.twowheeler.pojo.LtvPolicyMaster;

public interface LtvPolicyMasterRepo extends JpaRepository<LtvPolicyMaster, String> {

	@Query(value = "SELECT a.ltvPolicyDescription from LtvPolicyMaster a where a.schemeDescription =:scheme")
	Optional<String> getByLtvPolicyDescription(String scheme);

}
