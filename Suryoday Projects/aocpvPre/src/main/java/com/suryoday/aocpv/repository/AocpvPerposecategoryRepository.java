package com.suryoday.aocpv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.suryoday.aocpv.pojo.AocpvPerposecategory;

@Repository
public interface AocpvPerposecategoryRepository  extends JpaRepository<AocpvPerposecategory, Long>{
	
	public List<AocpvPerposecategory> findByCategoryId(String categoryId);

}
