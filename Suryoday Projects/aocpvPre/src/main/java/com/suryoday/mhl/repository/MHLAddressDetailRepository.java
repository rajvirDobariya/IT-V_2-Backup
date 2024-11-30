package com.suryoday.mhl.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.suryoday.mhl.pojo.AddressDetails;

@Repository
public interface MHLAddressDetailRepository extends JpaRepository<AddressDetails, Integer>{

	
	@Query(value = "SELECT * from TBL_ADDRESS_DETAILS a where a.application_no =:applicationNo and a.application_role =:applicationRole and a.address_type =:addressType",nativeQuery = true)
	Optional<AddressDetails> getByApplicationNo(@Param("applicationNo")String applicationNo,@Param("applicationRole") String applicationRole,@Param("addressType") String addressType);
	
	
}
