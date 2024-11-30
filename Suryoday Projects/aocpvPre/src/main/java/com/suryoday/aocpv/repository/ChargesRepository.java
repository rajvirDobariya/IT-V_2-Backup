package com.suryoday.aocpv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.suryoday.aocpv.pojo.Charges;

@Component
@Repository
public interface ChargesRepository extends JpaRepository<Charges,String>{

	@Query(value="Select a from Charges a where a.loanAmount=:loanAmount and a.tenure=:tenure")
	List<Charges> fetchByProductCode(String loanAmount, String tenure);

	@Query(value="Select * from [tbl_charges] a where a.product_code=:productCode",nativeQuery = true)
	List<Charges> fetchByProductCode(String productCode);

	@Query(value="Select a from Charges a ")
	List<Charges> getAll();

}
