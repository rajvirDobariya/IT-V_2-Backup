package com.suryoday.aocpv.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.suryoday.aocpv.pojo.EmiResponseMaster;

public interface EmiResponseMasterRepository extends JpaRepository<EmiResponseMaster, Long>{

	@Query(value = "SELECT a from EmiResponseMaster a where a.productCode =:productCode")
	Optional<EmiResponseMaster> getbyAmountRequestedAndTerm(String productCode);

}
