package com.suryoday.twowheeler.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.suryoday.twowheeler.pojo.SchemeMaster;

@Component
@Repository
public interface SchemeMasterRepo extends JpaRepository<SchemeMaster, String> {
	@Query(value = "Select a from SchemeMaster a where a.SchemeDescription=:schemeCode")
	Optional<SchemeMaster> fetchBySchemeCode(String schemeCode);

	@Query(value = "Select * from tbl_tw_scheme where end_date > GETDATE();", nativeQuery = true)
	List<SchemeMaster> fetchAllSchemes();

	@Query(value = "select a.SchemeDescription from SchemeMaster a")
	List<String> fetchSchemeDesc();

}
