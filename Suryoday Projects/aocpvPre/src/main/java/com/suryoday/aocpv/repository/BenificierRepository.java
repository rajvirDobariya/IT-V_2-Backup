package com.suryoday.aocpv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.suryoday.aocpv.pojo.Benificier;

@Repository
public interface BenificierRepository  extends  JpaRepository<Benificier,Long>{ 
	
	
	     
	@Query(value = "SELECT * FROM Tbl_Benificier",  nativeQuery = true)
	      public List<Benificier>    findAlla();
	   
	
	@Query(value ="SELECT COUNT(*) FROM tbl_benificier u WHERE u.customer_id=:customernumber ",nativeQuery = true)
    long totolcustomer(@Param("customernumber") String customernumber);
	
	@Query(value ="select TOP 1 sequence from Tbl_Benificier u where u.customer_id=:customernumber order by sequence desc",nativeQuery = true)
    long totolcustomer1(@Param("customernumber") String customernumber);
	
	
	@Query(value = "SELECT * FROM Tbl_Benificier u where u.customer_id=:customernumber",  nativeQuery = true)
	  List<Benificier> findByCustomerId(@Param("customernumber")  String customerId);
	
	@Query(value = "SELECT * FROM Tbl_Benificier u where u.status=:customernumber",  nativeQuery = true)
	  List<Benificier> findByStatus(@Param("customernumber")  String customerId);
	
	@Query(value = "SELECT * FROM Tbl_Benificier u where u.customer_id=:customernumber and u.type=:acounttype",  nativeQuery = true)
	  List<Benificier> viewCustomerId(@Param("customernumber")  String customerId,@Param("acounttype")  String acounttype);

}
