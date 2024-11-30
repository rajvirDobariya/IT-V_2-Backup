package com.suryoday.hastakshar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.hastakshar.pojo.HastProductType;

@Repository
public interface ProductTypeRepo extends JpaRepository<HastProductType, Integer> {

	@Query(value = "SELECT a FROM HastProductType a WHERE a.productCode=:productCode")
	HastProductType fetchProductTypeByProductCode(String productCode);

	@Query(value = "SELECT a FROM HastProductType a WHERE a.approval=:approval")
	HastProductType fetchProductTypeByApproval(String approval);

}
