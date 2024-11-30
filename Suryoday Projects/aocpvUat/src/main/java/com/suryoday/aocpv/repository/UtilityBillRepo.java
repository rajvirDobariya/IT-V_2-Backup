package com.suryoday.aocpv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.suryoday.aocpv.pojo.BillServiceProviders;

@Component
@Repository
public interface UtilityBillRepo extends JpaRepository<BillServiceProviders, String> {

	@Query(value = "Select a from BillServiceProviders a")
	List<BillServiceProviders> fetchServiceProviders();

}
