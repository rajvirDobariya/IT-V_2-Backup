package com.suryoday.twowheeler.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.suryoday.twowheeler.pojo.NtbSchemeMaster;

public interface NtbSchemeMasterRepo extends JpaRepository<NtbSchemeMaster, String> {

	@Query(value = "Select a from NtbSchemeMaster a where a.SchemeDescription=:schemeName")
	Optional<NtbSchemeMaster> getBySchemeDescription(String schemeName);

}
