package com.suryoday.dsaOnboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.suryoday.dsaOnboard.pojo.DsaonboardSchemeMaster;

@Repository
public interface DsaSchemeMasterRepository extends JpaRepository<DsaonboardSchemeMaster, Long> {

	@Query("SELECT m.schemeCode FROM DsaonboardSchemeMaster m WHERE m.productGroup IN :ids")
	List<String> getSchemeCode(@Param("ids") List<String> ids);

}
