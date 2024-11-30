package com.suryoday.roaocpv.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.suryoday.roaocpv.pojo.ROAOCPVProductType;

@Repository
public interface ROAOCPVProductTypeRepo extends JpaRepository<ROAOCPVProductType, Integer>{

	
	@Query(value = "SELECT a from ROAOCPVProductType a where a.productType =:productType")
	List<ROAOCPVProductType> fetchByCategoryType(String productType );

	@Query(value = "SELECT a from ROAOCPVProductType a")
	List<ROAOCPVProductType> fetchByAll();
}

