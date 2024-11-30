package com.suryoday.twowheeler.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.twowheeler.pojo.Insurance;


@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Integer>{

	@Query(value = "SELECT a from Insurance a where a.productCode =:productCode and a.bankNameShort =:bankNameShort and a.tenure =:tenure")
	Optional<Insurance> fetchProduct(String productCode, String bankNameShort, String tenure);

	@Query(value = "SELECT a from Insurance a where a.productCode =:productCode")
	List<Insurance> fetchProductName(String productCode);
	
	@Query(value = "SELECT a from Insurance a where a.productCode =:productCode and a.bankNameShort =:bankNameShort")
	List<Insurance> fetchBankName(String productCode,String bankNameShort);
	
	@Query(value = "SELECT a from Insurance a where a.tenure =:tenure")
	Optional<Insurance> fetchTenure(String tenure);
}
