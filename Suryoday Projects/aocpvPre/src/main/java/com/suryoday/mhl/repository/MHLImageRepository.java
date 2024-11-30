package com.suryoday.mhl.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.suryoday.mhl.pojo.MHLImages;

@Repository
public interface MHLImageRepository extends JpaRepository<MHLImages, Integer>{
	
	
	
	@Query(value = "SELECT * from mhl_images a where a.application_no=:applicationNo and a.documenttype=:documenttype and a.member=:member",nativeQuery = true)
	Optional<MHLImages> getByApplicationNo(@Param("applicationNo")String applicationNo,@Param("documenttype") String documenttype,@Param("member") String member); 

}
